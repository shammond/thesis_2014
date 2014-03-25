import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import environ.*;

/**
 * <p><b>JTDispAction</b> - Menu and button action listener for
 * <b>JTDisp</b>.</p>
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

public class JTDispAction implements ActionListener
{
   private JTerm        theApp;
   private JTModel      model;
   private JTDisp       display;
   private int          type     = EXIT_RQST;
   private int          debug    = 0;
   private ReportError  r = new ReportError("JTDispAction");

   /**
    * ActionListener constructor. A separate instance is created
    * for each menu item and action button defined in the window.
    * The type set by the constructor defines the action to be
    * taken when the controlling object is clicked.
    */   
   public JTDispAction(JTerm theApp, int type, int debug)
   {
      this.theApp = theApp;
      this.type = type;
      this.debug = debug;

      model = theApp.getModel();
      display = theApp.getWindow();
   }

   /**
    * Handle all actions associated with menu items and action buttons.
    * The ActionEvent is ignored; the private <b>type</b> variable
    * set by the constructor selects the action to be taken when the
    * menu item is selected or the action button is clicked.
    */
   public void actionPerformed(ActionEvent e)
   {
      JTServerConnect serverDialog = null;
      JTSerialConnect portDialog = null;
      
      if (debug > 0)
         r.trace("JTDispAction.actionPerformed: " +
                 ts[type] + " (" + type  + ")");
 
      switch(type)
      {
         case EXIT_RQST:
            theApp.disconnect();
            display.dispose();
            System.exit(0);
            break;
                           
         case HELP_RQST:
            String     s = JTHelpContent.getText();
            HelpDialog ohd = new HelpDialog("JTerm help", s);
            ohd.setVisible(true);
            break;
                           
         case ABOUT_RQST:
            JOptionPane.showMessageDialog(display,
                                          theApp.getVersion(),
                                          "About JTerm",
                                          JOptionPane.PLAIN_MESSAGE);
            break;
                           
         case OPEN_SERVER:
            if (debug > 1)
               r.trace("OPEN_SERVER started");

            serverDialog = display.getServerDialog();
            if (debug > 1)
               r.trace("JTServerConnect() reference retrieved");

            serverDialog.setValues(model.getHostname(),
                                   model.getPortNumber());
            serverDialog.setVisible(true);
            if (debug > 1)
               r.trace("OPEN_SERVER finished");

            break;
            
         case CLOSE_SERVER:
            model.disconnectServer();
            break;
            
         case OPEN_PORT:
            if (debug > 1)
               r.trace("OPEN_PORT started");

            portDialog = display.getPortDialog();
            if (debug > 1)
               r.trace("JTSerialConnect() reference retrieved");

            portDialog.setPortList(model.getPortList());
            portDialog.setOSList(model.getOSList());
            portDialog.setVisible(true);
            if (debug > 1)
               r.trace("OPEN_PORT finished");

            break;
            
         case CLOSE_PORT:
            model.closePort();
            break;
            
         default:
            JOptionPane.showMessageDialog(display,
                                          "Undefined action requested",
                                          "Fatal Error",
                                          JOptionPane.ERROR_MESSAGE);
            System.exit(1);
      }
   }

   final static int EXIT_RQST       = 0;
   final static int HELP_RQST       = 1;
   final static int ABOUT_RQST      = 2;
   final static int OPEN_SERVER     = 3;
   final static int CLOSE_SERVER    = 4;
   final static int OPEN_PORT       = 5;
   final static int CLOSE_PORT      = 6;

   final static String ts[] = { "EXIT_RQST", "HELP_RQST", "ABOUT_RQST",
                                "OPEN_SERVER", "CLOSE_SERVER",
                                "OPEN_PORT", "CLOSE_PORT" };
}
