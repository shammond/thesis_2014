import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import environ.*;

/**
 * <p><b>JTDisp</b> - display handler for <b>JTerm</b>.</p>
 *
 * <p>This class handles its own window events via the
 * <i>protected processWindowEvent()</i> method.</p>
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

public class JTDisp extends JFrame
{
   private JTerm              theApp = null;
   private JTServerConnect    serverDialog = null;
   private JTSerialConnect    portDialog = null;

   private JMenu              fileMenu = null;
   private JMenuItem          quit = null;

   private JMenu              serverMenu = null;
   private JMenuItem          openServer = null;
   private JMenuItem          closeServer = null;

   private JMenu              portMenu = null;
   private JMenuItem          openPort = null;
   private JMenuItem          closePort = null;

   private JMenu              helpMenu = null;
   private JMenuItem          helpText = null;
   private JMenuItem          about = null;

   private JTScreen           screen = null;
   private String             titleString = null;

   private int                debug = 0;
   private Dimension          screenSize = null;
   
   private ReportError        r = new ReportError("JTDisp");

   /**
    * The only constructor.
    */
   public JTDisp(JTerm  theApp, int debug)
   {
      Toolkit   screenDets = getToolkit();
      screenSize = screenDets.getScreenSize();

      if (debug > 0)
         r.trace("JTDisp constructor started");

      this.theApp = theApp;
      this.debug = debug;

      /*
         Set the initial window size and position
      */
      setBounds(setSize(screenSize.width, 20.0),   /* position */
                setSize(screenSize.height, 20.0),
                setSize(screenSize.width, 60.0),   /* size     */
                setSize(screenSize.height, 60.0));
      /*
         Enable window event handling
      */
      enableEvents(AWTEvent.WINDOW_EVENT_MASK);

      if (debug > 0)
         r.trace("JTDisp constructor finished");
   }

   /**
    * This method is called after the model and controller have been
    * created to add the menubar and content pane objects.
    */
   public void addContent(String title,
                          String version,
                          String copyright)
   {
      String initString = new String(title + " - " + version +
                                     ", " + copyright +
                                     "\n\n");

      if (debug > 0)
         r.trace("JTDisp addContent(" + title + ","
                                      + version + ","
                                      + copyright + ")");
            
      /*
         Define the menu bar
      */
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      /*
         Build the File menu
      */
      fileMenu = new JMenu("File");              /* Define the file menu */

      quit = new JMenuItem("Exit");
      quit.addActionListener(new JTDispAction(theApp,
                                              JTDispAction.EXIT_RQST,
                                              debug));
      fileMenu.add(quit);
      menuBar.add(fileMenu);

      /*
         Build the Server menu
      */
      serverMenu = new JMenu("Server");
      openServer = new JMenuItem("Connect");
      openServer.addActionListener(new JTDispAction(theApp,
                                                    JTDispAction.OPEN_SERVER,
                                                    debug));
      serverMenu.add(openServer);
      closeServer = new JMenuItem("Disconnect");
      closeServer.addActionListener(new JTDispAction(theApp,
                                                     JTDispAction.CLOSE_SERVER,
                                                     debug));
      serverMenu.add(closeServer);
      menuBar.add(serverMenu);
      
      /*
         Build the Serial port menu
      */
      portMenu = new JMenu("Serial port");
      openPort = new JMenuItem("Open");
      openPort.addActionListener(new JTDispAction(theApp,
                                                  JTDispAction.OPEN_PORT,
                                                  debug));
      portMenu.add(openPort);
      closePort = new JMenuItem("Close");
      closePort.addActionListener(new JTDispAction(theApp,
                                                   JTDispAction.CLOSE_PORT,
                                                   debug));
      portMenu.add(closePort);
      menuBar.add(portMenu);

      /*
         Build the Help menu
      */
      helpMenu = new JMenu("Help");
      helpText = new JMenuItem("Description");
      helpText.addActionListener(new JTDispAction(theApp,
                                                  JTDispAction.HELP_RQST,
                                                  debug));
      helpMenu.add(helpText);            
      about = new JMenuItem("About");
      about.addActionListener(new JTDispAction(theApp,
                                               JTDispAction.ABOUT_RQST,
                                               debug));
      helpMenu.add(about);
      menuBar.add(helpMenu);

      if (debug > 1)
         r.trace("addContent() - menu structure built");

      /*
         Control 'Server' and 'Serial port' menu items.
         Open Server is enabled, rest greyed out
      */
      setMenu(true, false, false, false, title);

      /*
         Build the rest of the screen
      */
      Container cp = getContentPane();

      screen = new JTScreen(24, 80, debug);
      screen.addKeyListener(new JTKeyListener(theApp, r, debug));

      screen.update(8, initString);
      cp.add(screen);
      pack();
      if (debug > 1)
         r.trace("JTDisp addContent() - content pane complete");

      /*
         Set up the server connection and serial port connection
         dialog windows but leave them invisible.
      */
      serverDialog  = new JTServerConnect(theApp, getBounds(), debug);
      portDialog  = new JTSerialConnect(theApp, getBounds(), debug);
      
      if (debug > 0)
         r.trace("JTDisp addContent() finished");
   }

   /**
    * Get a reference to the server connection dialog.
    */
   public JTServerConnect getServerDialog()
   {
      return serverDialog;
   }

   /**
    * Get a reference to the serial port opening dialog.
    */
   public JTSerialConnect getPortDialog()
   {
      return portDialog;
   }

   /**
    * Set the state of the open and close menu items
    * and change the frame title to suit.
    */
   public void setMenu(boolean os,
                       boolean cs,
                       boolean op,
                       boolean cp,
                       String title)
   {
      openServer.setEnabled(os);
      openServer.setArmed(os);
      closeServer.setEnabled(cs);
      closeServer.setArmed(cs);
      openPort.setEnabled(op);
      openPort.setArmed(op);
      closePort.setEnabled(cp);
      closePort.setArmed(cp);
      setTitle(title);
   }

   /**
    * Window event handler. It only overrides the WINDOW_CLOSING event.
    * When this is received the window's resources are released and
    * the application terminates. 
    */
   protected void processWindowEvent(WindowEvent e)
   {
      if (e.getID() == WindowEvent.WINDOW_CLOSING)
      {
         theApp.disconnect();
         dispose();
         System.exit(0);
      }

      super.processWindowEvent(e);
   }

   /**
    * Force the cursor to be displayed.
    */
   public void showCursor()
   {
      screen.showCursor();
   }
   
   /**
    * Update the screen with received data.
    */
   public void update(int tabSpace, String data)
   {
      screen.update(tabSpace, data);
   }
   
   /**
    * Private helper method used when calculating object sizes.
    */
   private int setSize(int size, double percent)
   {
      double raw = size * percent / 100.0;
      
      return  (int)Math.round(raw);
   }
}
