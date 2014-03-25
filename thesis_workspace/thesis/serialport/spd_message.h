/* spd_message.h - headers for assembling and unpacking messages
                   exchanged with the client.

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

int build_message(char    *buff,
                  int     blth,
                  char    *cmd,
                  int     vlth,
                  char    *val,
                  int     debug);

int unpack_message(char   *msg,
                   int    mlth,
                   char   *cmd,
                   int    clth,
                   char   *val,
                   int    vlth,
                   int    debug);
