package guyue.hu;

import java.awt.*;
import java.awt.event.*;

/**
 * @author guyue
 * @date 2017年11月29日 下午9:21:28
 * @class describ:
 */
public class TankClient extends Frame {
	private int tankLocationX = 100;
	private int tankLocationY = 100;

	public static void main(String[] args) {
		new TankClient().launch();
	}
	
	//启动
	public void launch() {
		this.setBounds(200, 100, 800, 600);
		this.setResizable(false);
		this.setTitle("Tank War");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		new Thread(new TankMove()).start();
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		Color defaultColor = g.getColor();
		g.setColor(Color.DARK_GRAY);
		g.fillOval(tankLocationX, tankLocationY, 30, 30);
		g.setColor(defaultColor);
		tankLocationX = tankLocationX + 5;
	}
	
	private class TankMove implements Runnable {

		@Override
		public void run() {
			try {
				for(int i=0; i<500; i++) {
					repaint();
					Thread.sleep(200);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
