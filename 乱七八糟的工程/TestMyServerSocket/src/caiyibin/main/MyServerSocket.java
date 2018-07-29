package caiyibin.main;

public class MyServerSocket {

	public static void main(String[] args) {
		//建立与使用
//		try {
//			ServerSocket serverSocket = new ServerSocket(12345);
//			//block
//			Socket socket = serverSocket.accept();
//			//建立连接
//			JOptionPane.showMessageDialog(null,"连接12345");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		new ServerListener().start();

	}

}
