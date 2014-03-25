import java.awt.*;
import javax.swing.*;

/**
 * <b>JTHelpContent</b> builds the content of the <b>JTerm</b> help dialog.
 *
 * <PRE>
 * <b>SerialPort - serial connections for Java</b>
 * Copyright (C) 2005  Martin C Gregorie
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * </PRE>
 *
 * <p>Contact Martin Gregorie at
 * <a href="mailto:martin@gregorie.org">martin@gregorie.org</a>.<br>
 * Read the <a href="../license/gpl.html">GNU General Public License</a>.</p>
 */
public class JTHelpContent
{
   /**
    * Return the help text for this application.
    */
   public static String getText()
   {
      StringBuffer sb = new StringBuffer("<html>");
      
      sb.append("<head><title>JTerm Help Text</title></head>");
      sb.append("<body>");
      sb.append("<h1>Using JTerm</h1>");
      sb.append("<h2>Server connection</h2>");
      sb.append("<p><b>Server|Connect</b> pops up a dialog box ");
      sb.append("that allows you to specify the host name and port ");
      sb.append("before making the connection to <b>spd</b>. ");
      sb.append("The port is normally 16001. This is the default.</p> ");

      sb.append("<h2>Opening the serial port</h2>");
      sb.append("<p>You must be connected to the <b>spd</b> server. ");
      sb.append("before you can select a serial port.</p>");
      sb.append("<p><b>Serial port|Open</b> obtains a list of available ");
      sb.append("ports from the server and presents them as a radio list. ");
      sb.append("The default settings for that port are displayed. ");
      sb.append("Make any changes you require. Finally, you must select ");
      sb.append("the target operating system. This determines the byte ");
      sb.append("code(s) sent to the target application when the ");
      sb.append("<b>Return</b> key is typed. <i>Unix</i> selects LF, ");
      sb.append("<i>OS-9</i> selects CR and <i>MS-DOS</i> selects CRLF. ");
      sb.append("Choose a target operating system and then click <b>OK</b>.");
      
      sb.append("<h2>Closing the serial port</h2>");
      sb.append("<p><b>Serial port|Close</b> is greyed out ");
      sb.append("unless there is an active serial port. Selecting ");
      sb.append("it closes the connection, grays out the menu item ");
      sb.append("and enables the <b>Open a port</b> menu item.</p>");
      
      sb.append("<h2>Closing the server connection</h2>");
      sb.append("<p><b>Server|Disconnect</b> is greyed out ");
      sb.append("unless there is an active connection. Selecting ");
      sb.append("it closes the connection, grays out the menu item ");
      sb.append("and enables the <b>Connect to server</b> menu item.</p>");
      
      sb.append("<h2>Using the terminal</h2>");
      sb.append("<p>The terminal can be used to talk to the remote host ");
      sb.append("once a serial port is open.");
      sb.append("Anything you type will be sent to the remote host ");
      sb.append("and its responses will be displayed.</p>");
      sb.append("<p>A different session can be started by closing the ");
      sb.append("serial port and, if necessary, the server connection ");
      sb.append("before connecting to another server and serial port.</p>");
      
      sb.append("<h2>Ending the session</h2>");
      sb.append("<p>Selecting <b>File|Exit</b> exits from JTerm.</p>");
      sb.append("</body></html>");

      return sb.toString();
   }
}
 
 
