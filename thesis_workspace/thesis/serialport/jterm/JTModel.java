import java.util.*;
import environ.*;
import SerialPort.*;

/**
 * <p><b>JTModel</b> - holds the <b>JTerm</b> connection details
 * and response.</p>
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

public class JTModel extends Observable
{
   public final String        UNIX = new String("Unix");
   public final String        OS9 = new String("OS-9");
   public final String        MSDOS = new String("MS-DOS");
   private final String       osList = new String("Unix,OS-9,MS-DOS");
   private final int          LF = 0; 
   private final int          CR = 1; 
   private final int          CRLF = 2; 
   
   private JTerm              theApp = null;
   private SerialPort         server = null;
   private JTReader           reader = null;
   private String             progName = null;
   private String             hostName = null;
   private int                portNumber = 0;
   
   private boolean            serverConnection = false;
   private boolean            serialConnection = false;
   private boolean            activeReader = false;
   private String             serialPortList = null;
   private String             serialPortName = null;
   private String             serialParameters = null;
   private int                error = 0;
   private String             errorMessage = null;
   private StringBuffer       receivedBuffer = new StringBuffer();
   private boolean            receivedData = false;
   private boolean            selectedOS = false;
   private String             osName = UNIX;
   private int                lineSeparator = LF;
   private int                tabSpace = 8;

   private int                debug = 0;
   private ReportError        re = new ReportError("JTerm");   

   /**
    * The only constructor. It initialises the model by setting
    * defaulting the server host and port to <i>localhost:16001</i>.
    * It marks the the connection as inactive and itself as changed.
    */ 
   public JTModel(JTerm theApp, String progName, int debug)
   {
      this.theApp = theApp;
      this.progName = progName;
      this.debug = debug;
      hostName = new String("localhost");
      portNumber = 16001;

      serverConnection = false;
      setChanged();
      notifyObservers();
   }

   /**
    * Open a connection to <b>spd</b>, the <b>SerialPort</b> server.
    * Store server connection details in the model as plain text,
    * save the final state code and mark the model as changed.
    * Return <b>true</b> if the connection attempt fails.
    */
   synchronized public int connectServer(String host, int port)
   {
      if (debug > 0)
         re.trace("connectServer(" +
                     hostName + "," + portNumber + ")");
         
      hostName = new String(host);
      portNumber = port;
      serverConnection = false;
      serialConnection = false;

      /*
       * Make the connection and retrieve the portlist.
       */
      server = new SerialPort((debug >= 3 ? debug - 2 : 0),
                              hostName,
                              portNumber);
      error = server.findPortList();
      if (error >= 0)
      {
         serverConnection = true;
         serialPortList = server.getPortList();
      }
      else
      {
         errorMessage = server.getError();
      }

      setChanged();
      notifyObservers();
      if (debug > 0)
         re.trace(error + " = connectServer()");
         
      return error;
   }

   /**
    * Close the server connection and mark as inactive.
    */
   synchronized public void disconnectServer()
   {
      if (serverConnection)
      {
         if (serialConnection)
            closePort();
      
         if (error == 0)
            error = server.delete();
            if (error == 0)
               serverConnection = false;

         if (error == 0)
            errorMessage = server.getError();
         
         setChanged();
         notifyObservers();
      }
      
      if (debug > 0)
         re.trace("disconnectServer()");
   }

   /**
    * Cause <b>spd</b> to open the specified serial port.
    * Store port details in the model as plain text,
    * save the final state code and mark the model as changed.
    * Returns a non-zero value if the connection attempt fails.
    */
   synchronized public int openPort(String port)
   {
      if (debug > 0)
         re.trace("openPort(" + port + ")");

      serialConnection = false;
      serialPortName = port;
      error = server.open(serialPortName);
      if (error >= 0)
      {
         serialConnection = true;
         serialParameters = server.getPortParameters();
      }
      else
         errorMessage = server.getError();      

      setChanged();
      notifyObservers();
      if (debug > 0)
         re.trace(error + " = openPort()");
         
      return error;
   }

   /**
    * Stop polling the server.
    * Close the port and mark as inactive.
    */
   synchronized public void closePort()
   {
      if (serialConnection)
      {
         if (activeReader)
         {
            reader.exit();
            activeReader = false;
         }
         
         error = server.close(true);
         if (error == 0)
            serialConnection = false;
      
         if (error != 0)
            errorMessage = server.getError();

         selectedOS = false;
         setChanged();
         notifyObservers();
      }
      
      if (debug > 0)
         re.trace("closePort()");
   }

   /**
    * Return the error description.
    */
   synchronized public String getError()
   {
      if (debug > 0)
         re.trace(errorMessage + " = getError()");
         
      return errorMessage;
   }

   /**
    * Return the TCP/IP hostname of the server.
    */
   synchronized public String getHostname()
   {
      if (debug > 0)
         re.trace(hostName + " = getHostname()");
         
      return hostName;
   }

   /**
    * Return the operating system list, used to identify the
    * target operating system.
    */
   synchronized public String getOSList()
   {
      if (debug > 0)
         re.trace(osList + " = getPortList()");
         
      return osList;
   }

   /**
    * Return the serial port list.
    */
   synchronized public String getPortList()
   {
      if (debug > 0)
         re.trace(serialPortList + " = getPortList()");
         
      return serialPortList;
   }

   /**
    * Return the serial port name.
    */
   synchronized public String getPortName()
   {
      if (debug > 0)
         re.trace(serialPortName + " = getPortName()");
         
      return serialPortName;
   }

   /**
    * Return the server's TCP/IP port number.
    */
   synchronized public int getPortNumber()
   {
      if (debug > 0)
         re.trace(portNumber + " = getPortNumber()");
         
      return portNumber;
   }

   /**
    * Return the serial port parameters.
    */
   synchronized public String getPortParameters()
   {
      if (debug > 0)
         re.trace(serialParameters + " = getPortParameters()");
         
      return serialParameters;
   }

   /**
    * Retrieve any data received from the serial connection.
    */
   synchronized public String getResponse()
   {
      String   r = new String(receivedBuffer.toString());
      int      n = r.length();
      
      receivedBuffer.delete(0, n);
      receivedData = false;
      if (debug > 1)
      {
         HexString hr = new HexString(r);
         
         re.trace(hr.getHex() + " = getResponse()");
      }

      return r;
   }

   /**
    * Return the (target operating system dependent) tab spacing.
    */
   public int getTabSpace()
   {
      return tabSpace;
   }
      
   /**
    * Return the frame title.
    */
   synchronized public String getTitle()
   {
      StringBuffer t = new StringBuffer(progName);

      if (serverConnection)
      {
         t.append(" - " + hostName + ":" + portNumber);
         if (serialConnection)
         {
            t.append(" <-> " + serialPortName + " " + serialParameters);
            if (selectedOS)
            {
               t.append(" (" + osName + ")");
            }
         }
      }
         
      if (debug > 0)
         re.trace(t.toString() + " = getTitle()");
         
      return t.toString();
   }

   /**
    * Returns true if the server connection is open.
    */
   synchronized public boolean isConnectedToServer()
   {
      if (debug > 0)
         re.trace(serverConnection + " = isConnectedToServer()");
         
      return serverConnection;
   }

   /**
    * Returns true if a serial connection is open.
    */
   synchronized public boolean isConnectedToPort()
   {
      if (debug > 0)
         re.trace(serialConnection + " = isConnectedToPort()");
         
      return serialConnection;
   }

   /**
    * Check whether an error has ocurred.
    */
   synchronized public boolean isErrorCondition()
   {
      return (error != 0);
   }

   /**
    * Check whether new data has been received from the
    * serial port.
    */
   synchronized public boolean isResponse()
   {
      return (receivedData);
   }

   /**
    * Check whether an operating system has been selected.
    */
   synchronized public boolean isOSSelected()
   {
      return (selectedOS);
   }

   /**
    * <p>Accept a single character input by the keyboard.
    * Pass it to the server and notify the reader thread
    * that input is expected.
    * Notify observers so the typed key gets displayed.</p>
    *
    * <p><b>NOTE:</b> This function puts an error message
    * on the screen if there is no serial connection open.</p>
    */
   synchronized public void keyTyped(char c)
   {
      int      err = 0;

      if (serialConnection)
      {
         err = server.put(internal2external(c));
         if (err < 0)
            serialError();
         
         reader.interrupt();
      }
      else
      {
         receivedBuffer.append("\n**** NO SERIAL CONNECTION ****\n");
         receivedData = true;
      }
      
      setChanged();
      notifyObservers();

      if (debug > 0)
      {
         HexString hc = new HexString(c);
         re.trace("model.keyTyped(" + hc.getHex() + ")");
      }
   }

   /**
    * Poll the server for data. If any is available
    * it is appended to the receive buffer,
    * the reader thread is prepared to accept more
    * data and screen updating is triggered.
    */
   synchronized public void scanForData()
   {
      int      n = 0;
      int      e = 0;
      String   s = null;

      if (debug > 1)
         re.trace("scanForData() started");
      
      /*
       * Query the server for received bytes.
       */ 
      e = server.query();
      if (e < 0)
      {
         serialError();
      }
      else
      {
         /*
          * Find out how many bytes were received
          */
         n = server.getInputCount();
         if (n > 0)
         {
            /*
             * At least one byte has been received. Find out
             * how many are waiting and grab them.
             */
            e = server.get(n);
            if (e != n)
               serialError();

            /*
             * Store the retrieved bytes.
             * Make sure we stay in fast polling for at least
             * one more scan.
             * Notify observers, etc. so the new stuff gets displayed.
             */
            s = external2internal(server.getBytes());   
            receivedBuffer.append(s);
            receivedData = true;
            reader.setFastPoll();
            setChanged();
            notifyObservers();
            if (debug > 1)
            {
               HexString hs = new HexString(s);
               
               re.trace("scanForData() found [" + hs.getHex() + "]");
            }
         }
         else
         {
            if (debug > 1)
               re.trace("scanForData() found nothing");
         }
      }         
   }
   
   /**
    * Trap and report <b>SerialPort</b> errors.
    */
   public void serialError()
   {
      re.error(server.getError());
   }

   /**
    * Store the OS name.
    */
   synchronized public void setOSName(String name)
   {
      osName = name;
      selectedOS = true;
      if (debug > 0)
         re.trace("setOSName(" + name + ")");

      if (osName.compareTo(UNIX) == 0)
      {
         server.setDelay(0);     /* No delay between characters */
         lineSeparator = LF;     /* LF is newline value */
         tabSpace = 8;
      }
      else
      if (osName.compareTo(OS9) == 0)
      {
         server.setDelay(50);    /* 50 mS delay between characters */ 
         lineSeparator = CR;     /* CR is newline value */
         tabSpace = 4;
      }
      else
      if (osName.compareTo(MSDOS) == 0)
      {
         server.setDelay(0);     /* No delay between characters */
         lineSeparator = CRLF;   /* CR+LF is newline separator */
         tabSpace = 8;
      }
      else
      {
         re.error("Invalid OS type: " + osName);
      }
      
      setChanged();
      notifyObservers();
   }

   /**
    * Store the serial port parameters.
    */
   synchronized public int setPortParameters(String params)
   {
      int error = 0;
      
      serialParameters = params;
      JTStringParser sp = new JTStringParser(params);
      try
      {
         int baud = Integer.parseInt(sp.getField(0));
         int dataBits = Integer.parseInt(sp.getField(1));
         String parity = sp.getField(2);
         int stopBits = Integer.parseInt(sp.getField(3));
         error = server.setPortParameters(baud,
                                          dataBits,
                                          parity.charAt(0),
                                          stopBits);
         if (error < 0)
            errorMessage = server.getError();
      }
      catch (NumberFormatException e)
      {
         error = -1;
         errorMessage = e.getMessage();
      }   
           
      if (debug > 0)
         re.trace(error + " = setPortParameters([" + params + "])");

      setChanged();
      notifyObservers();
      return error;
   }

   /**
    * Create and start a thread to poll the server for input.
    */
   public void startReader()
   {
      reader = new JTReader(theApp, debug);
      reader.start();
      activeReader = true;
   }
   
   /**
    * Clear the changed flag.
    */
   public void updateCompleted()
   {
      if (debug > 0)
         re.trace("updateCompleted()");
         
      clearChanged();
   }

   /**
    * Convert external, target operating system dependent,
    * newlines to internal newline representation.
    */
   private String external2internal(byte[] b)
   {
      StringBuffer w = new StringBuffer();

      for (int i = 0; i < b.length; i++)
      {
         switch(lineSeparator)
         {
            case LF:    w.append((char)b[i]);
                        break;

            case CR:
            case CRLF:  if (b[i] != '\r')
                           w.append((char)b[i]);

                        break;

            default:    re.error("invalid lineSeparator code");
         }
      }

      if (debug > 2)
      {
         HexString hi = new HexString(b);
         HexString ho = new HexString(w.toString());
         
         re.trace(ho.getHex() + " = external2internal(" + hi.getHex() + ")");
      }
      
      return w.toString();
   }

   /**
    * Convert internal newlines to the external, target operating
    * system dependent, newline representation.
    */
   private byte[] internal2external(char c)
   {
      byte[] b = null;

      if (c == '\n')
      {
         switch(lineSeparator)
         {
            case LF:    b = new byte[1];
                        b[0] = '\n';
                        break;

            case CR:    b = new byte[1];
                        b[0] = '\r';
                        break;

            case CRLF:  b = new byte[2];
                        b[0] = '\r';
                        b[1] = '\n';
                        break;

            default:    re.error("invalid lineSeparator code");
         }
      }
      else
      {
         b = new byte[1];
         b[0] = (byte)c;
      }

      if (debug > 2)
      {
         HexString hi = new HexString(c);
         HexString ho = new HexString(b);

         re.trace(ho.getHex() + " = internal2external(" + hi.getHex() + ")");
      }
      
      return b;
   } 
}
