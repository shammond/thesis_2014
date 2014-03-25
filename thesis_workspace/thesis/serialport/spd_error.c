/*
   <p>Error message database and functions used by <i>spd</i>, the
   serial port server.</p>

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
   Server error messages
   =====================
*/
char *spdmsg[] =  {  "UNKNOWN ERROR CODE",
                     "Invalid baud rate",
                     "Invalid data bits: not 5, 6, 7 or 8",
                     "Invalid data bits/parity combination",
                     "Invalid parity: not O,E or N",
                     "Invalid stop bits: not 1 or 2",
                     "Port already in use",
                     "Port not open",
                     "Invalid serial port name",
                     "Serial port marked inactive",
                     "Unrecognised command"
                  };

/*
   Return the text for the error code <u>e</u>. This becomes the value
   field when a request is rejected by the <b>SerialPort</b> server,
   <i>spd</i>. The messages are held in an internal database in this
   module.
*/
char *spd_errmsg(int e)
{
   int   i;

   i = -e;
   if (e >=  SPEDEFAULT || e < SPEBADCMD)
      i = SPEDEFAULT;

   return spdmsg[i];
}

/*
   Return the length of the text associated with the error code <u>e</u>.
   This is needed to complete an error response message: see
   <i>spd_errmsg()</i> for more details.
*/
int spd_errlth(int e)
{
   int   i;

   i = -e;
   if (e >= SPEDEFAULT || e < SPEBADCMD)
      i = SPEDEFAULT;

   return strlen(spdmsg[i]);
}

