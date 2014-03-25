import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import environ.*;

/**
 * <p><b>JTServerConnect</b> - sets up the connection conditions
 * by inputting the host name and port.</p>
 *
 * <p>The object consists of two <b>JTInput</b> fields and
 * two controlling JButtons within a JPanel.</p>
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

public class JTServerConnect extends JDialog
{
   private int          debug = 0;
   private JTerm        theApp = null;
   private JTDisp       window = null;
   private ReportError  r = new ReportError("JTServerConnect");
   
   private JTInput      host = null;
   private JTInput      port = null;
   private JButton      ok = null;
   private JButton      cancel = null;

   private String       hostname = null;
   private String       portnum = null;

   /**
    * The only constructor.
    */   
   public JTServerConnect(JTerm theApp,
                          Rectangle bounds,
                          int debug)
   {
      super(theApp.getWindow(), "Connect server", true);
      if (debug > 0)
         r.trace("constructor(" + theApp + "," + bounds + "," + debug + ")");
                                                      
      this.theApp = theApp;
      this.debug = debug;
      window = theApp.getWindow();

      bounds.width = setSize(bounds.width, 50.0);
      bounds.height = setSize(bounds.height, 40.0);
      if (debug > 1)
         r.trace("Adjusted bounds:" + bounds);

      setBounds(bounds);
      setResizable(false);
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      if (debug > 1)
         r.trace("Server connect window defined");

      GridBagLayout        vll = new GridBagLayout();
      GridBagConstraints   vlc = new GridBagConstraints();

      JPanel values = new JPanel();
      host = addInputField("Host:", 15, values, vll, vlc); 
      port = addInputField("Port:", 5,  values, vll, vlc);

      GridLayout  bgrid = new GridLayout(1, 2);
      JPanel      buttons = new JPanel(bgrid);
      ok = addButton("Connect", buttons); 
      ok.addActionListener(new JTServerAction(theApp,
                                              this,
                                              JTServerAction.CONNECT_RQST,
                                              debug));
      cancel = addButton("Cancel", buttons);
      cancel.addActionListener(new JTServerAction(theApp,
                                                  this,
                                                  JTServerAction.CANCEL_RQST,
                                                  debug));

      GridBagLayout        fpg = new GridBagLayout();
      GridBagConstraints   fpc = new GridBagConstraints();

      fpc.gridheight = 1;
      fpc.gridwidth  = 1;
      fpc.gridx      = 0;
      fpc.gridy      = GridBagConstraints.RELATIVE;
      fpc.anchor     = GridBagConstraints.CENTER;
      
      JPanel   fp = new JPanel(fpg);
      fpg.setConstraints(values, fpc);
      fp.add(values);
      fpg.setConstraints(buttons, fpc);
      fp.add(buttons);

      Container cp = getContentPane();
      cp.add(fp);
      pack();
      
      if (debug > 0)
         r.trace("Server connect constructor finished");
   }

   /**
    * Make the dialogue visible.
    */
   public void setVisible(boolean v)
   {
      Container   cp = window.getContentPane();
      
      super.setLocationRelativeTo(cp);
      super.setVisible(v);
   }

   /**
    * Set initial displayable values.
    */
   public void setValues(String h, int p)
   {
      Integer i = new Integer(p);

      hostname = h;
      portnum = i.toString();

      host.setText(hostname);
      port.setText(portnum);
   }

   /**
    * Return the hostname.
    */
   public String getHostname()
   {
      hostname = host.getText();
      if (debug > 0)
         r.trace(hostname + " = getHostname()");
         
      return hostname;
   }

   /**
    * Return the port number.
    */
   public int getPortNumber()
   {
      portnum = port.getText();
      Integer i = new Integer(portnum);
      if (debug > 0)
         r.trace(portnum + " = getPortNumber()");
         
      return i.intValue();
   }

   /**
    * Private helper method, used to add buttons.
    */
   private JButton addButton(String label, Container c)
   {
      JButton     b = new JButton(label);
      Border      e = new BevelBorder(BevelBorder.RAISED);
      Dimension   d = new Dimension(70,25);

      b.setBorder(e);
      b.setMinimumSize(d);
      b.setPreferredSize(d);
      c.add(b);
      return b;
   } 

   /**
    * Private helper method, used to add input fields.
    */
   private JTInput addInputField(String label,
                                 int n,
                                 Container p,
                                 GridBagLayout l,
                                 GridBagConstraints c)
   {
      JTInput in = new JTInput(label, n);

      l.setConstraints(in, c);
      p.add(in);
      return in;
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
