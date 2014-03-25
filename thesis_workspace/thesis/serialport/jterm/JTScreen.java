import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import environ.*;

/**
 * <p><b>JTScreen</b> - the terminal screen used by <b>JTerm</b>.</p>
 *
 * <p>The terminal text display area. It extends JPanel adding
 * a non-editable JTextArea and defining its appearance.</p>
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

public class JTScreen extends JPanel
{
   private JTextArea          screen = null;
   private DefaultCaret       cursor = new DefaultCaret();
   private int                screenSize = 0;
   private int                line = 0;
   private int                linePos = 0;
   private String             blankLine = null;

   private int                debug = 0;
   private int                height = 0;
   private int                width = 0;
   
   private ReportError        r = new ReportError("JTScreen");

   /**
    * The only constructor. The display <u>width</u> is
    * specified in characters and the <u>height</u> in lines of text.
    */
   public JTScreen(int height, int width, int debug)
   {
      StringBuffer init = null;
      
      if (debug > 0)
         r.trace("JTScreen constructor started");

      this.height = height;
      this.width = width;
      this.debug = debug;

      /*
         Create the blank screen contents
      */
      screenSize = height * width;
      init = new StringBuffer();
      for (int i = 0; i < screenSize; i++)
         init.append(' ');
         
      /*
         Define the text area and add it to the panel
      */
      screen = new JTextArea(init.toString(), height, width);
      screen.setBackground(Color.DARK_GRAY);
      screen.setForeground(Color.GREEN);
      screen.setEditable(false);
      screen.setFont(new Font("Monospaced", Font.PLAIN, 12));
      screen.setLineWrap(true);
      screen.setWrapStyleWord(false);
      add(screen);

      /*
       * Create a blank line to speed up scrolling
       */
      init = new StringBuffer();
      for (int i = 0; i < width; i++)
         init.append(' ');

      blankLine = init.toString();

      /*
         Set the cursor's initial position
      */
      setCursor(0, 0);

      if (debug > 0)
         r.trace("JTScreen constructor finished");
   }

   /**
    * Add a KeyListener to the TextArea object.
    */
   public void addKeyListener(JTKeyListener kl)
   {
      screen.addKeyListener(kl);
      if (debug > 0)
         r.trace("JTScreen.addKeyListener(" + kl + ")");
   }

   /**
    * Make sure the cursor is visible at its current location.
    * This method is called if it is possible that the cursor isn't
    * visible and the screen content has not been updated.
    */
   public void showCursor()
   {
      setCursor(line, linePos);
   }
   
   /**
    * Update the screen with a string of characters.
    * Special codes: newline, backspace and tab.
    * This method always repositions the cursor and
    * makes it visible.
    */
   public void update(int tabSpace, String s)
   {
      int            n = s.length();
      char           c;

      if (debug > 1)
      {
         HexString hs = new HexString(s);
         
         r.trace("update(" + hs.getHex() + ") started" + tracePointers());
      }

      /*
         Prepare the replacement string
      */      
      for (int i = 0; i < n; i++)
      {
         c = s.charAt(i);
         switch(c)
         {
            case '\b':  backspace();
                        break;
                        
            case '\n':
            case '\r':  newLine();
                        break;
                        
            case '\t':  tabulate(tabSpace);
                        break;
                        
            default:    if (linePos >= width)
                           newLine();

                        insertChar(c);
         }
      }

      setCursor(line, linePos);
      if (debug > 1)
      {
         HexString hs = new HexString(s);
         
         r.trace("update(" + hs.getHex() + ") ended" + tracePointers());
      }
   }
    
   /**
    * Backspace one character. The character to the left of the cursor
    * is removed and a space inserted at the end of the line.
    */
   private void backspace()
   {
      boolean doDelete = true;
      
      if (linePos > 0)
         linePos--;
      else
      {
         if (line > 0)
         {
            linePos = width - 1;
            line--;
         }
         else
            doDelete = false;
      }

      if (doDelete)
      {
         int pos = setPos();
         screen.replaceRange(null, pos, pos + 1);
         char[] ca = new char[1];
         ca[0] = ' ';
         String x = new String(ca);
         pos = line * width + width - 1;
         screen.insert(x, pos);
      }

      if (debug > 3)
         r.trace("backspace()" + tracePointers());
   }

   /**
    * Insert a new character in the screen.
    */
   private void insertChar(char c)
   {
      char[] ca = new char[1];
      int    pos = setPos();

      ca[0] = c;
      String x = new String(ca);

      screen.replaceRange(x, pos, pos +1);
      linePos++;
      if (debug > 3)
      {
         HexString hc = new HexString(c);
         
         r.trace("insertChar(" + hc.getHex() +")" + tracePointers());
      }
   }

   /**
    * A newline causes the rest of the current line to be filled
    * with spaces. If this is not the bottom of the screen, advance
    * the line pointer. If it is the end of the screen, move
    * everything up a line and append a blank line.
    * Leave the cursor at the start of the new line.
    */
   private void newLine()
   {
      int   n = width - linePos;

      if (n > 0)
      {
         String   blankRun = blankLine.substring(0, n - 1);
         int      pos = setPos();
         
         screen.replaceRange(blankRun, pos, pos + n - 1);
      }

      if (line < height - 1)
      {
         line++;
      }
      else
      {
         screen.replaceRange(null, 0, width);
         screen.append(blankLine);
         line = height - 1;
      }

      linePos = 0;
      if (debug > 3)
         r.trace("newLine()" + tracePointers());
   }
 
   /**
    * Position and display the cursor.
    */
   private void setCursor(int line, int linePos)
   {
      this.line = line;
      this.linePos = linePos;
      screen.setCaret(cursor);
      screen.setCaretColor(Color.WHITE);
      screen.setCaretPosition(setPos());
      cursor.setVisible(true);
      cursor.setBlinkRate(500);

      if (debug > 3)
         r.trace("setCursor(" + line + "," + linePos + ")" + tracePointers());
   }

   /**
    *  Calculate the buffer offset from the line and line position.
   */
   private int setPos()
   {
      return (line * width + linePos);
   }
   
   /**
    * Insert a tab stop of eight spaces.
    */
   private void tabulate(int tabSpace)
   {
      int   n = linePos % tabSpace;

      if (n * tabSpace >= width)
         newLine();
      else
      if (n > 0)
      {
         String   blankRun = blankLine.substring(0, n - 1);
         int      pos = setPos();
         
         screen.replaceRange(blankRun, pos, pos + n - 1);
      }
      
      if (debug > 3)
         r.trace("tabulate(" + tabSpace + ")" + tracePointers());
   }

   /**
    * Trace the screen area pointers.
    */
   private String tracePointers()
   {
      return " - line=" + line + "/" + linePos + " pos=" + setPos();
   }
}
