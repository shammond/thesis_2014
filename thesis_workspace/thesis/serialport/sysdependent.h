/* SYSDEPENDENT header file

   SerialPort - serial connections for Java
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
  
   Contact Martin Gregorie at martin@gregorie.org

*/

#ifdef OSK
int set_termio();
char *set_termio_error();
int decode_baud_rate();

#else
int set_termio(int fd,
               int baud,
               int dbits,
               char parity,
               int sbits,
               int debug); 
char *set_termio_error();
int decode_baud_rate(int baud);
#endif
