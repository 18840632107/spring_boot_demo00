package caiyibin.main;

public class MyServerSocket {

	public static void main(String[] args) {
		//������ʹ��
//		try {
//			ServerSocket serverSocket = new ServerSocket(12345);
//			//block
//			Socket socket = serverSocket.accept();
//			//��������
//			JOptionPane.showMessageDialog(null,"����12345");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		new ServerListener().start();

	}

}
