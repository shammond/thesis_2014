/* SPD.H - Header file for spd, the serial port server

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

/*
   Global constants
   ================
*/
#ifndef TRUE
#define TRUE            1
#endif

#ifndef FALSE
#define FALSE           0
#endif

#define FLAGCNT         4

#define HELP            0      /* -? help          */
#define DEBUG           1      /* -d debug mode    */
#define DAEMON          2      /* -D daemon mode   */
#define PORT            3      /* -p=port          */

#define MAYBEMARK       '%'
#define BADMARK         '!'
#define OK              ' '
#define OPTLIST         "?dDp:"

#define CMDBUFF         10
#define CONFIG_FILE     "spd.conf"
#define DEFTIMEOUT      2000        /* milliseconds */
#define DEFPORT         16001
#define TCPBUFF         1500
#define MUST_EXIST      1
#define MUST_NOT_EXIST  0
#define PORT_SESSION    2
#define TCP_SESSION     1

/*
   spd server error message codes
   ==============================
*/
#define SPEDEFAULT      0
#define SPEINVBAUD      -1
#define SPEINVDBITS     -2
#define SPEINVCOMB      -3
#define SPEINVPARITY    -4
#define SPEINVSBITS     -5
#define SPEPORTUSED     -6
#define SPEPORTCLOSED   -7
#define SPEINVPORT      -8
#define SPEINACTIVE     -9
#define SPEBADCMD       -10
   
/*
   Function templates
   ==================
*/
int   accept_connection(FDL *pl, int caught_fd, int debug);
int   handle_event(FDL *pl,
                   int caught_fd,
                   int *connect_count,
                   int port_count,
                   int debug);
int   main(int argc, char *argv[]);
char  *port_check(int pd);
int   port_close(int pd, int force);
int   port_config(char *prog, char *file, int debug);
int   port_find(char *key);
int   port_getcount();
char  *port_geterr();
int   port_getfd(int portref);
int   port_getincount(int portref);
int   port_getoutcount(int portref);
int   port_getbuffsize(int portref);
int   port_open(int pd);
char  *port_params(int pd);
int   port_read_one(int f);
int   port_read(int pd, char *buff, int bufflth);
void   port_reset(int pd, char *line);
char  *port_selection();
void  port_set_parameters(int pd, char *line);
void  port_set_delay(int pd, char *line);
void  port_set_separator(int pd, char *line);
char  *port_status(int pd);
int   port_write_one(int f);
int   port_write(int pd, char *value, int valuelth);
int   readargs(int argc, char *argv[], int flags[], char **pipe);
int   respond_to_input(FDL *pl,
                       int fd,
                       int sd,
                       int *connected,
                       int port_count,
                       int debug);
int   session_create(int fd, int s_type);
void  session_delete(int fd);
int   session_find(int fd);
int   session_get_ref(int sd);
int   session_has_ref(int sd);
void  session_init(int n, int debug);
void  session_set_ref(int sd, int ref, int has_ref);
int   session_type(int sd);
void  showhelp();
int   spd_cmd_handler(FDL *pl,
                      int fd,
                      int sd,
                      void *msg,
                      int msglth,
                      void *reply,
                      int replylth,
                      int debug);
int  spd_errlth(int e);
char *spd_errmsg(int e);

