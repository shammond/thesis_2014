/*
   <p>This module contains message handling code for <i>spd</i>,
   the serial port server. These functions analyse and execute
   commands received from client processes via the <i>libsp.a</i>
   C interface library or the <b>SerialPort</b> Java interface class.</p>

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

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <ctype.h>
#include <string.h>
#include <poll.h> 
#include <fdlist.h> 
#include <environ.h>

#include "spd_message.h"
#include "spd_codes.h"
#include "spd.h"

/*
   Private function templates
   ==========================
*/
int cmd_status(void *output, int outputlth, int dbg);
int cmd_stop(void *output, int outputlth, int dbg);

int sp_close(FDL *pl,
             int sd,
             char *value,
             void *output,
             int outputlth,
             int dbg);
int sp_delay(int sd, char *value, void *output, int outputlth, int dbg);
int sp_get(FDL *pl, int sd, char *size, void *output, int outputlth, int dbg);
int sp_lookup(char *value, void *output, int outputlth, int dbg);
int sp_open(FDL *pl,
            int sd,
            char *value,
            void *output,
            int outputlth,
            int dbg);
int sp_put(FDL *pl,
           int fd,
           int sd,
           char *value,
           int valuelth,
           void *output,
           int outputlth,
           int dbg);
int sp_query(int sd, void *output, int outputlth, int dbg);
int sp_reset(int sd, char *value, void *output, int outputlth, int dbg);
int sp_setport(int sd, char *value, void *output, int outputlth, int dbg);
int sp_setsep(int sd, char *value, void *output, int outputlth, int dbg);

char *spd_status_report(int bufflth);

int unknown_command(void *output, int outputlth, int dbg);

/*
   This module contains message handling code for <i>spd</i>,
   the serial port server. These functions analyse and execute
   commands received from client processes via the <i>libsp.a</i>
   C interface library or the <b>SerialPort</b> Java interface class.
   The function returns the length of the response message or a negated
   response length if the server is to shut down.

   This is the only externally referenced function in this module.
*/
int spd_cmd_handler(FDL *pl,
                    int fd,
                    int sd,
                    void *input,
                    int inputlth,
                    void *output,
                    int outputlth,
                    int debug)
{
   int   dbg = (debug < 1 ? 0 :debug - 1);
   int   reply_lth = 0;
   char  command[CMDBUFF];
   char  value[TCPBUFF];
   int   vlth;

   if (dbg)
      fprintf(stderr,
              "spd_cmd_handler(%p,%d,%d,input,%d,output,%d,%d) called\n",
              pl,
              fd,
              sd,
              inputlth,
              outputlth,
              debug);
              
   /*
      Unpack the message and act on it
   */
   vlth  = unpack_message(input,
                          inputlth,
                          command,
                          CMDBUFF,
                          value,
                          TCPBUFF,
                          dbg);

   if (strcasecmp(command, SPC_STATUS) == 0)
      reply_lth = cmd_status(output, outputlth, dbg);
   else
   if (strcasecmp(command, SPC_STOP) == 0)
   {
      reply_lth = cmd_stop(output, outputlth, dbg);
      reply_lth = -reply_lth;
   }
   else
   if (strcasecmp(command, SP_CLOSE) == 0)
      reply_lth = sp_close(pl, sd, value, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_DELAY) == 0)
      reply_lth = sp_delay(sd, value, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_GET) == 0)
      reply_lth = sp_get(pl, sd, value, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_LOOKUP) == 0)
      reply_lth = sp_lookup(value, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_OPEN) == 0)
      reply_lth = sp_open(pl, sd, value, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_PUT) == 0)
      reply_lth = sp_put(pl, fd, sd, value, vlth, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_QUERY) == 0)
      reply_lth = sp_query(sd, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_RESET) == 0)
      reply_lth = sp_reset(sd, value, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_SETSEP) == 0)
      reply_lth = sp_setsep(sd, value, output, outputlth, dbg);
   else
   if (strcasecmp(command, SP_SETUP) == 0)
      reply_lth = sp_setport(sd, value, output, outputlth, dbg);
   else
      reply_lth = unknown_command(output, outputlth, dbg);

   if (dbg)
   {
      fprintf(stderr,
              "%d = spd_msg_handler(%p,%d,%d,input,%d,output,%d,%d)\n",
              reply_lth,
              pl,
              fd,
              sd,
              inputlth,
              outputlth,
              debug);
   }
   
   return reply_lth;
}

/*
   cmd_status() - STATUS request from spc.
   ============
*/
int cmd_status(void *output, int outputlth, int dbg)
{
   char *status;
   
   status = spd_status_report(TCPBUFF);
   return build_message(output,
                        outputlth,
                        SP_ACK,
                        strlen(status),
                        status,
                        dbg);
}

