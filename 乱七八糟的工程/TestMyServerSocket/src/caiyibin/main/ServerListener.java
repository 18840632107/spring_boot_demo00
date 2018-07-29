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
			//建立连接
			JOptionPane.showMessageDialog(null,"连接12345");
			//传递给新线程
			ChatSocket cs = new ChatSocket(socket);
			cs.start();
			ChatManager.getChatManager().add(cs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
