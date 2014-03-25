/* SERIALPORT.H - prototypes and declarations for the serialport functions

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

extern char *errmsg[];

/*
   Public error codes
*/
enum errcodes  {  SPEOK = 0, SPEINVPTR,
                  SPEHOSTLOST, SPEHOSTOPEN, SPEHOSTCLOSED, SPEHOSTBAD,
                  SPEPORTCLOSED,
                  SPEPORTLONG,
                  SPEREMERR,
                  SPETIMERR,
                  SPEPARAMERR
               };

/*
   Interface control structure. NOT TO BE ACCESSED DIRECTLY.
   The contents of this structure is subject to change without
   notice. It should be hidden but isn't, just like the FILE
   structure in the standard library.
*/
typedef struct
{
   int   debug;         /* Debugging level for the connection instance     */
   char  *host;         /* name of spd's host computer                     */
   int   port;          /* spd's listener port number                      */
   char  *errmsg;       /* Text of the last error to be reported           */
   int   connectstate;  /* OPEN if there is a connection to spd or CLOSED  */
   int   connection;    /* fd of the connection to spd                     */
   int   portstate;     /* OPEN if spd has opened a serial port or CLOSED  */  
   int   baudrate;      /* set by sp_open() and overridden by sp_setport() */
   char  parity;        /* set by sp_open() and overridden by sp_setport() */
   int   databits;      /* set by sp_open() and overridden by sp_setport() */
   int   stopbits;      /* set by sp_open() and overridden by sp_setport() */
   int   txdelay;       /* set by sp_open() and overridden by sp_setport() */
   int   rxdelay;       /* calculated by rxdelay() from baudrate..txdelay  */
   int   txbytes;       /* Bytes sent since sp_open() or last sp_sleep     */
   int   rxbytes;       /* Bytes received by last sp_get()                 */
   int   buffsize;      /* SPD buffer size (same for input and output)     */
} SPC;

/*
   Public serialport client access functions.
*/
int sp_close(SPC *cb, char *force);
SPC *sp_create(int debug, char *host, int port);
int sp_delay(SPC *cb, int delay);
int sp_delete(SPC **cb);
int sp_echo(SPC *cb,
            char *charout,
            int outlth,
            int adj,
            char *charsin,
            int inlth);
char *sp_error(SPC *cb);
int sp_get(SPC *cb, char *buff, int n);
int sp_listports(SPC *cb, char *ports, int lth);
int sp_open(SPC *cb, char *name);
int sp_put(SPC *cb, char *buff, int n);
int sp_query(SPC *cb, int *incount, int *outcount, int *buffsize);
int sp_reset(SPC *cb, char line_settings, char tx_delay, char separator);
int sp_setport(SPC *cb,
	       int baud,
               int bits,
               char parity,
               int stopbits);
int sp_getport(SPC *cb,
	       int *baud,
               int *bits,
               char *parity,
               int *stopbits);
int sp_setsep(SPC *cb, char active, char marker, char ext);
int sp_sleep(SPC *cb, int expected, int adj);

