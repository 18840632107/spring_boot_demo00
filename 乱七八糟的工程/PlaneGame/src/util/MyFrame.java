package util;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyFrame extends Frame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8136883943648935257L;
	public void launchFrame(){
	    	setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
	    	setLocation(100,100);
	    	setVisible(true);
	    	
	        new PaintThread().start();
	        
	    //override	当发生关闭事件的时候调用
	    addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	    		  System.exit(0);
	    	}
	    });
	 }
	 
	 
	 class PaintThread extends Thread {
	    	public void run(){
	    		while(true){
	    			repaint();
	    			try {
						Thread.sleep(30);
					} 
	    			catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
	    	}
	    	
	  }   
	    
	   
}

