package thesis;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

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
	private static long startTime;
	private long lastTime;
	private static BufferedWriter clickLog;
	//uncomment for real time
	private Timer minuteTimer;
	private Timer secondTimer;
	ConnectionJNI eegConnect;
	List<Double[]> eegBuffer;
	private static CSVWriter sessionData;
	boolean reading;

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
			//uncomment for real time
			sessionData = new CSVWriter(new FileWriter(subjectStr + "-conditions.csv"));
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
		
		setVisible(true);

		startTime = System.currentTimeMillis();
		lastTime = startTime;
		log.info("Program start: " + startTime);
		timer.start();
		reading = true;
		
		//uncomment for real time
		minuteTimer.start();
		secondTimer.start();

	}
	
	private void setUp() {
		
		setSize(1152,840);
		
		getContentPane().setBackground(Color.BLACK);
		getContentPane().setLayout(null);
		
		mainDisplay = new myTextPane(2);
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
				

				
				mainDisplay.nextPage();

				
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
				
				//uncomment for real time
				reading = false;
				minuteTimer.stop();
				//secondTimer.stop();
				eegBuffer.clear();
				
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
				
				//uncomment for real time
				float [] temp = eegConnect.getClassData();

				try {
					if (temp != null) 
						clickLog.write("data size after pause: " + temp.length);
					else
						clickLog.write("data size after pause: 0");
					clickLog.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				reading = true;
				minuteTimer.restart();
				//secondTimer.restart();
				pauseTimer.stop();
			}
			
		};
		
		//uncomment for real time
		ActionListener minuteListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("minute");
				
				// after 1 minute attempt to classify
				// check the condition here?
				//ArrayList<Object[]> eegdata = new ArrayList<Object[]>();
				
				//
				minuteTimer.stop();
				//secondTimer.stop();
				
				int nSize = 13;
				float [] epoch = eegConnect.getClassData();
				
				try {
					if (epoch != null) 
						clickLog.write("data size: " + epoch.length);
					else
						clickLog.write("data size: 0");
					clickLog.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (epoch != null) {
					for (int i=0; i< (epoch.length / nSize);i++) {
						Double [] eegLine = new Double[nSize];
						for (int j=0; j< nSize;j++) {
							eegLine[j] = (double)epoch[i*nSize+j];
						}
						
						eegBuffer.add(eegLine);
					}
					
				/*	Double[] eegLine 
					for (int i = epoch.length - 14; i< epoch.length; i++)*/
						
					
					//}
				}
				boolean check = checkCondition(eegBuffer);
				System.out.println(check);
			
				if (check)
					mainDisplay.setFont(new Font("Arial", Font.PLAIN, 20));
			
				//eegBuffer.clear();
				minuteTimer.restart();
				//secondTimer.restart();
			}
			
		};
		
		ActionListener secondListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//System.out.println("second");
				
					
				// collect the eegdata as they read
				//if (reading) {
				int nSize = 13;
				float [] epoch = eegConnect.getClassData();
				if (epoch!=null) {
					try {
						clickLog.write("first offset: " + String.valueOf(epoch[0]));
						clickLog.newLine();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					secondTimer.stop();
					try {
						clickLog.write(String.valueOf(epoch[0]));
						clickLog.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			/*	if (epoch != null) {
					//System.out.println("offset: " + epoch[0]);
					for (int i=0; i< (epoch.length / nSize);i++) {
						Double [] eegLine = new Double[nSize];
						for (int j=0; j< nSize;j++) {
							eegLine[j] = (double)epoch[i*nSize+j];
						}
						
						if (reading)
							eegBuffer.add(eegLine);
					}*/
					
				/*	Double[] eegLine 
					for (int i = epoch.length - 14; i< epoch.length; i++)*/
						
					
					//}
				//}
				
			}
				
			
		};
		
		// length of each reading period 75 seconds
		timer = new Timer(75000,al);
		
		// length of a pause 25 seconds
		pauseTimer = new Timer(25000,pl);
		
		//uncomment for real time
		
		// length of data acquisition 20 seconds
		minuteTimer = new Timer(20000,minuteListener);
		
		// length between accessing eeg data 1 second
		secondTimer = new Timer(1000,secondListener);
		

		
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
				
				//uncomment for real time
				eegConnect.closeConnection();
				minuteTimer.stop();
				//secondTimer.stop();
				try {
					clickLog.flush();
					clickLog.close();
					//uncomment for real time
					sessionData.flush();
					sessionData.close();
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
		
		//uncomment for real time
		
		// set up connection to EEG
		eegConnect = new ConnectionJNI();
		try {
			//new ConnectionJNI().openConnection();
			System.out.println("before open");
			System.out.println(eegConnect.openConnection("", "", true));  // ip address and port	
			System.out.println("after open");
		}
		catch(UnsatisfiedLinkError e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	
		eegBuffer = new ArrayList<Double[]>();
		return;

	}
	
	private static long getTime() {
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
	
	static boolean checkCondition(List<Double []> eegArray) {
		
		int [] condCounts = {0,0,0};
		
		for (Double [] line : eegArray) {
			
			/*pData[i].fClassificationEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 5;
			pData[i].fHighEngagementEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 6;
			pData[i].fLowEngagementEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 7;
			pData[i].fDistractionEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 8;
			pData[i].fDrowsyEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 9;
			pData[i].fWorkloadFBDSEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 10;
			pData[i].fWorkloadBDSEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 11;
			pData[i].fWorkloadAverageEstimate = pF*/
			
			Double [] cols = { line[9],line[8],line[7],line[6],line[5],line[10],line[11],line[12] };
			//System.out.println(String.valueOf(line[0])+String.valueOf(line[9])+String.valueOf(line[8])+String.valueOf(line[7])+
				//	String.valueOf(line[6])+String.valueOf(line[5])+String.valueOf(line[10])+String.valueOf(line[11])+String.valueOf(line[12]));
			
			/*int numvals = 7;
			int index = 5;
			int j = 0;
			for (int k=0;k<9;k++) {
				for (int i=0;i<numvals;i++) {
					cols[j] = line[index+i];
					j++;
				}
				index += 9;
			}*/
			
			Double p = Double.NaN;
			try {
				p = AdamDBrainClassifier.classify(cols);  // the classifier will change for each subject
				//System.out.println(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String [] csvdata = new String[cols.length + 2];
			csvdata[0] = String.valueOf(line[0]);
			for (int i=0;i<cols.length;i++) {
				//System.out.print(line[i] + ",");
				csvdata[i+1] = String.valueOf(cols[i]);
			}
			csvdata[cols.length+1] = String.valueOf(p);
			//uncomment for real time
			sessionData.writeNext(csvdata);
			
			if (p==Double.NaN) {
				condCounts[0]++;
			}
			else {
				if (cols[6] >= 0)
					condCounts[(int)p.doubleValue()+1]++;
			}
			
			//for (int i = 0; i<condCounts.length; i++)
				//System.out.print(condCounts[i] + " ");
			//System.out.println();
		}
		
		try {
			if (condCounts[2]==maxArray(condCounts))
				clickLog.write("Condition 2 recorded at " + getTime());
			else if (condCounts[1]==maxArray(condCounts))
				clickLog.write("Condition 1 recorded at " + getTime());
			else
				clickLog.write("Not Classified recorded at " + getTime());
			clickLog.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		new TextDisplaySession2();
		
		/*List<Double[]> data = new ArrayList<Double[]>();
		
		Double[] line1 = {0.0,2.3,4.5,3.4};
		Double[] line2 = {0.0,2.3,4.5,3.4};
		Double[] line3 = {0.0,2.3,4.5,3.4};
		Double[] line4 = {1.0,1.0,1.0,1.0};
		Double[] line5 = {0.0,2.3,4.5,3.4};
		
		data.add(line1);
		data.add(line2);
		data.add(line3);
		data.add(line4);
		data.add(line5);
		
		
		System.out.println(checkCondition(data));
		
		ConnectionJNI cj = new ConnectionJNI();
		float [] brainData = cj.getClassData();
		for (float item : brainData) {
			System.out.print(item);
		}*/
	}
}

