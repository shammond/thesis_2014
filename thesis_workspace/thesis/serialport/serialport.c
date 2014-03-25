/*
   <h3>SerialPort - Java and C access to local or remote serial ports.</h3>
   
   <p>The SerialPort package provides a Java or C program with access
   to serial ports. This is the C interface module, used by a client
   program to access the SerialPort server, <i>spd</i>. It allows its
   caller to control the port's settings (speed, parity, etc) and to
   transfer data to and from a remote peer. Each open connection to a
   serial port has its own control block and operates independently
   of any other serial ports the program may be using. It uses TCP/IP
   to connect to <i>spd</i>, the server that handles serial ports on
   its behalf. <i>spd</i> is written in C. It is always run on the
   host which has the serial ports, but a SerialPort client can
   communicate with <i>spd</i> regardless of whether it is running on
   the same system or on a remote host.</p>

   <p>All functions contain in-line code with no internal delays apart from
   the <i>sp_sleep()</i> function, which is provided as a convenient way to
   wait for remote activity to occur and to allow for transmission time.
   There are no internal delays in the serial server. The intent
   is to allow a client program to choose when or if it needs to wait for
   external events. The functions do, however, contain sufficient
   validation to ensure that the function should complete during normal
   operation.</p>

   <p>The control block is created by calling <i>sp_create()</i> and
   finally deleted by calling <i>sp_delete()</i>. The latter fails if
   the connection to <i>spd</i> is still open. All other functions
   specify the control block as their first or only argument:
   its contents must not be altered except via the functions provided
   for that purpose. If they are called with a null pointer to
   the control block they will return the SPEINVPTR error.</p>

   <p><i>sp_listports()</i> establishes the connection it needs to
   retrieve the list of ports if one does not exist. Subsequent
   connection requests will use this connection. If a connection
   already exists the <i>sp_open()</i> function will re-use it.
   Pre-existing information in the control block, set up by
   <i>sp_create()</i>, is used to identify the hostname and port
   that will be used to establish a connection.</i>

   <p><i>sp_open()</i> claims the serial port to be used by the
   application and <i>sp_close()</i> releases it and closes the
   TCP connection to the server. A connection that has been closed
   can be re-opened by another call to <i>sp_open()</i>.</p>

   <p>Call <i>sp_delete()</i> to release the memory occupied by
   the control block. This will fail if the connection is still open.</p>

   <p>Function definitions are included by</p>

   <p><kbd>#include <serialport.h><br>
   #include <environ.h></kbd></p>

   <p>The second <kbd>include</kbd> pulls in a set of general
   purpose utilities used to support the interface functions.
   The executable must be linked with <i>libenviron.a</i> and
   <i>libsp.a</i>.</p>

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

   <p>The following sections describe the C interface functions
   which are contained in the <i>libsp.a</i> library module.</p>
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <termios.h>
#include <time.h>
#include <environ.h>
#include <skt.h>

#include "serialport.h"
#include "spd_codes.h"
#include "paramdecode.h"
#include "paramcheck.h"
#include "rxdelay.h"

/*
   Internal constants
   ==================
*/

char *errmsg[] =  {  "OK",
                     "Invalid control block pointer",
                     "Host connection lost",
                     "Host connection open",
                     "Host connection not open",
                     "Host not found: 'host:port'",
                     "Port not open",
                     "Port name too long for buffer",
                     "Server reported error",
                     "Sleep timer error",
                     "spd.conf definition for this port is wrong"
                  };

const int CLOSED  = -1;
const int OPEN    = 0;
const int MAXMSG  = 1500;
const int MAXCMD  = 10;
const int MAXERR  = 100;
const int RXFUDGE = 3;  /* Rx delay tuning constant */
const int TXFUDGE = 3;  /* Rx delay tuning constant */


/*
   Prototypes for internal functions
   =================================
*/
char  *bad_host(char *host, int port);
int   spd_error(SPC *cb, char *msg);
int   set_error(SPC *cb, int code);
int   sanity_check(SPC *cb);
int   connect_server(SPC *cb);
int   server_transact(SPC  *cb,
                      char *ccmd,
                      char *cval,
                      int  cvlth,
                      char *rcmd,
                      int  rclth,
                      char *rval,
                      int  rvlth);
