/*
   <h3><i>sptarget</i>, a target system emulator.</h3>
   
   <p><i>sptarget</i> is a C program that acts as the remote target system
   during <i>spd</i> testing. See the <i>sptarget</i> manpage for more
   details.</p>

   <ul>
     <li>Written by M.C.Gregorie.</li>
     <li>Version history</li>
       <ul>             
         <li>1.0, Initial release 23/02/05</li>
         <li>1.1, Added the logging option 07/03/05</li>
         <li>1.2, Added statistics output 21/03/05</li>
         <li>1.3, Enabled W and P commands for Linux 12/04/05</li>
         <li>1.4, Added L and M commands 06/07/05</li>
       </ul>
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

#define VERSION     "1.4"
#define COPYRIGHT   "(c) M.C.Gregorie, 2005"

#include <environ.h>

#ifdef OSK
#include <stdio.h>
#include <modes.h>
#include <strings.h>
#include <argparse.h>   /* header for getopt, getarg */
#endif

#ifdef MSDOS
#include <stdio.h>
#include <modes.h>
#include <stdlib.h>
#include <string.h>
#include <argparse.h>   /* header for getopt, getarg */
#endif

#ifdef LINUX
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <time.h>
#include <string.h>
#endif

#include "sysdependent.h"
#include "sptarget.h"

#ifndef TRUE
#define	TRUE	1
#endif

#ifndef FALSE
#define	FALSE	0
#endif

#define	FLAGCNT	3

#define HELP	 	0  		/* -? help */
#define	DEBUG		1		/* -d debug mode */
#define	LOGGING		2		/* -l log input and output */

#define MAYBEMARK 	'%'
#define BADMARK		'!'
#define OK			' '
#define OPTLIST 	"?dlz%"

/*
   Private function templates
*/
#ifdef OSK
int runtests();
int paramval();
void paramstring();
void sendfile();
void savetext();
int cleartext();
void sendtext();
void setline();
void reportline();
void delay_start();
void write_one();
void fix_newline();
int lread();
int lwrite();
void millisleep();
#else
int runtests(int debug);
int paramval(int debug);
void paramstring(int sl, char *s, int debug);
void sendfile(char *file, int wait, int pause, int debug);
void savetext(int length, char *store, int debug);
int cleartext(char *store, int debug);
void sendtext(int length, char *store, int wait, int pause, int debug);
void setline(char *params, int debug);
void reportline(int debug);
void delay_start(int wait, int debug);
void write_one(char x, int pause, int debug);
void fix_newline(char x);
int lread(int fd, char *buff, int bufflth);
int lwrite(int fd, char *buff, int bufflth);
void millisleep(int wait);
#endif

/*
   logfile controls are global
*/
char  *logname = "sptarget.log";
FILE  *lg      = 0;
int   logging  = FALSE;
int   logdir   = 0;
char  logbuff[MAXLINE];
int   charsin;
int   charsout;

/*
   Line control values: set with the L command
   and reported with the M command
*/
int   baud = 0;
int   dbits = 8;
int   sbits = 1;
char  parity = 'N';
char  line_status[MAXLINE];
char  line_params[MAXLINE];

/*
   This function scans the command line for options and does
   all the associated setup.
*/
int readargs(argc,argv,flags)
int   argc;
char  *argv[];
int   flags[];
{
   int   i, 
         opt;

   /* initialise flags */

   for (i=0 ; i<FLAGCNT ; i++) flags[i] = FALSE;

   /* set up options from the command line */

   optmaybe = MAYBEMARK;
   optbad = BADMARK;
   optind = 1;
   while ((opt=getopt(argc,argv,OPTLIST)) != EOF)   
   {
      switch (opt) {
         case 0:        continue;
         case BADMARK:  return(opt);
         case '?':      flags[HELP] = TRUE;       
                        break;
         case 'd':      flags[DEBUG]++;
                        break;
         case 'l':      flags[LOGGING] = TRUE;
                        break;
         default :      error(argv[0],
                              "impossible happened parsing options",
                              "");
      }
   }
   return(OK);
}

