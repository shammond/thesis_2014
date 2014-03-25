/*
   <p>The functions in this group are used by the <b>SerialPort</b>
   server, <i>spd</i> and included in the C client interface library,
   <i>libspd.a</i>.</p>

   <p>They are used by the interface library to validate serial
   port parameters prior to passing them to <i>spd</i> and within <i>spd</i>
   to validate serial port parameters in the configuration as well as to
   re-validate serial port parameters received from a client program.</p>

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

#include <string.h>
#include <termios.h>
#include <environ.h>
#include "sysdependent.h"
#include "paramcheck.h"
/*
   global constants
*/
static int        databit_vals[] = { 5, 6, 7, 8, -1 };                          
static char       *parity_vals   = "NOE";
static int        stopbit_vals[] = { 1, 2, -1 };

/*
   private function declarations
*/
int intval_err(int b, int val[]);
int param_charval_err(char c, char *vals);

/*
   Sanity-check serial port parameters.
   <ul>
   <li><u>baud</u> holds the line speed as a numeric baud rate.</li> 
   <li><u>databits</u> holds the number of data bits per character.</li> 
   <li><u>parity</u> is O(dd), E(ven) or N(one).</li> 
   <li><u>stopbits</u> holds the number of stop bits.</li> 
   </ul>

   The function returns NULL if all values are acceptable and a pointer
   to a string containing the error message if errors were detected.    
*/
char *paramcheck(int  baud,
                 int  databits,
                 char parity,
                 int  stopbits)
{
   static char *r;

   r = NULL;
   if (decode_baud_rate(baud) == -1)
      r = "Invalid baud rate"; 
   else
   if (intval_err(databits, databit_vals))
      r = "Invalid data bit setting"; 
   else
   if (param_charval_err(parity, parity_vals))
      r = "Invalid parity setting"; 
   else
   if (intval_err(stopbits, stopbit_vals))
      r = "Invalid stop bit setting"; 
   else
   if (databits == 8 && parity != 'N')
      r = "Invalid combination of databits and parity"; 

   return r;
}

/*
   Check that the char <u>c</u> is in the <u>vals</u> string.
   It returns TRUE (an error <i>HAS</i> ocurred)
   if <u>c</u> is not in <u>vals</u>, which lists
   acceptable values, and FALSE otherwise.
*/
int param_charval_err(char c, char *vals)
{
   int   r = TRUE;
   
   if (strchr(vals, c))
      r = FALSE;

   return r;
}

/*
   Check that <u>b</u> is in <u>val</u>, the list of valid values.
   It returns TRUE (an error FALSE <i>HAS</i> occurred) if <u>b</u> is in
   the <u>val</u> array, which lists acceptable values, and FALSE otherwise.
   The array must be terminated with -1, which is not a valid value.
*/

int intval_err(int b, int val[])
{
   int   r = TRUE;
   int   i;

   for (i = 0; val[i] != -1; i++)
   {
      if (b == val[i])
      {
         r = FALSE;
         break;
      }
   }

   return r;
}