char  translate_boolean(char b, int debug);

/*
   Public functions
   ================
*/
/*   
   <p>Create a control block and set the debugging level,
   the name of the host supporting the server, <i>spd</i>
   and the port number. The connection to <i>spd</i> is
   not established until <i>sp_open()</i> is called.
   If the server is in the same system as the client,
   set <u>host</u> to "localhost".
   NULL is returned on failure.</p>

   <p>The error notification
   function, <i>sp_error()</i>, cannot be used until the
   control block is successfully created.</p>
*/
SPC *sp_create(int debug, char *host, int port)
{
   SPC *cb;

   cb = (SPC*)get_space(sizeof(SPC));
   cb->debug = debug;
   cb->host = (char*)get_space(strlen(host)+1);
   strncpy(cb->host, host, strlen(host)+1);
   cb->port = port;
   set_error(cb, SPEOK);
   cb->connectstate = CLOSED;
   cb->portstate = CLOSED;

   if (cb->debug)
      fprintf(stderr,
             "%p = sp_create(%d,%s,%d)\n",
             cb,
             cb->debug,
             cb->host,
             cb->port);
             
   return cb;
}

/*
   Releases the control block if the connection has been closed.
   Otherwise it returns the SPEHOSTOPEN error.
   Returns -ve on error.
   Errors are SPEINVPTR, SPEHOSTOPEN
*/
int sp_delete(SPC **cb)
{
   int r;
   int debug; 
   SPC *cbt;

   cbt = *cb;
   debug = cbt->debug;

   r = sanity_check(cbt);
   if (r == SPEOK)
   {
      if (cbt->connectstate == OPEN)
         r = set_error(cbt, SPEHOSTOPEN);
      else
      {
         free(cbt->host);
         free(cbt);
         *cb = NULL;
      }
   }
  
   if (debug)
      fprintf(stderr, "%d = sp_delete(%p)\n", r, cbt);
             
   return r;
}

/*
   Returns a pointer to the error string or "OK" if no error ocurred.
*/
char *sp_error(SPC *cb)
{
   if (cb->debug)
      fprintf(stderr, "%s = sp_error()\n", cb->errmsg);

   return cb->errmsg;
}

/*
   <p>Returns a comma separated list of available ports as a string.
   <u>lth</u> is the maximum size of the buffer.</p>
   
   <p>Returns -ve on error.
   Errors are SPEINVPTR, SPEHOSTBAD, SPEPORTLONG.</p>
*/
int sp_listports(SPC *cb, char *ports, int lth)
{
   int   r;
   char  rcmd[MAXCMD];

   r = sanity_check(cb);
   if (r == SPEOK)
   {
      strcpy(ports, "");
      if (cb->connectstate == CLOSED)
         r = connect_server(cb);

      if (r == SPEOK)
         r = server_transact(cb,
                             SP_LOOKUP,
                             "",
                             0,
                             rcmd,
                             MAXCMD,
                             ports,
                             lth);
   }
   
   if (cb->debug)
      fprintf(stderr, "%d = sp_listports(%p,[%s],%d)\n", r, cb, ports, lth);

   return r;
} 

