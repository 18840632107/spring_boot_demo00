package caiyibin.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerListener extends Thread {
	public void run(){

		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			//block
			while(true){
			Socket socket = serverSocket.accept();
			//��������
			JOptionPane.showMessageDialog(null,"����12345");
			//���ݸ����߳�
			ChatSocket cs = new ChatSocket(socket);
			cs.start();
			ChatManager.getChatManager().add(cs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
