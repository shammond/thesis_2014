/*
   <p>Serial port-related functions. These are used by the serial
   port server, spd, to manipulate a serial port.</p>

   <kbd>
   <b>SerialPort - serial connections for Java</b><br>
   Copyright (C) 2005  Martin C Gregorie
   
   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
  
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
   </kbd>
  
   <p>Contact Martin Gregorie at <i>martin@gregorie.org</i></p>
*/

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <termio.h>
#include <time.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>
#include <poll.h>
#include <fdlist.h>
#include <environ.h>

#include "spd.h"
#include "sysdependent.h"
#include "paramdecode.h"
#include "paramcheck.h"

typedef struct {
   char           *port_id;      /* name used to open the serial port   */
   char           *device;       /* serial port's device name           */
   char           type;          /* P = port, F = file, L = loop-back   */
   char           active;        /* Y = available, N = don't use        */
   int            def_baud;      /* default port parameters             */
   int            def_databits;
   char           def_parity;
   int            def_stopbits;
   int            open;          /* TRUE = open, FALSE = closed         */
   int            fd;            /* port's fd                           */
   int            baud;          /* active port parameters              */
   int            databits;
   char           parity;
   int            stopbits;
   int            txdelay;
   int            sep_active;
   char           sep_marker;
   int            sep_external;
   CIRC_BUFF      *inbuff;       /* Message buffers */
   CIRC_BUFF      *outbuff;
   struct termio  ts_orig;       /* Original line settings */
} port_dets;

static int        max_message = 0;
static int        debug = 0;
static int        devcount = 0;
static port_dets  **ports;
static char       *valid_types   = "PFL";
static char       *active_vals   = "YN";
static char       *lasterr = "";

/*
   Private functions not called outside this source file
   =================
*/
int         port_istrue(char flag);
void        port_defaults(int i);
port_dets   *port_getdets(int n);
int         port_setup(int i, char *line);
void        port_store_defaults(int i);
int         port_save_termio(int i, int debug);
int         port_set_termio(int i, int debug);
int         port_restore_termio(int i, int debug);

/*
   Public interface functions
   ==========================
*/   

/*
   <p>Create serial port definitions from the configuration file.
   It is passed the name of the configuration file as
   <u>file</u>. <u>prog</u> is used in error messages.
   <u>dbg</u> sets the debugging level.</p>
   
   <p>The serial port definitions and all associated data for each
   port is maintained in a private structure that can only be
   accessed via the <i>port_xxx()</i> functions.</p>
   
   <p>It returns the number of port definitions found in the
   configuration file.</p>
*/
int port_config(char *prog, char *file, int dbg)
{
   FILE  *cf;
   char  line[MAXLINE];
   char  *hit;
   int   i;
   int   errcount = 0;

   debug = (dbg <=1 ? 0 : dbg -1);
   cf = config_open(prog, file, dbg);
   buff_debug(debug);

   while(config_getline(cf, line, MAXLINE))
   {
      if (strstr(line, "devices="))
      {
         /*
            Found a devices=nn line
         */
         if (devcount == 0)
         {
            /*
               Found the first devices line
            */
            hit = strchr(line, '=');
            if (hit)
            {
               devcount = atoi(++hit);
               if (devcount <= 0)
                  error(progname(), "invalid device count specifier:", line);
            
               if (debug > 1)
                  fprintf(stderr,
                          "%d device specifiers expected\n",
                          devcount);
                          
               ports = get_space(sizeof(port_dets*) * devcount);
               i = 0;
            }
            else
               error(progname(), "invalid device count specifier:", line);
         }
         else
            error(progname(), "too many device count specifiers", "");
      }
      else
      if (strstr(line, "maxmessage="))
      {
         /*
            Found a maxmessage=nn line
         */
         if (max_message == 0)
         {
            /*
               Found the first maxmessages line
            */
            hit = strchr(line, '=');
            if (hit)
            {
               max_message = atoi(++hit);
               if (max_message < 100 || max_message > 16000)
                  error(progname(),
                        "invalid maximum message size:", line);
            
               if (debug > 1)
                  fprintf(stderr,
                          "maximum message size is %d\n",
                          max_message);
                          
            }
            else
               error(progname(), "invalid message size:", line);
         }
         else
            error(progname(), "too many message size specifiers", "");
      }
      else
      {
         /*
            Found a device definition
         */
         if (max_message == 0)
            error(progname(), "no maximum message size specified", "");

         if (i >= devcount)
            error(progname(),
                  "number of device definitions",
                  "exceeds the number specified");

         ports[i] = get_space(sizeof(port_dets));
         if (port_setup(i++, line))
            errcount++;            
      }
   }

   config_close(cf);

   if (i != devcount)
      error(progname(),
            "number of device definitions",
            "does not match the number specified");

   if (errcount)
      error(progname(), "One or more device definitions have errors", "");

   if (debug)
      fprintf(stderr,
              "%d = port_config(%s,%s,%d)\n",
              devcount,
              prog,
              file,
              dbg);
              
   return devcount;
}