/*
   cmd_stop() - STOP command received from spc.
   ==========
*/
int cmd_stop(void *output, int outputlth, int dbg)
{
   if (dbg)
      fprintf(stderr, "STOP command received\n");

   return build_message(output,
                        outputlth,
                        SP_ACK,
                        2,
                        "OK",
                        dbg);
}

/*
   sp_close() - Close a port.
   ==========
*/
int sp_close(FDL *pl,
             int sd,
             char *value,
             void *output,
             int outputlth,
             int dbg)
{
   char  *status;
   int   psref;
   int   portref;
   int   fd;
   int   force;
   int   reply_lth;

   /*
      Determine whether forced close is required
   */
   if (strlen(value) == 0)
      force = FALSE;
   else
   {
      switch (value[0])
      {
         case 'T':
         case 't':
         case '1':
            force = TRUE;
            break;

         default:
            force = FALSE;
      }
   }

   if (dbg)
      fprintf(stderr,
              "value=%s force=%d - %s\n",
              value,
              force,
              (force ? "TRUE" : "FALSE"));
      
   /*
      Get hold of the port details.
      Save the fd before closing the port.
   */
   psref = session_get_ref(sd);
   portref = session_get_ref(psref);
   fd = port_getfd(portref);
   if (port_close(portref, force) < 0)
   {
      /*
         Port wasn't closed
      */
      status = port_geterr();
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                strlen(status),
                                status,
                                dbg);
   }
   else
   {
      /*
         The port is now closed.
         Remove the file from the poll list.
         Delete the port session control block, which is
         referenced by the port fd.
         Remove the port reference from the TCP session control block.
      */
      if (fdlist_del(pl, fd) == -1)
         error(progname(), "can't find a poll list entry to delete it","");
      
      session_delete(psref);
      session_set_ref(sd, -1, FALSE);
      reply_lth = build_message(output,
                                outputlth,
                                SP_ACK,
                                0,
                                "",
                                dbg);
   }

   if (dbg)
      fprintf(stderr,
              "%d = sp_close(%p,%d,%s,%s,&d,%d)\n",
              reply_lth,
              pl,
              sd,
              value,
              output,
              outputlth,
              dbg);
              
   return reply_lth;
}

/*
   sp_delay() - Change the current port's transmission delay
   ============
*/
int sp_delay(int sd, char *value, void *output, int outputlth, int dbg)
{
   int   psref;
   int   portref;
   char  *status;
   int   reply_lth;
   
   if (session_has_ref(sd))
   {
      psref = session_get_ref(sd);            /* Find port session    */
      portref = session_get_ref(psref);       /* and extract port ref */
      port_set_delay(portref, value);
      if (dbg)
         fprintf(stderr, "Port tx delay reset: %s\n", port_status(portref));
         
      status = port_check(portref);
      if (!status)
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ACK,
                                   0,
                                   "",
                       dbg);
      else
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ERROR,
                                   strlen(status),
                                   status,
                                   dbg);
   }
   else
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                spd_errlth(SPEPORTCLOSED),
                                spd_errmsg(SPEPORTCLOSED),
                                dbg);

   return reply_lth;
}

/*
   sp_get() - Get up to size bytes of input from the current port.
   ========   'size' must be converted to an integer first.
*/
int sp_get(FDL *pl, int sd, char *size, void *output, int outputlth, int dbg)
{
   char  *input;
   int   inputlth;
   char  *status;
   int   read_length = 0;
   int   spref;
   int   portref;
   short pollflags;
   int   reply_lth;

   read_length = atoi(size);
   spref = session_get_ref(sd);
   if (spref >= 0)
   {
      portref = session_get_ref(spref);
      if (portref >= 0)
      {
         input = get_space(read_length + 1);
         if ((inputlth = port_read(portref, input, read_length)) >=0)
         {
            /*
               Return the retrieved value to the client.
               Make sure the POLLIN flag is on for this port
            */
            reply_lth = build_message(output,
                                      outputlth,
                                      SP_GET,
                                      inputlth,
                                      input,
                                      dbg);
            if (fdlist_getevents(pl, spref, &pollflags) == 0)
               fdlist_setevents(pl, spref, pollflags | POLLIN);
            else
               error(progname(), "sp_get() failed:", "can't set POLLIN");
         }
         else
         {
            /*
               Read operation failed
            */
            status = port_geterr();
            reply_lth = build_message(output,
                                      outputlth,
                                      SP_ERROR,
                                      strlen(status),
                                      status,
                                      dbg);
         }

         input = release_space(input);
      }
      else
      {
         /*
            Bad port id in a port session: fatal error
         */
         error(progname(),
               "sp_get() failed",
               "Bad port id in a port session control block");  
      }
   }
   else
   {
      /*
         No port assigned to the TCB session.
         Probably because somebody else has the port open.
         Return a suitable error.
      */
      status = "GET attempt when port is not open";
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                strlen(status),
                                status,
                                dbg);
   }

   return reply_lth;
}

