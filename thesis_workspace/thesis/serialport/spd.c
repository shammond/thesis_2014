/* 
   <h3><i>spd</i>, the SerialPort server.</h3>
   
   <p><i>spd</i> provides remote access to serial ports for client processes
   using the Java <b>SerialPort</b> interface class or the <i>libsp.a</i>
   C interface library. See the <i>spd</i> manpage for more details.

   <ul>
     <li>Written by M.C.Gregorie.</li>
     <li>Version history</li>
       <ul>
         <li>1.0, Pre-release version 27/09/03</li>
         <li>1.1, Asynchronous serial port handling implemented 09/03/05</li>
         <li>1.2, Loopback testing facility added 24/12/05</li>
         <li>1.3, Can be run as a daemon 07/02/06</li>
       </ul>
     </li>
   </ul>

   The remainder of this page documents the internal functions
   declared in this header file.

   <kbd>
   <b>SerialPort - serial connections for Java</b><br>
   Copyright (C) 2005,2006  Martin C Gregorie
   
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

#define VERSION         "1.3"
#define COPYRIGHT       "(c) M.C.Gregorie, 2005,2006"

#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <errno.h>
#include <stropts.h>
#include <poll.h>
#include <environ.h>
#include <skt.h>
#include <fdlist.h>

#include "spd.h"

/*
   This function scans the command line for options and does
   all the associated setup.
*/
int readargs(int argc, char *argv[], int flags[], char **pipe)
{
   int         i; 
   int         opt;
   static char *nullstr = "";

   /* initialise flags */

   for (i=0 ; i<FLAGCNT ; i++)
      flags[i] = FALSE;

   flags[PORT] = DEFPORT;

   /* set up options from the command line */

   optmaybe = MAYBEMARK;
   optbad = BADMARK;
   optind = 1;
   *pipe = nullstr;
   
   while ((opt=getopt(argc,argv,OPTLIST)) != EOF)   
   {
      switch (opt)
      {
         case 0:         continue;
         case BADMARK:   return(opt);
         case '?':       flags[HELP] = TRUE;       
                         break;
         case 'd':       flags[DEBUG]++;
                         break;
         case 'D':       flags[DAEMON] = TRUE;
                         break;
         case 'p':       flags[PORT] = argval(optarg, 0);
                         check_range(flags[PORT], 1024, 65535, "port");
                         break;
         default :       error(argv[0],
                               "impossible happened parsing options",
                               "");
      }
   }
   return(OK);
}

/*
   Display a summary of the program's operation
   in response to the -? command line option.
*/
void showhelp()
{
   printf("\nspd v%s %s\n\n",VERSION,COPYRIGHT);
   printf("Syntax  : spd [<opts>]\n");
   printf("Function: Provides remote access to serial ports\n");
   printf("Options :\n");
   printf("     -d           debug mode\n");
   printf("     -D           run as a daemon\n");
   printf("     -p=port      listen on this port\n");
   printf("                  default is %d\n", DEFPORT);
}

