import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p><b>JTList</b> - defines a labelled input list field.</p>
 *
 * <p>The object consists of a JLabel and a JList
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

public class JTList extends JPanel
{
   private Border             blackEdge =
                                 BorderFactory.createLineBorder(Color.black);
   private JList              list = null;
   private DefaultListModel   model = new DefaultListModel();
   
   /**
    * Constructor parameters: the field label and the selection list.
    */   
   public JTList(String label)
   {
      super(new FlowLayout()); 

      add(new JLabel(label));
      
      list = new JList(model);
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      list.setLayoutOrientation(JList.VERTICAL);
      list.setBorder(blackEdge);
      add(list);
   }

   /**
    * Attach a mouse listener to the list.
    */
   public void addMouseListener(JTMouseListener ml)
   {
      list.addMouseListener(ml);
   }

   /**
    * Get the displayed text.
    */
   public String getSelected()
   {
      int ix = list.getSelectedIndex();
      String x = (String)model.elementAt(ix);
      list.setSelectionBackground(Color.GRAY);
      return x;
   }

   /**
    * See if there is a selection.
    */
   public boolean isSelected()
   {
      int ix = list.getSelectedIndex();
      
      return ix >= 0;
   }

   /**
    * Set the selectable content from a string
    * containing a comma delimited list of values. 
    */
   public void setSelectionList(String vals)
   {
      JTStringParser st = new JTStringParser(vals);
      int n = st.getFieldCount();
      
      model.clear();
      for (int i = 0; i < n; i++)
         model.addElement(st.getField(i));
      
      list.setVisibleRowCount(Math.min(n, 8));
   }
}
