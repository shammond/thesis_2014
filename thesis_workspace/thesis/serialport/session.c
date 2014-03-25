/*
   <p>Functions and data structures required to record
   session details for TCP/IP connections or open serial ports
   within <i>spd</i>, the serial port server.</p>

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

typedef struct {
   int      type;       /* TCP_SESSION or PORT_SESSION                      */
   int      has_ref;    /* TRUE or FALSE                                    */
   int      ref;        /* subscript into session array (TCP_SESSION)       */
                        /* or port array (PORT_SESSION) depending on type   */ 
} session;

static int     max_sessions;
static session **slist;
static int     debug;

/*
   Private function templates
   ==========================
*/
void session_check(int fd, int must_exist);
char *session_typedisp(int fd);
void session_list_dump(int fd, int added);

/*
   Public Functions
   ================
*/

/*
   Initialise session storage structures.
   <u>n</u> is the number of serial ports configured in the
   <i>spd</i> configuration file. <u>dbg</u> sets the
   debugging level for all the <i>session_xxx()</i> functions.
   If the debugging level is 1 or more it merely traces
   <i>session_xxx()</i> function calls as an aid to debugging the
   calling program.
*/

void session_init(int n, int dbg)
{
   int   i;
   
   /*
      Max sessions is three times the number of ports plus four:
      - one session per port
      - one session for each TCP/IP connection associated with a port
      - one control session per port. This should be overkill.
      - the three standard fds plus the connection listener.
   */
   debug = (dbg <= 1 ? 0 :  dbg -1);
   if (debug)
      fprintf(stderr, "session_init(%d,%d) called\n", n, debug);
              
   max_sessions = n * 3 + 4;
   slist = get_space(sizeof(session*) * max_sessions);
   if (debug)
      fprintf(stderr,
              "max sessions=%d, %d bytes\n",
              max_sessions,
              sizeof(slist));
              
   for (i = 0; i < max_sessions; i++)
      slist[i] = NULL;

   if (debug)
      fprintf(stderr, "session_init() exited\n");
}

/*
   <p>Create a new session control block. <u>sd</u> is the
   required session identifier and must not exist.
   If it does exist the program will abandon with an error
   message because this can only happen as the result of
   a programming error.</p>
   
   <p>At this release the session identifier is the same as
   the session control block's index and is typically the
   same as the associated connection or serial port's <i>fd</i>.
   In a Linux/UNIX system these are unique within a process so this
   scheme will not generate conflicts.</p>
   
   <p><u>s_type</u> is the session type, which must be
   TCP_SESSION if the session is associated with a TCP/IP
   connection or PORT_SESSION if it is associated with
   a serial port. The function returns the session identifier.</p>
*/
int session_create(int sd, int s_type)
{
   session_check(sd, MUST_NOT_EXIST);
   slist[sd] = (session*)get_space(sizeof(session));
   slist[sd]->type = s_type;
   slist[sd]->has_ref = FALSE;
   slist[sd]->ref = -1;

   if (debug)
   {
      fprintf(stderr,
              "%d = session_create(%d,%s)\n",
              sd,
              sd,
              session_typedisp(sd));
      session_list_dump(sd, TRUE);   
   }
   return sd;
}

/*
   Destroy a session control block. <u>sd</u> is the
   session identifier of the block that will be destroyed.
*/
void session_delete(int sd)
{
   session_check(sd, MUST_EXIST);
   free(slist[sd]);
   slist[sd] = NULL;

   if (debug)
   {
      fprintf(stderr, "session_delete(%d)\n", sd);
      session_list_dump(sd, FALSE);
   }
}

/*
   Find a session control block. <u>sd</u> is checked
   to determine that it is valid and returned to the caller.
*/
int session_find(int sd)
{
   session_check(sd, MUST_EXIST);
   if (debug)
      fprintf(stderr, "%d = session_find(%d)\n", sd, sd);

   return sd;
}

/*
   Return the session type from the session control
   block identified by <u>sd</u>.
*/
int session_type(int sd)
{
   session_check(sd, MUST_EXIST);
   return slist[sd]->type;
}

