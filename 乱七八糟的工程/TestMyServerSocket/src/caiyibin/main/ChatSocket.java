package caiyibin.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ChatSocket extends Thread {
	
	Socket socket;
	public ChatSocket(Socket s){
		this.socket=s;
	}
   public void run(){
	   try {
		socket.getOutputStream();
		//一个一个往上包装
		   BufferedWriter bw = 
				   new BufferedWriter(
						   new OutputStreamWriter(
								   socket.getOutputStream()));
		   int count=0;
		   while(true){
			   bw.write("loop:"+count);
			   try {
				sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   super.run();
   }
}
