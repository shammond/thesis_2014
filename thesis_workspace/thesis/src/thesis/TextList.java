package thesis;

public class TextList {
	
	private int current;
	private String [] files;
	private String type;
	private int fontSize;

	public TextList(String typename, int font, String [] texts) {
		// TODO Auto-generated constructor stub
		
		current = 0;
		files = texts;
		type = typename;
		fontSize = font;
	}
	
	public String getType() {
		return type;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setList(String [] texts) {
		files = texts;
	}
	
	public String currentFile() {
		if (current<files.length)
			return files[current];
		else
			return null;
	}
	
	public boolean next() {
		current = (current + 1) % files.length;
		System.out.println(files.length);
		if (current>=files.length)
			return false;
		return true;
	}
 
}