/*
   <p>Change the settings of <i>ref</i> and <i>has_ref</i> 
   in the session control block identified by <u>sd</u>.</p>

   <p>A TCP_SESSION control block's <i>ref</i> field points to a
   PORT_SESSION control block if it has an open serial port
   and <i>has_ref</i> is true.</p>

   <p>A PORT_SESSION control block's <i>ref</i> field points to the
   port control block (managed by the <i>port_xxx()</i> functions
   and <i>has_ref</i> is true.</p>

   If <i>has_ref</i> is false the contents of <i>ref</i> are undefined.
 
*/
void session_set_ref(int sd, int ref, int has_ref)
{
   session *scb;

   session_check(sd, MUST_EXIST);
   scb = slist[sd];
   scb->ref = ref;
   scb->has_ref = has_ref;
   if (debug)
   {
      fprintf(stderr, "session_set_ref(%d,%d,%d)\n", sd, ref, has_ref);
      session_list_dump(sd, FALSE);
   }
}

/*
   Retrieve the <i>ref</i> field from the session control block
   identified by <u>sd</u>.
*/
int session_get_ref(int sd)
{
   int ref;
   
   session_check(sd, MUST_EXIST);
   ref = slist[sd]->ref;
   if (debug)
      fprintf(stderr, "%d = session_get_ref(%d)\n", ref, sd);

   return ref;
}

/*
   Retrieve the <i>has_ref</i> flag from the session
   control block identified by <u>sd</u>. This function
   returns TRUE or FALSE. The preferred way of retrieving
   the reference from a session control block is:

   <pre><kbd>
   if (session_has_ref(sd))
   {
      psref = session_get_ref(sd);
      .....
   }
   </kbd></pre>
*/
int session_has_ref(int sd)
{
   int has_ref;
   
   session_check(sd, MUST_EXIST);
   has_ref = slist[sd]->has_ref;
   if (debug)
      fprintf(stderr, "%d = session_has_ref(%d)\n", has_ref, sd);

   return has_ref;
}

/*
   Private functions
   =================

   session_check() - sanity check a session function
   ===============
*/
void session_check(int sd, int must_exist)
{
   char buff[MAXLINE+1];
   char *type; 
      
   if (sd > max_sessions)
      error(progname(), "session fd exceeds max_sessions", "");

   type = session_typedisp(sd);
   if (must_exist)
   {
      if (!slist[sd])
      { 
         snprintf(buff,
                  MAXLINE,
                  "session %s sd %d has not been created",
                  type, 
                  sd);
         error(progname(), buff, "");
      }
   }
   else
   {
      if (slist[sd])
      {
         snprintf(buff,
                  MAXLINE,
                  "session %s sd %d is already in use",
                  type, 
                  sd);
         error(progname(), buff, "");
      }
   }
}

/*
   session_typedisp() - translate the session type to a string
   ==================
*/
char *session_typedisp(int sd)
{
   static char *s;
   session     *scb;

   if (sd < max_sessions && slist[sd])
   {
      scb = slist[sd];
      if (scb->type == TCP_SESSION)
         s = "TCP SESSION";
      else
      if (scb->type == PORT_SESSION)
         s = "PORT SESSION";
      else
         s = "UNKNOWN SESSION TYPE";
   }
   else
      s = "INVALID SESSION ID";

   return s;
}  

/*
   session_list_dump() - debugging display of the session list contents
   ===================
*/
void session_list_dump(int sd, int added)
{
   session  *scb;
   int      i;
   char     buff[10];
   char     *label;
   char     unset[20];

   for (i = 0; i < max_sessions; i++)
   {
      if (slist[i])
      {
         scb = slist[i];
         snprintf(buff, 10, "%d", scb->ref);
         label = (scb->type == TCP_SESSION ? "port_session" : "port" );
         snprintf(unset, 20, "(no %s)", label);
         fprintf(stderr,
                 "Session %d: %12s %s %s %s\n",
                 i,
                 session_typedisp(i),
                 (scb->has_ref ? label : unset),
                 (scb->has_ref ? buff : ""),
                 (i == sd && added ? "session added" : ""));
      }
      else
      {
         if (sd == i && !added)
            fprintf(stderr, "Session %d: session deleted\n", i);
      }
   }
}