/*
   Open a connection to <i>spd</i> and claim the serial port
   identified by <u>name</u>.
   Default run time conditions are set by the <i>spd</i> configuration file.
   Returns -ve on error.
   Errors are: SPEINVPTR, SPEHOSTBAD, SPEUNKNOWN, SPEPORTUSED.
*/
int sp_open(SPC *cb, char *name)
{
   int   r;
   char  rcmd[MAXCMD];
   char  val[MAXERR];
   char  *param_err;

   r = sanity_check(cb);
   if (r == SPEOK)
   {
      if (cb->connectstate == CLOSED)
         r = connect_server(cb);

      if (r == SPEOK)      
      {
         r = server_transact(cb,
                             SP_OPEN,
                             name,
                             strlen(name),
                             rcmd,
                             MAXCMD,
                             val,
                             MAXERR);
         if (strcmp(rcmd, SP_OPEN) == 0)
         {
            /*
               Clear any error condition,
               mark the spd serial port state as open,
               decode and validate the serial port parameters
               returned by spd.
            */
            r = set_error(cb, SPEOK);
            cb->portstate = OPEN;
            cb->txdelay = 0;
            paramdecode(val,
                        &cb->baudrate,
                        &cb->databits,
                        &cb->parity,
                        &cb->stopbits);
            param_err = paramcheck(cb->baudrate,
                                   cb->databits,
                                   cb->parity,
                                   cb->stopbits);
            if (param_err == NULL)
            {
               /*
                  Parameters are OK: calculate the wait time per
                  character for sp_sleep() and clear byte counts.
                  NOTE: If the parameters 
               */
               cb->rxdelay = rxdelay(cb->baudrate,
                                     cb->databits,
                                     cb->parity,
                                     cb->stopbits,
                                     cb->txdelay,
                                     cb->debug);
               cb->txbytes = 0;
               cb->rxbytes = 0;
	    }
	    else
	    {
	       r = spd_error(cb, param_err);
	       r = sp_close(cb, "TRUE");
	       if (!r)
	          r = set_error(cb, SPEPARAMERR);
	    }
         }
         else
            r = spd_error(cb, val);
      }  
   }

   if (cb->debug)
      fprintf(stderr, "%d = sp_open(%p,%s)\n", r, cb, name);

   return r;
}

/*
   Releases the serial port and closes the connection.
   If the <u>force</u> argument starts with T, t or 1
   the serial port will be forced to close with possible data loss.
   Otherwise the serial port is not closed if there is still data
   in the buffers.
   Returns -ve on error. Errors are SPEINVPTR, SPEHOSTLOST.
*/
int sp_close(SPC *cb, char *force)
{
   int   r;
   char  cval[MAXCMD];
   char  rcmd[MAXCMD];
   char  val[MAXERR];

   r = sanity_check(cb);

   if (cb->connectstate == OPEN)
   {
      if (cb->portstate == OPEN)
      {
         if (force == NULL || strlen(force) == 0)
            strcpy(cval,"FALSE");
         else
            strcpy(cval, force);
            
         r = server_transact(cb,
                             SP_CLOSE,
                             cval,
                             strlen(cval),
                             rcmd,
                             MAXCMD,
                             val,
                             MAXERR);
         if (strcmp(rcmd, SP_ACK) == 0)
         {
            r = set_error(cb, SPEOK);
            cb->portstate = CLOSED;
         }
         else
            r = spd_error(cb, val);
      }
   }
      
   if (cb->connectstate == OPEN && cb->portstate == CLOSED)
   {
      skt_close(cb->connection);
      cb->connectstate = CLOSED;
   }
   
   if (cb->debug)
      fprintf(stderr, "%d = sp_close(%p, %s)\n", r, cb, force);
             
   return r;
}

