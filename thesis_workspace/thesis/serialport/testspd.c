/*
   <h3><i>testspd</i>, a SerialPort client used for testing.</h3>
   
   <p>This is a test harness for <i>libsp.a</i>, the C SerialPort interface
   and <i>spd</i>, the serial port server. It is designed for regression
   testing within the <i>spd_regress</i> script.</p>

   <ul>
     <li>Written by M.C.Gregorie.</li>
     <li>Version history</li>
       <ul>
         <li>1.0, Initial release 18/05/04</li>
         <li>1.1, Added the login command 08/07/05</li>
         <li>1.2, Added sp_getport command 23/12/05</li>
       </ul>
     </li>
   </ul>

   <p>The program runs one or more test scripts, which are files named
   on the command line. The only acceptable option, apart from
   <b>-?</b>, is the debug option, <b>-d</b>. This causes diagnostic
   output on stderr. Progressively more debug options cause additional
   diagnostic detail to be output.</p>

   <p>Within a script blank lines and comment lines starting with #
   are ignored. The commands prefixed by <i>sp_</i> correspond to the
   functions in the C SerialPort interface though the parameters may
   differ in number and order. The other commands have been added
   to make the scripting language easier to use. Commands must be written
   on a single line and may be followed by a comment which starts
   with a #. A command consists of a command word that may be
   followed by a number of space separated parameters. Commands are
   documented below:</p>
   <dl>
     <dt><kbd><b>pause 999 </b></kbd></dt>
         <dd>pause the script for 999 milliseconds.
             This has nothing to do with the SerialPort interface.
             It is a convenience function that makes scripting easier.</dd>
     <dt><kbd><b>login name [password] </b></kbd></dt>
         <dd>Login sequence. The password is only needed if the login
             name demands one. This command is built from SerialPort
             interface functions combined with conditional expressions
             that the scripting language doesn't support.</dd>
     <dt><kbd><b>sp_create hostname port </b></kbd></dt>
         <dd>start a SerialPort session</dd>
     <dt><kbd><b>sp_delete </b></kbd></dt>
         <dd>end a SerialPort session</dd>
     <dt><kbd><b>sp_listports </b></kbd></dt>
         <dd>list available serial ports</dd>
     <dt><kbd><b>sp_open serialport </b></kbd></dt>
         <dd>open a serial port</dd>
     <dt><kbd><b>sp_close TRUE|FALSE </b></kbd></dt>
         <dd>close the serial port</dd>
     <dt><kbd><b>sp_setport baud dbits parity sbits </b></kbd></dt>
         <dd>set port conditions</dd>
     <dt><kbd><b>sp_getport</b></kbd></dt>
         <dd>display port operating conditions</dd>
     <dt><kbd><b>sp_delay delay </b></kbd></dt>
         <dd>set transmission delay between characters</dd>
     <dt><kbd><b>sp_setsep active sepchar external </b></kbd></dt>
         <dd>set field terminator active/inactive, specify the terminator
             character and specify it as external/part of the field.
             <u>Active</u> and <u>external</u> are T/F, t/f or 1/0.
             The separator character may be a normal character,
             \n (system newline), \r (CR), \l (LF), \t (tab) or \0 (null).
             Any other character prefixed with \ takes its own value.</dd>
     <dt><kbd><b>sp_reset line_settings tx_delay separator</b></kbd></dt>
         <dd>cause the line settings, tx delay and record separator
             to be selectively reset to default values.
             All three parameters are T/F, t/f or 1/0.
             If a parameter is T,t or 1 the associated set of control
             values are reset.</dd>
     <dt><kbd><b>sp_put text </b></kbd></dt>
         <dd>send data</dd>
     <dt><kbd><b>sp_get 999 </b></kbd></dt>
         <dd>retrieve up to 999 characters</dd>
     <dt><kbd><b>sp_echo text 999 adj </b></kbd></dt>
         <dd>send 'text', wait <u>adj</u> mS, receive up to 999 chars</dd>
     <dt><kbd><b>sp_query </b></kbd></dt>
         <dd>display bytes in input and output queues</dd>
     <dt><kbd><b>sp_sleep 999 adj </b></kbd></dt>
         <dd>wait while up to 999 chars are received + <u>adj</u> mS </dd>
   </dl>


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

#define VERSION         "1.2"
#define COPYRIGHT       "(c) M.C.Gregorie, 2005"

#include <environ.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>

#include "serialport.h"
#include "testspd.h"

#ifndef TRUE
#define TRUE       1
#endif

#ifndef FALSE
#define FALSE      0
#endif

#define FLAGCNT    2

#define HELP       0      /* -? help */
#define DEBUG      1      /* -d debug mode */

