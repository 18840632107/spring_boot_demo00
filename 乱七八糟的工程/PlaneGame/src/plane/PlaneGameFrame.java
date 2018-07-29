package plane;

import java.awt.Graphics;
import java.awt.Image;

import util.GameUtil;
import util.MyFrame;

public class PlaneGameFrame extends MyFrame {
	/**
	 * 
	 */
	Image bg = GameUtil.getImage("images\2.jpg");
	Image plane = GameUtil.getImage("images\1.jpg");
	public void paint(Graphics g){
		g.drawImage(bg,0,0,null);
		g.drawImage(plane,50,50,null);
	} 
	public static void main(String[] args) {
	    new PlaneGameFrame().launchFrame();
	}
}
