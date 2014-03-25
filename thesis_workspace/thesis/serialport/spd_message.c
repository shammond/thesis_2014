/*
   <p>Functions for assembling and unpacking messages
   exchanged between the serial port server, <i>spd</i>, and
   <i>libsp.a</i>, the C  interface library or <b>SerialPort</b>,
   the Java interface class.</p>

   <p>The functions in this module are called by <i>spd</i>
   functions as well as functions in <i>serialport.c</i>,
   the main component of <i>libsp.a</i>.</p>

   <p>The message format is:</p>

   <kbd>command,lth,value</kbd>

   <p>where <i>command</i> is the request code in messages sent to <i>spd</i>.
   In responses from <i>spd</i> it echoes the the command code unless the
   request was rejected, in which case it is <b>E</b>. <i>value</i> is an
   array of <i>lth</i> bytes. There is no terminating character because
   <i>value</i> can include any byte value from 0 to 255.</p>  

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
#include <environ.h>

#include "spd_message.h"

#define MAXNUM 4  /* Value lengths are 1-4 digits */

/*
   Assemble a message from its components. The assembled message is
   placed in <u>buff</u>, whose length is given by <u>blth</u>.
   The command is a string in <u>cmd</u>. The value is a byte array
   of <u>vlth</u> bytes in <u>val</u>. It returns the length of the
   assembled message. The function call will be traced if <u>debug</u>
   is non-zero.
*/
int build_message(char *buff,
                   int blth,
                   char *cmd,
                   int vlth,
                   char *val,
                   int debug)
{
   int   n;
   
   snprintf(buff, blth-1, "%c,%04d,", cmd[0], vlth);
   n = strlen(cmd) + 6;
   if ((n + vlth) > blth)
      error(progname(), "message too large for buffer", "");
      
   memcpy(buff+n, val, vlth);
   buff[n + vlth] = 0;

   if (debug)
   {
      fprintf(stderr,
              "%d = build_message(buff,%d,%s,%d,val,%d)\n",
              n + vlth,
              blth,
              cmd,
              vlth,
              debug);
      dumphex(stderr, buff, n + vlth);
   }

   return n + vlth;
}

/*
   Unpack a message in <u>msg</u> of <u>mlth</u> total length,
   extracting the command into <u>cmd</u> and the value into <u>val</u>.
   The length of the command and value buffers are given by <u>clth</u>
   and <u>vlth</u> respectively. The function call will be
   traced if <u>debug</u> is non-zero.
   The length of the value field is returned by <i>unpack_message()</i>.
*/
int unpack_message(char *msg,
                   int mlth,
                   char *cmd,
                   int clth,
                   char *val,
                   int vlth,
                   int debug)
{
   int   i = 0;
   int   j;
   int   n;
   char  dec[MAXNUM+1];
   char  x;

   for (j = 0; j < clth-1; j++)           /* Get the command            */
   {
      x = msg[i++];
      if (x == ',' || x == 0)             /* Stop on comma or null      */
         break;
      cmd[j] = x;
   }
   
   cmd[j] = 0;                            /* Add null terminator        */

   
   if (i < mlth)                          /* Skip if input finished     */
   {
      for (j = 0; j < MAXNUM; j++, i++)   /* Get 4 digit value length   */
      {
         x = msg[i];
         if (x == ',' || x == 0)
            break;
         dec[j] = x;     
      }
   
      dec[j] = 0;                         /* Null terminate the length  */
      if (strlen(dec) == MAXNUM)          /* If count ended number      */
         i++;                             /* Skip the following comma   */
         
      j = atoi(dec);                      /* Convert length to integer  */
      n = min(j, vlth-1);                 /* Calculate value length     */
      memcpy(val, msg+i, n);              /* Extract the value          */
      val[n] = 0;                         /* Add string terminator      */
      
   }
   else
   {
      vlth = 0;
      strcpy(val, "");
   }
   
   if (debug)
   {
      fprintf(stderr,
              "%d = unpack_message(msg,%d,%s,val,%d,%d)\n",
              j,
              mlth,
              cmd,
              vlth,
              debug);
      dumphex(stderr, msg, mlth);
   }

   return j;
}
