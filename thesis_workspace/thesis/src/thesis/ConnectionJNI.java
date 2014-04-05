package thesis;

public class ConnectionJNI {
	static {
		System.loadLibrary("connection");
		System.loadLibrary("ABM_Datastreaming");
	}
	
	static int m_nHandle;
	
	native int openConnection(String sIPAddress, String sPort, boolean bTCP);
	native float[] getClassData();
	
	public static void main(String[] args) {
		try {
			//new ConnectionJNI().openConnection();
		}
		catch(UnsatisfiedLinkError e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
