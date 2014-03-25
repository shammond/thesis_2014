import java.util.*;
import java.io.*;
import environ.*;
import SerialPort.*;

/**
 * <p>This is the test harness for the <b>SerialPort</b> class and
 * the serial port server, <i>spd</i>. It is normally used for
 * regression testing in the <i>Java_regress</i> script.</p>
 * <ul>
 *  <li>Written by M.C.Gregorie.</li>
 *  <li>Version history
 *   <ul>          
 *     <li>1.0, Initial release 18/12/02</li>
 *     <li>1.1, Added the login command 08/07/05</li>
 *     <li>1.2, Added sp_getport command 23/12/05</li>
 *   </ul>
 *  </li>
 * </ul>
 * <p>The program runs one or more test scripts, which are files named
 * on the command line. The only acceptable option, apart from
 * <b>-?</b>, is the debug option, <b>-d</b>. This causes diagnostic
 * output on stderr. Progressively more debug options cause additional
 * diagnostic detail to be output.</p>
 *
 * <p>Within a script blank lines and comment lines starting with #
 * are ignored. The commands prefixed by <i>sp_</i> correspond to the
 * functions in the C SerialPort interface though the parameters may
 * differ in number and order. Where required, combinations of methods
 * are invoked to emulate the corresponding C function. This apparent
 * oddity was introduced so that the same test scripts can exercise both
 * the Java and C interfaces to demonstrate that they achieve the same
 * functionality. Other commands have been added to make the scripting
 * language easier to use.</p>
 *
 * <p>Commands must be written on a single line and may be followed by
 * a comment which starts with a #. A command consists of a command word
 * that may be followed by a number of space separated parameters.
 * The commands are documented below:
 * <dl>
 *   <dt><kbd><b>pause 999 </b></kbd></dt>
 *       <dd>pause the script for 999 milliseconds.
 *           This has nothing to do with the SerialPort interface.
 *           It is a convenience function that makes scripting easier.</dd>
 *   <dt><kbd><b>login name [password] </b></kbd></dt>
 *       <dd>Login sequence. The password is only needed if the login
 *           name demands one. This command is built from SerialPort
 *           interface functions combined with conditional expressions
 *           that the scripting language doesn't support.</dd>
 *   <dt><kbd><b>sp_create hostname port </b></kbd></dt>
 *       <dd>start a SerialPort session</dd>
 *   <dt><kbd><b>sp_delete </b></kbd></dt>
 *       <dd>end a SerialPort session</dd>
 *   <dt><kbd><b>sp_listports </b></kbd></dt>
 *       <dd>list available serial ports</dd>
 *   <dt><kbd><b>sp_open serialport </b></kbd></dt>
 *       <dd>open a serial port</dd>
 *   <dt><kbd><b>sp_close TRUE|FALSE </b></kbd></dt>
 *       <dd>close the serial port</dd>
 *   <dt><kbd><b>sp_setport baud dbits parity sbits</b></kbd></dt>
 *       <dd>set port conditions</dd>
 *   <dt><kbd><b>sp_getport</b></kbd></dt>
 *       <dd>display the port's baudrate,databits,parity and stopbits</dd>
 *   <dt><kbd><b>sp_delay txdelay</b></kbd></dt>
 *       <dd>set delay for each transmitted character</dd>
 *   <dt><kbd><b>sp_setsep active sepchar external </b></kbd></dt>
 *       <dd>set field terminator active/inactive, specify the terminator
 *           character and specify it as external/part of the field.
 *           <u>Active</u> and <u>external</u> are T/F, t/f or 1/0.
 *           The separator character may be a normal character,
 *           \n (system newline), \r (CR), \l (LF), \t (tab) or \0 (null).
 *           Any other character prefixed with \ takes its own value.</dd>
 *   <dt><kbd><b>sp_reset line_settings tx_delay separator </b></kbd></dt>
 *       <dd>reset control values if the associated parameter is true.
 *           Parameter values are are T/F, t/f or 1/0.</dd>
 *   <dt><kbd><b>sp_put text </b></kbd></dt>
 *       <dd>send data</dd>
 *   <dt><kbd><b>sp_get 999 </b></kbd></dt>
 *       <dd>retrieve up to 999 characters</dd>
 *   <dt><kbd><b>sp_echo text 999 adj </b></kbd></dt>
 *       <dd>send 'text', wait <u>adj</u> mS, receive up to 999 chars</dd>
 *   <dt><kbd><b>sp_query </b></kbd></dt>
 *       <dd>display bytes in input and output queues</dd>
 *   <dt><kbd><b>sp_sleep 999 adj </b></kbd></dt>
 *       <dd>wait while up to 999 chars are received + <u>adj</u> mS </dd>
 * </dl>
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

public class TestSPD
{
   private static final String VERSION   = "1.2";
   private static final String COPYRIGHT = "Copyright M.C.Gregorie, 2005";
   private static final int    MAXPARAMS = 6;
   private static final int    MAXMSG = 1500;

   private static int          debug = 0;
   private static boolean      help = false;
   private static Vector       scripts = new Vector();
   private static String       errstring = null;
   private static SerialPort   sp = null;


   /**
    * The program is run when <i>java</i> invokes this class.
    */
   public static void main(String args[])
   {
      /*
         Parse and set up command options
      */
      readArgs(args);

      /*
         If help was asked for, give it and quit
      */
      if (help) 
      {
         showHelp();
         System.exit(0);
      }

      for (int i = 0; i < scripts.size(); i++)
      {
         if (!runTest((String)scripts.elementAt(i)))
            break;
      }
   }

   /**
    * This method scans the command line for options and does
    * all the associated setup.
    */
   private static void readArgs(String args[])
   {
      String arg = null;

      ArgParser ap = new ArgParser("TestSPD", args, "?d");

      try
      {
         ap.parse();
      }
      catch(ArgumentException e)
      {
         System.out.println("Error:" + e.getMessage());
         System.exit(1);
      }

      for (int i = 1;; i++)
      {
         arg = ap.nextOption();
         if (arg == null)
            break;

         if (arg.equals("?"))
            help = true;
         else
         if (arg.equals("d"))
            debug++;            
      }

      for (int i = 1;; i++)
      {
         arg = ap.nextArgument();
         if (arg == null)
            break;

         scripts.add(arg);
      }            
   }

   /**
    *  Show the program's purpose and command-line options
    */
   private static void showHelp()
   {
      System.out.println("\nTestSPD " + VERSION + " " + COPYRIGHT + "\n");
      System.out.println("Syntax  : java TestSPD [<opts>] script ...");
      System.out.println("Function: Java SerialPort class test harness runs");
      System.out.println("          the test scripts and displays " +
                                   "test results.");
      System.out.println("Options :");
      System.out.println("     -d           debug mode if given");
   }

   /**
    * Run tests from a single script
    */
   private static boolean runTest(String script)
   {
      File            f = null;
      FileReader      fr = null;
      BufferedReader  br = null;

      int             errcnt = 0;
      int             err = 0;
      boolean         scriptEnd = false;

      int             lineno = 0;
      String          line = null;
      Integer         l = null;
      String          lineNumber = null;
      StringTokenizer lt = null;
      int             linelth = 0;
      int             comment_marker = 0;
      String          s = null;
      String          fn = null;

      String          p[] = new String[MAXPARAMS];
      int             pn[] = new int[MAXPARAMS];
      String          value = null;
      byte            byte_buffer[] = new byte[MAXMSG];
      int             c = 0;
      boolean         bv = false;
      int             rx = 0;
      int             tx = 0;
      int             buffsize = 0;

      errcnt = 0;
      sp = new SerialPort(debug, "localhost", 16001);
      
      f = new File(script);
      if (!f.canRead())
      {
         System.out.println("Can't open " + script);
         return false;
      }
      else
      {
         System.out.println("Processing script " + script);
      }

      try
      {
         fr = new FileReader(f);
      }
      catch (FileNotFoundException fne)
      {
         System.out.println("FileNotFoundException " +
                            fne.getMessage() +
                            " reading script " +
                            script);
         return false;
      }
      
      br = new BufferedReader(fr);
      
      while (!scriptEnd)
      {
         try
         {
            line = br.readLine();
         }
         catch (IOException ie)
         {
            System.out.println("IOException " +
                               ie.getMessage() +
                               " reading script " +
                               script);
            return false;
         }

         scriptEnd = (line == null);
         if (scriptEnd)
            break;
            
         /*
            Count lines read.
            Print the line.
         */
         linelth = line.length();
         lineno++;
         l = new Integer(lineno);
         lineNumber = l.toString();
         for (int i = 0; i < (3 - lineNumber.length()) ; i++)
            System.out.print(" ");
            
         System.out.println(lineNumber + ": " + line);

         /*
            Ignore blank lines and those starting with #
         */
         if (isBlank(line) || line.charAt(0) == '#')
            continue;

         /*
            If the line has a trailing comment, remove it
         */
         comment_marker = line.indexOf('#');
         if (comment_marker >= 0)
         {
            line = line.substring(0, comment_marker);
            linelth = line.length();
            if (debug > 2)
            {
               System.out.println("Line truncated to '" + line + "'");
            }
         }

         /*
            Parse the command
         */
         lt = new StringTokenizer(line, " \t");
         for (int i = 0; i < MAXPARAMS; i++)
         {
            p[i] = new String("");
            pn[i] = 0;
         }
         
         try
         {
            fn = lt.nextToken();
         }
         catch (NoSuchElementException e)
         {
            System.out.println("     Error: No method in this line");
            errcnt++;
            continue;
         }

         for (int i = 0; i < MAXPARAMS; i++)
         {
            try
            {
               p[i] = lt.nextToken();
            }
            catch (NoSuchElementException e)
            {
               p[i] = new String("");
            }

            if (p[i].length() != 0)
               pn[i] = strToInt(p[i]);
         }
         
         if (debug > 1)
         {
            System.out.println("     Debug:   fn=" + fn);
            for (int i = 0; i < MAXPARAMS; i++)
               if (p[i].length() > 0)
                  System.out.println("              p[" +
                                     i + "]:[" +
                                     p[i] +
                                     "]=" +
                                     pn[i]);
         }

         /*
            Start by clearing errors returned by exceptions
         */
         errstring = new String();

         /*
            Identify the method to be called and do so, reporting results
         */
         if (fn.equals("pause"))
         {
            try
            {
               Thread.sleep(pn[0]);
               err = 0;
            }
            catch(InterruptedException e)
            {
               errstring = e.getMessage();
               err = -1024;
            }  
         }
         else
         if (fn.equals("login"))
         {
            err = login(p[0], p[1], debug);
         }
         else
         if (fn.equals("sp_create"))
         {
            sp = new SerialPort(debug, "localhost", 16001);
            if (sp == null)
            {
               err = -1024;
               errstring = new String("can't create SerialPort object");
            }
            else
            {
               System.out.println("     Object: " + sp.toString());
               err =  0;
            }
         }
         else
         if (fn.equals("sp_delete"))
         {
            err = sp.delete();
         }
         else
         if (fn.equals("sp_listports"))
         {
            value = new String();
            err = sp.findPortList();
            if (err == 0)
               value = sp.getPortList();

            if (err >=0)
               System.out.println("     Port list: [" +
                                  value + "] (" +
                                  value.length() + " bytes)");
         }
         else
         if (fn.equals("sp_open"))
         {
            err = sp.open(p[0]);
         }
         else
         if (fn.equals("sp_close"))
         {
            if (p[0].length() == 0)
            {
               bv = false;
            }
            else
            {
               c = p[0].charAt(0);
               if (c == 'T' || c == 't' || c == '1')
                  bv = true;
               else
                  bv = false;
            }
            
            err = sp.close(bv);
         }
         else
         if (fn.equals("sp_setport"))
         {
            err = sp.setPortParameters(pn[0], pn[1], p[2].charAt(0), pn[3]);
         }
         else
         if (fn.equals("sp_getport"))
         {
            value = sp.getPortParameters();
            System.out.println("     Port parameters: [" +
                               value + "] (" +
                               value.length() + " bytes)");
            err = 0;
         }
         else
         if (fn.equals("sp_delay"))
         {
            err = sp.setDelay(pn[0]);
         }
         else
         if (fn.equals("sp_setsep"))
         {
            err = sp.setSeparator(makeChar(p[0]),
                                  makeChar(p[1]),
                                  makeChar(p[2]));
         }
         else
         if (fn.equals("sp_reset"))
         {
            err = sp.reset(makeChar(p[0]),
                           makeChar(p[1]),
                           makeChar(p[2]));
         }
         else
         if (fn.equals("sp_reset"))
         {
            err = sp.reset(makeChar(p[0]),
                           makeChar(p[1]),
                           makeChar(p[2]));
         }
         else
         if (fn.equals("sp_put"))
         {
            byte_buffer = p[0].getBytes();
            err = sp.put(byte_buffer);
         }
         else
         if (fn.equals("sp_get"))
         {
            value = new String("");
            err = sp.get(pn[0]);
            if (err >= 0)
            {
               byte_buffer = sp.getBytes();
               value = new String(byte_buffer);
               System.out.println("     Return string: [" +
                                  value + "] (" +
                                  err + " bytes)");
               err = 0;
            }
         }
         else
         if (fn.equals("sp_echo"))
         {
            value = new String("");
            byte_buffer = p[0].getBytes();
               
            err = sp.echo(byte_buffer, pn[1], pn[2]);
            if (err >= 0)
            {
               byte_buffer = sp.getBytes();
               value = new String(byte_buffer);
               System.out.println("     Return string: [" +
                                  value + "] (" +
                                  err + " bytes)");
               err = 0;
            }
         }
         else
         if (fn.equals("sp_query"))
         {
            err = sp.query();
            if (err == 0)
            {
               rx = sp.getInputCount();
               tx = sp.getOutputCount();
               buffsize = sp.getBufferSize();
               System.out.println("     Return values: Received bytes=" + rx +
                                  "  Transmission queue=" + tx +
                                  "  Buffer size=" + buffsize); 
            }
         }
         else
         if (fn.equals("sp_sleep"))
         {
            err = sp.sleep(pn[0], pn[1]);
         }
         else
         {
            System.out.println("     Error: unknown method name");
            errcnt++;
            continue;
         }

         /*
            See if the method threw an error and report it
         */
         if (err != 0)
         {
            if (err == -1024)
            {
               System.out.println("     Error:  " + err + "," + errstring);
            }
            else
            {  
               errstring = sp.getError();
               System.out.println("     Error:  " + err + "," + errstring);
               errcnt++;
            }
         }
      }

      /*
         Run complete
      */
      System.out.println("Script ended: " + errcnt + " errors");
      System.out.println();

      try
      {
         br.close();
      }
      catch (IOException e)
      {
         System.out.println("IOException " +
                            e.getMessage() +
                            " closing script " +
                            script);
         return false;
      }
      
      return true;
   }

   /*
      Helper class - check that a string is empty
   */
   static private boolean isBlank(String s)
   {
      for (int i = 0; i < s.length(); i++)
      {
         if (!Character.isWhitespace(s.charAt(i)))
            return false;
      }

      return true;
   }

   /*
      Implement the login sequence for an OS-9 system:
      
         send CR, get three lines ending with "User name?: "
         send login_name CR, get a response
         if "password:"
            send password, get a response
         end-if
         if response contains "Invalid password" or "Who?"
            report login failure
         else
            report login OK
         end-if

      The whole logic flow of this method is more or less destroyed by error
      checking. If an error ocurrs, the rest of the function is skipped.
      You have been warned!
   */
   private static int login(String name, String password, int debug)
   {
      final int   VALSIZE = 500;
      final int   OS9DELAY = 100;   /* mSec */
      final char  ON = 'T';
      final char  OFF = 'F';
      final char  CR = '\r';
      final char  NIL = 0;
   
      String      value = null;
      int         err = 0;

      if (debug > 0)
         System.err.println("login(" + name +
                            "," + password +
                            "," + debug + ")");

      errstring = "OK";

      /*
         Set an active, external separator of CR
         Send a CR to wake up tsmon and get a login prompt
         by sending a zero length field, which will have an external
         separator of CR added.
         Wait for OS-9 to wake up and send a prompt
         Read in the complete three line prompt
      */
      err = sp.setSeparator(ON, CR, ON);
      if (err >= 0)
      {
         String blank = new String("");
         err = sp.put(blank.getBytes());
            if (err >= 0)
            err = sp.setSeparator(OFF, NIL, OFF);
            if (err >= 0)
               err = sp.sleep(VALSIZE, OS9DELAY);
               if (err >=0)
                  err = sp.get(VALSIZE);
                  if (err >= 0)
                     value = new String(sp.getBytes());
      }
      
      if (err >= 0)
      {
         /*
            Throw an error if a login prompt wasn't received
         */
         if (value.indexOf("User name?:") < 0)
         {
            err = -1024;
            errstring = "login prompt not received";
         }
      }

      if (err >= 0)
      {
         /*
            Re-enable CR field termination.
            Send the CR terminated login name.
            Disable field termination: the response might be a password
            prompt or a (possibly multiline) initial prompt. We need to
            read the lot.
            Wait while the response is received
            Retrieve the response
         */
         err = sp.setSeparator(ON, CR, ON);
         if (err >= 0)
            err = sp.put(name.getBytes());
            if (err >= 0)
               err = sp.setSeparator(OFF, NIL, OFF);
               if (err >= 0)
                  err = sp.sleep(VALSIZE, OS9DELAY);
                  if (err >= 0)
                     err = sp.get(VALSIZE);
                     if (err >=0)
                        value = new String(sp.getBytes());
      }

      if (err >= 0 && value.indexOf("Password:") >= 0)
      {
         /*
            Password prompt: send the password with CR termination.
            Disable field termination and read the (possibly)
            multiline prompt.
         */
         err = sp.setSeparator(ON, CR, ON);
         if (err >= 0)
            err = sp.put(password.getBytes());
            if (err >= 0)
               err = sp.setSeparator(OFF, NIL, OFF);
               if (err >= 0)
                  err = sp.sleep(VALSIZE, OS9DELAY);
                  if (err >= 0)
                     err = sp.get(VALSIZE);
                     if (err >= 0)
                        value = new String(sp.getBytes()); 
      }

      if (err >= 0)
      {
         /*
            End of either sequence: check whether the login succeeded
            and report an error if it didn't.
         */
         if (value.indexOf("Who?") >= 0 ||
             value.indexOf("Invalid password") >= 0)
         {
            err = -1024;
            errstring = "Login failed";
         }
         else
         {
            System.out.println("     Login OK");
         }
      }

      /*
         Set the separator back to the default condition
      */
      if (err >= 0)
         sp.setSeparator(OFF, NIL, OFF);

      /*
         If a SerialPort error ocurred, retrieve the error.
         A value of -1024 indicates an error detected by login(),
         not the result of a failed SerialPort call.
      */
      if (err < 0 && err != -1024)
         errstring = sp.getError();

      /*
         Correct the reply value: anything positive is returned as zero
      */
      if (err > 0)
         err = 0;
 
      if (debug > 0)
      {
         System.err.println(err +
                            " = login(" + name +
                            "," + password +
                            "," + debug +
                            ") - error status: " + errstring);
      }
   
      return err;
   }

   /*
      Helper class - convert an escaped character into its actual value
   */
   private static char makeChar(String s)
   {
      char  c;

      if (s.charAt(0) != '\\')
      {
         c = s.charAt(0);
      }
      else
      {
         switch(s.charAt(1))
         {
            case 'n':   c = '\n';
                        break;
            case 'r':   c = 0x0d;
                        break;
            case 'l':   c = 0x0a;
                        break;
            case 't':   c = '\t';
                        break;
            case '0':   c = 0;
                        break;
            default:    c = s.charAt(1);
         }
      }  

      return c;
   }
   
   /*
      Helper class - make int from String value
   */
   private static int strToInt(String s)
   {
      int   r;
      
      try
      {
         Integer n = new Integer(s);
         r = n.intValue();
      }
      catch (NumberFormatException e)
      {
         r = 0;
      }

      return r;
   }
}