/*
   Return a string containing a comma delimited list of the
   names of available serial ports.
*/
char *port_selection()
{
   int         i;
   int         n = 0;
   static char *pl;

   for (i = 0; i < devcount; i++)
      n += strlen(ports[i]->port_id) + 1;

   pl = get_space(n);
   strcpy(pl, "");
   for (i = 0; i < devcount; i++)
      if (ports[i]->active == 'Y')
      {
         if (i > 0)
            strcat(pl, ",");
            
         strcat(pl, ports[i]->port_id);
      }

   if (debug)
      fprintf(stderr, "%s = port_selection()\n", pl);
      
   return pl;
}

/*
   Return the index to a serial port definition or an error.
   <u>key</u> contains the name of the port.
*/
int port_find(char *key)
{
   int   i;
   int   r = SPEINVPORT;

   for (i = 0; i < devcount; i++)
   {
      if (strcmp(ports[i]->port_id, key) == 0)
      {
         if (ports[i]->active == 'Y')
            r = i;
         else
            r = SPEINACTIVE;
            
         break;
      }
   }

   if (debug)
      fprintf(stderr, "%d = port_find(%s,%d)\n", r, key);

   return r;
}

/*
   Return the number of serial port definitions.
*/
int port_getcount()
{
   if (debug)
      fprintf(stderr, "%d = port_getcount()\n", devcount);

   return devcount;
}

/*
   Return the serial port's fd.
   <u>portref</u> is the index returned by <i>port_find()</i>.
*/
int port_getfd(int portref)
{
   port_dets *pd;

   pd = port_getdets(portref);
   if (debug)
      fprintf(stderr, "%d = port_getfd(%d)\n", pd->fd, portref);

   return pd->fd;
}

/*
   Return the number of bytes in the port's input buffer.
   <u>portref</u> is the index returned by <i>port_find()</i>.
*/
int port_getincount(int portref)
{
   port_dets *pd;
   int       count;

   pd = port_getdets(portref);
   count = buff_usedspace(pd->inbuff);
   if (debug)
      fprintf(stderr, "%d = port_getincount(%d)\n", count, portref);

   return count;
}

/*
   Return the number of bytes in the port's output buffer.
   <u>portref</u> is the index returned by <i>port_find()</i>.

*/
int port_getoutcount(int portref)
{
   port_dets *pd;
   int       count;

   pd = port_getdets(portref);
   count = buff_usedspace(pd->outbuff);
   if (debug)
      fprintf(stderr, "%d = port_getoutcount(%d)\n", count, portref);

   return count;
}

/*
   Return the size of the port's output buffer: this is the
   same size as the input buffer.
   <u>portref</u> is the index returned by <i>port_find()</i>.

*/
int port_getbuffsize(int portref)
{
   port_dets *pd;
   int       size;

   pd = port_getdets(portref);
   size = buff_size(pd->outbuff);
   if (debug)
      fprintf(stderr, "%d = port_getoutcount(%d)\n", size, portref);

   return size;
}

/*
   Return a serial port's current settings as a formatted,
   displayable string.
   <u>i</u> is the index returned by <i>port_find()</i>.
*/
char *port_status(int i)
{
   static char buff[MAXLINE];
   static char sepbuff[MAXLINE];
   char        *typ = "*Err";
   char        *st = "Inactive";
   port_dets   *pd;

   pd = port_getdets(i);
   if (pd->type == 'P')
      typ = "Port";
   else
   if (pd->type == 'F')
      typ = "File";
   else
   if (pd->type == 'L')
      typ = "Loop";
      
   if (pd->active == 'Y')
      st = (pd->open ? "Open" : "Closed");

   if (pd->sep_active)
      snprintf(sepbuff,
               MAXLINE,
               "sep=%s%s",
               byteout(pd->sep_marker),
               (pd->sep_external ? "(ext)" : ""));
   else
      snprintf(sepbuff, MAXLINE, "nosep");

   snprintf(buff,
            MAXLINE,
            "%4s: %04s %12s %8s %d,%d,%c,%d Dly tx=%d mS %s",
            pd->port_id,
            typ,
            pd->device,
            st,
            pd->baud,
            pd->databits,
            pd->parity,
            pd->stopbits,
            pd->txdelay,
            sepbuff);

   if (debug)
      fprintf(stderr, "[%s] = port_status(%d)\n", buff, i);
      
   return buff;    
}