/*
   sp_lookup() - Request a list of available ports.
   ===========
*/
int sp_lookup(char *value, void *output, int outputlth, int dbg)
{
   strncpy(value, port_selection(), TCPBUFF);
   return build_message(output,
                        outputlth,
                        SP_LOOKUP,
                        strlen(value),
                        value,
                        dbg);
}

/*
   sp_open() - Open a port.
   =========
*/
int sp_open(FDL *pl,
            int sd,
            char *value,
            void *output,
            int outputlth,
            int dbg)
{
   int   portref;
   int   fd;
   char  *status;
   char  *errstr;
   int   pd;
   int   result;
   int   reply_lth;

   if (dbg)
      fprintf(stderr,
              "sp_open(%p,%d,%s,%s,%d,%d) entered\n",
              pl,
              sd,
              value,
              output,
              outputlth,
              dbg);
              
   portref = port_find(value);
   if (portref < 0)
   {
      /*
         Port does not exist or is marked inactive
      */
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                spd_errlth(portref),
                                spd_errmsg(portref),
                                dbg);
   }
   else
   {
      pd = port_open(portref);
      if (pd >=0)
      {
         /*
            This prevents a second open from destroying
            the port reference thats already in the session
            control block.

            Save the portref in the TCP session control block to link the two.
            Build a session control block for the port keyed on its fd.
            Save the port session reference in the TCP session control block
            as a forward link.
         */
         fd = port_getfd(portref);                 /* get fd of port        */
         result = fdlist_add(pl, fd, CONNECTION);  /* add port to poll list */
         if (result == ADDED)
         {
            if (dbg)
               fprintf(stderr, "Port added as CONNECTION %d\n", fd);

            session_create(fd, PORT_SESSION);   /* Create  Port session     */
            session_set_ref(sd, fd, TRUE);      /* Link it to TCP session   */
            session_set_ref(fd, portref, TRUE); /* Put PCB ref into it      */
            status = port_params(portref);      /* Get baud rate, etc       */
            reply_lth = build_message(output,   /* and report success.      */
                                      outputlth,
                                      SP_OPEN,
                                      strlen(status),
                                      status,
                                      dbg);
         }
         else
         {
            /*
               Failed to add the port to the polling list.
               This is a fatal error.
            */
            error(progname(),"Can't add a port session for", value);
	 }         
      }
      else
      {
         status = port_geterr();
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ERROR,
                                   strlen(status),
                                   status,
                                   dbg);
      }
   }

   if (dbg)
      fprintf(stderr, "%d = sp_open() exited\n", reply_lth);

   return reply_lth;
}

/*
   sp_put() - Send data to the current port.
   ========
*/
int sp_put(FDL *pl,
           int caught_fd,
           int sd,
           char *value,
           int valuelth,
           void *output,
           int outputlth,
           int dbg)
{
   char  *status;
   int   portref;
   int   psref;
   int   fd;
   short pollflags;
   int   reply_lth;

   psref = session_get_ref(sd);               /* get port session ref       */
   if (psref >= 0)
   {
      portref = session_get_ref(psref);       /* get port from session */
      if (portref >=0)
      {
         if (port_write(portref, value, valuelth) == valuelth)
         {
            /*
               Enable output polling to kick off the transfer
            */
      
            fd = port_getfd(portref);
            if (fdlist_getevents(pl, fd, &pollflags) == 0)
               fdlist_setevents(pl, fd, pollflags | POLLOUT);
            else
               error(progname(), "sp_put() failed:", "can't set POLLOUT");
         
            /*
               Build response message
            */
            reply_lth = build_message(output,
                                      outputlth,
                                      SP_ACK,
                                      0,
                                      "",
                                      dbg);
         }
         else
         {
            /*
               Write operation failed
            */
            status = port_geterr();
            reply_lth = build_message(output,
                                      outputlth,
                                      SP_ERROR,
                                      strlen(status),
                                      status,
                                      dbg);
         }
      }
      else
      {
         /*
            No port fcb id in the port session:
            this is a fatal error
         */
         error(progname(),
               "sp_put() failed",
               "Bad port id in a port session control block");  
      }
   }
   else
   {
      /*
         No port assigned to the TCB session.
         Probably because somebody else has the port open.
         Return a suitable error.
      */
      status = "PUT attempt when port is not open";
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                strlen(status),
                                status,
                                dbg);
   }

   return reply_lth;
}

