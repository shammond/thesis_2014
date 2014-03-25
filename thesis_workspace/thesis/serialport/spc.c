/*
   <h3><i>spc</i>, the SerialPort control program.</h3>
   
   <p>This is used to control <i>spd</i>, the <b>SerialPort</b> server.
   It is used to request the server's status and to shut it down.
   See the <i>spc</i> manpage for more details.</p>

   <ul>
     <li>Written by M.C.Gregorie.</li>
     <li>Version history
       <ul>
         <li>1.0, Initial release 22/05/04</li>
       <ul>
     </li>
   </ul>

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

#define VERSION         "1.0"
#define COPYRIGHT       "(c) M.C.Gregorie, 2004"

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <environ.h>
#include <skt.h>

#include "spc.h"

#ifndef TRUE
#define TRUE       1
#endif

#ifndef FALSE
#define FALSE      0
#endif

#define FLAGCNT    4

#define HELP       0      /* -? help */
#define DEBUG      1      /* -d debug mode */
#define HOST       2      /* -h=host */
#define PORT       3      /* -p=port */

#define MAYBEMARK  '%'
#define BADMARK    '!'
#define OK         ' '
#define OPTLIST    "?dh:p:"

#define RESPHDDR   7
#define DEFHOST    "localhost"
#define DEFPORT    16001

/*
   Private function declarations
*/
int readargs(int argc, char *argv[], int flags[], char **host);
void showhelp();
int do_request(int skt, char *request, int debug);

/*
   This function scans the command line for options and does
   all the associated setup.
*/
int readargs(int argc, char *argv[], int flags[], char **host)
{
   int         i; 
   int         opt;
   static char *nullstr = "";
   static char *defaulthost = DEFHOST;

   /* initialise flags */

   for (i=0 ; i<FLAGCNT ; i++)
      flags[i] = FALSE;

   flags[PORT] = DEFPORT; 

   /* set up options from the command line */

   optmaybe = MAYBEMARK;
   optbad = BADMARK;
   optind = 1;
   *host = defaulthost;
   
   while ((opt=getopt(argc,argv,OPTLIST)) != EOF)   
   {
      switch (opt) {
         case 0:      	 continue;
         case BADMARK:   return(opt);
         case '?':       flags[HELP] = TRUE;       
                         break;
         case 'd':       flags[DEBUG]++;
                         break;
         case 'h':       flags[HOST] = TRUE;
                         *host = (char*)malloc(max(strlen(optarg),1));
                         if (*host == NULL)
                         {
                            error(argv[0],
                                  "can't allocate space",
                                  "for -h host name");
                         }
                            
                         strcpy(*host,optarg);
                         break;
         case 'p':       flags[PORT] = argval(optarg, 0);
                         if (flags[PORT] < 1024 || flags[PORT] > 65535 )
                         {
                            error(argv[0],
                                  "port must be in range",
                                  "1024 - 65535");
                         }
                         break;
         default :       error(argv[0],
                               "impossible happened parsing options",
                               "");
      }
   }
   return(OK);
}

/*
   Show the punter what the options are
*/
void showhelp()
{
   printf("\nspc v%s %s\n\n",VERSION,COPYRIGHT);
   printf("Syntax  : spc [<opts>] commands [<opts>] \n");
   printf("Function: Send control commands to spd and display its response\n");
   printf("Options :\n");
   printf("     -d           debug mode\n");
   printf("     -h=hostname  connect to a server on the named host\n");
   printf("                  default is '%s'\n", DEFHOST);
   printf("     -p=port      connect to the server using this port\n");
   printf("                  default is %d\n", DEFPORT);
   printf("Commands:\n");
   printf("     status       show spd's current operational state\n");
   printf("     stop         shut down spd\n");
}

/*
   The main program starts here
   ----------------------------
*/
int main(int argc, char *argv[])
{
   char  *hostname;
   char  buffer[MAXNAME];
   int   port;
   int   flags[FLAGCNT];
   int   skt;
   int	 processed;
   int	 finished;
   int	 argfound;
   int   n;
   int   reply;

   /*
      Parse and set up command options
   */
   reply = 0;
   setprog(argv[0]);
   if (readargs(argc,argv,flags,&hostname) == BADMARK)
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
      Set socket debug levels
   */
   skt_debug((flags[DEBUG] - 1));

   if (flags[DEBUG])
   {
      fprintf(stderr, "Using host:port -  %s:%d\n", hostname, flags[PORT]);
   }

   if ((skt = skt_connect(hostname, flags[PORT])) < 0)
      error(progname(), skt_error(), "");

   if (flags[DEBUG])
   {
      if (skt_getname(skt, REMOTE_HOST, buffer, MAXNAME) < 0)
         error(progname(), skt_error(), "");

      port = skt_getport(skt, REMOTE_HOST);
      if (port < 0)
         error(progname(), skt_error(), "");
            
      fprintf(stderr, "Server host:port - %s:%d\n", buffer, port);

      if (skt_getname(skt, LOCAL_HOST, buffer, MAXNAME) < 0)
         error(progname(), skt_error(), "");

      port = skt_getport(skt, LOCAL_HOST);
      if (port < 0)
         error(progname(), skt_error(), "");
            
      fprintf(stderr, "Client host:port - %s:%d\n", buffer, port);
   }

   /*
      The following code drives the application. 
   */
   optind = 1;
   processed = 0;
   argfound = 0;

   while (argfound != EOF)
   {
      argfound = getarg(argc,argv);
      if (argfound == 1)
      {
         processed++;
         strncpy(buffer, optarg, MAXNAME);
         finished = do_request(skt, buffer, flags[DEBUG]);
         if (finished)
         {
            reply = 2;
            break;
         }
      }
   }

   skt_close(skt);

   if (flags[DEBUG])   
      printf("%d requests processed\n", processed);
   else
   if (processed == 0)
      error(progname(), "no commands supplied for spd to process","");
   return(reply);
}

int do_request(int skt, char *request, int debug)
{
   int   quit;
   int   n;
   char  response[RESPHDDR];
   int   valsize;
   char  *val;
   int   errflag;

   if (debug)
   {
      fprintf(stderr, "Request is %d bytes: [%s]\n", strlen(request), request);
   }
   
   quit = FALSE;
   if (skt_send(skt, request, strlen(request)) < 0)
   {
      fprintf(stderr,
              "%s: Error sending %s: %s\n",
              progname(),
              request,
              strerror(errno));
   }
   else
   {
      n = skt_receive(skt, response, RESPHDDR);
      if (n == 0)
      {
         printf("%s: Server closed connection\n", progname());
         quit = TRUE;
      }
      else
      {         
         if (debug)
            printf("Hddr: %d [%s]\n", strlen(response), response);

         if (n == RESPHDDR)
         {
            errflag = (strstr(response, "E,") ? TRUE : FALSE);
            valsize = atoi(response+2);
            val = (char*)get_space(valsize+1);
            n = skt_receive(skt, val, valsize);
            if (debug)
               printf("Value: %d [%s]\n", strlen(val), val);
               
            if (n == valsize)
            {
               if (errflag)
                  printf("%s: error: %s\n", progname(), val);
               else
                  printf("%s\n", val);
            }
            else
            {
               printf("%s: System error: malformed message = [%s%s]\n",
                      progname(),
                      response,
                      val);
            }
         }
         else
         {
            printf("%s: System error: malformed message = [%s]\n",
                   progname(),
                   response);
         }
         
         fflush(stdout);
      }
   } 
   
   return quit;
}
