import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import environ.*;

/**
 * <p><b>JTServerAction</b> - Menu and button action listener
 * for <b>JTServerConnect</b>.</p>
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

public class JTServerAction implements ActionListener
{
   private JTerm              theApp;
   private JTModel            model;
   private JTServerConnect    display;
   private int                type = CONNECT_RQST;
   private int                debug = 0;
   private ReportError        r = new ReportError("JTServerAction");

   /**
    * ActionListener constructor. A separate instance is created
    * for each menu item and action button defined in the window.
    * The type set by the constructor defines the action to be
    * taken when the controlling object is clicked.
    */   
   public JTServerAction(JTerm theApp,
                         JTServerConnect display,
                         int type,
                         int debug)
   {
      this.theApp = theApp;
      this.display = display;
      this.type = type;
      this.debug = debug;

      model = theApp.getModel();
   }

   /**
    * Handle all actions associated with menu items and action buttons.
    * The ActionEvent is ignored; the private <b>type</b> variable
    * set by the constructor selects the action to be taken when the
    * menu item is selected or the action button is clicked.
    */
   public void actionPerformed(ActionEvent e)
   {
      if (debug > 0)
         r.trace("actionPerformed: " + ts[type] + " (" + type  + ")");
 
      switch(type)
      {
         case CONNECT_RQST:
            if (model.connectServer(display.getHostname(),
                                    display.getPortNumber()) != 0)
               JOptionPane.showMessageDialog(display,
                                             model.getError(),
                                             "Connection error",
                                             JOptionPane.ERROR_MESSAGE);
            else
               display.setVisible(false);

            break;
                           
         case CANCEL_RQST:
            display.setVisible(false);
            break;
                           
         default:
            JOptionPane.showMessageDialog(display,
                                          "Undefined action requested",
                                          "Fatal Error",
                                          JOptionPane.ERROR_MESSAGE);
            System.exit(1);
      }
   }

   final static int CONNECT_RQST  = 0;
   final static int CANCEL_RQST   = 1;

   final static String ts[] = { "CONNECT_RQST", "CANCEL_RQST" };
}
