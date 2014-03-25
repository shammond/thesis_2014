import java.util.*;

/**
 * <p><b>JTStringParser</b> - Split a comma-delimited string
 * into fields. The fields are accessible individually or as
 * an array of Strings.</p>
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

public class JTStringParser
{
   private String      result[] = null;
   
   /**
    * Construct a parser for the <u>vals</u> String.
    */   
   public JTStringParser(String vals)
   {
      StringTokenizer st = new StringTokenizer(vals, ",");
      int n = st.countTokens();

      result = new String[n];
      for (int i = 0; i < n; i++)
         result[i] = st.nextToken();
   }
   
   /**
    * Get an array containing the parsed string.
    */
   public String[] getArray()
   {
      return result;
   }

   /**
    * Get the nth field. Fields are enumerated from zero.
    * If <u>n</u> &lt; 0 or <u>n</u> &gt;= <i>getFieldCount()</i>
    * an empty string is returned.
    */
   public String getField(int n)
   {
      String r = new String("");
      
      if (n >= 0 || n < result.length)
         r = result[n];
         
      return r;
   }

   /**
    * Return the number of fields in the string.
    */
   public int getFieldCount()
   {
      return result.length;
   }
}