/*
   Sets baud rate, bits (5-8), parity (O, E, N), stopbits (1,2).
   Calculates and stores the time (in mS) to read or write one
   character through the serial port.
   Returns -ve on error.
   Errors are  SPEINVPTR, SPEHOSTLOST.
*/
int sp_setport(SPC *cb,
               int baud,
               int bits,
               char parity,
               int stopbits)
{
   int   r;
   char  rcmd[MAXCMD];
   char  val[MAXERR];

   r = sanity_check(cb);
   if (cb->connectstate == OPEN)
   {
      snprintf(val,
               MAXMSG,
               "%d,%d,%c,%d",
               baud,
               bits,
               parity,
               stopbits);
      r = server_transact(cb,
                          SP_SETUP,
                          val,
                          strlen(val),
                          rcmd,
                          MAXCMD,
                          val,
                          MAXERR);
      if (strcmp(rcmd, SP_ACK) == 0)
      {
         /*
            The command was accepted:
            - clear previous error indication.
            - calculate the new wait time per character for sp_sleep()
         */
         r = set_error(cb, SPEOK);
         cb->baudrate = baud;
         cb->databits = bits;
         cb->parity = parity;
         cb->stopbits = stopbits;
         cb->rxdelay = rxdelay(baud,
                               bits,
                               parity,
                               stopbits,
                               cb->txdelay,
                               cb->debug);
      }
      else
      {
         r = spd_error(cb, val);
      }
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
      fprintf(stderr,
              "%d = sp_setport(%p,%d,%d,%c,%d)\n",
              r,
              cb,
              baud,
              bits,
              parity,
              stopbits);
             
   return r;
}

/*
   Gets baud rate, bits (5-8), parity (O, E, N), stopbits (1,2).
   If the port has not been opened successfully
   the returned values are meaningless.
   Returns -ve on error.
   Errors are  SPEINVPTR, SPEHOSTLOST.
*/
int sp_getport(SPC *cb,
               int *baud,
               int *bits,
               char *parity,
               int *stopbits)
{
   int   r;

   r = sanity_check(cb);
   if (cb->connectstate == OPEN)
   {
      *baud = cb->baudrate;
      *bits = cb->databits;
      *parity = cb->parity;
      *stopbits = cb->stopbits;
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
      fprintf(stderr,
              "%d = sp_getport(%p,%d,%d,%c,%d)\n",
              r,
              cb,
              *baud,
              *bits,
              *parity,
              *stopbits);
             
   return r;
}

/*
   Sets time that <i>spd</i> must wait between sending characters to
   the serial port. The delay time is specified in mS and must be in the
   range 0 - 1000 mS. The receive delay time is recalculated.
   Returns -ve on error.
   Errors are  SPEINVPTR, SPEHOSTLOST.
*/
int sp_delay(SPC *cb, int delay)
{
   int   r;
   char  rcmd[MAXCMD];
   char  val[MAXERR];

   r = sanity_check(cb);
   if (cb->connectstate == OPEN)
   {
      snprintf(val,
               MAXMSG,
               "%d",
               delay);
      r = server_transact(cb,
                          SP_DELAY,
                          val,
                          strlen(val),
                          rcmd,
                          MAXCMD,
                          val,
                          MAXERR);
      if (strcmp(rcmd, SP_ACK) == 0)
      {
         /*
            The command was accepted:
            - clear previous error indication.
            - calculate the new wait time per character for sp_sleep()
         */
         r = set_error(cb, SPEOK);
         cb->txdelay = delay;
         cb->rxdelay = rxdelay(cb->baudrate,
                               cb->databits,
                               cb->parity,
                               cb->stopbits,
                               cb->txdelay,
                               cb->debug);
      }
      else
      {
         r = spd_error(cb, val);
      }
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
      fprintf(stderr,
              "%d = sp_delay(%p,%d)\n",
              r,
              cb,
              delay);
             
   return r;
}

/*
   <p>Resets the line settings, tx delay and record separator to their
   default settinfs as required
   The arguments are boolean values encoded as T/F, t/f or 1/0.
   <ul>
     <li><u>line_settings</u>, if true, causes the baud rate, data bits,
     parity and stop bits to be reset to the values defined in the
     configuration file for this port.</li>
     <li><u>tx_delay</u>, if true, causes the delay between sernding
     successive bytes to be reset to zero.</li>
     <li><u>separator</u>, if true, causes the record separator
     control values to be reset to their default values: inactive
     separator, separator value is NULL, separator is internal to its
     record.</li>
   </ul>
   <p>Returns -ve on error.</p>
*/
int sp_reset(SPC *cb, char line_settings, char tx_delay, char separator)
{
   int   r;
   char  rcmd[MAXCMD];
   char  val[MAXERR];
   char  l;
   char  d;
   char  s;
   char  lstr[5];
   char  dstr[5];
   char  sstr[5];

   r = sanity_check(cb);
   l = translate_boolean(line_settings, cb->debug);
   d = translate_boolean(tx_delay, cb->debug);
   s = translate_boolean(separator, cb->debug);
   if (cb->connectstate == OPEN)
   {
      val[0] = l;
      val[1] = d;
      val[2] = s;
      val[3] = 0;
      r = server_transact(cb,
                          SP_RESET,
                          val,
                          3,
                          rcmd,
                          MAXCMD,
                          val,
                          MAXERR);
      if (strcmp(rcmd, SP_ACK) == 0)
         r = set_error(cb, SPEOK);
      else
         r = spd_error(cb, val);
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
   {
      strcpy(lstr, byteout(line_settings));
      strcpy(dstr, byteout(tx_delay));
      strcpy(sstr, byteout(separator));
      fprintf(stderr,
              "%d = sp_reset(%p,%s,%s,%s)\n",
              r,
              cb,
              lstr,
              dstr,
              sstr);
   }
          
   return r;
}

/*
   <p>Sets the record separator and its application conditions.
   The arguments <u>active</u> and <u>external</u> are
   boolean values encoded as T/F, t/f or 1/0.
   <u>active</u> determines whether the field separator
   character, set by <u>sep</u>, is in use. If it is active
   <i>sp_put()</i> and <i>sp_get()</i> operate at the field level.
   <i>sp_get()</i> stops when the separator character is found and
   <i>sp_put()</i> should send one field at a time.
   If <u>external</u> is true the separator character is added
   during an <i>sp_put()</i> operation and not returned to the
   caller by an <i>sp_get()</i> operation. If <u>external</u> is
   false the caller must ensure that each string sent by
   <i>sp_put()</i> is terminated by the separator character
   and must expect the separator at the end of the string
   returned by an <i>sp_get()</i> operation.</p>
   
   <p>The default is that <u>active</u> and <u>external</u> are
   false and <u>sep</u> is 0 (zero).</p>
  
   <p>Returns -ve on error.</p>
*/               
int sp_setsep(SPC *cb, char active, char marker, char ext)
{
   int   r;
   char  rcmd[MAXCMD];
   char  val[MAXERR];
   char  a;
   char  e;
   char  astr[5];
   char  mstr[5];
   char  estr[5];

   r = sanity_check(cb);
   a = translate_boolean(active, cb->debug);
   e = translate_boolean(ext, cb->debug);
   if (cb->connectstate == OPEN)
   {
      val[0] = a;
      val[1] = marker;
      val[2] = e;
      val[3] = 0;
      r = server_transact(cb,
                          SP_SETSEP,
                          val,
                          3,
                          rcmd,
                          MAXCMD,
                          val,
                          MAXERR);
      if (strcmp(rcmd, SP_ACK) == 0)
         r = set_error(cb, SPEOK);
      else
         r = spd_error(cb, val);
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
   {
      strcpy(astr, byteout(active));
      strcpy(mstr, byteout(marker));
      strcpy(estr, byteout(ext));
      fprintf(stderr,
              "%d = sp_setsep(%p,%s,%s,%s)\n",
              r,
              cb,
              astr,
              mstr,
              estr);
   }
          
   return r;
}

/*
   Sends <u>n</u> characters from the buffer to the port. 
   If requested by <i>sp_setsep()</i>, a terminator
   may be appended to the byte string.
   Returns -ve on error. Errors are SPEINVPTR, SPEHOSTLOST.
*/
int sp_put(SPC *cb, char *buff, int n)
{
   int   r;
   char  rcmd[MAXCMD];
   char  val[MAXERR];

   r = sanity_check(cb);
   if (cb->connectstate == OPEN)
   {
      r = server_transact(cb,
                          SP_PUT,
                          buff,
                          n,
                          rcmd,
                          MAXCMD,
                          val,
                          MAXERR);
      if (strcmp(rcmd, SP_ACK) == 0)
      {
         r = set_error(cb, SPEOK);
      }
      else
      {
         r = spd_error(cb, val);
      }
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
      fprintf(stderr, "%d = sp_put(%p,%s,%d)\n", r, cb, buff, n);
             
   return r;
}

/*
   <p>Reads up to <u>n</u> characters from the port and stores them 
   in <u>buff</u>.
   If requested by <i>sp_setsep()</i>, the separator character may
   terminate the characters read (active separator is true)
   and may be hidden from the caller (external separator is
   true).</p>
 
   <b>Note:</b> if the remote response is slower than
   expected an incomplete message may be returned. In this case
   the caller should wait for a suitable time and call
   <i>sp_get()</i> again. See the <i>sp_sleep()</i> function for 
   suggestions on preventing this.</p>
   
   <p><i>sp_get()</i> returns the number of characters read or
   -ve on error. Errors are SPEINVPTR, SPEHOSTLOST.</p>
*/
int sp_get(SPC *cb, char *buff, int n)
{
   int   r;
   int   lim;
   char  rcmd[MAXCMD];
   char  val[MAXMSG];

   r = sanity_check(cb);
   if (cb->connectstate == OPEN)
   {
      itoa(n, val, 10);
      r = server_transact(cb,
                          SP_GET,
                          val,
                          strlen(val),
                          rcmd,
                          MAXCMD,
                          val,
                          MAXMSG);
      if (strcmp(rcmd, SP_GET) == 0)
      {
         /*
            Return as many bytes as will fit in the buffer,
            accumulate the bytes read total
            and clear the error indication.
         */ 
         lim = min(r, n);
         strncpy(buff, val, lim);
         buff[lim] = 0;
         set_error(cb, SPEOK);
      }
      else
      {
         r = spd_error(cb, val);
      }
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
      fprintf(stderr, "%d = sp_get(%p,%s,%d)\n", r, cb, buff, n);
             
   return r;
}

/*
   <p>A convenience method that calls the <i>sp_put()</i>,
   <i>sp_sleep()</i> and <i>sp_get()</i> functions.
   Sends <u>outlth</u> bytes from <u>charsout</u> to the port by
   calling <i>sp_put()</i>.
   The <u>expected</u> and <u>adj</u> arguments are passed to
   <i>sp_sleep()</i>.
   <i>sp_get()</i> is called to retrieve the available bytes and
   put them in <u>charsin</u>. A maximum of <u>inlth</u> bytes will
   be read.
   If a terminator is active then it will terminate the input string.
   <i>sp_echo()</i> returns the number of characters read or -ve on
   error. Errors are SPEINVPTR, SPEHOSTLOST.</p>
*/
int sp_echo(SPC *cb,
            char *charsout,
            int outlth,
            int adj,
            char *charsin,
            int inlth)
{
   int   r;

   r = sp_put(cb, charsout, outlth);
   if (r == 0)
      r = sp_sleep(cb, inlth, adj);

   if (r == 0)
      r = sp_get(cb, charsin, inlth);
  
   if (cb->debug)
   {
      fprintf(stderr,
              "%d = sp_echo(%p,%s,%d,%d,%s,%d)\n",
              r,
              cb,
              charsout,
              outlth,
              adj,
              charsin,
              inlth);
   }
          
   return r;
}

/*
   Queries the server for the numbers of bytes in the input
   and output buffers plus the buffer size and decodes the result.
   Values are returned in <u>incount</u>, <u>outcount</u> and
   <u>buffsize</u> respectively.
   Returns -ve on error.
*/
int sp_query(SPC *cb, int *incount, int *outcount, int *buffsize)
{
   int   r;
   char  rcmd[MAXCMD];
   char  val[MAXMSG];

   r = sanity_check(cb);
   if (cb->connectstate == OPEN)
   {
      r = server_transact(cb,
                          SP_QUERY,
                          "",
                          0,
                          rcmd,
                          MAXCMD,
                          val,
                          MAXMSG);
      if (strcmp(rcmd, SP_QUERY) == 0)
      {
         sscanf(val, "%d,%d,%d", incount, outcount, buffsize);
         r = set_error(cb, SPEOK);
      }
      else
         r = spd_error(cb, val);
   }
   else
   {
      r = set_error(cb, SPEHOSTCLOSED);
   }
  
   if (cb->debug)
   {
      fprintf(stderr,
              "%d = sp_query(%p,%d,%d,%d)\n",
              r,
              cb,
              *incount,
              *outcount,
              *buffsize);
   }
          
   return r;
}

/*
   <p>Cause the calling process to sleep while the
   remote process generates and sends a reply.</p>
   
   <p>First it queries the server for buffer content.
   Then it calculates the delay time needed for all
   waiting bytes to be sent and all <u>expected</u>
   incoming bytes to have been received. Finally it adds
   on <u>adj</u>, which is in mS.</p>
   
   <p><i>sp_sleep()</i> waits for the calculated time.</p>
   
   <p>This is repeated until the output buffer is empty and
   the number of bytes in the input buffer stops changing
   or there are enough bytes in the buffer to match the
   expected number.</p>
    
   <p><u>adj</u> is a signed integer that is added to the
   calculated time. It is in mS and is intended to let
   the caller specify additional sleep time to make
   allowance for a remote process that is known to have a
   long response time. <u>adj</u> can also be used to
   adjust the sleep time downwards but it cannot reduce
   the time below zero.</p>
   
   <p>Returns -ve on error.</p>
*/
int sp_sleep(SPC *cb, int expected, int adj)
{
   int               r = 0;
   int               tval = 0;
   int               last_rx = 0;
   int               do_pause = TRUE; 
   struct timespec   t;

   while (do_pause)
   {
      r = sp_query(cb, &cb->rxbytes, &cb->txbytes, &cb->buffsize);
      if (r == SPEOK)
      {
         /*
            This is the basic delay time calculation
            If there is no tx delay specified, use the
            calculated Rx delay for both. Otherwise
            use Rx delay for expected bytes and Tx delay
            for bytes to be sent.
         */
         if (cb->txdelay != 0)
            tval = cb->rxdelay * expected * RXFUDGE +
                   cb->txdelay * cb->txbytes * TXFUDGE;
         else
            tval = cb->rxdelay * (expected + cb->txbytes) * RXFUDGE;

         /*
            Now add in the adjustment factor,  making sure
            the time can't go negative
         */
         if (tval + adj < 0)
            tval = 0;       
         else
            tval += adj;

         /*
            Set up the timer and sleep for the calculated time
         */
         t.tv_sec = tval / 1000;
         t.tv_nsec = (tval - t.tv_sec * 1000) * 1000;
         r = nanosleep(&t, NULL);
         if (r < 0)
         {
            cb->errmsg = strerror(errno);
         }
         else
         {
            set_error(cb, SPEOK);
         }

         if (cb->debug > 0)
         {
            fprintf(stderr,
                    "expected=%d rxdelay=%d rxfudge=%d\n",
                    expected,
                    cb->rxdelay,
                    RXFUDGE);
            fprintf(stderr,
                    "txbytes=%d txdelay=%d txfudge=%d\n", 
                     cb->txbytes,
                     cb->txdelay,
                     TXFUDGE);
            fprintf(stderr, "adj=%d\n", adj);
            fprintf(stderr,
                    "tval=%d mS (%d Sec, %d uS) errno %d, %s\n",
                    tval,
                    t.tv_sec,
                    t.tv_nsec,
                    errno,
                    cb->errmsg);
         }

         last_rx = cb->rxbytes;

         /*
            End the wait if all output bytes have been sent
            and either we have enough bytes on hand to cover the
            expected message size or no more bytes are coming in
         */
         if (cb->txbytes == 0 &&
             (cb->rxbytes >= expected || cb->rxbytes == last_rx))
            do_pause = FALSE;
      }

      if (r != SPEOK)
         do_pause = FALSE;
   }

   cb->rxbytes = 0;
   cb->txbytes = 0;
   if (cb->debug)
   {
      fprintf(stderr, "%d = sp_sleep(%p,%d,%d)\n", r, cb, expected, adj);
   }

   return r;
}

/*
   Helper functions
   ================

   bad_host() - build a bad host message
   ==========
*/
char *bad_host(char *host, int port)
{
   char        *fmt = "Host not found: %s:%d";
   static char *s = NULL;
   int         n;

   n = strlen(fmt) + strlen(host) + 10;
   if (s)
   {
      s = (char*)release_space(s);
      s = (char*)get_space(n);
   }
   else
      s = (char*)get_space(n);

   snprintf(s, n, fmt, host, port);
   return(s);
}

/*
   spd_error() - set up an spd originated error message
   ===========
*/
int spd_error(SPC *cb, char *msg)
{
   static char *s = NULL;

   if (s)
   {
      s = release_space(s);
      s = (char*)get_space(strlen(msg)+1);
   }
   else
      s = (char*)get_space(strlen(msg)+1);

   strncpy(s, msg, strlen(msg)+1);
   cb->errmsg = s;
   return(-SPEREMERR);
}

/*
   set_error() - set the current error code and message
   ===========
*/
int set_error(SPC *cb, int code)
{
   if (cb)
      cb->errmsg = errmsg[code];
      
   return -code;
}

/*
   sanity_check() - check the control block pointer is set
   ==============
*/
int sanity_check(SPC *cb)
{
   int r = SPEOK;

   if (!cb)
      r = set_error(cb, SPEINVPTR);

   return r;
}

/*
   connect_server() - Connect to the spd server
   ================
*/
int connect_server(SPC *cb)
{
   int   r;

   skt_debug((cb->debug - 1));
   if ((cb->connection = skt_connect(cb->host, cb->port)) > 0)
   {
      cb->connectstate = OPEN;
      r = set_error(cb, SPEOK);
   }
   else
   {
      r = -SPEHOSTBAD;
      cb->errmsg = bad_host(cb->host, cb->port);
   }

   return r;
}

/*
   server_transact() - send a command to the server and retrieve the result
   =================   Returns value length or -ve for errors.
*/
int server_transact(SPC *cb,
                    char *ccmd,
                    char *cval,
                    int  cvlth,
                    char *rcmd,
                    int  rclth,
                    char *rval,
                    int  rvlth)
{
   int   r;
   int   n;
   int   debug = cb->debug;
   char  command[MAXMSG];
   int   command_lth;
   char  response[MAXMSG];
   int   response_lth;

   if (debug < 0)
      debug = 0;

   if (debug > 1)
   {
      fprintf(stderr,
              "server_transact(%p,%s,cval,%d,.....)\n",
              cb,
              ccmd,
              cvlth);
      dumphex(stderr, cval, cvlth);
   }

   r = set_error(cb, SPEHOSTLOST);
   command_lth = build_message(command,
                               sizeof(command),
                               ccmd,
                               cvlth,
                               cval,
                               debug);
   if (skt_send(cb->connection, command, command_lth) >= 0)
   {
      n = skt_receive(cb->connection, response, sizeof(response));
      if (n > 0)
      {
         response_lth = unpack_message(response,
                                       n,
                                       rcmd,
                                       rclth,
                                       rval,
                                       rvlth,
                                       debug);
         set_error(cb, SPEOK);
         r = response_lth;
      }
   }  

   if (debug > 1)
   {
      fprintf(stderr,
              "%d = server_transact(%p,%s,cval,%d,%s,%d,rval,%d)\n",
              r,
              cb,
              ccmd,
              cvlth,
              rcmd,
              rclth,
              rvlth);
      fprintf(stderr, "rval %d bytes:\n", r);
      dumphex(stderr, rval, r);
   }
           
   return r;
}

/*
   translate_boolean() - translate the input to a boolean representation
   ===================   acceptable to spd.
*/
char  translate_boolean(char b, int debug)
{
   char x;

   if (b == 'T' || b == 't' || b == '1')
      x = 'T';
   else
   if (b == 'F' || b == 'f' || b == '0')
      x = 'F';
   else
   if (b)
      x = 'T';
   else
      x = 'F';

   if (debug > 1)
      fprintf(stderr,
              "%c = translate_boolean(%c<0x%02x>)\n",
              x,
              (isascii(b) && isalnum(b) ? b : '.'),
              b);
              
   return x;   
}