/*
   <p>This function contains the essential outline of the program.
      It directly controls the following actions, handing off all the
      detailed processing to other functions apart from running
      the main polling loop:</p>
   <ul>
      <li>parsing and processing the the command line arguments</li>
      <li>reading configuration file and setting up the serial port
          control blocks</li>
      <li>creating the listener socket</li>
      <li>setting up polling</li>
      <li>executing the main polling loop until the program is
          stopped by a command from <i>spc</i> or a kill signal
          is received.</li>
      <li>finally, tidying up by closing all connections and terminating.</li>
   </ul>
*/
int main(int argc, char *argv[])
{
   char                 *pipename;
   int                  flags[FLAGCNT];
   int                  timeout;
   int                  reply;
   int                  listener;
   int                  run;
   int                  connect_count;
   int                  port_count;
   FDL                  *pl;
   int                  caught_fd;
   int                  signum;
   int                  nevents;
   int                  event;

   /*
      Parse and set up command options
   */
   setprog(argv[0]);
   if (readargs(argc,argv,flags,&pipename) == BADMARK)
   {
      return(1);
   }

   /*
      If help was asked for, give it and quit
   */
   if (flags[HELP]) 
   {
      showhelp();
      return(0);
   }

   /*
      Option validation
   */
   if (!flags[PORT])
   {
      error(progname(), "Must specify a port to listen on", "");
   }

   /*
      Become a daemon if required.
      On success the parent exits.
      Following the daemon() call we will only continue
      as the child or if the call failed.
   */
   if (flags[DAEMON])
   {
      if (daemon(TRUE, TRUE) != 0)
      {
         error(progname(), "failed to become a daemon:", strerror(errno));
      }
   }
   
   /*
      Read in configuration details.
      Initialise TCP/IP session storage
      These functions abandon the run if they encounter errors.
   */
   port_count = port_config(progname(), CONFIG_FILE, flags[DEBUG]);
   session_init(port_count, flags[DEBUG]);

   /*
      Set up socket function debugging and open a port
   */
   skt_debug((flags[DEBUG] - 1));
   if (flags[DEBUG])
   {
      fprintf(stderr, "Using port: %d\n", flags[PORT]);
   }
      
   if ((listener = skt_listen("", flags[PORT])) < 0)
      error(progname(), skt_error(), "");

   /*
      Plumb in polling
      Set debug levels, create the list and add the listener to it
   */
   fdlist_debug((flags[DEBUG] - 1));
   if (!(pl = fdlist_create()))
      error(progname(), "can't create fd list", "");

   if (fdlist_add(pl, listener, LISTENER) != ADDED)
      error(progname(), "couldn't add listener to fd list", "");

   /*
      Set up the signal handler
   */
   set_sigtrap_debug(flags[DEBUG] - 1);
   if ((signum = set_sigtraps()) != 0)
      error(progname(), "Can't set handler for", get_sigtrap_name(signum));

   /*
      Set initial poll timeout and clear the connection count
   */
   timeout = DEFTIMEOUT;
   connect_count = 0;
   /*
      Report run conditions if debugging is on
   */
   if (flags[DEBUG])
   {
      fprintf(stderr, "Poll timeout is %d mSec\n", timeout);
      fprintf(stderr, "Server must be killed or sent a STOP command\n");
   }

   /*
      Main loop
      =========
   */
   run = TRUE;
   while(run)
   {
      /*
         Wait for an event and handle it
      */
      nevents = poll(fdlist_fds(pl), fdlist_size(pl), timeout);

      switch (nevents)
      {
         case -1: if (errno == EINTR)
                  {
                     /*
                        A signal has been trapped
                     */
                     if (flags[DEBUG])
                     {
                        signum = get_sigtrap_type();
                        fprintf(stderr,
                                "Caught signal: %s\n",
                                get_sigtrap_name(signum));
                     }

                     run = FALSE; /* Force shutdown */
                  }
                  else
                     error(progname(), "Polling error:", strerror(errno));

                  break;

         case 0:  /*
                     Timeout: no special actions are required
                  */
                  if (flags[DEBUG] > 1)
                      fprintf(stderr, "Poll timeout\n");

                  break;

         default: /*
                     Poll events caught: deal with them in turn
                  */
                  for (event = 0; event < nevents; event++)
                  {
                     caught_fd = fdlist_getfd(pl);

                     if (caught_fd < 1)
                     {
                        /*
                           Orphan POLLIN or POLLOUT event.
                           The file has been closed and removed from
                           the poll list.
                        */
                        if (flags[DEBUG])
                           fprintf(stderr,
                                   "*** fd %d (event %d of %d): skipping\n",
                                    caught_fd,
                                    event,
                                    nevents);
                                    
                        continue;
                     }
                                  
                     if (fdlist_islistener(pl, caught_fd) == LISTENER)
                     {
                        /*
                           Event came from the listener:
                           accept an incoming connection
                           and build a session control block for it:
                        */
                        if (accept_connection(pl, caught_fd, flags[DEBUG]))
                           connect_count++;
                     }
                     else
                     {
                        /*
                           Event on non-listener connection:
                           identify the event type and action it
                        */
                        run = handle_event(pl,
                                           caught_fd,
                                           &connect_count,
                                           port_count,
                                           flags[DEBUG]);
                     }

                  } 
      }
   }

   /*
      Server termination
      ==================
   */

   skt_closelistener(listener, pipename);
   
   if (flags[DEBUG])
   {
      fprintf(stderr, "Listener closed: server stopping\n");
   }
   
   return(0);
}

