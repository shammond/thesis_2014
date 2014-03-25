package SerialPort;

import environ.*;

/**
 * <p>This class contains a message sent to and received from the
 * serial port server, <i>spd</i>. Its methods are used for
 * assembling, unpacking and retrieving the message.</p>
 *
 * <p>This class is not normally accessible to the calling
 * program. It is used internally by the <b>SerialPort</b>
 * class and no references to <i>SPDMessage</i> objects are
 * returned to the caller.</p>
 *
 * <p>Messages consist of a command field and a value field:
 * Both fields must be present, but the value field may be empty.
 * Messages sent to the server use the command field to request
 * an action to be taken with parameters sent in the value field.
 * Messages received from the server are always a response to the
 * previous command. Here the command field indicates whether the
 * value is a valid response to the request or an error response.</p>
 *
 * <p>The <i>environ</i> jar file must be in the classpath for this
 * class to compile and at run time.</p> 
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
public class SPDMessage
{
   private final int    MAXNUM = 4;  /* Value lengths are 1-4 digits */
   private int          debug = 0;
   private ReportError  r = new ReportError("SPDMessage");

   private String       message_buffer = null;
   private StringBuffer command = null;
   private StringBuffer value_length = null;
   private StringBuffer value = null;

   /**
    * Construct an <i>SPDMessage</i> object from a received message. Set the
    * debugging level. <i>unpackMessage()</i> must be invoked before any other
    * methods if this constructor is used.
    */
   public SPDMessage(String msg, int debug)
   {
      message_buffer = msg;
      this.debug = debug;
      command = new StringBuffer("");
      value_length = new StringBuffer("");
      value = new StringBuffer("");
   }

   /**
    * Construct an empty message object and set the debugging level.
    * <i>buildMessage()</i> must be invoked before any other methods if
    * this constructor is used.
    */
   public SPDMessage(int debug)
   {
      this("", debug);
   }

   /**
    * Assemble a message from its components.
    */
   public void buildMessage(String cmd, String val)
   {
      int   n;

      /*
         Set up globals
      */
      command = new StringBuffer(cmd);
      value = new StringBuffer(val);
      n = val.length();

      /*
       * Set up the length buffer
       */
      value_length = new StringBuffer(String.valueOf(n));
      while (value_length.length() < 4)
         value_length.insert(0,'0');
      
      /*
         Build the message
      */
      message_buffer = new String(cmd.charAt(0) +
                                  "," +
                                  value_length +
                                  "," +
                                  val);
      n = message_buffer.length();

      if (debug > 0)
      {
         HexString hv = new HexString(value);
         HexString hm = new HexString(message_buffer);
         r.trace("buildMessage([" +
                 cmd +
                 "],[" +
                 hv.getHex() +
                 "]) ==> [" + hm.getHex() + "] = " + n);
      }
   }

   /**
    * Unpack a message, extracting the command and value.
    * The length of the value is returned. Use <i>getCommand()</i>
    * and <i>getValue()</i> to retrieve the message components.
    */
   public int unpackMessage()
   {
      int            i;
      int            j;
      StringBuffer   dec = new StringBuffer("");
      char           x;
      int            left = 0;
      int            n = 0;
      Integer        nv = null;

      command = new StringBuffer("");
      value = new StringBuffer("");
      value_length = new StringBuffer("0");
      
      for (i = 0, j = 0; j < 2; j++)                /* Get the command            */
      {
         x = message_buffer.charAt(i++);
         if (x == ',' || x == 0)             /* Stop on comma or null      */
            break;
         command.append(x);
      }
   
      if (i < message_buffer.length())                 /* Skip if input finished     */
      {
         for (j = 0; j < MAXNUM; j++, i++)   /* Get 4 digit value length   */
         {
            x = message_buffer.charAt(i);
            if (x == ',' || x == 0)
               break;
            dec.append(x);     
         }
   
         if (dec.length() == MAXNUM)         /* If count ended number      */
            i++;                             /* Skip the following comma   */

         nv = new Integer(dec.toString());
         n = nv.intValue();
         left = message_buffer.length() - i;
         n = Math.min(n, left);
         value_length = new StringBuffer(dec.toString());
         for (j = 0; j < n; j++, i++)        /* Extract the value          */ 
            value.append(message_buffer.charAt(i));      
      }
   
      if (debug > 0)
      {
         HexString hv = new HexString(value);
         
         r.trace("     command = " + command + "  value = " + hv.getHex());
         r.trace(n + " = unpackMessage()");
      }

      return n;
   }
   
   /**
    * Retrieve the message. This method is used to extract a
    * message for sending to the server.
    */    
   public String getMessage()
   {
      if (debug > 0)
      {
         HexString hm = new HexString(message_buffer);
         
         r.trace("[" + hm.getHex() + "] = getMessage()");
      }
         
      return message_buffer;
   }
   
   /**
    * Retrieve the command field.
    */    
   public String getCommand()
   {
      if (debug > 0)
         r.trace("[" + command + "] = getCommand()");
         
      return command.toString();
   }
   
   /**
    * Retrieve the value field.
    */
   public String getValue()
   {
      if (debug > 0)
      {
         HexString hv = new HexString(value);
         
         r.trace("[" + hv.getHex() + "] = getValue()");
      }
         
      return value.toString();
   }

   /**
    * Retrieve the message length.
    */
   public int length()
   {
      int l = message_buffer.length();
      
      if (debug > 0)
         r.trace(l + " = length()");
         
      return l;
   }
}