/*
   Display brief usage details in response to the -? option.
*/
void showhelp()
{
   printf("\nsptarget v%s %s\n\n",VERSION,COPYRIGHT);
   printf("Syntax  : sptarget [<opts>]\n");
   printf("Function: Provides a test target for the serial port server.\n");
   printf("          Takes no parameters except option(s).\n");
   printf("          LINUX:    W and P enabled\n");
   printf("          DOS, OS9: W and P have no effect\n");
   printf("          Input 'Q' to exit if input is from stdin.\n");
   printf("Options :\n");
   printf("    -d    debug mode\n");
   printf("    -l    logging mode.\n");
   printf("          Copies all input and output to %s\n", logname);
}

/*
   The program entry point
*/
int main(argc,argv)
int   argc;
char  *argv[];
{
   int   flags[FLAGCNT],
         processed,
         reply;

   setprog(argv[0]);
   
   /*
     Parse and set up command options
   */
   if (readargs(argc,argv,flags) == BADMARK)
      return(1);

   /*
      If help was asked for, give it and quit
   */
   if (flags[HELP]) 
   {
      showhelp();
      return(0);
   }

   /*
      The following code drives the application. 
   */
   logging = flags[LOGGING];
   if (logging)
   {
      lg = fopen(logname, "w");
      if (!lg)
         error("sptarget", "can't open the log file:", logname);
   }
   
   reply = runtests(flags[DEBUG]);

   if (logging)
      fclose(lg);
   
   return(reply);
}

/*
 * This function defines the application.
 * It interprets the input stream. Any commands, which
 * are all upper case letters, will be executed to manipulate
 * the output stream. All other characters in the input stream
 * are copied to the output stream.
 */
int runtests(debug)
int debug;
{
   int   reply = 0;
   int   run;
   char  x[MAXLINE];
   char  s[MAXLINE];
   char  cmd;
   char  n;

   int   wait = 0;           /* mS wait before outputting text */
   int   pause = 0;          /* mS pause between outputting characters */
   char  file[MAXLINE+1];    /* Name of file to be output */
   int   length = 0;         /* Length of stored text */
   char  store[MAXLINE+1];   /* Text store */

   if (debug)
      fprintf(stderr, "Entered runtests()\n");

   if (logging)
      fprintf(lg, "Start of log in %s\n", logname);

   charsin = 0;
   charsout = 0;
   length = cleartext(store, debug);
   run = TRUE;
   while (run)
   {
      n = lread(0, x, 1);
      x[1] = 0;
      if (!n)
         cmd = 'Q';      /* Force end at EOF */
      else
         cmd = x[0];
         
      if (debug)
      {
         if (index("QWPSDATELM", cmd))
            fprintf(stderr, " Command is %c\n", cmd);
         else
         if (debug > 1)
            fprintf(stderr, " copied %c\n", cmd);
      }
         
      switch(cmd)
      {
         case 'Q':         /* Terminate the run */
            run = FALSE;
            sprintf(store, "\nRead %d  Wrote %d\n", charsin, charsout);
            sendtext(strlen(store), store, wait, pause, debug);
            break;

         case 'W':         /* Set wait before a command's output */
            wait = paramval(debug);
            break;
            
         case 'P':         /* Set pause before each byte output */
            pause = paramval(debug);
            break;
            
         case 'S':         /* Send a file */
            paramstring(MAXLINE, file, debug);
            sendfile(file, wait, pause, debug);
            break;
            
         case 'D':         /* Discard input */
            length = paramval(debug);
            savetext(length, store, debug);
            length = cleartext(store, debug);
            break;
            
         case 'A':         /* Accept and store input */
            length = paramval(debug);
            savetext(length, store, debug);
            break;
            
         case 'T':         /* Transmit stored input */
            sendtext(length, store, wait, pause, debug);
            break;
            
         case 'E':         /* Echo a record */
            length = paramval(debug);
            savetext(length, store, debug);
            sendtext(length, store, wait, pause, debug);
            break;
         
         case 'L':         /* Change the line settings */
            paramstring(MAXLINE, line_params, debug);
            setline(line_params, debug);
            break;
         
         case 'M':         /* Display line settings */
            reportline(debug);
            sendtext(strlen(line_status), line_status, wait, pause, debug);
            break;
         
         default:
            fix_newline(cmd);
      }
   }

   if (logging && logdir)
   {
      /*
         logging is on and has written something.
         Output a terminator.
      */
      fprintf(lg, "]\nEnd of %s\n", logname);
   }
   
   if (debug)
      fprintf(stderr, "r = runtests()\n", reply);
   else
      printf("\n");

   return(reply);
} 

