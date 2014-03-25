package thesis;

import java.awt.Dialog;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSVReader;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;



public class DataClassifier {
	
	static BufferedReader breader;
	static ArffReader arffLoader;
	static Instances data;
	static BufferedWriter arffWriter;
	static int firstcolumn;
	static int numAttrs;

	public DataClassifier() {
	}	
		
	static void createArff(File eegFile, File timeData, String arffName, int start, int end) {
		
		BufferedReader eegReader = null;
		BufferedReader timeReader = null;
		
		try {
			eegReader = new BufferedReader(new FileReader(eegFile));
			timeReader = new BufferedReader(new FileReader(timeData));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		File arffFile = new File("arffFiles//" + arffName + ".arff");
		arffWriter = null;
		try {
			arffWriter = new BufferedWriter(new FileWriter(arffFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		// start with relation and define attributes
		// state: (0,pause),(1,simple,standard),(2,simple,small),(3,complex,standard),(4,complex,small)
		firstcolumn = 3;
		numAttrs = 6;
		
		try {
			arffWriter.write("@relation readingType");
			arffWriter.newLine();
			arffWriter.newLine();
			writeAttributes();
			arffWriter.write("@attribute speed numeric");
			arffWriter.newLine();
			arffWriter.write("@attribute state {1,2,3,4}");  // 4 conditions
			arffWriter.newLine();
			arffWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		CSVReader reader = new CSVReader(eegReader);
		List<String[]> eegRows = null;
		try {
			eegRows = reader.readAll();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}
		
		// get rid of any -99999 values
		eegRows = nonegnines(eegRows);
		
	/*	for (String[] line : eegRows) {
			for (String item : line)
				System.out.print(item + ", ");
			System.out.println();
		}
		
		if (true)
			return;
		*/

		// map times to average reading speeds
		HashMap<Integer,Double> speedMap = createSpeedMap(timeReader);
		
		
		// add data to arff file
		
		int time = 0;
		
		try {
			arffWriter.write("@data");
			arffWriter.newLine();
			
			int offset = -1;
		
			for (String [] line : eegRows) {
				
				if (start <= offset && offset <= end ) {
					// set i to be the column of the first attribute, go until have all attributes
					//System.out.println("time: " + time);
					
					// add the condition as the last attribute
					if (inRange(time,0,75) || inRange(time,600,675) || inRange(time,900,975) || inRange(time,1400,1475)) {
						writeArffLine(line,speedMap.get(time),"1");
					}
					else if (inRange(time,100,175) || inRange(time,700,775) || inRange(time,1100,1175) || inRange(time,1200,1275)) {
						writeArffLine(line,speedMap.get(time),"2");
					}
					else if (inRange(time,200,275) || inRange(time,500,575) || inRange(time,800,875) || inRange(time,1500,1575)) {
						writeArffLine(line,speedMap.get(time),"3");
					}
					else if (inRange(time,300,375) || inRange(time,400,475) || inRange(time,1000,1075) || inRange(time,1300,1375)) {
						writeArffLine(line,speedMap.get(time),"4");
					}
				//	else {
			//			arffWriter.write("0");
			//		}
					
					time++;
				}
				offset++;
		}
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}
		
		
		try {
			reader.close();
			eegReader.close();
			//timeReader.close();
			arffWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		return;


	}
	
	static boolean inRange(int time,int lower,int upper) {
		return ((lower <= time) && (time < upper));
	}
	
	static void writeArffLine(String[] line, Double speed, String condition) {
		try {
			for (int i=firstcolumn;i<firstcolumn+numAttrs;i++) {
				arffWriter.write(line[i] + ",");
			}
			arffWriter.write(Double.toString(speed) + ",");
			arffWriter.write(condition);
			arffWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return;
	}
	
	static void writeAttributes() {

			try {
				for (int i = 0; i < 6; i++) {
					arffWriter.write("@attribute data" + i + " numeric");
					arffWriter.newLine();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			
			return;
	}
	
	static List<String[]> nonegnines(List<String[]> eegdata) {
		// get rid of any -99999 values in the data by replacing with values between previous and next measured values
		
		for (int line = 2; line < eegdata.size(); line++) {
			for (int col = 3; col < eegdata.get(line).length; col++) {
				if (eegdata.get(line)[col].equals("-99999")) {
					double before = 0;
					if (line > 2)
						before = Double.parseDouble(eegdata.get(line-1)[col]);
					int numlines = 0;
					while ((line+numlines < eegdata.size()) && eegdata.get(line+numlines)[col].equals("-99999")) {
						numlines++;
					}
					
					double after = 0;
					if (line+numlines==eegdata.size())
						after = before;
					else {
						after = Double.parseDouble(eegdata.get(line+numlines)[col]);
						if (before == 0)
							before = after;
					}
					
					if (before < after)
						eegdata.get(line)[col] = Double.toString(before + Math.abs(after - before) / (numlines + 1));
					else
						eegdata.get(line)[col] = Double.toString(before - Math.abs(after - before) / (numlines + 1));
				}
			}
		}
		return eegdata;
	}
	
	static HashMap<Integer,Double> createSpeedMap(BufferedReader timeReader) {
		// assign average reading speeds to each second
		
		// create time hashmap
		CSVReader tReader = new CSVReader(timeReader);
		List<String[]> timeRows = null;
		try {
			timeRows = tReader.readAll();
			tReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		HashMap<Integer,Double> speedMap = new HashMap<Integer,Double>();

		int t2;
		int timeInd = 0;
		Double minSpeed = 8.0;
		Double speed = minSpeed; // min speed
		int index = 0;
		int articleEnd = 75;
		while (index < timeRows.size()) {
			
			t2 = Integer.parseInt(timeRows.get(index)[0]);
			
			// when article ends, the speed from timeInd to articleEnd is the last measured speed
			if (t2>=articleEnd) {
				while (timeInd <= articleEnd) {
					speedMap.put(timeInd, speed);
					timeInd++;
				}
				articleEnd+=100;
				timeInd+=24;
			}
			
			// if the user did not click at all while reading an article, set at min speed 8
			while (t2 >= articleEnd) {
				while (timeInd <= articleEnd) {
					speedMap.put(timeInd, minSpeed);
					timeInd++;
				}
				timeInd+=24;
				articleEnd+=100;
			}
			
			// set speed from last timeInd to t2 to be new speed
			speed = Double.parseDouble(timeRows.get(index)[1]);			
			
			while (timeInd <= t2) {
					speedMap.put(timeInd, speed);
					timeInd++;
			}
			
			index++;
		}
		
		while (timeInd < 1600) {
			speedMap.put(timeInd, speed);
			timeInd++;
		}
		
		return speedMap;
	}
	
	public static void main(String [] args) {
		
		File classification = null;
		File clicks = null;
		
		//createArff(".//data//183102000_Ref_Class.csv",".//data//justinsbrain.arff",60,1660);
		JFrame parent = new JFrame();
		JFileChooser selectFile = new JFileChooser();
		int fcval = selectFile.showDialog(parent, "Select a classification file.");
		if (fcval==JFileChooser.APPROVE_OPTION)
			classification = selectFile.getSelectedFile();
		else
			System.exit(0);
		selectFile.setCurrentDirectory(new File("logs"));
		fcval = selectFile.showDialog(parent, "Select a clicks file.");
		if (fcval==JFileChooser.APPROVE_OPTION)
			clicks = selectFile.getSelectedFile();
		else
			System.exit(0);
		
		String subjectName = (String)JOptionPane.showInputDialog(parent,"Enter Decision Tree Class Name:", "File Name",JOptionPane.PLAIN_MESSAGE);
		if (subjectName==null)
			System.exit(0);
		int start = 0;
		try {
			start = Integer.parseInt(JOptionPane.showInputDialog(parent,"Enter start index:", "Start Index",JOptionPane.PLAIN_MESSAGE));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//createArff(".//src//thesis//184103000_Classification.csv",".//logs//3-8-2014-adamd-clicks","adamd-test.arff",57,1657);
		// change
		
		createArff(classification,clicks,subjectName,start,start+1600);
		
		if (true)
			return;
		
		try {
			breader = new BufferedReader(new FileReader(".//arffFiles//" + subjectName +".arff"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		/*try {
			arffLoader = new ArffReader(breader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}*/
		
		try {
			data = new Instances(breader);
			breader.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}
		data.setClassIndex(data.numAttributes()-1);
		
		J48 dtree = new J48();
		try {
			dtree.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		
		BufferedWriter bsc = null;
		try {
			bsc = new BufferedWriter(new FileWriter(".//src//thesis//" + subjectName + "Classifier.java"));
			bsc.write("package thesis;");
			bsc.newLine();
			bsc.newLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}
		
		try {
			//System.out.println(dtree.toSource("BrainStateClassifier"));
			bsc.write(dtree.toSource(subjectName + "Classifier"));
			bsc.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}