/*
   Open the serial port.
   <u>i</u> is the index returned by <i>port_find()</i>.
   The input and output buffers are cleared, the port is opened
   and its speed, parity, etc. are set to the defaults in the
   configuration file after saving the original values.
   It returns the subscript to the port array or a negative value
   if the port could not be opened and configured.
*/
int port_open(int i)
{
   port_dets   *pd;
   int         r = i;
   int         reply;

   if (i >= 0)
   {
      pd = port_getdets(i);
      if (pd->open)
      {
         r = -1;
         lasterr = "Port is already open";
      }
      else
      {
         /*
            Restore the port default settings,
            clear the buffers and open the port.
         */
         port_defaults(i);
         buff_reset(pd->inbuff);
         buff_reset(pd->outbuff);
         pd->fd = open(pd->device, O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
         pd->open = (pd->fd >= 0);
         if (!pd->open)
         {
            r = -1;
         }

         if (r >= 0)
         {
            /*
              Save original settings and set requested settings
            */
            reply = port_save_termio(i, debug);
            if (reply >= 0)            
               reply = port_set_termio(i, debug);

            if (reply < 0)
               r = -1;
         }

         if (r < 0)
         {
            r = -1;
            lasterr = strerror(errno);
         }
      }
   }
   else
   {
      r = -1;
      lasterr = "Invalid port array subscript"; 
   }

   if (debug)
      fprintf(stderr, "%d = port_open(%d)\n", r, i);
      
   return r;
}

/*
   Close the serial port.
   <u>i</u> is the index returned by <i>port_find()</i>.
   If <u>force</u> is TRUE the port always closes.
   If <u>force</u> is FALSE it only closes if the buffers are empty.
   The serial port has the settings that were saved by <i>port_open()</i>
   restored before it is closed.
   The function returns <u>i</u> on success and a negative number
   if the port was not closed.
*/

int port_close(int i, int force)
{
   port_dets   *pd;
   int         r = i;
   int         reply;
   int         inused;
   int         outused;
   int         totused;

   if (i < 0)
   {
      r = -1;
      lasterr = "Invalid port array subscript";
   }
   else
   {
      pd = port_getdets(i);
      if (pd->open)
      {
         /*
            'force' closes the port regardless of what is still
            in the buffers. Otherwise the presence of data causes
            a diagnostic message to be sent and the port is not
            closed.
         */
         if (force)
            totused = 0;
         else
         {
            inused = buff_usedspace(pd->inbuff);
            outused = buff_usedspace(pd->outbuff);
            totused = inused + outused;
         }
         
         if (totused == 0)
         {
            if (pd->fd >= 0)
            {
               /*
                  'File' and 'Port' ports need original settings restored
                  and the file closed.
               */
               reply = port_restore_termio(i, debug);
               if (reply >= 0)
               {
                  /*
                     Close the port
                  */
                  reply = close(pd->fd);
                  if (reply == 0)
                  {
                     pd->fd = -1;
                     pd->open = FALSE;
                  }
                  else
                  {
                     pd->fd = -1;
                     pd->open = FALSE;
                  }
               }

               if (reply < 0)
               {
                  r = -1;
                  lasterr = strerror(errno);
               }
            }
            else
            {
               r = -1;
               lasterr = "Port marked open but fd is unset";
            }
         }
         else
         {
            if (inused && outused)
               lasterr = "Still data in input and output buffers";
            else
            if (inused)
               lasterr = "Still data to be read";
            else
            if (outused)
               lasterr = "Still data waiting to be sent";

            r = -1;
         }
      }
      else
      {
         r = -1;
         lasterr = "Port is not open";
      }
   }

   if (debug)
      fprintf(stderr,
              "%d = port_close(%d,%s)\n",
              r,
              i,
              ( force ? "TRUE" : "FALSE"));
      
   return r;
}

/*
   Extract a byte from the output buffer and write it to the serial port.
   <u>f</u> is the index returned by <i>port_find()</i>.
   This function is called by the polling routines when there is
   data to be written and the serial port has generated a POLLOUT
   event.
   It returns the number of bytes written, zero if there was nothing to write
   or -1 on error.
*/
int port_write_one(int f)
{
   port_dets         *pd;
   struct timespec   t;
   char              wb[2];
   int               r = 0;
   int               rw = 0;

   if (debug)
      fprintf(stderr, "port_write_one(%d) called\n", f);
      
   if (f >= 0)
   {
      pd = port_getdets(f);
      if (pd->open)
      {
         if (pd->fd >= 0)
         {
            r = buff_extract_one(pd->outbuff);
            if (r > 0)
            {
               /*
                  Send the byte with a delay, if needed, after it has been sent.
                  We use a separate reply variable for the delay so we don't
                  lose the reply from write();
               */
               wb[0] = (char)r;
               wb[1] = 0;
               r = write(pd->fd, wb, 1);
               if (r == 1)
               {
                  if (pd->txdelay !=0)
                  {
                     t.tv_sec = pd->txdelay / 1000;
                     t.tv_nsec = (pd->txdelay - t.tv_sec * 1000) * 1000;
                     if (debug > 1)
                        fprintf(stderr,
                                "TX delay = %d, %d\n",
                                t.tv_sec,
                                t.tv_nsec);
                     rw = nanosleep(&t, NULL);
                     if (rw < 0)
                     {
                        r = -1;
                        lasterr = strerror(errno);
                     }
                  }
               }
               else
               {
                  r = -1;
                  lasterr = strerror(errno);
               }
            }
            else
            if (r == 0)
            {
               lasterr = "Output buffer is empty";
            }
            else
            if (r < 0)
            {
               r = -1;
               lasterr = "Error getting a byte from the output buffer";
            }
            
            /* Nothing retrieved means buffer emptied - not an error */
         }
         else
         {
            r = -1;
            lasterr = "Port marked open but fd is unset";
         }
      }
      else
      {
         r = -1;
         lasterr = "Port is not open";
      }
   }
   else
   {
      r = -1;
      lasterr = "Invalid port array subscript";
   }

   if (debug)
   {
      wb[1] = 0;            
      fprintf(stderr, "%d = port_write_one(%d)", r, f);
      if (r <= 0)
         fprintf(stderr, "\n");
      else
         fprintf(stderr, " - wrote '%s'\n", byteout(wb[0]));
   }

   return r;
}

/*
   <p>Write to the serial port's output buffer.
   <u>f</u> is the index returned by <i>port_find()</i>.</p>
   
   <p>If the port type is <b>p</b>ort or <b>f</b>ile then
   this function adds <u>valuelth</u> bytes from <u>value</u>
   to the port's output buffer from where <i>port_write_one()</i>,
   which is called by the polling mechanism, extracts
   bytes and writes them to the serial port.</p>
   
   <p>If the port type is <b>l</b>oopback then
   this function adds <u>valuelth</u> bytes from <u>value</u>
   to the port's <i>input</i> buffer. The effect is the same as if
   the data had been sent via a serial port with a loopback cable
   connected to it.</p>
   
   <p>It returns the number of bytes written or -1 on error.</p>
*/
int port_write(int f, char *value, int valuelth)
{
   port_dets   *pd;
   int         r = 0;

   if (debug)
      fprintf(stderr,
              "port_write(%d,[%s],%d) entered\n",
              f,
              value,
              valuelth);

   if (f >= 0)
   {
      pd = port_getdets(f);
      if (pd->type == 'L')
      {
         /* Loopback port - just put the stuff in the input buffer */
         if (buff_freespace(pd->inbuff) >= valuelth + 1)
         {
            buff_add_string(pd->inbuff, value, valuelth);

            /*
               Append a record separator if requested to do so:
               the separator must be active and external to the
               data sent by the client. Available space has
               already been checked.
            */
            if (pd->sep_active && pd->sep_external)
               buff_add_string(pd->inbuff, &pd->sep_marker, 1);

            /*
               Set return value for success
            */
            r = valuelth;
         }
         else
         {
            r = -1;
            lasterr = "Input buffer is full";
         }
      }
      else
      {
         if (buff_freespace(pd->outbuff) >= (valuelth + 1)) 
         {
            buff_add_string(pd->outbuff, value, valuelth);

            /*
               Append a record separator if requested to do so:
               the separator must be active and external to the
               data sent by the client. Available space has
               already been checked.
            */
            if (pd->sep_active && pd->sep_external)
               buff_add_string(pd->outbuff, &pd->sep_marker, 1);

            /*
               Set return value for success
            */
            r = valuelth;
         }
         else
         {
            r = -1;
            lasterr = "Output buffer is full";
         }
      }
   }
   else
   {
      r = -1;
      lasterr = "Invalid port array subscript";
   }

   if (debug)
      fprintf(stderr, "port_write() returned %d\n", r);
      
   return r;
}

/*
   Read the next byte from the serial port and store it in the input buffer.
   <u>f</u> is the index returned by <i>port_find()</i>.
   This function is called asynchronously by the polling system when
   a POLLIN event is generated by the serial port.
   It returns the count of bytes just read,
   -1 if a fatal error ocurred or -2 if there is no
   space in the input buffer and in consequence the
   byte wasn't read.
*/
int port_read_one(int f)
{
   port_dets   *pd;
   int         r;
   char        rb[2];

   if (debug)
      fprintf(stderr, "port_read_one(%d) called\n", f);
      
   if (f >= 0)
   {
      pd = port_getdets(f);
      if (pd->open)
      {
         if (buff_freespace(pd->inbuff) >= 2)
         {
            if (pd->fd >= 0)
            {
               r = read(pd->fd, rb, 1);          /* Get a byte  */
               if (r > 0)
               {
                  rb[1] = 0;                     /* Add NULL for debugging */
                  r = buff_add_one(pd->inbuff, rb[0]);
                  if (r <= 0)
                  {
                     r = -1;
                     lasterr = "Unexpected inbuff overflow";
                  }
               }
               else
               if (r < 0)
               {
                  r = -1;
                  lasterr = strerror(errno);
               }

               /* a read reply of zero is end of input */
            }
            else
            {
               r = -1;
               lasterr = "Port marked open but fd is unset";
            }
         }
         else
         {
            r = -2;
            lasterr = "Input buffer is full";
         }
      }
      else
      {
         r = -1;
         lasterr = "Port is not open";
      }
   }
   else
   {
      r = -1;
      lasterr = "Invalid port array subscript";
   }

   if (debug)
      fprintf(stderr,
              "%d = port_read_one(%d) - read '%s'\n",
              r,
              f,
              byteout(rb[0]));

   return r;
}

/*
   Reads up to <u>bufflth</u> bytes into <u>buff</u> from
   the serial port's input buffer.
   <u>f</u> is the index returned by <i>port_find()</i>.
   It returns a count of the bytes read or -1 on error.
*/
int port_read(int f, char *buff, int bufflth)
{
   port_dets   *pd;
   int         i;
   int         r = 0;
   int         lth = 0;
   char        *b;

   if (f >= 0)
   {
      pd = port_getdets(f);
      lth = buff_extract_string(pd->inbuff,
                                buff,
                                bufflth,
                                pd->sep_active,
                                pd->sep_external,
                                pd->sep_marker);
      r = lth;
   }
   else
   {
      r = -1;
      lasterr = "Invalid port array subscript";
   }

   if (debug)
   {
      buff[lth] = 0;
      fprintf(stderr, "%d = port_read(%d,[", r, f);
      for (i = 0; i < lth; i++)
            fprintf(stderr, "%s", byteout(buff[i]));

      fprintf(stderr, "],%d)\n", bufflth);
   }
      
   return r;
}

/*   
   Set up baud rate, databits, parity and stopbits for the serial port.
   <u>i</u> is the index returned by <i>port_find()</i>.
   <u>line</u> is a comma separated list of settings in the format:

   baudrate,databits(5-8),parity(O,E,N),stopbits(1,2)

   After decoding, the settings are checked for validity and, if they
   pass this check, they are applied to the serial port. If the
   check fails the global error flag is set.
*/
void port_set_parameters(int i, char *line)
{
   port_dets   *pd;
   
   if (debug)
      fprintf(stderr, "port_set_parameters(%d,[%s])\n", i, line);

   /*
      Get the port control block pointer
   */
   pd = port_getdets(i);
   
   paramdecode(line,
               &pd->baud,
               &pd->databits,
               &pd->parity,
               &pd->stopbits);
   lasterr = paramcheck(pd->baud,
                        pd->databits,
                        pd->parity,
                        pd->stopbits);

   if (!lasterr)
   {
      /*
         Set the requested port settings
      */
      if (port_set_termio(i, debug))
      {
         lasterr = strerror(errno);
      }
   }

   if (debug)
      port_status(i);
}

/*   
   Set up the delay between writing characters to the serial port.
   <u>i</u> is the index returned by <i>port_find()</i>.
   <u>line</u> is the delay (in mS) as a character string.
   After decoding, the delay is checked for validity and, if it
   passes this check, it is applied to the serial port. If the
   check fails the global error flag is set.
*/
void port_set_delay(int i, char *line)
{
   port_dets   *pd;
   int         delay;
   
   if (debug)
      fprintf(stderr, "port_set_delay(%d,[%s])\n", i, line);

   delay = atoi(line);
   if (delay < 0 || delay > 1000)
      lasterr = "Tx delay is out of range (0-1000 mSec)";
   else
      lasterr = NULL;

   if (!lasterr)
   {
      /*
         Get the port control block pointer.
         Set the transmission delay
      */
      pd = port_getdets(i);
      pd->txdelay = delay;
   }

   if (debug)
      port_status(i);
}

/*   
   Set up record separator and conditions.
   <u>i</u> is the index returned by <i>port_find()</i>.
   The three values are the first three bytes in <u>line</u>.
   These are:
   <ol>
     <li>the <i>active</i> flag. Set on for values t,T or 1 and off for
         f,F or 0.</li>
     <li>The character that is to act as the field separator.</li>
     <li>the <i>external separator</i> flag. Set on for values t,T or 1
         and off for f,F or 0.</li>
   </ol>
*/
void port_set_separator(int i, char *line)
{
   port_dets   *pd;
   int         active;
   int         marker;
   int         ext;
   
   /*
      Get the port control block pointer
   */
   pd = port_getdets(i);
   
   /*
      Decode the serial parameters: AmE (active,marker,external)
      and set them up as the active values.

      Get the active flag, marker value and external flag
   */
   active = line[0];
   marker = line[1];
   ext = line[2];
   pd->sep_active = port_istrue(active);
   pd->sep_marker = marker;
   pd->sep_external = port_istrue(ext);

   if (debug)
   {
      fprintf(stderr,
              "port_set_separator(%d,%c%s%c)\n",
              i,
              active,
              byteout(marker),
              ext);
      port_status(i);
   }
}

/*   
   Reset the line settings, tx delay and record separator controls
   the their default conditions.
   <u>i</u> is the index returned by <i>port_find()</i>.
   The three values are the first three bytes in <u>line</u>.
   These are:
   <ol>
     <li>the <i>line settings</i> flag. Set on for values t,T or 1 and off for
         f,F or 0. If on, the line settings (baud rate, data bits, parity
         and stop bits are reset to the values defined in the configuration
         file for this port.</li>
     <li>the <i>tx delay</i> flag. Set on for values t,T or 1
         and off for f,F or 0. If on, the transmission delay is reset to
         zero delay.</li>
     <li>the <i>record separator</i> flag. Set on for values t,T or 1
         and off for f,F or 0. If on, the record separator control values
         are reset to the defaults: separator inactive, separator character
         is null, separator is internal.</li>
   </ol>
*/
void port_reset(int i, char *line)
{
   port_dets   *pd;
   int         linespec;
   int         delay;
   int         separator;
   
   /*
      Get the port control block pointer
   */
   pd = port_getdets(i);
   
   /*
      Decode the serial parameters: AmE (active,marker,external)
      and set them up as the active values.

      Get the line settings, tx delay and separator flags
   */
   linespec = line[0];
   delay = line[1];
   separator = line[2];

   /*
      Reset the line settings if required
   */
   if (port_istrue(linespec))
   {
      pd = port_getdets(i);
      pd->baud = pd->def_baud;
      pd->databits = pd->def_databits;
      pd->parity = pd->def_parity;
      pd->stopbits = pd->def_stopbits;

      /*
         Apply these to the port
      */
      if (port_set_termio(i, debug))
      {
         lasterr = strerror(errno);
      }
   }

   /*
      Reset the tx delay if required
   */
   if (port_istrue(delay))
   {
      pd->txdelay = 0;
   }

   /*
      Reset the record separator controls if required
   */
   if (port_istrue(separator))
   {
      pd->sep_active = FALSE;
      pd->sep_marker = 0;
      pd->sep_external = FALSE;
   }

   if (debug)
   {
      fprintf(stderr,
              "port_reset(%d,%c%c%c)\n",
              i,
              linespec,
              delay,
              separator);
      port_status(i);
   }
}

/*
   Sanity-check port parameters.
   <u>i</u> is the index returned by <i>port_find()</i>.
   It returns NULL if no problems were found and a string describing
   the problem if an error was encountered.
*/
char *port_check(int i)
{
   static char *r;
   port_dets   *pd;

   r = NULL;
   pd = port_getdets(i);
   if (!pd->port_id)
      r = "Missing port id";
   else
   if (param_charval_err(pd->type, valid_types))
      r = "Invalid device type";
   else
   if (!pd->device)
      r = "Missing device name";
   else
   if (param_charval_err(pd->active, active_vals))
      r = "Invalid active device flag"; 
   else
      r = paramcheck(pd->baud,
                     pd->databits,
                     pd->parity,
                     pd->stopbits);

   return r;
}

/*
   Retrieve a string containing the last error that ocurred on
   any serial port. The error details are held globally, so errors must
   be checked for and retrieved after every <i>port_xxx()</i> call.
*/
char *port_geterr()
{
   if (debug)
      fprintf(stderr, "[%s] = port_geterr()\n", lasterr);

   return lasterr;      
}
   
/*
   Return a string containing the serial port parameters.
   It is identical with the input string for <i>port_set_parameters()</i>
   (baud,databits,parity,stopbits,transmit delay).
   <u>i</u> is the index returned by <i>port_find()</i>.
*/
char *port_params(i)
{
   static char val[100];
   port_dets   *pd;

   pd = port_getdets(i);
   snprintf(val,
            100,
            "%d,%d,%c,%d",
            pd->baud,
            pd->databits,
            pd->parity,
            pd->stopbits);

   if (debug)
   {
      fprintf(stderr, "[%s] = port_params(%d)\n", val, i);
   }

   return val;
}

/*
   Private functions
   =================
   The functions defined from here on are only defined and used
   locally within this source file.
*/   

/*
   port_setup() - set up a port definition
   ============
*/
int port_setup(int i, char *line)
{
   char        *ptr;
   char        *saved_line;
   port_dets   *pd;
   int         n;
   int         errflag;
   
   if (debug > 1)
      fprintf(stderr, "port_setup(%d,[%s])\n", i, line);

   /*
      Save a copy of the line for error reporting
      and initialise the error count
   */
   saved_line = get_space(strlen(line+1));
   strcpy(saved_line, line);
   errflag = FALSE;
   
   /*
      Get the port control block pointer
      Initialise the control block
   */
   pd = port_getdets(i);
   pd->port_id = NULL;
   pd->type = 'X';
   pd->device = NULL;
   pd->active = 'X';
   pd->def_baud = -1;
   pd->def_databits = -1;
   pd->def_parity = 'X';
   pd->def_stopbits = -1;
   pd->open = FALSE;
   pd->fd = -1;
   pd->baud = -1;
   pd->databits = -1;
   pd->parity = 'X';
   pd->stopbits = -1;
   pd->txdelay = 0;
   pd->sep_active = FALSE;
   pd->sep_marker = 0;
   pd->sep_external = FALSE;
   pd->inbuff = 0;
   pd->outbuff = 0;
   
   /*
      Decode a device definition:
      port_id,device name,active,serial parameters
   */
   ptr = strchr(line, ',');
   n = ptr - line;
   if (ptr)
   {
      /*
         Get the name used to reference the serial device
      */      
      pd->port_id = (char*)get_space(n+1);
      strncpy(pd->port_id, line, n);
      pd->port_id[n] = 0;
      line = ++ptr;
      if (ptr)
      {
         /*
            Get the device type
         */
         pd->type = toupper(*line);
         line += 2;
         ptr = strchr(line, ',');
         n = ptr - line;
         
         if (ptr)
         {
            /*
               Get the absolute device name
            */      
            pd->device = (char*)get_space(n+1);
            strncpy(pd->device, line, n);
            pd->device[n] = 0;
            line = ++ptr;
            ptr = strchr(line, ',');
            n = ptr - line;
            if (ptr)
            {
               /*
                  Get the active/inactive flag
               */
               pd->active = toupper(*line);
               ptr++;
               /*
                  Handle the serial port parameters:
                  baud rate,databits,parity,stopbits,transmit delay
               */
               paramdecode(ptr,
                           &pd->baud,
                           &pd->databits,
                           &pd->parity,
                           &pd->stopbits);
            }
         }
      }
   }

   /*
      Set the defaults from the active port parameters.
      Check the parameters for validity.
   */
   port_store_defaults(i);
   ptr = port_check(i);
   if (ptr)
   {
      fprintf(stderr, "%s: Error in %s: %s\n", progname(), saved_line, ptr);
      errflag = TRUE;
   }

   /*
      Create the input and output buffers.
   */
   if (!errflag)
   {
      pd->inbuff = buff_create((max_message * 2));
      pd->outbuff = buff_create((max_message * 2));
   }
   
   /*
      Release the storage holding the saved line
   */
   release_space(saved_line);

   if (debug > 1)
      fprintf(stderr, "Port %d set-up: %s\n", i, port_status(i));

   return errflag;
}

/*
   port_defaults() - reset active serial port parameters
   ===============
*/
void port_defaults(int i)
{
   port_dets   *pd;

   pd = port_getdets(i);
   pd->baud = pd->def_baud;
   pd->databits = pd->def_databits;
   pd->parity = pd->def_parity;
   pd->stopbits = pd->def_stopbits;
   pd->txdelay = 0;
   pd->sep_active = FALSE;
   pd->sep_marker = 0;
   pd->sep_external = FALSE;
   if (debug > 1)
      fprintf(stderr, "Restored port settings: %s\n", port_status(i));
}


/*
   port_store_defaults() - store the default values for this port
   =====================
*/
void port_store_defaults(int i)
{
   port_dets   *pd;

   pd = port_getdets(i);
   pd->def_baud = pd->baud;
   pd->def_databits = pd->databits;
   pd->def_parity = pd->parity;
   pd->def_stopbits = pd->stopbits;
}

/*
   port_getdets() - return a pointer to a port control block
   ==============   or NULL
*/
port_dets *port_getdets(int n)
{
   port_dets *ptr = NULL;
   
   if (n >= 0 && n < devcount)
      ptr = ports[n];
   else
      error(progname(), "port_getdets(): ", "port subscript out of range");

   if (debug > 2)
      fprintf(stderr, "%p = port_getdets(%d)\n", ptr, n);
      
   return ptr;
}

/*
   port_istrue() - recognises a true flag. Can be T/F, t/f, 1/0
   =============
*/
int port_istrue(char flag)
{
   int r = FALSE;
   
   if (flag == 'T' || flag == 't' || flag == '1')
      r = TRUE;
   else
   if (flag == 'F' || flag == 'f' || flag == '0')
      r = FALSE;
   else
   if (flag)
      r = TRUE;
   else
      r = FALSE;

   return r;
}

/*
   port_save_termio() - save the original termio settings
   ==================   returns 0 for OK and -1 for error
*/
int port_save_termio(int i, int debug)
{
   port_dets   *pd;
   int         r = 0;

   if (debug > 1)
      fprintf(stderr, "port_save_termio()\n");
   
   pd = port_getdets(i);
   if (pd->type == 'P')
   {
      r = ioctl(pd->fd, TCGETA, &(pd->ts_orig));
      if (r < 0)
      {
         r = -1;
         lasterr = strerror(errno);
      }
   }
   
   if (debug > 1)
      fprintf(stderr, "%d = port_save_termio(%d, %d)\n", r, i, debug);

   return r;
}

/*
   port_set_termio() - set termio to config or requested values
   =================   returns 0 for OK and -1 for error
*/
int port_set_termio(int i, int debug)
{
   port_dets      *pd;
   int            fd;
   struct termio  ts;
   int            r = 0;
   int            speed = B0;
   int            databits = CS8;
   int            stopbits = 0;
   int            parity = 0;

   if (debug > 1)
      fprintf(stderr, "port_set_termio(%d,%d)\n", i, debug);
   
   pd = port_getdets(i);
   if (pd->type == 'P')
   {
      /*
         Set the port to the requested operating conditions.
         This is only done for a true port (type is P) and skipped
         if the port is being emulated by a file (type is F).
      */
      fd = port_getfd(i);

      /*
         Call the system dependent code to set the port's new
         operating conditions and retrieve the text of any error
         that is reported.
      */
      r = set_termio(fd,
                     pd->baud,
                     pd->databits,
                     pd->parity,
                     pd->stopbits,
                     debug);
      if (r < 0)
         lasterr = set_termio_error();
   }  

   if (debug > 1)
      fprintf(stderr, "%d = port_set_termio(%d, %d)\n", r, i, debug);

   return r;
}

/*
   port_restore_termio() - restore the original termio settings
   =====================   returns 0 for OK and -1 for error
*/
int port_restore_termio(int i, int debug)
{
   port_dets   *pd;
   int         r = 0;

   if (debug > 1)
      fprintf(stderr, "port_restore_termio()\n");
   
   pd = port_getdets(i);
   if (pd->type == 'P')
   {
      r = ioctl(pd->fd, TCSETA, &(pd->ts_orig));
      if (r < 0)
      {
         r = -1;
         lasterr = strerror(errno);
      }
   }
   
   if (debug > 1)
      fprintf(stderr, "%d = port_restore_termio(%d, %d)\n", r, i, debug);

   return r;
}
