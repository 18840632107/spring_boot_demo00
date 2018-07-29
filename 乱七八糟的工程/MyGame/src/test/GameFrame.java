package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import test.GameUtil;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends Frame{	
    public void launchFrame(){
    	setSize(500,500);
    	setLocation(100,100);
    	setVisible(true);
        new PaintThread().start();
    //override	当发生关闭事件的时候调用
    addWindowListener(new WindowAdapter(){
    	public void windowClosing(WindowEvent e){
    		  System.exit(0);
    	}
    }
    );
   }
    Image img = GameUtil.getImage("imges/u=2378125284,4190274469&fm=21&gp=0.jpg");
    private double x=100,y=100;
    private double degree=3.14/3;
    public void paint(Graphics g){  //g是画笔
    	
    	g.drawImage(img, (int)x,(int)y,null);
    	x+=10*Math.cos(degree);
    	y+=10*Math.sin(degree);
        if(y>500-120){
        	degree=-degree;
        }
        if(y<0){
        	degree=-degree;
        }
        if(x>500-120){
        	degree=Math.PI-degree;
        }
        if(x<0){
        	degree=Math.PI-degree;
        }
    }
    //重画窗口的线程类
    class PaintThread extends Thread {
    	public void run(){
    		while(true){
    			repaint();
    			try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
    }
    
    
    public static void main(String[] args){
    	GameFrame gf = new GameFrame();
    	gf.launchFrame();
    }
 }
	

