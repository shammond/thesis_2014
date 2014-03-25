/*
   <p>This collection of functions contain
   system-dependent code used by the SerialPort package.</p>

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
#include <environ.h>
#include "sysdependent.h"

char *lasterr = "OK";

#ifdef LINUX
#include <errno.h>
#include <stdio.h>
#include <termios.h>
#include <unistd.h>
#include <string.h>

/*
   Configure a serial port to use the requested values.
   The port must be open before this function is called.
   Set the port referenced by <u>fd</u> to operate at <u>baud</u>
   rate with <u>dbits</u> data bits (5-8), <u>parity</u> (N, E, O) and
   <u>sbits</u> stop bits (1,2).

   Returns -1 on error and zero on success. Use <i>set_termio_error()</i>
   to retrieve the error description.
   
   The internals of this function are highly system dependent.
   This is the Linux version.
*/
int set_termio(int fd,
               int baud,
               int dbits,
               char parity,
               int sbits,
               int debug)
{
   struct termios ts;
   int            r = 0;
   int            speed = B0;
   tcflag_t       databits = 0;
   tcflag_t       parity_code = 0;;
   tcflag_t       stopbits = 0;
   tcflag_t       bmask = 0;
   tcflag_t       bval = 0;
   int            new_speed = 0;

   if (debug)
      fprintf(stderr,
              "set_termio(%d,%d,%d,%c,%d,%d)\n",
              fd,
              baud,
              dbits,
              parity,
              sbits,
              debug);
   
   r = tcgetattr(fd, &ts);
   if (r < 0)
   {
      /*
         Problem: set the error code to report it. 
      */
      r = -1;
      lasterr = strerror(errno);
   }
   else
   {
      /*
         Get the correct baud rate code
      */
      speed = decode_baud_rate(baud);
      if (speed < 0)
      {
         r = -1;
         lasterr = "Invalid baud rate";
      }

      if (r >= 0)
      {
         /*
            Set character size to 5-8 bits
         */
         switch(dbits)
         {
            case 5:  databits = CS5;
                     break;
                     
            case 6:  databits = CS6;
                     break;
                     
            case 7:  databits = CS7;
                     break;
                     
            case 8:  databits = CS8;
                        break;
                     
            default: r = -1;
                     lasterr = "Databits can only be 5, 6, 7, or 8";
         }
      }

      if (r >= 0)
      {
         /*
            Set stopbits to 1 or 2 bits
         */
         switch(sbits)
         {
            case 1:  stopbits = 0;
                     break;
                      
            case 2:  stopbits = CSTOPB;
                     break;
                      
            default: r = -1;
                     lasterr = "Stop bits can only be 1 or 2";
         }
      }

      if (r >= 0)
      {
         /*
            Set parity to none, odd or even. Line settings
            have already been validated to ensure that parity
            can't be requested for 8 bits.
         */
         switch(parity)
         {
            case 'N':
            case 'n':   parity_code = 0;
                        break;

            case 'E':                  
            case 'e':   parity_code = PARENB;
                        break;

            case 'O':
            case 'o':   parity_code = PARENB + PARODD;
                        break;
                  
            default :   r = -1;
                        lasterr = "Parity can only be E, N, or O";
         }
      }

      if (r >= 0)
      {
         if (dbits == 8 && parity_code != 0)
         {
            r = -1;
            lasterr = "Invalid combination of data bits and parity";
         }
      }

      if (r >= 0)
      {
         if (debug > 1)
         {
            fprintf(stderr,
                    "set_termio: databits=%d      code=%s\n",
                    dbits,
                    bitsout(databits, (sizeof(tcflag_t) * 8)));
            fprintf(stderr,
                    "set_termio: parity=%c        code=%s\n",
                    parity,
                    bitsout(parity_code, (sizeof(tcflag_t) * 8)));
            fprintf(stderr,
                    "set_termio: stopbits=%d      code=%s\n",
                    sbits,
                    bitsout(stopbits, (sizeof(tcflag_t) * 8)));
         }

         if (debug > 1)
            fprintf(stderr,
                    "set_termio:           ts.c_cflag=%s before\n",
                    bitsout(ts.c_cflag, (sizeof(tcflag_t) * 8)));
         
         /*
            All settings have been set up as required by ioctl()
            Use them to modify the settings retrieved earlier.
         */
         r = cfsetispeed(&ts,speed);
         if (r == 0)
         {
            r = cfsetospeed(&ts, speed);
            if (r == 0)
            {
               bmask = ~(CSIZE + CSTOPB + PARENB + PARODD); 
               bval = (databits + stopbits + parity_code);
               ts.c_cflag = ts.c_cflag & bmask | bval;

               /*
                  Now reset the port
               */
               r = tcsetattr(fd, TCSADRAIN, &ts);
            }
         }
      
         if (r)
         {
            r = -1;
            lasterr = strerror(errno);
         }
         else
         if (debug)
         {
            fprintf(stderr,
                    "set_termio:                bmask=%s\n",
                    bitsout(bmask, (sizeof(bmask) * 8)));
            fprintf(stderr,
                    "set_termio:                 bval=%s\n",
                    bitsout(bval, (sizeof(bval) * 8)));
            fprintf(stderr,
                    "set_termio:           ts.c_cflag=%s after\n",
                    bitsout(ts.c_cflag, (sizeof(ts.c_cflag) * 8)));
            new_speed = cfgetispeed(&ts);
            fprintf(stderr,
                    "set_termio: baud=%-5d (%d)  new=%d\n",
                    baud,
                    speed,
                    new_speed);
         }
      }
   }

   if (debug)
      fprintf(stderr,
              "%d = set_termio(%d,%d,%d,%c,%d,%d)\n",
              r,
              fd,
              baud,
              dbits,
              parity,
              sbits,
              debug);

   return r;
}

