/*
   <p>This function is used by the <b>SerialPort</b>
   server, <i>spd</i> and included in the C client interface library,
   <i>libspd.a</i>.</p>

   <p>It calculates the time needed to receive a character.</p>

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
#include "rxdelay.h"
/*
   Calculate the single character receive
   time out value. The parameters are:
   <ul>
   <li><u>baud</u> is the line speed as a numeric baud rate.</li> 
   <li><u>databits</u> is the number of data bits per character.</li> 
   <li><u>parity</u> is the parity setting as O(dd), E(ven)
       or N(one).</li> 
   <li><u>stopbits</u> is the number of stop bits.</li> 
   <li><u>txdelay</u> is the number of mSec to delay before sending
       each character to a slow peer, such as a PIC-based device</li>
   <li><u>debug</u> sets the tracing level. Zero is no debugging.
       1 is useful for debugging the calling code as it traces the
       function call. 2 displays intermediate results during the
       calculation.</li> 
   </ul>

   It returns the time to receive one character, rounded up to the
   next millisecond.
*/
int rxdelay(int baud,
            int databits,
            char parity,
            int stopbits,
            int txdelay,
            int debug)
{
   double      bitrate;
   int         bits;
   int         timeout;

   if (debug)
      fprintf(stderr,
              "rxdelay(%d,%d,%c,%d,%d,%d) entered\n",
              baud,
              databits,
              parity,
              stopbits,
              txdelay,
              debug);

   bitrate = 1000.0 / baud;
   bits = (parity == 'N' ? 0 : 1) + databits + stopbits;
   timeout = bitrate * bits * 2.0 + 0.5;                 /* Round up */
   if (debug > 1)
      fprintf(stderr, "bitrate=%f mS  bits=%d\n", bitrate, bits);
   
   if (debug)
      fprintf(stderr, "%d = rxdelay()\n", timeout);

   return timeout;
}

