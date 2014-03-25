package SerialPort;

import java.util.*;
import environ.*;

/**
 * <p><b>SerialPort</b>
 * - Java and C access to local or remote serial ports.</p>
 *
 * <p>The SerialPort package provides a Java program with access
 * to serial ports. It allows its caller to control the port's
 * settings (speed, parity, etc) and to transfer data to and from a
 * remote client. One instance of the SerialPort object accesses one
 * serial port and operates independently of any other serial ports
 * the program may be using. This class is compatible with all
 * releases of Java. It uses TCP/IP to connect to <i>spd</i>,
 * a server that handles serial ports on its behalf. <i>spd</i> is
 * written in C. It is always run on the host which has the serial
 * ports, but a SerialPort object can communicate with <i>spd</i>
 * regardless of whether it is running on the same system or on a
 * remote host.</p>
 *
 * <p>All SerialPort methods contain in-line code with no internal
 * delays apart from the <i>sleep()</i> method, which is provided as a
 * convenient way to wait for remote activity to occur and to allow
 * for transmission time.</p>
 *
 * <p>There are no internal delays in the serial port server. The intent
 * is to allow a client program to choose when or if it needs to wait for
 * external events. The methods do, however, contain sufficient
 * validation to ensure that each method will complete during normal
 * operation.</p>
 *
 * <p>The constructor does not open any connections: it just sets the
 * hostname and TCP/IP port to be used by the following
 * <i>listPort()</i> and <i>open()</i> invocations. The <i>open()</i>
 * method opens a connection to the server if necessary and claims the
 * serial port.</p>
 *
 * <p>The <i>close()</i> method closes the connection, leaving the
 * object ready to be re-used by an <i>open()</i> invocation. It is
 * good practise to invoke the <i>delete()</i> method when the object
 * is to be disposed of because this checks that no connections have
 * been left open. <i>delete()</i> fails if the connection to the
 * serial port server is still open.</p>
 *
 * <p>The <i>listPorts()</i> method establishes the connection it needs
 * to retrieve a list of ports if one does not exist. Subsequent method
 * invocations will re-use the connection opened by <i>listPorts()</i>.
 * If a connection already exists the <i>open()</i> method will reuse
 * it.</p>
 *
 * <p>The <i>environ</i> jar file and the <i>SDPMessage</i> class must
 * be available on the current classpath when <b>SerialPort</b> is used
 * or compiled.</p>
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

public class SerialPort
{
   private int                debug = 0;
   private String             host = null;
   private int                port = 0;
   private String             error = errmsg[SPEOK];
   private ClientConnection   connection = null;
   private int                connectstate = CLOSED;
   private int                portstate = CLOSED;
   private String             portlist = new String("");
   private byte[]             bytebuffer = new byte[MAXCMD];
   private SPDMessage         current_message = new SPDMessage(debug-1);
   private int                baud = -1;
   private int                databits = -1;
   private char               parity = 'X';
   private int                stopbits = -1;
   private int                txdelay = 0;
   private int                rxdelay = -1;
   private int                txbytes = 0;
   private int                rxbytes = 0;
   private int                buffsize = 0;

   private static int SPEOK = 0;
   private static int SPEINVPTR = 1;
   private static int SPEHOSTLOST = 2;
   private static int SPEHOSTOPEN = 3;
   private static int SPEHOSTCLOSED = 4;
   private static int SPEHOSTBAD = 5;
   private static int SPEPORTCLOSED = 6;
   private static int SPEPORTLONG = 7;
   private static int SPEREMERR = 8;
   private static int SPETIMERR = 9;
   private static int SPEPARAMERR = 10;
  
   private static String errmsg[] = {  "OK",
                                       "Invalid control block pointer",
                                       "Host connection lost",
                                       "Host connection open",
                                       "Host connection not open",
                                       "Host not found: 'host:port'",
                                       "Port not open",
                                       "Port name too long for buffer",
                                       "Server reported error",
                                       "Sleep timer error",
                                       "Invalid port parameters"
                                    };

   private final String SP_ACK = new String("A");
   private final String SP_CLOSE = new String("C");
   private final String SP_DELAY = new String("D");
   private final String SP_ERROR = new String("E");
   private final String SP_GET = new String("G");
   private final String SP_INTERACTIVE = new String("I");
   private final String SP_LOOKUP = new String("L");
   private final String SP_OPEN = new String("O");
   private final String SP_PUT = new String("P");
   private final String SP_QUERY = new String("Q");
   private final String SP_RESET = new String("R");
   private final String SP_SETSEP = new String("T");
   private final String SP_SETUP = new String("S");

   private static int CLOSED  = -1;
   private static int OPEN    = 0;
   private static int MAXMSG  = 1500;
   private static int MAXCMD  = 10;
   private static int MAXERR  = 100;
   private static int RXFUDGE = 3;  /* Rx delay calculation tuning factor */
   private static int TXFUDGE = 3;  /* Tx delay calculation tuning factor */

   private static ReportError re = new ReportError("SerialPort");

   /**
    * Construct the object, set the 
    * name of the host supporting the desired serial port,
    * the TCP/IP port number and debugging level.
    * Debugging level 1 provides a trace of invoked methods
    * that is helpful when developing a program that uses
    * SerialPort. Higher debugging levels are used to
    * trace the inner workings of SerialPort methods.
    */
   public SerialPort(int debug, String host, int port)
   {
      this.debug = debug;
      this.host = host;
      this.port = port;

      if (debug > 1)
      {
         re.trace("SerialPort(" + debug + "," + host + "," + port + ")");
      }
   }

   /**
    * Called when the object is no longer required.
    * Returns an error if the connection is still open.
    */
   public synchronized int delete()
   {
      int r = setError(SPEOK);
      if (portstate == OPEN)
         r = setError(SPEHOSTOPEN);

      if (debug > 1)
         re.trace(r + " = delete()");
         
      return r;
   }
   
   /**
    * Returns the current error string or "OK"
    */  
   public synchronized String getError()
   {
      if (debug > 1)
      {
         re.trace(error + " = getError()");
      }

      return error;
   }

   /**
    * Builds a list of available ports.
    * Returns -ve on error.
    */
   public synchronized int findPortList()
   {
      int      r = setError(SPEOK);
      String[] rcmd = new String[MAXCMD];
      String   ports = new String("");

      portlist = new String("");
      if (connectstate == CLOSED)
         r = connectServer();

      if (r == SPEOK)
      {
         r = sendCommand(SP_LOOKUP, 0, "");
         if (r == SPEOK)
         {
            r = getReply();
            if (r == SPEOK)
               portlist = getReplyValue();
         }

         if (r != SPEOK)
            r = setServerError(getReplyValue());
      }
	 
      if (debug > 1)
         re.trace(r + " = findPortList()");

      return r;
   } 

   /**
    * Returns the ports found by the <i>findPortList()</i>
    * method as a comma delimited list in a single String object.
    */
   public synchronized String getPortList()
   {
      if (debug > 1)
         re.trace(portlist + " = getPortList()");

      return portlist;
   }

   /**
    * Opens a connection to <i>spd</i> and claims the
    * serial port identified by <u>name</u>.
    * Default conditions are set by the server's configuration file.
    * Returns -ve on error.
    */
   public synchronized int open(String name)
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   err = null;
      
      if (connectstate == CLOSED)
         r = connectServer();

      if (r == SPEOK)
      {
         r = sendCommand(SP_OPEN, name.length(), name);
         if (r == SPEOK)
         {
            r = getReply();
            if (r == SPEOK)
            {
               ack = getReplyCommand();
               if (ack.equals(SP_OPEN))
               {
                  r = setError(SPEOK);
                  portstate = OPEN;
                  decodeParams(getReplyValue());
                  err = checkParams();
                  if (err == null)
                  {
                     rxdelay = calcRxDelay();
                     txbytes = 0;
                     rxbytes = 0;
                  }
                  else
                  {
                     r = setServerError(err);
                     r = close(true);
                     if (r == 0)
                        r = setError(SPEPARAMERR);
                  }
               }
               else
               {
                  r = setServerError(getReplyValue());
               }
            }  
         }
      }
	 
      if (debug > 1)
         re.trace(r + " = open(" + name + ")");

      return r;
   }

   /**
    * Releases the serial port and closes the server
    * connection. If <u>force</u> is TRUE the port is
    * forced to close with possible data loss. If it
    * is FALSE the port is only closed if the buffers
    * are empty.
    * Returns -ve on error.
    */
   public synchronized int close(boolean force)
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   cval = null;
      
      if (connectstate == OPEN)
      {
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               if (force)
                  cval = new String("TRUE");
               else
                  cval = new String("FALSE");
                  
               r = sendCommand(SP_CLOSE, cval.length(), cval);
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_ACK))
                     {
                        r = setError(SPEOK);
                        portstate = CLOSED;
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
      }
         
      if (connectstate == OPEN && portstate == CLOSED)
      {
         connection.close();
         connectstate = CLOSED;
      }
      
      if (debug > 1)
         re.trace(r + " = close()");

      return r;
   }

   /**
    * Sets baud rate, databits (5-8), parity (O, E, N) and
    * stopbits (1,2) for the serial port.
    * Returns -ve on error.
    */
   public synchronized int setPortParameters(int baud,
                                             int bits,
                                             char parity,
                                             int stopbits)
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   val = null;
      
      if (connectstate == OPEN)
      {
         this.baud = baud;
         this.databits = bits;
         this.parity = parity;
         this.stopbits = stopbits;
         val = new String(baud + "," +
                          bits + "," +
                          parity + "," +
                          stopbits);
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               r = sendCommand(SP_SETUP, val.length(), val);
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_ACK))
                     {
                        r = setError(SPEOK);
                        rxdelay = calcRxDelay();
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
         else
         {
            r = setError(SPEPORTCLOSED);
         }
      }
      else
         r = setError(SPEHOSTCLOSED);
      
      if (debug > 1)
         re.trace(r + " = setPort(" +
                  baud + "," +
                  bits + "," +
                  parity + "," +
                  stopbits + ")");

      return r;
   }

   /**
    * Returns the operating parameters for the serial port that's
    * currently open as a comma delimited String formatted as
    * "baudrate,databits,parity,stopbits" where parity is O,E or N
    * for Odd, Even or None. If the port has not been opened successfully
    * the contents of the returned string are meaningless.
    */
   public synchronized String getPortParameters()
   {
      String val = new String(baud + "," +
                              databits + "," +
                              parity + "," +
                              stopbits);
      if (debug > 1)
         re.trace(val + " = getPortParameters()");

      return val;
   }
	
   /**
    * Sets the delay (in mS) between writing
    * characters to the serial port.
    * Returns -ve on error.
    */
   public synchronized int setDelay(int delay)
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   val = null;
      
      if (connectstate == OPEN)
      {
         Integer temp = new Integer(delay);
         val = new String(temp.toString());
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               r = sendCommand(SP_DELAY, val.length(), val);
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_ACK))
                     {
                        r = setError(SPEOK);
                        txdelay = delay;
                        rxdelay = calcRxDelay();
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
         else
         {
            r = setError(SPEPORTCLOSED);
         }
      }
      else
         r = setError(SPEHOSTCLOSED);
      
      if (debug > 1)
         re.trace(r + " = setDelay(" + delay + ")");

      return r;
   }

   /**
    * <p>Resets the line settings, tx delay and record separator.
    * The arguments are boolean values encoded as T/F, t/f or 1/0.
    * <ul>
    *   <li><u>line_settings</u>, if true, causes the line settings
    *   to be reset to their defaults which are defined in the
    *   configuration file.</li>
    *   <li><u>tx_delay</u>, if true, causes the delay before each
    *   character is transmitted to be reset to zero.</li>
    *   <li><u>separator</u>, if true, causes the record separator control
    *   values to be reset to their defaults: inactive separator,
    *   separator character set to NULL and marked as part of the record</li>
    * </ul>
    * <p>Returns -ve on error.</p>
    */
   public synchronized int reset(char line_settings,
                                 char tx_delay,
                                 char separator)
   {
      int      r = setError(SPEOK);
      char     line = translateBoolean(line_settings);
      char     delay = translateBoolean(tx_delay);
      char     sep = translateBoolean(separator);
      String   ack = null; 
      String   val = null;
      
      if (connectstate == OPEN)
      {
         val = new String("" + line + delay + sep);
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               r = sendCommand(SP_RESET, val.length(), val);
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_ACK))
                     {
                        r = setError(SPEOK);
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
         else
         {
            r = setError(SPEPORTCLOSED);
         }
      }
      else
         r = setError(SPEHOSTCLOSED);
      
      if (debug > 1)
         re.trace(r + " = reset(" +
                  line_settings + "," +
                  tx_delay + "," +
                  separator + ")");
      return r;
   }

   /**
    * <p>Sets the record separator and its application conditions.
    * The arguments <u>active</u> and <u>external</u> are
    * boolean values encoded as T/F, t/f or 1/0.
    * <u>active</u> determines whether the field separator
    * character, set by <u>sep</u>, is in use. If it is active
    * <i>put()</i> and <i>get()</i> operate at the field level.
    * <i>get()</i> stops when the separator character is found.
    * <i>put()</i> sends one field at a time. If <u>external</u>
    * is true the separator character is added during a
    * <i>put()</i> operation and not returned to the caller by
    * a <i>get()</i> operation. If <u>external</u> is false the
    * caller must ensure that each string sent by <i>put()</i>
    * is terminated by the separator character and must expect
    * the separator at the end of a string returned by a
    * <i>get()</i> operation.</p>
    *
    * <p>The default is that <u>active</u> and <u>external</u> are
    * false and <u>sep</u> is 0 (zero).</p>
    *
    * <p>Returns -ve on error.</p>
    */
   public synchronized int setSeparator(char active, char sep, char external)
   {
      int      r = setError(SPEOK);
      char     act = translateBoolean(active);
      char     ext = translateBoolean(external);
      String   ack = null;
      String   val = null;
      
      if (connectstate == OPEN)
      {
         val = new String("" + act + sep + ext);
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               r = sendCommand(SP_SETSEP, val.length(), val);
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_ACK))
                     {
                        r = setError(SPEOK);
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
         else
         {
            r = setError(SPEPORTCLOSED);
         }
      }
      else
         r = setError(SPEHOSTCLOSED);
      
      if (debug > 1)
      {
         HexString hs = new HexString(sep);
         
         re.trace(r + " = setSeparator(" +
                  active + "," +
                  hs.getHex() + "," +
                  external + ")");
      }
      
      return r;
   }

   /**
    * Sends bytes in the array to the serial port.
    * If requested by <i>setSeparator()</i>, a
    * terminator may be appended to the byte string.
    * Returns -ve on error.
    */
   public synchronized int put(byte[] buff)
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   val = new String(buff);
      
      if (connectstate == OPEN)
      {
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               r = sendCommand(SP_PUT, val.length(), val);
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_ACK))
                     {
                        r = setError(SPEOK);
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
         else
         {
            r = setError(SPEPORTCLOSED);
         }
      }
      else
         r = setError(SPEHOSTCLOSED);
      
      if (debug > 1)
      {
         HexString hv = new HexString(val);
         
         re.trace(r + " = put(" + hv.getHex() + ")");
      }

      return r;
   }

   /**
    * <p>Reads up to <u>n</u> characters from the port and stores them 
    * internally for later retrieval by the <i>getBytes()</i> method.
    * If requested by <i>setSeparator()</i>, the separator character
    * may terminate the characters read (active separator is true)
    * and may be hidden from the caller (external separator is true).</p>
    *
    * <b>Note:</b> if the remote response is slower than
    * expected an incomplete message may be returned. In this case
    * the caller should wait for a suitable time and invoke the
    * <i>get()</i> message again. See the <i>sleep()</i> method for
    * suggestions on preventing this ocurrence.</p>
    *
    * <p><i>get()</i> returns the number of characters read or -ve on
    * error.</p>
    */
   public synchronized int get(int n)
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   val = null;
      
      if (connectstate == OPEN)
      {
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               val = new String("" + n);
               r = sendCommand(SP_GET, val.length(), val);
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_GET))
                     {
                        val = getReplyValue();
                        bytebuffer = val.getBytes();
                        r = val.length();
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
         else
         {
            r = setError(SPEPORTCLOSED);
         }
      }
      else
         r = setError(SPEHOSTCLOSED);
      
      if (debug > 1)
         re.trace(r + " = get(" + n + ")");

      return r;
   }

   /**
    * Returns the bytes read by the last <i>get()</i> or
    * <i>echo()</i> invocation.
    */
   public synchronized byte[] getBytes()
   {
      if (debug > 1)
      {
         HexString bx = new HexString(bytebuffer);
         re.trace("[" + bx.getHex() + "] = getBytes()");
      }
      
      return bytebuffer;
   }

   /**
    * <p>A convenience method that calls <i>put()</i>, <i>sleep()</i>
    * and <i>get()</i> methods. <u>charsout</u> is sent by <i>put()</i>.
    * The <u>expected</u> and <u>adj</u> arguments are passed to
    * <i>sleep()</i>. <i>get()</i> retrieves the available bytes and
    * stores them locally. If a terminator is active then it will
    * terminate the input string. <i>echo()</i> returns number of
    * characters read or -ve on error.</p>
    *
    * <p>Use the <i>getBytes()</i> method to retrieve the response.</p>
    */
   public synchronized int echo(byte[] charsout, int expected, int adj)
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   val = new String(charsout);

      r = put(charsout);
      if (r == SPEOK)
      {
         r = sleep(expected, adj);
         if (r >=0)
         {
            r = get(expected);
         }
      }  

      if (debug > 1)
      {
         HexString co = new HexString(charsout);
         
         re.trace(r + " = echo(" +
                  co.getHex() + "," +
                  adj + "," +
                  expected + ")");
      }

      return r;
   }

   /**
    * <p>Queries the server for the numbers of bytes in the
    * input and output buffers and the buffer size: both buffers
    * are the same size. The response is decoded and the values stored.
    * Returns -ve on error.</p>
    *
    * <p>Use the <i>getInputCount()</i>, <i>getOutputCount()</i> and
    * <i>getBufferSize()</i> methods to retrieve the results.</p>
    */
   public synchronized int query()
   {
      int      r = setError(SPEOK);
      String   ack = null;
      String   val = null;
      
      if (connectstate == OPEN)
      {
         if (portstate == OPEN)
         {
            if (r == SPEOK)
            {
               r = sendCommand(SP_QUERY, 0, "");
               if (r == SPEOK)
               {
                  r = getReply();
                  if (r == SPEOK)
                  {
                     ack = getReplyCommand();
                     if (ack.equals(SP_QUERY))
                     {
                        val = getReplyValue();
                        decodeCounts(val);
                        r = setError(SPEOK);;
                     }
                     else
                     {
                        r = setServerError(getReplyValue());
                     }
                  }
               }  
            }
         }
         else
         {
            r = setError(SPEPORTCLOSED);
         }
      }
      else
         r = setError(SPEHOSTCLOSED);
      
      if (debug > 1)
         re.trace(r + " = query()");
   
      return r;
   }

   /**
    * Returns the number of bytes in the input buffer retrieved by
    * the preceeding invocation of <i>query()</i>.
    */
   public synchronized int getInputCount()
   {
      if (debug > 1)
         re.trace(rxbytes + " = getInputCount()");
   
      return rxbytes;
   }                 

   /**
    * Returns the number of bytes in the output buffer retrieved by
    * the preceeding invocation of <i>query()</i>.
    */
   public synchronized int getOutputCount()
   {
      if (debug > 1)
         re.trace(txbytes + " = getOutputCount()");
   
      return txbytes;
   }                 
    
   /**
    * Returns the size of the spd input and output buffers.
    * These are both the same size. The value is retrieved by
    * the preceeding invocation of <i>query()</i>.
    */
   public synchronized int getBufferSize()
   {
      if (debug > 1)
         re.trace(buffsize + " = getBufferSize()");
   
      return buffsize;
   }                 
    
   /**
    * <p>Cause the calling process to sleep while the
    * remote process generates and sends a reply.</p>
    *
    * <p>First it queries the server for buffer content.
    * Then it calculates the delay time needed for all
    * waiting bytes to be sent and all <u>expected</u>
    * incoming bytes to have been received. Finally it adds
    * on <u>adj</u>, which is in mS.</p>
    *
    * <p><i>sleep()</i> waits for the calculated time.<p>
    *
    * This is repeated until the output buffer is empty and
    * the number of bytes in the input buffer stops changing
    * or there are enough bytes in the buffer to match the
    * expected number.</p>
    *
    * <p><u>adj</u> is a signed integer that is added to the
    * calculated time. It is in mS and is intended to let
    * the caller specify additional sleep time to make
    * allowance for a remote process that is known to have a
    * long response time. <u>adj</u> can also be used to
    * adjust the sleep time downwards but it cannot reduce
    * the time below zero.</p>
    *
    * <p>Returns -ve on error.</p>
    */
   public synchronized int sleep(int expected, int adj)
   {
      int      r = 0;
      int      tval = 0;
      int      last_rx = 0;
      boolean  doPause = true;

      while (doPause)   
      {
         r = query();               /* get rxbytes and txbytes from spd */
         if (r == SPEOK)
         {
            /*
               This is the basic delay time calculation
               If there is no tx delay specified, use the
               calculated Rx delay for both. Otherwise
               use Rx delay for expected bytes and Tx delay
               for bytes to be sent.
            */
            if (txdelay != 0)
               tval = rxdelay * expected * RXFUDGE +
                      txdelay * txbytes * TXFUDGE;
            else
               tval = rxdelay * (expected + txbytes) * RXFUDGE;
               
            /*
               Now add in the adjustment factor,  making sure
               the time can't go negative
            */
            if (tval + adj < 0)  
               tval = 0;         
            else                 
               tval += adj;

            /*
               Sleep for the calculated time
            */
            try
            {
               Thread.sleep(tval);
               r = 0;
            }
            catch(InterruptedException e)
            {
               error = e.getMessage();
               r = SPETIMERR;
            }  
      
            if (debug > 2)
            {
               re.trace("rxbytes=" + rxbytes +
                        " rxdelay=" + rxdelay +
                        " rxfudge=" + RXFUDGE +
                        " txbytes=" + txbytes +
                        " txdelay=" + txdelay +
                        " txfudge=" + TXFUDGE);
               re.trace("tval=" + tval + "mS  error=" + error);
            }

            last_rx = rxbytes;

            /*
               End the wait if all output bytes have been sent
               and either we have enough bytes on hand cover the
               expected message size or no more bytes are coming in
            */
            if (txbytes == 0 &&
                (rxbytes >= expected || rxbytes == last_rx))
               doPause = false;
         }
            
         if (r != SPEOK)
            doPause = false;
      }

      rxbytes = 0;
      txbytes = 0;
      if (debug > 1)
         re.trace(r + " = sleep(" + expected + "," + adj + ")");
         
      return r;
   }

   /**
    * Diagnostic display of the constructor interface.
    */
   public synchronized String toString()
   {
      return "SerialPort(" + debug + "," + host + "," + port + ")";
   }

   /* Private methods, called from public methods.
    * ===============
    */

   /**
    * Sets up an spd-originated error message
    */
   private int setServerError(String msg)
   {
      error = msg;
      return(-SPEREMERR);
   }

   /**
    * Set the current error code and message
    */
   private int setError(int code)
   {
      error = new String(errmsg[code]);
      return(-code);
   }

   /**
    * Connect to the spd server 
    */
   private int connectServer()
   {
      int r = setError(SPEOK);

      connection = new ClientConnection();
      connection.setDebug((debug-1));
      if (connection.open(host, port))
      {
         r = setServerError(connection.getErrorMessage());
      }
      else
      {
         connectstate = OPEN;
         r = setError(SPEOK);
      }

      return r;
   }

   /**
    *  Send a command to the server.
    *  Returns zero or error code.
    */
   private int sendCommand(String ccmd, int  cvlth, String cval)
   {
      int         r = setError(SPEOK);

      current_message = new SPDMessage(debug);
      current_message.buildMessage(ccmd, cval);
      if (connection.send(current_message.getMessage()))
         r = setServerError(connection.getErrorMessage());

      if (debug > 1)
      {
         HexString cv = new HexString(cval);
         
         re.trace(r +
                  " = sendCommand(" +
                  ccmd +
                  "," +
                  cvlth +
                  "," +
                  cv.getHex() +
                  ")");
      }

      return r;
   }

   /**
    * Get a reply from the server
    * Returns zero or error code.
    */
   private int getReply()
   {
      int         r = setError(SPEOK);
      String      response = null;

      current_message = new SPDMessage(connection.receive(), debug);
      if (current_message == null || current_message.length() == 0)
         r = setServerError(connection.getErrorMessage());
      else
         current_message.unpackMessage();
   
      if (debug > 1)
         re.trace(r + " = getReply()");
         
      return r;
   }

   /**
    * Return the command portion of the last message
    */
   private String getReplyCommand()
   {
      String val = current_message.getCommand();
      
      if (debug > 1)
         re.trace("[" + val + "] = getReplyCommand()");
         
      return val;
   }

   /**
    * Return the value portion of the last message
    */
   private String getReplyValue()
   {
      String val = current_message.getValue();
      
      if (debug > 1)
      {
         HexString hv = new HexString(val);
         
         re.trace("[" + hv.getHex() + "] = getReplyValue()");
      }
         
      return val;
   }

   /**
    * Translate the input to a boolean representation acceptable to spd.
    */
   private char translateBoolean(char b)
   {
      char x;

      if (b == 'T' || b == 't' || b == '1')
         x = 'T';
      else
      if (b == 'F' || b == 'f' || b == '0')
         x = 'F';
      else
      if (b != 0)
         x = 'T';
      else
         x = 'F';

      if (debug > 1)
         re.trace(x + "= translateBoolean(" + b + ")");

      return x;
   }

   /**
    * Calculates the time to receive a single character
    */
   private int calcRxDelay()
   {
      double      bitrate;
      int         bits;
      int         timeout;

      if (debug > 1)
         re.trace("calcRxDelay() inputs:" +
                  " baud=" + baud +
                  " databits=" + databits +
                  " parity=" + parity +
                  " stopbits=" + stopbits);
                  
      bitrate = 1000.0 / baud;
      bits = (parity == 'N' ? 0 : 1) + databits + stopbits;
      timeout = (int)Math.round(bitrate * bits * 2.0 + 0.5);
      
      if (debug > 2)
         re.trace("bitrate=" + bitrate + " mS  bits=" + bits);
            
      if (debug > 1)
         re.trace(timeout + " = calcRxDelay()");

      return timeout;
   }

   /*
    * Decode the serial parameters: 9600,8,N,1
    * (baud,databits,parity,stopbits)
    * and store them in private data elements
    */
   private void decodeParams(String value)
   {
      StringTokenizer   st = new StringTokenizer(value, ",\n\r");
      String            s = null;

      baud = databits = stopbits = -1;
      parity = 'X';

      for (int i = 0; i < 5; i++)
      {
         if (!st.hasMoreTokens())
            break;

         s = st.nextToken();
         switch(i)
         {
            case 0:  baud = new Integer(s).intValue();
                     break;

            case 1:  databits = new Integer(s).intValue();
                     break;

            case 2:  parity = s.charAt(0);
                     break;

            case 3:  stopbits = new Integer(s).intValue();
                     break;

            default: re.error("More values than expected in open() reply");
         }
      }

      if (debug > 1)
         re.trace("decodeParams(" + value + ") decoded" +
                  " baud=" + baud +
                  " databits=" + databits +
                  " parity=" + parity +
                  " stopbits=" + stopbits);
   }

   /*
    * Validity check the serial port parameters
    */
   private String checkParams()
   {
      int     baud_rates[]   = {    50,    75,
                                   110,   150,   300,   600,
                                  1200,  2400,  4800,  9600,
                                 19200, 38400,
                                    -1 };
      int     databit_vals[] = { 5, 6, 7, 8, -1 };
      String  parity_vals = new String("NOE");
      int     stopbit_vals[] = { 1, 2, -1 };

      String r = null;

      if (intval_err(baud, baud_rates))
         r = new String("Invalid baud rate");
      else
      if (intval_err(databits, databit_vals))
         r = new String("Invalid data bit setting");
      else
      if (parity_vals.indexOf(parity) < 0)
         r = new String("Invalid parity setting");
      else
      if (intval_err(stopbits, stopbit_vals))
         r = new String("Invalid stop bit setting");
      else
      if (databits == 8 && parity != 'N')
         r = new String("Invalid combination of databits and parity");

      return r;
   }

   /*
    * Check a numeric value is in the val array.
    * The array must be terminated with -1, which
    * is not a valid value.
    */
   private boolean intval_err(int b, int val[])
   {
      boolean   r = true;
      int   i;

      for (i = 0; val[i] != -1; i++)
      {
         if (b == val[i])
         {
            r = false;
            break;
         }
      }

      return r;
   }
                                                                                                                           
   /*
    * Decode the query reply: bytesin,bytesout
    * and store them in private data elements
    */
   private void decodeCounts(String value)
   {
      StringTokenizer   st = new StringTokenizer(value, ",\n\r");
      String            s = null;

      rxbytes = 0;
      txbytes = 0;
      buffsize = 0;

      for (int i = 0; i < 3; i++)
      {
         if (!st.hasMoreTokens())
            break;

         s = st.nextToken();
         switch(i)
         {
            case 0:  rxbytes = new Integer(s).intValue();
                     break;

            case 1:  txbytes = new Integer(s).intValue();
                     break;

            case 2:  buffsize = new Integer(s).intValue();
                     break;

            default: re.error("More values than expected in query() reply");
         }
      }

      if (debug > 1)
         re.trace("decodeCounts(" + value + ") decoded" +
                  " rxbytes=" + rxbytes +
                  " txbytes=" + txbytes +
                  " buffsize=" + buffsize);
   }
}