/*
   Accept an incoming connection request. set up a socket
   and add it to the poll list. Create a session control
   block for the connection.
*/
int accept_connection(FDL *pl, int caught_fd, int debug)
{
   int   result;
   int   reply = FALSE;
   int   connection;
   char  dec[10];

   if ((connection = skt_accept(caught_fd)) < 0)
      error(progname(), skt_error(), "");

   result = fdlist_add(pl, connection, CONNECTION);
   if (result == ADDED)
   {
      reply = TRUE;
      if (debug)
         fprintf(stderr, "Client connected as %d\n", connection);

      session_create(connection, TCP_SESSION);
   }
   else
   if (result == ON_LIMIT)
   {
      reply = FALSE;
      skt_close(connection);
      if (debug)
         fprintf(stderr, "Client connection rejected: limit reached\n");
   }
   else
      error(progname(),
            "failed adding connection to fd list:",
            itoa(caught_fd, dec, 10));
}

/*
   <p>Decide what event happened to a polled file and deal
   with it appropriately. Polled files include all open
   TCP/IP connections and any open serial port: both cause
   POLLIN events whenever data is available to be read.
   Serial ports will also generate POLLOUT events whenever there
   is data waiting to be sent. Outbound polling is not used
   for TCP/IP connections.</p>

   <p><b>Note:</b> if a serial port has been configured as
   a filestore file for development testing, it may generate a
   continuous stream of POLLIN events even after EOF has been
   reached and without any data being available. <i>handle_event()</i>
   will handle these events correctly. This is the case under Linux
   kernel 2.4 and may possibly happen with later kernels as well.
   No attempt is made to turn off POLLIN events because a serial
   port does not exhibit this behavior.</p>
*/
int handle_event(FDL *pl,
                 int caught_fd,
                 int *connect_count,
                 int port_count,
                 int debug)
{
   short       event_flags;
   char        buff[80];
   char        dec[10];
   int         run = TRUE;
   int         connected = TRUE;
   int         ignored_event = TRUE;
   int         sd;
   int         portref;
   int         psref;
   int         fd;
   int         r;
   short       pollflags;

   event_flags = fdlist_geteventtype(pl, caught_fd);
   if (debug > 1)
      fprintf(stderr,
              "fd %d event flags: %s\n",
              caught_fd,
              fdlist_decode(event_flags));
      
   if (event_flags & POLLIN)
   {
      /*
         Input data is waiting. Process it. The required runstate
         is returned. This is 'continue' unless a STOP command of
         some sort was received from the client.
      */
      ignored_event = FALSE;
      if (session_type(caught_fd) == TCP_SESSION)
      {
         sd = session_find(caught_fd); 
         run = respond_to_input(pl,
                                caught_fd,
                                sd,
                                &connected,
                                port_count,
                                debug);
      }
      else
      {
         /*
            Incoming data has been found on a serial port.
            Grab it and stuff it into the input buffer. It will
            be removed from there by the next SP_GET command.
            If the buffer is full (port_read_one returns -2)
            turn off the POLLIN flag. It will be turned on again once
            a successful GET request has been executed.
         */
         ignored_event = FALSE;
         portref = session_get_ref(caught_fd);
         r = port_read_one(portref);
         if (r == -2)
         {
            if (fdlist_getevents(pl, caught_fd, &pollflags) == 0)
               fdlist_setevents(pl, caught_fd, pollflags & !POLLIN);
            else
               error(progname(),
                     "error setting POLLIN off:",
                     "input buffer full");
         }
         else
         if (r < 0)
            error(progname(), "error reading from a port:", port_geterr());
      }
   }

   if (event_flags & POLLOUT)
   {
      /*
         An Output file is ready to accept data. As we don't use
         async output to sockets, this can only be data for a serial
         port. We just grab the next byte from the output buffer and
         stuff it down the line. However, if the buffer is empty,
         we clear the POLLOUT flag to turn off output for this port.
         The next time an SP_PUT command is received the POLLOUT flag
         will be reinstated.
      */
      ignored_event = FALSE;
      portref = session_get_ref(caught_fd);
      r = port_write_one(portref);
      if (r == 0)
      {
         fd = port_getfd(portref);
         if (fdlist_getevents(pl, fd, &pollflags) == 0)
         {
            pollflags = pollflags - POLLOUT;
            fdlist_setevents(pl, fd, pollflags);
         }
         else
            error(progname(),
                  "error setting POLLOUT off:",
                  "output buffer empty");
      }

      if (r < 0)
         error(progname(), "Error writing to a port", port_geterr());
   }
   

   if (event_flags & POLLHUP || !connected)
   {
      /*
         The connection has been closed.
         Close it in the server,
         remove the connection from the polling list,
         decrease the count of active connections.
      */
      ignored_event = FALSE;
      skt_close(caught_fd);
      if (fdlist_del(pl, caught_fd) < 0)
         error(progname(),
               "couldn't delete connection to fd list",
               itoa(caught_fd, dec, 10));
                                    
      *connect_count = *connect_count -1;

      /*
         If the session is a TCP session we must check to see if
         it has a port still open and, if so, close it and delete its
         session so that future attempts to open the port won't fail.
         The TCP session is then deleted.
      */
      if (session_type(caught_fd) == TCP_SESSION)
      {
         if (session_has_ref(caught_fd))
         {
            sd = session_find(caught_fd);       /* Validate the TCP sesh ref  */
            psref = session_get_ref(sd);        /* Get the ptr to port sesh   */
            if (psref >=0)                      /* Port session exists        */
            {
               portref = session_get_ref(psref);/* Get PCB ref from session   */
               fd = port_getfd(portref);        /* Get fd from PCB            */
               port_close(portref, TRUE);       /* Force port closed          */
               if (fdlist_del(pl, fd) < 0)      /* Remove it from poll list   */
                  error(progname(),
                        "couldn't delete connection to fd list",
                        itoa(fd, dec, 10));
               session_delete(fd);              /* and delete port session    */
            }
            
            session_set_ref(sd, -1, FALSE);     /* Unset ref in TCP session   */
          }
   
         session_delete(caught_fd);             /* Finally delete TCP session */
      }
      
      if (debug)
      {
         fprintf(stderr, "Connection %d closed by the client\n", caught_fd);
      }
   }

   if (ignored_event)
   {
      /*
         Any other event is treated as an error
      */
      snprintf(buff,
               sizeof(buff),
               "unexpected event flag 0x%04x (%s) on connection %d",
               event_flags,
               fdlist_decode(event_flags),
               caught_fd);
      error(progname(), buff, "");
   }

   return run;
}

