package thesis;

public class ConnectionJNI {
	static {
		System.loadLibrary("connection");
		//System.loadLibrary("ABM_Datastreaming");
	}
	
	static int m_nHandle;
	
	native int openConnection(String sIPAddress, String sPort, boolean bTCP);
	native int closeConnection();
	native float[] getClassData();
	
	public static void main(String[] args) {
		try {
			ConnectionJNI connection = new ConnectionJNI();
			m_nHandle = connection.openConnection("192.168.1.2","4505",true);
			connection.getClassData();
			System.out.println(m_nHandle);
			//connection.closeConnection();
			
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

}
