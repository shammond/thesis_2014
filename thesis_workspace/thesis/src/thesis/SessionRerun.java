package thesis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class SessionRerun {
	
	
	private static BufferedWriter clickLog;
	static List<Double[]> eegBuffer;
	private static CSVWriter sessionData;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File eegFile = null;
		eegBuffer = new ArrayList<Double[]>();
		
		JFrame parent = new JFrame();
		
		JFileChooser selectFile = new JFileChooser();
		selectFile.setCurrentDirectory(new File("C:\\Users\\shammond\\Desktop\\Classification_Files\\Second_Session"));
		int fcval = selectFile.showDialog(parent, "Select a classification file.");
		if (fcval==JFileChooser.APPROVE_OPTION)
			eegFile = selectFile.getSelectedFile();
		else
			System.exit(0);
		
		String subjectName = (String)JOptionPane.showInputDialog(parent,"Enter file name for logging:", "File Name",JOptionPane.PLAIN_MESSAGE);
		if (subjectName==null)
			System.exit(0);
		String subjectStr = ".//logRerun//" + subjectName; // remember to change this for each test
		
		int start = 0;
		try {
			start = Integer.parseInt(JOptionPane.showInputDialog(parent,"Enter start index:", "Start Index",JOptionPane.PLAIN_MESSAGE));
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
			System.exit(0);
		}
		

		try {
			clickLog = new BufferedWriter(new FileWriter(subjectStr + "-rerun-clicks"));
			//uncomment for real time
			sessionData = new CSVWriter(new FileWriter(subjectStr + "-rerun-conditions.csv"));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		BufferedReader eegReader = null;
		
		try {
			eegReader = new BufferedReader(new FileReader(eegFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		
		CSVReader reader = new CSVReader(eegReader);
		List<String[]> eegRows = null;
		try {
			eegRows = reader.readAll();
			reader.close();
			eegReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		int marker = 1;
		int i=start+1;
		while (i<start+1561) {
			try {
				clickLog.write(String.valueOf(marker));
				clickLog.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			marker++;
			for (int l = 0; l<2; l++) {
				for (int j=0;j<20;j++) {
					Double [] instance = new Double[9];
					instance[0] = (double)i+j-1;
					for (int k=3;k<11;k++) {
						instance[k-2] = Double.valueOf(eegRows.get(i+j)[k]);
					}
					eegBuffer.add(instance);
				}
				checkCondition(eegBuffer);
				i+=20;
			}
			for (int j=0;j<20;j++) {
				Double [] instance = new Double[9];
				instance[0] = (double)i+j-1;
				for (int k=3;k<11;k++) {
					instance[k-2] = Double.valueOf(eegRows.get(i+j)[k]);
				}
				eegBuffer.add(instance);
			}
			checkCondition(eegBuffer);
			i+=35+25;
			eegBuffer.clear();
		}
		
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
		
		System.out.println("Done");
		System.exit(0);
		
		return;


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
			
			Double [] cols = { line[1],line[2],line[3],line[4],line[5],line[6],line[7],line[8] };
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
				p = EmmaTBrainClassifier.classify(cols);  // the classifier will change for each subject
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
				clickLog.write("Condition 2 recorded");
			else if (condCounts[1]==maxArray(condCounts))
				clickLog.write("Condition 1 recorded");
			else
				clickLog.write("Not Classified recorded");
			clickLog.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return (condCounts[2]==maxArray(condCounts));
		

	}


}