/*
   <p>Take the actions needed to receive and respond to a message.</p>
   
   <p>Incoming commands are read from the connection and passed to
   <i>spd_cmd_handler</i>, which takes the appropriate action and
   returns a response, which this function sends back to the command's
   originator. It also handles zero length messages: these indicate
   that the client has closed the connection.</p>
 
   <p>It returns FALSE if a command was received to stop the server and
   TRUE otherwise. On return <u>connected</u> is TRUE if the connection
   is still live and FALSE otherwise.</p>
*/
int respond_to_input(FDL *pl,
                     int fd,
                     int sd,
                     int *connected,
                     int port_count,
                     int debug)
{
   int   run = TRUE;
   int   reply_lth = 0;
   int   cmdlen;
   char  cmd[TCPBUFF];
   int   response_lth = TCPBUFF * port_count;
   char  *response;

   if (debug > 1)
      fprintf(stderr,
              "Called respond_to_input(%p,%d,%d,%d,%d,%d)\n",
              pl,
              fd,
              sd,
              *connected,
              port_count,
              debug);

   *connected = TRUE;
   response = (char*)get_space(response_lth+1);                            
   cmdlen = skt_receive(fd, cmd, sizeof(cmd));
   if (cmdlen > 0)
   {
      /*
         spd_cmd_handler is an externally defined application specific
         function. It must decode and act on the input before returning
         a response message. If the input causes the server to shut down
         it will return a negative response length and an acknowledgment
         response.
      */
      reply_lth = spd_cmd_handler(pl,
                                  fd,
                                  sd,
                                  cmd,
                                  cmdlen,
                                  response,
                                  response_lth,
                                  debug);
      run = (reply_lth > 0);
      if (skt_send(fd, response, abs(reply_lth)) < 0)
      {
         error(progname(), "error sending response:", strerror(errno));
      }
   }
   else
   {
      /*
         A zero length message signals that the client
         has closed the connection
      */
      if (debug > 1)
         fprintf(stderr, "Zero length message: client disconnected\n");

      *connected = FALSE;
   }

   if (debug > 1)
      fprintf(stderr,
              "%d = respond_to_input(%d,%d,%d)\n",
              run,
              fd,
              *connected,
              debug);

   return run;
}