/*
   Returns the description for the last error
   detected by <i>set_termio()</i>.
*/
char *set_termio_error()
{
   return lasterr;
}

/*
   Return the termios code for the baud rate.
   <u>baud</u> contains the baud rate as a numeric value.
   It returns the baud rate code required by termios code
   or -1 if the baud rate was invalid.
*/
int decode_baud_rate(int baud)
{
   static int  baud_rates[]   = {     50,     75,
                                     110,    150,   300,   600,
                                    1200,   2400,  4800,  9600,
                                   19200,  38400,
                                      -1 };
   static int  baud_codes[]   = {    B50,    B75,
                                    B110,   B150,  B300,  B600,
                                   B1200,  B2400, B4800, B9600,
                                  B19200, B38400,
                                       -1 };
   int   i;
   int   r = -1;

   i = 0;
   while (baud != baud_rates[i] && baud_rates[i] != -1)
   {
      i++;
   }

   r = baud_codes[i];
   return r;
}

#else

#include <stdio.h>
#include <modes.h>
#include <strings.h>
#include <errno.h>
#include <popen.h>

extern char **environ;
extern char *getenv();

/*
   Configure a serial port to use the requested values.
   Set the open port referenced by <u>fd</u> to operate at <u>baud</u>
   rate with <u>dbits</u> data bits (5-8), <u>parity</u> (N, E, O) and
   <u>sbits</u> stop bits (1,2).
   
   Returns -1 on error and zero on success. Use <i>set_termio_error()</i>
   to retrieve the error description.
   
   The internals of this function are highly system dependent.
   This is the OS-9 version.
*/
int set_termio(fd, baud, dbits, parity, sbits, debug)
int fd;
int baud;
int dbits;
char parity;
int sbits;
int debug;
{
	int 		r = 0;
	char		command[MAXLINE];
	int			speed;
	char		*port;
	char		*par_word;
	FILE		*cmdpipe;
	static char	reply[MAXLINE];
	int			repcount = 0;
	
   	if (debug)
   		fprintf(stderr,
                "set_termio(%d,%d,%d,%c,%d,%d)\n",
                fd,
                baud,
                dbits,
                parity,
                sbits,
                debug);

   	speed = decode_baud_rate(baud);
   	if (speed >= 0)
   	{
   		port = getenv("PORT");
   		switch(parity)
   		{
   			case 'e':
   			case 'E':	par_word = "even";
   						break;
   			case 'o':
   			case 'O':	par_word = "odd";
   						break;
   			case 'n':
   			case 'N':	par_word = "none";
   						break;
   			default:	r = -1;
   						lasterr = "Invalid parity (must be one of O,E,N)";
   		}

   		if (r >= 0)
   		{
   			sprintf(command,
   					"xmode %s baud=%d par=%s cs=%d stop=%d",
   					port,
   					speed,
   					par_word,
   					dbits,
   					sbits);
   			if (debug > 1)
   				fprintf(stderr, "set_termio: %s\n", command);

   			cmdpipe = popen(command, "r");
   			if (cmdpipe)
   			{
   				repcount = fread(reply, 1, MAXLINE-1, cmdpipe);
   				if (repcount > 0)
				{
					r = -1;
					lasterr = reply;
				}

				pclose(cmdpipe);
   			}
   			else
   			{
   				r = -1;
   				lasterr = "couldn't execute xmode";
   			}
   		}
   	}
   	else
   	{
   		r = -1;
   		lasterr =  "Invalid line speed";
   	}

   	if (debug)
   		fprintf(stderr,
                "%d = set_termio(%d,%d,%d,%c,%d,%d)\n",
                r,
                fd,
                baud,
                dbits,
                parity,
                sbits,
                debug);
	return r;
}

/*
   Returns the description for the last error
   detected by <i>set_termio()</i>.
*/
char *set_termio_error()
{
   return lasterr;
}

/*
   Return the required code for the baud rate.
   <u>baud</u> contains the baud rate as a numeric value.
   It returns the baud rate code required by xmode
   or -1 if the baud rate was invalid.
*/
int decode_baud_rate(baud)
int baud;
{
   static int  baud_rates[]   = {     50,     75,
                                     110,    150,   300,   600,
                                    1200,   2400,  4800,  9600,
                                   19200,  38400,
                                      -1 };
   static int  baud_codes[]   = {     50,     75,
                                     110,    150,   300,   600,
                                    1200,   2400,  4800,  9600,
                                   19200,  38400,
                                      -1 };
   int  i;
   int  r = -1;

   i = 0;
   while (baud != baud_rates[i] && baud_rates[i] != -1)
   {
      i++;
   }

   r = baud_codes[i];
   return r;
}

#endif
