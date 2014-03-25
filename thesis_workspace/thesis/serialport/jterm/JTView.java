import java.util.*;
import javax.swing.*;
import environ.*;

/**
 * <p><b>JTView</b> - implements the <b>JTerm</b> controller.</p>
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

public class JTView extends JComponent implements Observer
{
   private JTerm  theApp;
   private JTDisp    display;
   private int       debug = 0;
   private ReportError  r = new ReportError("JTView");

   /**
    * The only constructor.
    */
   public JTView(JTerm theApp, int debug)
   {
      this.theApp = theApp;
      display = theApp.getWindow();
      this.debug = debug;
   }

   /**
    * A method called when the Observable object,
    * <b>JTModel</b>, notifies changes.
    */
   public void update(Observable o, Object rectangle)
   {
      JTModel om = (JTModel)o;
      String  s  = null;

      if(debug > 0)
         r.trace("JTModel.update() called");

      if (om.isConnectedToServer())
      {
         if (om.isConnectedToPort())
            display.setMenu(false, true, false, true, om.getTitle());
         else
            display.setMenu(false, true, true, false, om.getTitle());
      }
      else
      {
         display.setMenu(true, false, false, false, om.getTitle());
      }

      if (om.isResponse())
         display.update(om.getTabSpace(), om.getResponse());
      else
         display.showCursor();
         
      om.updateCompleted();
   }
}
