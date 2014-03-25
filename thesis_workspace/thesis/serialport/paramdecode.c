/*
   <p>This function is used by the <b>SerialPort</b>
   server, <i>spd</i> and included in the C client interface library,
   <i>libspd.a</i>.</p>

   <p>It is used to extract the individual values from the value string
   used to pass the complete set of values between a client program and
   <i>spd</i>.

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
#include <string.h>
#include "paramdecode.h"

/*
   Decode the serial parameters string, e.g. "9600,8,N,1,1500"
   (baud,databits,parity,stopbits,max message size) and return
   them via arguments that are passed by reference.

   <ul>
   <li><u>s</u> contains the comma delimited list of values to be decoded</li>
   <li><u>baud</u> receives the line speed as a numeric baud rate.</li> 
   <li><u>databits</u> receives the number of data bits per character.</li> 
   <li><u>parity</u> receives the parity setting as O(dd), E(ven)
       or N(one).</li> 
   <li><u>stopbits</u> receives the number of stop bits.</li> 
   </ul>
   
*/
void paramdecode(char  *s,
                 int   *baud,  /* baud..txdelay are return values */
                 int   *databits,
                 char  *parity,
                 int   *stopbits)
{
   char     *ptr;

   *baud = *databits = *stopbits = -1;
   *parity = 'X';
   ptr = strtok(s, ",");
   if (ptr)
   {
      /*
         Get the baud rate
      */
      *baud = atoi(ptr);
      ptr = strtok(NULL, ",");
      if (ptr)
      {
         /*
            Get the data bits
         */
         *databits = atoi(ptr);
         ptr = strtok(NULL, ",");
         if (ptr)
         {
            /*
               Get the parity code
            */
            *parity = toupper(*ptr);
            ptr = strtok(NULL, ",");
            if (ptr)
            {
               /*
                  Get the stop bits
               */
               *stopbits = atoi(ptr);
            }
         }
      }
   }
}

