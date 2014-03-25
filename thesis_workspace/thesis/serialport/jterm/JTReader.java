import environ.*;

/**
 * This class implements a thread invoked by <b>JTModel</b>
 * to trigger scans for received data from <b>spd</b>.
 * <i>Thread</i>'s <i>start(), interrupt()</i> and <i>interrupted()</i>
 * methods are used without being overridden.
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
public class JTReader extends Thread
{
   /**
    * Shortest pause between successive input scans in mSec.
    */
   public final long    MINWAIT  = 8;

   /**
    * Longest pause between successive input scans in mSec.
    */
   public final long    MAXWAIT  = 1000;

   /*
    * Private variables
    */
   private boolean      again = true;
   private long         waitTime = 0;  /* in mSec */
   private int          debug = 0;

   private JTModel      model = null;
   private ReportError  r = new ReportError("JTReader");
   
   /**
    * The only constructor.
    */
   public JTReader(JTerm theApp, int debug)
   {
      model = theApp.getModel();
      this.debug = debug;
      waitTime = MAXWAIT;
      if (debug > 2)
         r.trace("JTReader thread created");
   }

   /**
    * The thread's main logic.
    */
   public void run()
   {
      if (debug > 2)
         r.trace("JTReader thread started");

      /*
       * The thread's main loop
       */
      while (again)
      {
         /*
          * First action is to sleep for the calculated number of mSec
          */
         if (debug > 2)
            r.trace("JTReader Sleeping for " + waitTime + " mSec");
         try
         {
            sleep(waitTime);
            
            /*
             * Query the server for received data unless the thread
             * was interrupted. If this happened, the interrupt
             * will have been trapped and control will have passed
             * to the 'catch' code.
             */
            model.scanForData();
         }
         catch (InterruptedException ex)
         {
            /*
             * If the thread was interrupted we always want fast
             * polling. If the interrupt comes from a stop request
             * again has been set to false, so the loop will
             * terminate without any further action.
             */
            waitTime = MINWAIT;
            if (debug > 2)
               r.trace("JTReader waitTime reset to " + waitTime);
         }

         /*
          * If the sleep was not interrupted, increment the
          * wait time to slow the loop down and continue.
          */
         waitTime *= 2;
         waitTime =  (waitTime > MAXWAIT ? MAXWAIT : waitTime);
      }

      if (debug > 2)
         r.trace("JTReader thread stopped");      
   }

   /**
    * Stop the thread.
    */
   public void exit()
   {
      again = false;
      super.interrupt();
      if (debug > 2)
         r.trace("JTReader.exit()");
   }

   /**
    * Force the next time round the loop to be fast.
    */
   public void setFastPoll()
   {
      waitTime = MINWAIT;
      if (debug > 2)
         r.trace("JTReader.setFastPoll(): waitTime reset to " + waitTime);
   }
}

 
