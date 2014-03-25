import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p><b>JTInput</b> - defines a labelled input field.</p>
 *
 * <p>The object consists of a JLabel and an editable JTextField
 *    within a JPanel.</p>
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

public class JTInput extends JPanel
{
   Border      blackEdge = BorderFactory.createLineBorder(Color.black);
   JTextField  text;

   /**
    * Constructor parameters: the field label and the length of the field.
    */   
   public JTInput(String label, int length)
   {
      super(new FlowLayout());

      add(new JLabel(label));
      
      text = new JTextField(length);
      text.setEditable(true);
      text.setBackground(Color.white);
      text.setSelectionColor(Color.black);
      text.setSelectedTextColor(Color.lightGray);
      text.setBorder(blackEdge);
      add(text);
   }

   /**
    * Get the displayed text.
    */
   public String getText()
   {
      return text.getText();
   }

   /**
    * Set the displayed text.
    */
   public void setText(String s)
   {
      text.setText(s);
   }
}
