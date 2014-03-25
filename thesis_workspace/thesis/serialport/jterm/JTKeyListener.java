import java.awt.event.*;
import environ.*;

/**
 * This class implements a KeyListener for <b>JTerm</b>.
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
public class JTKeyListener extends KeyAdapter
{
   JTModel     m = null;
   ReportError r = null;
   int         debug = 0;

   /**
    * The only constructor.
    */   
   public JTKeyListener(JTerm theApp, ReportError r, int debug)
   {
      m = theApp.getModel();
      this.r = r;
      this.debug = debug;
      if (debug > 0)
         r.trace("JTKeylistener(" + theApp + ","
                                  + r + ","
                                  + debug + ") constructed");
   }

   /**
    * Pass the key notified in the KeyEvent to the <b>JTerm</b> model.
    */
   public void keyTyped(KeyEvent e)
   {
      char     c;

      c = e.getKeyChar();
      if (debug > 0)
      {
         r.trace("JTKeyListener.keyTyped(" + showCharacter(c) + ")");
      }

      m.keyTyped(c);
   }

   /**
    * Show the character value for debugging.
    */
   private String showCharacter(char c)
   {
      HexString s = new HexString(c);

      return s.getHex();
   }
}