/*
   sp_query() - Request the nomber of bytes in input and output buffers.
   ==========
*/
int sp_query(int sd, void *output, int outputlth, int dbg)
{
   int   psref;
   int   portref;
   char  valbuff[20];
   int   reply_lth;

   psref = session_get_ref(sd);
   portref = session_get_ref(psref);

   snprintf(valbuff,
            20,
            "%d,%d,%d",
            port_getincount(portref),
            port_getoutcount(portref),
            port_getbuffsize(portref));
   return build_message(output,
                        outputlth,
                        SP_QUERY,
                        strlen(valbuff),
                        valbuff,
                        dbg);
}

/*
   sp_reset() - Reset the line settings, tx delay and separator
   ==========   to their default values.
*/
int sp_reset(int sd, char *value, void *output, int outputlth, int dbg)
{
   int   psref;
   int   portref;
   char  *status;
   int   reply_lth;
   
   if (session_has_ref(sd))
   {
      psref = session_get_ref(sd);
      portref = session_get_ref(psref);
      port_reset(portref, value);
      if (dbg)
         fprintf(stderr, "Port reset: %s\n", port_status(portref));
         
      status = port_check(portref);
      if (!status)
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ACK,
                                   0,
                                   "",
                                   dbg);
      else
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ERROR,
                                   strlen(status),
                                   status,
                                   dbg);
   }
   else
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                spd_errlth(SPEPORTCLOSED),
                                spd_errmsg(SPEPORTCLOSED),
                                dbg);

   return reply_lth;
}

/*
   sp_setport() - Change the current port's operating parameters
   ============
*/
int sp_setport(int sd, char *value, void *output, int outputlth, int dbg)
{
   int   psref;
   int   portref;
   char  *status;
   int   reply_lth;
   
   if (session_has_ref(sd))
   {
      psref = session_get_ref(sd);            /* Find port session    */
      portref = session_get_ref(psref);       /* and extract port ref */
      port_set_parameters(portref, value);
      if (dbg)
         fprintf(stderr, "Port reset: %s\n", port_status(portref));
         
      status = port_check(portref);
      if (!status)
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ACK,
                                   0,
                                   "",
                       dbg);
      else
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ERROR,
                                   strlen(status),
                                   status,
                                   dbg);
   }
   else
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                spd_errlth(SPEPORTCLOSED),
                                spd_errmsg(SPEPORTCLOSED),
                                dbg);

   return reply_lth;
}

/*
   sp_setsep() - Set the current port's record separator
   ===========
*/
int sp_setsep(int sd, char *value, void *output, int outputlth, int dbg)
{
   int   psref;
   int   portref;
   char  *status;
   int   reply_lth;
   
   if (session_has_ref(sd))
   {
      psref = session_get_ref(sd);
      portref = session_get_ref(psref);
      port_set_separator(portref, value);
      if (dbg)
         fprintf(stderr, "Separator set: %s\n", port_status(portref));
         
      status = port_check(portref);
      if (!status)
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ACK,
                                   0,
                                   "",
                                   dbg);
      else
         reply_lth = build_message(output,
                                   outputlth,
                                   SP_ERROR,
                                   strlen(status),
                                   status,
                                   dbg);
   }
   else
      reply_lth = build_message(output,
                                outputlth,
                                SP_ERROR,
                                spd_errlth(SPEPORTCLOSED),
                                spd_errmsg(SPEPORTCLOSED),
                                dbg);

   return reply_lth;
}

/*
   spd_status_report() - generate a status report message
   ===================
*/
char *spd_status_report(int bufflth)
{
   static char *buff = 0;
   static char one_det[TCPBUFF];
   int         i;
   int         n;
   char        *pdet;

   if (!buff)
      buff = (char*)get_space(bufflth);
      
   strcpy(buff, "");
   n = port_getcount();
   for (i = 0; i < n; i++)
   {
      pdet = port_status(i);
      snprintf(one_det, TCPBUFF-1, "%s\n", pdet);
      strncat(buff, one_det, TCPBUFF-1);  
   }

   return buff;
}

/*
   unknown_command() - Unknown command received.
   =================
*/
int unknown_command(void *output, int outputlth, int dbg)
{
   return build_message(output,
                        outputlth,
                        SP_ERROR,
                        spd_errlth(SPEBADCMD),
                        spd_errmsg(SPEBADCMD),
                        dbg);
}
