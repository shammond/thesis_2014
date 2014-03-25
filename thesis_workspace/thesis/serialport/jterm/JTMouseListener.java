import java.awt.event.*;
import environ.*;

/**
 * <p>This class traps mouse events used by <b>JTerm</b>.</p>
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
public class JTMouseListener extends MouseAdapter implements MouseListener
{
   private JTerm              theApp;
   private JTSerialConnect    display;
   private int                type = 0;
   private int                debug = 0;

   private JTSerialAction     jtsa = null;
   private ReportError        r = new ReportError("JTMouseListener");


   /**
    * The only constructor. The type supplied at creation is the
    * only value to be returned by mouse events detected by this object.
    */                  
   public JTMouseListener(JTerm theApp,
                          JTSerialConnect display,
                          int type,
                          int debug)                                                    
   {
      if (debug > 0)
         r.trace("JTMouseListener constructor(theApp, display," +
                 type + "," +
                 debug + ")");

      this.theApp = theApp;
      this.display = display;
      this.type = type;
      this.debug = debug;

      jtsa = new JTSerialAction(theApp, display, type, debug);
      if (debug > 0)
         r.trace("JTMouseListener constructed");
   }

   /**
    * Invoked when the mouse button has been clicked on the component.
    * It signals that an action has been performed to wake <b>JTerm</b> up.
    * The action to be taken is controlled by the type
    * passed as the <u>type</u> when the <b>JTSerialAction</b> object
    * was created and returned by the ActionEvent.
    */
   public void mouseClicked(MouseEvent e)
   {
      ActionEvent fa = new ActionEvent(this,
                                       ActionEvent.ACTION_FIRST,
                                       "Mouse clicked");

      if (debug > 0)
         r.trace("mouseClicked(" + e.paramString() + ")");

      /*
         The MouseEvent is ignored.
 
         The ActionEvent is a dummy to keep the method happy.
         The action to be taken is controlled by the type
         passed as the type when the JTSerialAction object
         was created.
      */
      jtsa.actionPerformed(fa);
   }      

   /**
    * Invoked when the mouse button has been pressed on the component.
    * This method does nothing except to trace its invocation when
    * debugging is turned on.
    */
   public void mousePressed(MouseEvent e)
   {
      if (debug > 0)
         r.trace("mousePressed(" + e.paramString() + ")");
   }      

   /**
    * Invoked when the mouse button has been released on the component.
    * This method does nothing except to trace its invocation when
    * debugging is turned on.
    */
   public void mouseReleased(MouseEvent e)
   {
      if (debug > 0)
         r.trace("mouseReleased(" + e.paramString() + ")");
   }      

   /**
    * Invoked when the mouse pointer enters the component.
    * This method does nothing except to trace its invocation when
    * debugging is turned on.
    */
   public void mouseEntered(MouseEvent e)
   {
      if (debug > 0)
         r.trace("mouseEntered(" + e.paramString() + ")");
   }      

   /**
    * Invoked when the mouse pointer exits the component.
    * This method does nothing except to trace its invocation when
    * debugging is turned on.
    */
   public void mouseExited(MouseEvent e)
   {
      if (debug > 0)
         r.trace("mouseExited(" + e.paramString() + ")");
   }      
}
            
