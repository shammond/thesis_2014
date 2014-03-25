import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import environ.*;

/**
 * <p><b>JTSerialConnect</b> - sets up the connection conditions
 * by selecting the port from a list and, when it has been selected,
 * allowing the serial connection's operating conditions to be changed.</p>
 *
 * <p>The object consists of <b>JTInput</b> and <b>JTList</b> fields and
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

public class JTSerialConnect extends JDialog
{
   private int             debug = 0;
   private JTerm           theApp = null;
   private JTDisp          window = null;
   private ReportError     r = new ReportError("JTSerialConnect");
   
   private JTList          osList = null;
   private JTList          portList = null;
   private JTInput         baudRate = null;
   private JTInput         dataBits = null;
   private JTInput         parity = null;
   private JTInput         stopBits = null;

   private JButton         ok = null;
   private JButton         cancel = null;

   /**
    * The only constructor.
    */   
   public JTSerialConnect(JTerm theApp,
                          Rectangle bounds,
                          int debug)
   {
      super(theApp.getWindow(), "Port", true);
      if (debug > 0)
         r.trace("constructor(" + theApp + ","
                                + bounds + ",["
                                + debug + ")");
                                                      
      this.theApp = theApp;
      this.debug = debug;
      window = theApp.getWindow();

      bounds.width = setSize(bounds.width, 150.0);
      bounds.height = setSize(bounds.height, 150.0);
      if (debug > 1)
         r.trace("Adjusted bounds:" + bounds);

      setBounds(bounds);
      setResizable(false);
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      if (debug > 0)
         r.trace("Serial Connect window defined");

      JPanel portBox = new JPanel();
      portList = addInputList("Port:", portBox);
      
      JTMouseListener ml = new JTMouseListener(theApp,
                                               this,
                                               JTSerialAction.SELECT_RQST,
                                               debug);
      portList.addMouseListener(ml);
      if (debug > 1)
         r.trace("portList mouse listener added");

      JPanel paramBox = new JPanel(new GridLayout(4, 1));
      baudRate = addInputField("Baud rate:", 5, paramBox);
      dataBits = addInputField("Data bits:", 1, paramBox);
      parity   = addInputField("Parity:   ", 1, paramBox);
      stopBits = addInputField("Stop bits:", 1, paramBox);

      JPanel osBox = new JPanel();
      osList = addInputList("Target OS:", osBox);
      
      JTMouseListener ml2 = new JTMouseListener(theApp,
                                                this,
                                                JTSerialAction.OS_RQST,
                                                debug);
      osList.addMouseListener(ml2);
      if (debug > 1)
         r.trace("osList mouse listener added");
                                                
      JPanel dataBox = new JPanel(new GridLayout(3, 1));
      dataBox.add(portBox);
      dataBox.add(paramBox);
      dataBox.add(osBox);
      
      GridLayout bgrid = new GridLayout(1, 2);
      JPanel   buttons = new JPanel(bgrid);
      ok       = addButton("Open", buttons); 
      ok.addActionListener(new JTSerialAction(theApp,
                                              this,
                                              JTSerialAction.CONNECT_RQST,
                                              debug));
      cancel   = addButton("Cancel", buttons);
      cancel.addActionListener(new JTSerialAction(theApp,
                                                  this,
                                                  JTSerialAction.CANCEL_RQST,
                                                  debug));

      GridBagLayout        fpg = new GridBagLayout();
      GridBagConstraints   fpc = new GridBagConstraints();

      fpc.gridheight = 1;
      fpc.gridwidth  = 1;
      fpc.gridx      = 0;
      fpc.gridy      = GridBagConstraints.RELATIVE;
      fpc.anchor     = GridBagConstraints.CENTER;
      
      JPanel   fp = new JPanel(fpg);
      fpg.setConstraints(dataBox, fpc);
      fp.add(dataBox);
      fpg.setConstraints(buttons, fpc);
      fp.add(buttons);

      Container cp = getContentPane();
      cp.add(fp);
      pack();
      
      if (debug > 0)
         r.trace("Serial Connect window constructor finished");
   }

   /**
    * Return the selected target operating system.
    */
   public String getOSName()
   {
      String on = osList.getSelected();

      if (debug > 0)
         r.trace(on + " = getOSName()");

      return on;
   }
   
   /**
    * Return the selected serial port.
    */
   public String getPortName()
   {
      String pn = portList.getSelected();

      if (debug > 0)
         r.trace(pn + " = getPortName()");

      return pn;
   }
   
   /**
    * Return the port parameters as a comma delimited string.
    */
   public String getPortParameters()
   {
      String pp = new String(baudRate.getText() + "," +
                             dataBits.getText() + "," +
                             parity.getText() + "," +
                             stopBits.getText());

      if (debug > 0)
         r.trace(pp + " = getPortParams()");

      return pp;
   }
   
   /**
    * See if a serial port has been selected.
    */
   public boolean isPortName()
   {
      boolean ps = portList.isSelected();

      if (debug > 0)
         r.trace(ps + " = isPortName()");

      return ps;
   }
   
   /**
    * Set the OS list ready for display.
    */
   public void setOSList(String osNames)
   {
      if (debug > 0)
         r.trace("setOSList([" + osNames + "]");
         
      osList.setSelectionList(osNames);

      if (debug > 0)
         r.trace("setOSList([" + osNames + "]) finished");
   }

   /**
    * Set the portlist ready for display.
    */
   public void setPortList(String ports)
   {
      if (debug > 0)
         r.trace("setPortList([" + ports + "]");
         
      portList.setSelectionList(ports);

      if (debug > 0)
         r.trace("setPortList([" + ports + "]) finished");
   }

   /**
    * Set the port parameters ready for display.
    */
   public void setPortParameters(String params)
   {
      if (debug > 0)
         r.trace("setPortParams([" + params + "])");

      JTStringParser sprm = new JTStringParser(params);
      baudRate.setText(sprm.getField(0));
      dataBits.setText(sprm.getField(1));
      parity.setText(sprm.getField(2));
      stopBits.setText(sprm.getField(3));

      if (debug > 0)
         r.trace("setPortParams([" + params + "]) finished");
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
                                 Container p)
   {
      JTInput in = new JTInput(label, n);

      p.add(in);
      return in;
   } 

   /**
    * Private helper method, used to add input lists.
    */
   private JTList addInputList(String label,
                               Container p)
   {
      JTList il = new JTList(label);

      p.add(il);
      if (debug > 0)
         r.trace("JTList(" + label + ") created");
         
      return il;
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
