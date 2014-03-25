import java.util.Vector;
import java.io.*;
import environ.*;

/**
 * <p><b>JTerm</b> - a Java terminal emulator.</p>
 *
 * <p><b>JTerm</b> uses the <b>SerialPort</b> package to access
 * serial ports.</p>
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
public class JTerm
{
   private static final String VERSION = new String("1.0");
   private static final String COPYRIGHT = new String("(c) M.C.Gregorie, 2005");
   private static final String progName = new String("JTerm");
   private static final String optlist = new String("?d");
   private static JTerm        theApp = null;
   private static JTDisp       display = null;
   private static JTModel      model = null;
   private static JTView       view = null;
   
   private static boolean      help = false;
   private static int          debug = 0;
   private static ReportError  r = new ReportError(progName);

   /**
    * Initial method called to start the application.
    */
   static public void main(String[] args)
   {
      /*
       * Parse and set up command options
       */
      readArgs(args);
      
      /*
       * If help was asked for, give it and quit
       */
      if (help) 
         showHelp();

      theApp = new JTerm();
      theApp.init();
   }

   /**
    * This method acts as the connector between window,
    * model and view controller.
    */
   private void init()
   {
      display = new JTDisp(theApp, debug);
      model = new JTModel(theApp, progName, debug);
      view = new JTView(theApp, debug);

      display.addContent(progName, VERSION, COPYRIGHT);
      model.addObserver(view);
      display.setVisible(true);
   }

   /**
    * Make sure the model has closed server and serial
    * connections before stopping the application. This is called
    * when the main window is closed or if File:Exit is selected.
    */
   public void disconnect()
   {
      model.disconnectServer();
   }
   
   /**
    * Make the window reference available to called classes.
    */
   public JTDisp getWindow()
   {
      return display;
   }
   
   /**
    * Make the model reference available to called classes.
    */
   public JTModel getModel()
   {
      return model;
   }
   
   /**
    * Make the view controller reference available to called classes.
    */
   public JTView getView()
   {
      return view;
   }

   /**
    * Make the window version string available to called classes.
    */
   public static String getVersion()
   {
      return (progName + " v" + VERSION + " " + COPYRIGHT);
   }
   
   /*
    * This method scans the command line for options and does
    * all the associated setup.
    */
   static private void readArgs(String[] args)
   {
      int      i; 
      String   opt;
      String   val;

      ArgParser ap = new ArgParser("JTerm", args, optlist);
      ap.setCaseSensitive(true);

      try
      {
         ap.parse();
      }
      catch(ArgumentException e)
      {
         r.error(e.getMessage());
      }

      while ((opt = ap.nextOption()) != null)
      {
         if (opt.equals("?"))
            help = true;
         else
         if (opt.equals("d"))
            debug++;
         else
            r.error("Impossible happened parsing options");
      }
   }

   /*
    *  Show the punter what the options are
    */
   static private void showHelp()
   {
      pHelp("\n" + getVersion() + "\n");
      pHelp("Syntax  : java JTerm [<opts>]");
      pHelp("Function: A Java terminal emulator");
      pHelp("Options :");
      pHelp("     -d           debug mode");
      System.exit(1);
   }

   /**
    * Make helper method for showHelp().
    */
   private static void pHelp(String s)
   {
      System.out.println(s);
   }
}
