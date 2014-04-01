package thesis;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author shammond
 *
 */
public class TextDisplaySession2 extends JFrame {
	
	private static Logger log;
	
	private myTextPane mainDisplay;
	private Timer timer;
	private Timer pauseTimer;
	private boolean mouseEnabled;
	private long startTime;
	private long lastTime;
	private BufferedWriter clickLog;

	/**
	 * @throws HeadlessException
	 */
	public TextDisplaySession2() throws HeadlessException {
		
		log = Logger.getLogger("Time Logging");
		FileHandler logHandler;
		SimpleFormatter formatter = new SimpleFormatter();
		
		String subjectName = (String)JOptionPane.showInputDialog(this,"Enter file name for logging:", "File Name",JOptionPane.PLAIN_MESSAGE);
		if (subjectName==null)
			System.exit(0);
		String subjectStr = ".//logs//" + subjectName; // remember to change this for each test

		try {
			logHandler = new FileHandler(subjectStr);
			log.addHandler(logHandler);
			logHandler.setFormatter(formatter);
			clickLog = new BufferedWriter(new FileWriter(subjectStr + "-clicks"));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		try {
			setUp();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		

	}
	
	private void setUp() {
		
		setSize(1152,840);
		
		getContentPane().setBackground(Color.BLACK);
		getContentPane().setLayout(null);
		
		mainDisplay = new myTextPane();
		mainDisplay.setWrapStyleWord(true);
		mainDisplay.setLineWrap(true);
		mainDisplay.setFont(new Font("Arial", Font.PLAIN, 20));
		mainDisplay.initialize();
		mainDisplay.setEditable(false);
		mainDisplay.setBounds(150, 12, 880, 780);
		//mainDisplay.setBounds(220, 22, 1000, 788);
		mainDisplay.setMargin(new Insets(90,10,10,6));
		
		getContentPane().add(mainDisplay);
				
		MouseListener ml = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!mouseEnabled)
					return;
				String info = getTime()/1000 + ","
					+ mainDisplay.getText().length() * 1000.0 / (System.currentTimeMillis() - lastTime);
				log.info( "Chars per second: " + info);
				try {
					clickLog.write(info);
					clickLog.newLine();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
				
				// check the condition here?
				ArrayList<Object[]> eegdata = new ArrayList<Object[]>();
				boolean check = checkCondition(eegdata);
				
				mainDisplay.nextPage();
				
				if (check)
					mainDisplay.setFont(new Font("Arial", Font.PLAIN, 20));
				
				lastTime = System.currentTimeMillis();
				log.info("Next slide: " + mainDisplay.loggingInfo() + " "+ getTime());
				return;
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		
		mouseEnabled = true;
		
		addMouseListener(ml);
		mainDisplay.addMouseListener(ml);
		
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				log.info("Condition change start pause: " + getTime());
				mainDisplay.pause();
				mouseEnabled = false;
				
				pauseTimer.restart();
				
			}
			
		};
		
		ActionListener pl = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				lastTime = System.currentTimeMillis();
				log.info("Condition change end pause: " + getTime());
				mainDisplay.nextFileSet();
				mouseEnabled = true;
				timer.restart();
				pauseTimer.stop();
			}
			
		};
		
		// length of each reading period 75 seconds
		timer = new Timer(75000,al);
		
		// length of a pause 25 seconds
		pauseTimer = new Timer(25000,pl);

		
		addWindowListener( new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				log.info("Application closed: " + getTime());
				timer.stop();
				pauseTimer.stop();
				try {
					clickLog.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		setVisible(true);
		
		startTime = System.currentTimeMillis();
		lastTime = startTime;
		log.info("Program start: " + startTime);
		timer.start();
	}
	
	private long getTime() {
		return System.currentTimeMillis()-startTime;
	}
	
	private static int maxArray(int[] ary) {
		int maxVal = 0;
		for (int i = 0; i < ary.length; i++) {
			if (ary[i] > maxVal)
				maxVal = ary[i];
		}
		return maxVal;
	}
	
	static boolean checkCondition(List<Object []> eegArray) {
		
		int [] condCounts = {0,0,0,0,0,0};
		
		for (Object [] line : eegArray) {
			
			Double p = Double.NaN;
			try {
				p = AudreyRefClassClassifier.classify(line);  // the classifier will change for each subject
				System.out.println(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (p==Double.NaN) {
				condCounts[0]++;
			}
			else {
				condCounts[(int)p.doubleValue()+1]++;
			}
			
			for (int i = 0; i<condCounts.length; i++)
				System.out.print(condCounts[i] + " ");
			System.out.println();
		}
		
		return (condCounts[2]==maxArray(condCounts));
		

	}
	
	

	/**
	 * @param arg0
	 */
	public TextDisplaySession2(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public TextDisplaySession2(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TextDisplaySession2(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String [] args) {
		//new TextDisplaySession2();
		
		List<Object[]> data = new ArrayList<Object[]>();
		
		Object[] line1 = {0.0,2.3,4.5,3.4};
		Object[] line2 = {0.0,2.3,4.5,3.4};
		Object[] line3 = {0.0,2.3,4.5,3.4};
		Object[] line4 = {1.0,1.0,1.0,1.0};
		Object[] line5 = {0.0,2.3,4.5,3.4};
		
		data.add(line1);
		data.add(line2);
		data.add(line3);
		data.add(line4);
		data.add(line5);
		
		
		System.out.println(checkCondition(data));
		
	}
}