/*
   Read a command parameter as an int
*/
int paramval(debug)
int debug;
{
   int   n = 0;
   char  c[MAXLINE];
   char  x;
   int   i;

   for (i = 0; i < MAXLINE; i++)
   {
      lread(0, &x, 1);
      if (x == ';')
         break;

      c[i] = x;
   }

   c[i] = 0;
   n = atoi(c);
   if (debug)
      fprintf(stderr, " %d = paramval(%d)\n", n, debug);
   
   return n;
}

/*
   Read a command parameter as a string
*/
void paramstring(sl, s, debug)
int   sl;
char  *s;
int debug;
{
   char  x;
   int   i;

   for (i = 0; i < sl; i++)
   {
      lread(0, &x, 1);
      if (x == ';')
         break;

      s[i] = x;
   }

   s[i] = 0;
   if (debug)
      fprintf(stderr, " paramstring(%s, %d)\n", s, debug);
}

/*
   Output a file's contents
*/
void sendfile(file, wait, pause, debug)
char  *file;
int   wait;
int   pause;
int   debug;
{
   int   f;
   int   r;
   char  x;
   char  err[MAXLINE];

   if (debug)
      fprintf(stderr, "\nsendfile(%s,%d,%d,%d)\n", file, wait, pause, debug);

#ifdef OSK
   f = open(file, S_IREAD);
#else
   f = open(file, O_RDONLY);
#endif

   if (f < 0)
   {
      sprintf(err, "sptarget: can't open %s\n", file);
      sendtext(strlen(err), err, wait, pause, debug);
   }
   else
   {   
      delay_start(wait, debug);
      r = 1;
      while (r)
      {
         /*
            File content is not logged
         */
         r = read(f, &x, 1);
         if (r)
            write_one(x, pause, debug);
      }   
      close(f);
   }

   if (debug)
      fprintf(stderr, "\nsendfile() finished\n");
}

/*
   Store text following a command
*/
void savetext(length, store, debug)
int   length;
char  *store;
int   debug;
{
   lread(0, store, length);
   store[length] = 0;
   if (debug)
      fprintf(stderr, "\nsavetext(%d,[%s],%d)\n", length, store, debug);
}

/*
   Clear a text store
*/
int cleartext(store, debug)
char  *store;
int   debug;
{
   int   i;

   for (i = 0; i < MAXLINE; i++)
      store[i] = 0;

   if (debug)
      fprintf(stderr, "\n0 = cleartext(%p,%d)\n", store, debug);

   return 0;
}

/*
   Send stored text
*/
void sendtext(length, store, wait, pause, debug)
int   length;
char  *store;
int   wait;
int   pause;
int   debug;
{
   int   i;

   if (debug)
      fprintf(stderr,
              "\nsendtext(%d,%s,%d,%d,%d)\n",
              length,
              store,
              wait,
              pause,
              debug);

   if (wait)
      delay_start(wait, debug);

   if (pause)
      for(i = 0; i < length; i++)
         write_one(store[i], pause, debug);
   else
      lwrite(1, store, length);

   if (debug)
      fprintf(stderr, "\nsendtext() finished\n");
}

/*
   Alter the line settings using the parameter string in params.
   
*/
void setline(params, debug)
char *params;
int  debug;
{
   int r;
   
   sscanf(params, "%d,%d,%c,%d", &baud, &dbits, &parity, &sbits);

   /*
      Alter the device settings. This is highly system dependent,
      so it is in a separate source file.
   */
   r = set_termio(0, baud, dbits, parity, sbits, debug);
   if (r)
      error(progname(), "Error in set_termio():", set_termio_error());
      
   if (debug)
      fprintf(stderr,
              "setline(%s,%d): baud=%d dbits=%d parity=%c stopbits=%d\n",
              params, debug, baud, dbits, parity, sbits); 
}

