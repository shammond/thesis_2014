package thesis;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class myTextPane extends JTextArea {
	
	int setIndex;
	TextList files;
	TextList [] allFiles;
	TextList simpleNormal;
	TextList simpleSmall;
	TextList complexNormal;
	TextList complexSmall;
	TextList ending;
	
	BufferedReader reader;
	String line;
	int sMaxChar;
	int cMaxChar;
	//int sLineLength;
	//int cLineLength;
	
	String [] questions;
	int qindex;
	
	public myTextPane() {
		// TODO Auto-generated constructor stub
		
		String [] qs = {"What did the letter to Lupita say?", "What did he want to use Google glass for?",
						"What was the previous article about?", "What kind of probability did the previous article discuss?",
						"What was the author's opinion on government?", "What is Astley's?",
						"Was the previous article tongue in cheek or serious?", "True or False: The Pennington's adopt to add diversity to their family.",
						"What is the narrator looking at?", "What is Coleman's challenge?",
						"What is the 'best physic to preserve health'?","What is the mission of the burrito boyz?",
						"How did the child treat the dog?", "How does Shakespeare describe the maid?",
						"What was the old woman's wish?", "Name a location mentioned in the previous exerpt.",
						"What was the main subject of the previous article?"};
		
		questions = qs;
		qindex = 0;
		
		String [] snarray = {"lupita.txt", "onion.txt", "football1.txt", "thumbelina.txt", "scarletstockings.txt", "scarletstockings2.txt", "scarletstockings3.txt","norway1.txt"};
		String [] ssarray = {"googlass.txt", "orphans.txt", "burritos.txt", "darkbrowndog.txt", "ladycrusoe.txt"};
		String [] cnarray = {"mortgage.txt", "astleys.txt", "persistent.txt", "aeneid.txt", "stocks.txt"};
		String [] csarray = {"chancesare.txt", "commonsense.txt", "regimenthealth.txt","shakespeare.txt", "cloud.txt", "socialclass.txt" };
		String [] theend = {"end.txt"};
		
		simpleNormal = new TextList("simple normal", 20, snarray);
		simpleSmall = new TextList("simple small", 7, ssarray);
		complexNormal = new TextList("complex normal", 20, cnarray);
		complexSmall = new TextList("complex small", 7, csarray);
		ending = new TextList("ending", 20, theend);
		
		TextList [] alarray = {simpleNormal,simpleSmall,complexNormal,complexSmall,
								complexSmall,complexNormal,simpleNormal,simpleSmall,
								complexNormal,simpleNormal,complexSmall,simpleSmall,
								simpleSmall,complexSmall,simpleNormal,complexNormal,ending};
		allFiles = alarray;
		
		files = simpleNormal;
		setIndex = 0;
		reader = null;
		
		sMaxChar = 500;
		cMaxChar = 500;
	}
	
	public myTextPane(int session) {
		
		String [] qs = {"How did Brennan help get the Civil Rights Act passed?","What did the author's father say about becoming a philosophy professor?",
						"Describe the playground.","Why is Erbil important?","What is ZunZuneo?","What is the author's issue with the #CancelColbert campaign?",
						"Why does Menaleaus get excited?","True or False. The boys had more than one shuttlecock.","What did the report say about China?",
						"Why did the police stop and frisk the people in the courtyard?","What happened to comic book sales after WWII?",
						"What is Essentialism?","Name some things people are protesting.","Who is Mrs. Parker?","What did the Little Mermaid want most?",
						"Describe the interaction between Venus and Adonis.","Describe the previous article."};

		questions = qs;
		qindex = 0;
		
		String [] snarray = {"overprotected.txt","peony.txt","stopandfrisk.txt","protest.txt",};
		String [] ssarray = {"philosophy.txt","cuba.txt","captain.txt","mermaid.txt"};
		String [] cnarray = {"lbj.txt","iliad.txt","socialclass.txt","skylight.txt"};
		String [] csarray = {"fertilecrescent.txt", "satire.txt","stocks.txt","venus.txt"};
		String [] theend = {"end.txt"};
		
		simpleNormal = new TextList("simple normal", 20, snarray);
		simpleSmall = new TextList("simple small", 7, ssarray);
		complexNormal = new TextList("complex normal", 20, cnarray);
		complexSmall = new TextList("complex small", 7, csarray);
		ending = new TextList("ending", 20, theend);
		
		TextList [] alarray = {complexNormal,simpleSmall,simpleNormal,complexSmall,
								simpleSmall,complexSmall,complexNormal,simpleNormal,
								complexSmall,simpleNormal,simpleSmall,complexNormal,
								simpleNormal,complexNormal,simpleSmall,complexSmall,ending};
		allFiles = alarray;
		
		files = complexNormal;
		setIndex = 0;
		reader = null;
		
		sMaxChar = 500;
		cMaxChar = 500;
	}

	public myTextPane(StyledDocument arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public void initialize() {
		setFile(files.currentFile());
		return;
	}
	
	public void nextPage() {
		if (reader != null) {
			continueRead();
		}
		else if (files.next()){
			setFile(files.currentFile());
		}
		return;
	}
	
	public void pause() {
		setFont(new Font("Arial", Font.PLAIN, 20));
		setText(questions[qindex]);
		if (qindex < questions.length - 1)
			qindex = qindex + 1;
		return;
	}
	
	public String loggingInfo() {
		return files.getType() + " " + files.currentFile();
	}
	
	public boolean nextFileSet() {
		
		files.next();
		setIndex++;
		if (setIndex<allFiles.length)
			files = allFiles[setIndex];
		else
			return false;
		if (files.currentFile()!=null) {
			setFile(files.currentFile());
		}
		else
			return false;
		setFont(new Font("Arial", Font.PLAIN, files.getFontSize()));
		return true;
	}
	
	public void setFile(String filename) {
		
		String path = ".//articles//";
	
		File file = new File(path + filename);
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		continueRead();
	}
/*		
		setText("");
		int charNum = 0;
		int maxChar;
		boolean more = false;
		
		if (files.getFontSize()==7) {
			maxChar = cMaxChar;
			//lineLength = cLineLength;
		}
		else {
			maxChar = sMaxChar;
			//lineLength = sLineLength;
		}

		
		try {
			while (reader.ready()) {
				line = reader.readLine();
				charNum += line.length(); //+ lineLength + line.length() % lineLength;
				append(line+"\n");
				if (charNum >= maxChar) {
					more = true;
					break;
				}
			}
			if (!reader.ready() && !more) {
				reader.close();
				reader = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		return;
	}
	*/
	public void continueRead() {
		
		setText("");
		int charNum = 0;
		int maxChar;
		boolean more = false;
		
		if (files.getFontSize() == 7) {
			maxChar = cMaxChar;
		}
		else {
			maxChar = sMaxChar;
		}
		
		try {
			while ((reader.ready())) {
				line = reader.readLine();
				charNum += line.length();
				append(line+"\n");
				if (charNum >= maxChar)
				{
					more = true;
					break;
				}
			}
			if(!reader.ready() && !more) {
				reader.close();
				reader = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		return;
	}

}