#define MAYBEMARK  '%'
#define BADMARK    '!'
#define OK         ' '
#define OPTLIST    "?d"

#define MAXPARAMS  6

/*
   Global variables
*/
char  *es;

/*
  Private function declarations
*/
int readargs(int argc, char *argv[], int flags[]);
void showhelp();
int runtest(char *script, int debug);
int login(SPC *cb, char *name, char *password, int debug);
char makechar(char *s);

/*
   This function scans the command line for options and does
   all the associated setup.
*/
int readargs(int argc, char *argv[], int flags[])
{
   int i; 
   int opt;

   /* initialise flags */

   for (i=0 ; i<FLAGCNT ; i++) flags[i] = FALSE;

   /* set up options from the command line */

   optmaybe = MAYBEMARK;
   optbad = BADMARK;
   optind = 1;
   
   while ((opt=getopt(argc,argv,OPTLIST)) != EOF)   
   {
      switch (opt) {
         case 0:         continue;
         case BADMARK:   return(opt);
         case '?':       flags[HELP] = TRUE;       
                         break;
         case 'd':       flags[DEBUG]++;
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
   printf("\ntestspd v%s %s\n\n",VERSION,COPYRIGHT);
   printf("Syntax  : testspd [<opts>] script ...\n");
   printf("Function: runs spd serial port server and serialport\n");
   printf("          interface test scripts and displays test results.\n");
   printf("Options :\n");
   printf("     -d           debug mode\n");
}

/*
   The main program starts here
   ----------------------------
*/
int main(int argc, char *argv[])
{
   int   flags[FLAGCNT];
   int   argtype;
   int   reply;
   int   processed;

   /*
      Parse and set up command options
   */
   setprog(argv[0]);
   if (readargs(argc,argv,flags) == BADMARK)
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

   optind = 1;
   processed = 0;
   while ((argtype = getarg(argc, argv)) >= 0)
   {
      if (argtype > 0)
      {
         if (processed)
            sleep(1);      /* wait for connections to reset */
            
         reply = runtest(optarg, flags[DEBUG]);
         processed++;
         if (reply)
            break;
      }
   }

   if (processed != 1)
      printf("%d tests run\n", processed);
      
   return(reply);
}

int runtest(char *script, int debug)
{
   FILE              *f;
   char              line[MAXLINE];
   int               lineno;
   int               linelth;
   char              *comment;
   int               i;

   char              *fn;
   char              *s;

   char              *p[MAXPARAMS];
   int               pn[MAXPARAMS];
   int               pp_baud, pp_data, pp_stop;
   char              pp_parity;
   SPC               *cb;
   char              value[1600];
   int               c;
   int               incount;
   int               outcount;
   int               buffsize;
   struct timespec   t;

   int               e;
   int               errcnt;

   lineno = 0;
   errcnt = 0;

   /* Claim space to store parameter strings */
   
   for (i = 0; i < MAXPARAMS; i++)
   {
      p[i] = (char*)get_space(MAXLINE+1);
      if (!p[i])
         error(progname(), "can't get parameter storage space", "");
   }
      
   f = fopen(script, "r");
   if (f)
   {
      printf("Processing script %s\n", script);
   }
   else
   {
      printf("Can't open %s\n\n", script);
      return errno;
   }

   while (fgets(line, MAXLINE, f))
   {
      /*
         Count lines read.
         Remove trailing newlines and spaces.
         Print the line.
      */
      lineno++;
      linelth = strlen(line);
      if (line[linelth-1] == '\n')
      {
      	line[linelth-1] = '\0';
      }

      for (i = strlen(line) -1; i >=0 ;i--)
      {
         if (isspace(line[i]))
            line[i] = '\0';
         else
            break;
      }
            
      printf("%3d: %s\n", lineno, line);

      /*
         Ignore empty lines and those starting with #
      */
      if (line[0] == '#' || strlen(line) == 0)
         continue;

      /*
         If the line has a trailing comment, remove it
      */
      comment = strchr(line, '#');
      if (comment)
      {
         *comment = '\0';
         if (debug > 2)
         {
            printf("Line truncated to '%s'\n", line);
         }
      }

      /*
         Parse the command. Start by clearing parameters
      */
      for (i = 0; i < MAXPARAMS; i++)
      {
         strcpy(p[i],"");
         pn[i] = 0;
      }

      fn = strtok(line, " \t");  /* Get the function name */
      if (!fn)
      {
         printf("     Error:  No function in this line\n");
         errcnt++;
         continue;
      }

      for (i = 0; i < MAXPARAMS; i++)  /* Get the parameters */
      {      
         s = strtok(NULL, " \t");
         if (!s)
            break;
         strncpy(p[i], s, MAXLINE);
         pn[i] = atoi(p[i]);
         if (debug > 2)
            printf("Parameter %d: [%s]=%d\n", i, p[i], pn[i]);
      }

      /*
         Identify the function to be run and do so, reporting results
      */
      if (strcmp("pause", fn) == 0)
      {
         t.tv_sec = pn[0] / 1000;
         t.tv_nsec = (pn[0] - t.tv_sec * 1000) * 1000;
         e = nanosleep(&t, NULL);
         if (e < 0)
         {
            e = -1024;
            es = strerror(errno);
         }
         else
         {
            e = SPEOK;
         }
      }
      else
      if (strcmp("login", fn) == 0)
      {
         e = login(cb, p[0], p[1], debug);
      }
      else
      if (strcmp("sp_create", fn) == 0)
      {
         cb = sp_create(debug, p[0], pn[1]);
         if (cb)
            printf("     Created: %s:%s\n", p[0], p[1]);

         if (cb)
         {
            e = 0;
         }
         else
         {
            e = -1024;
            es = "sp_create() failed to initialise a control block";
         }
      }
      else
      if (strcmp("sp_delete", fn) == 0)
      {
         e = sp_delete(&cb);
      }
      else
      if (strcmp("sp_listports", fn) == 0)
      {
         strcpy(value, "");
         e = sp_listports(cb, value, sizeof(value));
         if (e >= 0)
         {
            printf("     Port list: [%s] (%d bytes)\n", value, e);
            e = 0;
         }  
      }
      else
      if (strcmp("sp_open", fn) == 0)
      {
         e = sp_open(cb, p[0]);
      }
      else
      if (strcmp("sp_close", fn) == 0)
      {
         e = sp_close(cb, p[0]);
      }
      else
      if (strcmp("sp_setport", fn) == 0)
      {
         c = p[2][0];
         e = sp_setport(cb, pn[0], pn[1], c, pn[3]);
      }
      else
      if (strcmp("sp_getport", fn) == 0)
      {
         c = p[2][0];
         e = sp_getport(cb, &pp_baud, &pp_data, &pp_parity, &pp_stop);
         if (e >= 0)
         {
            printf("     Port parameters: baud=%d data=%d parity=%c stop=%d\n",
                   pp_baud,
                   pp_data,
                   pp_parity,
                   pp_stop);
            e = 0;
         }  
      }
      else
      if (strcmp("sp_delay", fn) == 0)
      {
         e = sp_delay(cb, pn[0]);
      }
      else
      if (strcmp("sp_setsep", fn) == 0)
      {
         e = sp_setsep(cb, makechar(p[0]), makechar(p[1]), makechar(p[2]));
      }
      else
      if (strcmp("sp_reset", fn) == 0)
      {
         e = sp_reset(cb, makechar(p[0]), makechar(p[1]), makechar(p[2]));
      }
      else
      if (strcmp("sp_put", fn) == 0)
      {
         e = sp_put(cb, p[0], strlen(p[0]));
      }
      else
      if (strcmp("sp_get", fn) == 0)
      {
         e = sp_get(cb, value, pn[0]);
         if (e >=0)
         {
            printf("     Return string: [%s] (%d bytes)\n", value, e);
            e = 0;
         }
      }
      else
      if (strcmp("sp_echo", fn) == 0)
      {
         strcpy(value, "");
         pn[1] = min(pn[1], sizeof(value));
         e = sp_echo(cb, p[0], strlen(p[0]), pn[2], value, pn[1]);
         if (e >=0)
         {
            printf("     Return string: [%s] (%d bytes)\n", value, e);
            e = 0;
         }
      }
      else
      if (strcmp("sp_query", fn) == 0)
      {
         e = sp_query(cb, &incount, &outcount, &buffsize);
         if (!e)
            printf("     Return values: %s=%d  %s=%d  %s=%d\n",
                   "Received bytes",
                   incount,
                   "Transmission queue",
                   outcount,
                   "Buffer size",
                   buffsize);
      }
      else
      if (strcmp("sp_sleep", fn) == 0)
      {
         e = sp_sleep(cb, pn[0], pn[1]);
      }
      else
      {
         printf("     Error: unknown function name\n");
         errcnt++;
         continue;
      }

      /*
         See if the function threw an error and report it
      */
      if (e)
      {
         if (e != -1024)
            es = sp_error(cb);
            
         printf("     Error:  %d,%s\n", e, es);
         errcnt++;
      }
   }

   /*
      Run complete
   */
   printf("Script ended: %d errors\n\n", errcnt);
   fclose(f);
   return 0;
}

/*
   Implement the login sequence for an OS-9 system:
      send CR, get three lines ending with "User name?: "
      send login_name CR, get a response
      if "password:"
         send password, get a response
      end-if
      if response contains "Invalid password" or "Who?"
         report login failure
      else
         report login OK
      end-if

   The whole logic flow of this function is more or less destroyed by error
   checking. If an error ocurrs, the rest of the function is skipped.
   You have been warned!
*/
int login(SPC *cb, char *name, char *password, int debug)
{
   const int      VALSIZE = 500;
   const int      OS9DELAY = 100;   /* mSec */
   const char     ON = 'T';
   const char     OFF = 'F';
   const char     CR = '\r';
   
   char           value[1600];
   int            e = 0;

   if (debug)
      fprintf(stderr, "login(%p,%s,%s,%d)\n", cb, name, password, debug);

   es = "OK";

   /*
      Set an active, external separator of CR
      Send a CR to wake up tsmon and get a login prompt
      by sending a zero length field, which will have an external
      separator of CR added.
      Wait for OS-9 to wake up and send a prompt
      Read in the complete three line prompt
   */
   e = sp_setsep(cb, ON, CR, ON);
   if (e >= 0)
   {
      e = sp_put(cb, "", 0);
      if (e >= 0)
         e = sp_setsep(cb, OFF, CR, OFF);
         if (e >= 0)
            e = sp_sleep(cb, VALSIZE, OS9DELAY);
            if (e >=0)
               e = sp_get(cb, value, VALSIZE);
   }
      
   if (e >= 0)
   {
      /*
         Throw an error if a login prompt wasn't received
      */
      if (strstr(value, "User name?:") == 0)
      {
         e = -1024;
         es = "login prompt not received";
      }
   }

   if (e >= 0)
   {
      /*
         Re-enable CR field termination.
         Send the CR terminated login name.
         Disable field termination: the response might be a password
         prompt or a (possibly multiline) initial prompt. We need to
         read the lot.
         Wait while the response is received
         Retrieve the response
      */
      e = sp_setsep(cb, ON, CR, ON);
      if (e >= 0)
         e = sp_put(cb, name, strlen(name));
         if (e >= 0)
            e = sp_setsep(cb, OFF, 0, OFF);
            if (e >= 0)
               e = sp_sleep(cb, VALSIZE, OS9DELAY);
               if (e >= 0)
                  e = sp_get(cb, value, VALSIZE);
   }

   if (e >= 0 && strstr(value, "Password:"))
   {
      /*
         Password prompt: send the password with CR termination.
         Disable field termination and read the (possibly)
         multiline prompt.
      */
      e = sp_setsep(cb, ON, CR, ON);
      if (e >= 0)
         e = sp_put(cb, password, strlen(password));
         if (e >= 0)
            e = sp_setsep(cb, OFF, 0, OFF);
            if (e >= 0)
               e = sp_sleep(cb, VALSIZE, OS9DELAY);
               if (e >= 0)
                  e = sp_get(cb, value, VALSIZE); 
   }

   if (e >= 0)
   {
      /*
         End of either sequence: check whether the login succeeded
         and report an error if it didn't.
      */
      if (strstr(value, "Who?") || strstr(value, "Invalid password"))
      {
         e = -1024;
         es = "Login failed";
      }
      else
      {
         printf("     Login OK\n");
      }
   }

   /*
      Set the separator back to the default condition
   */
   if (e >= 0)
      sp_setsep(cb, OFF, 0, OFF);

   /*
      If a SerialPort error ocurred, retrieve the error.
      A value of -1024 indicates an error detected by login(),
      not the result of a failed SerialPort call.
   */
   if (e < 0 && e != -1024)
      es = sp_error(cb);

   /*
      Correct the reply value: anything positive is returned as zero
   */
   if (e > 0)
      e = 0;
 
   if (debug)
   {
      fprintf(stderr,
              "%d = login(%p,%s,%s,%d) - error status: %s\n",
              e,
              cb,
              name,
              password,
              debug,
              es);
   }
   
   return e;
}

/*
   Translate \n, \r, \l, \t, \0 and \x into
   system newline, CR, NL, tab, NULL and x characters.
*/
char makechar(char *s)
{
   char c;

   if (s[0] != '\\')
   {
      c = s[0];
   }
   else
   {
      switch(s[1])
      {
         case 'n':   c = '\n';
                     break;
         case 'r':   c = 0x0d;
                     break;
         case 'l':   c = 0x0a;
                     break;
         case 't':   c = '\t';
                     break;
         case '0':   c = 0;
                     break;
         default:    c = s[1];
      }
   }  

   return c;
}