/*
   Generate the output for the M command
*/
void reportline(debug)
int debug;
{
   if (baud == 0)
      sprintf(line_status, "Unset\n");
   else
      sprintf(line_status,
              "Baud=%d  Databits=%d  Parity=%c  Stopbits=%d\n",
              baud,
              dbits,
              parity,
              sbits);

   if (debug)
      fprintf(stderr, "reportline(): status = %s", line_status);
} 

/*
   Wait before starting to send output. This simulates
   a remote target with a slow response time.
*/
void delay_start(wait, debug)
int wait;
int debug;
{
   millisleep(wait);
   if (debug > 1)
      fprintf(stderr, "delay_start(%d mS)\n", wait);
}

/*
   Output a single character. If its is configured on,
   this function introduces a pause before each character
   is output to simulate the operation of a slow processor,
   e.g. a PIC chip.
*/
void write_one(x, pause, debug)
char  x;
int   pause;
int   debug;
{
   millisleep(pause);
   fix_newline(x);
}

/*
   All output is sent through this function, which fixes newlines
   as summarised below. All other characters are passed unchanged.
   It in turn sends all output via the lwrite() function.
   
   OSK:  Output CRLF in place of CR and drop LF if it appears in the input
   UNIX: Output CRLF in place of LF and drop CR if it appreas in the input
   MSDOS:Copy everything
*/ 
void fix_newline(x)
char  x;
{
   char   CR = 0x0d;
   char   LF = 0x0a;

#ifdef OSK   
   if (x == CR)
   {
      lwrite(1, &CR, 1);
      lwrite(1, &LF, 1);
   }
   else
   if (x != LF)
      lwrite(1, &x, 1);
#endif

#ifdef MSDOS   
   lwrite(1, &x, 1);
#endif

#ifdef LINUX   
   if (x == LF)
   {
      lwrite(1, &CR, 1);
      lwrite(1, &LF, 1);
   }
   else
   if (x != CR)
      lwrite(1, &x, 1);
#endif
}

/*
   This function provides a logged input stream.
   All input is read through it.
*/
int lread(fd, buff, bufflth)
int   fd;
char  *buff;
int   bufflth;
{
   int   n;
   int   i;
   
   n = read(fd, buff, bufflth);
   charsin += n;
   if (logging)
   {
      if (logdir == 0)
         strcpy(logbuff, " read [");
      else   
      if (logdir != 'r')
         sprintf(logbuff, "]\n read [");
         
      for (i = 0; i < n; i++)
      {
         if (strlen(logbuff) > MAXLINE-6)
         {
            logbuff[strlen(logbuff)] = 0;
            break;
         }

         strcat(logbuff, byteout(buff[i]));
      }

      fprintf(lg, "%s", logbuff);
      logdir = 'r';
      logbuff[0] = 0;
   }

   return n;
}

/*
   This function provides a logged output stream.
   All output passes through it.
*/
int lwrite(fd, buff, bufflth)
int   fd;
char  *buff;
int   bufflth;
{
   int   n;
   int   i;
   
   if (logging)
   {
      if (logdir == 0)
         strcpy(logbuff, " write [");
      else   
      if (logdir != 'w')
         sprintf(logbuff, "]\n write [");

      for (i = 0; i < bufflth; i++)
      {
         if (strlen(logbuff) > MAXLINE-6)
         {
            logbuff[strlen(logbuff)] = 0;
            break;
         }

         strcat(logbuff, byteout(buff[i]));
      }

      fprintf(lg, "%s", logbuff);
      logdir = 'w';
      logbuff[0] = 0;
   }

   n = write(fd, buff, bufflth);
   charsout += n;
   return n;
}

/*
   Pause for the specified number of milliseconds.
   This is only implemented for Linux: in other systems
   it returns immediately.
*/
void millisleep(wait)
int wait;
{
#ifdef LINUX
   
   struct timespec t;

   if (wait)
   {   
      t.tv_sec = wait / 1000;
      t.tv_nsec = (wait - t.tv_sec * 1000) * 1000;
      if (nanosleep(&t, NULL) < 0)
         error(progname(), "millisleep() error", strerror(errno));
   }
#endif

}
