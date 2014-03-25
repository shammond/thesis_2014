import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import environ.*;

/**
 * <p><b>JTSerialAction</b> - Menu and button action listener
 * for <b>JTSerialConnect</b>.</p>
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

public class JTSerialAction implements ActionListener
{
   private JTerm              theApp;
   private JTModel            model;
   private JTSerialConnect    display;
   private int                type = CONNECT_RQST;
   private int                debug = 0;
   private ReportError        r = new ReportError("JTSerialAction");

   /**
    * ActionListener constructor. A separate instance is created
    * for each menu item and action button defined in the window.
    * The type set by the constructor defines the action to be
    * taken when the controlling object is clicked.
    */   
   public JTSerialAction(JTerm theApp,
                         JTSerialConnect display,
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
         case SELECT_RQST:
            if (model.openPort(display.getPortName()) >= 0)
            {
               display.setPortParameters(model.getPortParameters());
            }
            else
            {
               JOptionPane.showMessageDialog(display,
                                             model.getError(),
                                             "Serial port failed to open",
                                             JOptionPane.ERROR_MESSAGE);
            }

            break;
                           
         case OS_RQST:
            if (!model.isConnectedToPort())
            {
               JOptionPane.showMessageDialog(display,
                                             "Select a serial port",
                                             "Serial port not selected",
                                             JOptionPane.ERROR_MESSAGE);
            }
            else            
            if (model.setPortParameters(display.getPortParameters()) < 0)
            {
               JOptionPane.showMessageDialog(display,
                                             model.getError(),
                                             "Invalid serial port parameters",
                                             JOptionPane.ERROR_MESSAGE);
            }
            else
            {
               model.setOSName(display.getOSName());
            }

            break;
                           
         case CONNECT_RQST:
            if (!model.isConnectedToPort())
            {
               JOptionPane.showMessageDialog(display,
                                             "Select a serial port",
                                             "Serial port not selected",
                                             JOptionPane.ERROR_MESSAGE);
            }
            else            
            if (!model.isOSSelected())
            {
               JOptionPane.showMessageDialog(display,
                                             "Select a target Operating System",
                                             "Operating System not selected",
                                             JOptionPane.ERROR_MESSAGE);
            }
            else
            {
               model.startReader();
               display.setVisible(false);
            }

            break;

         case CANCEL_RQST:
            if (model.isConnectedToPort())
            {
               if (debug > 0)
                  r.trace("Closing the port");
                  
               model.closePort();
            }
            
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

   final static int SELECT_RQST   = 0;
   final static int OS_RQST       = 1;
   final static int CONNECT_RQST  = 2;
   final static int CANCEL_RQST   = 3;

   final static String ts[] = { "SELECT_RQST",
                                "OS_RQST",
                                "CONNECT_RQST",
                                "CANCEL_RQST" };
}
