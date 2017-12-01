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
	private Image offScreenImage = null;

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
		g.setColor(Color.ORANGE);
		g.fillOval(tankLocationX, tankLocationY, 30, 30);
		g.setColor(defaultColor);
		tankLocationX = tankLocationX + 5;
	}
	
	//双缓冲解决闪烁问题
	@Override
	public void update(Graphics g) {
		//1.首先获得一张画布
		if(offScreenImage == null) {
			offScreenImage = this.createImage(800, 600);
		}
		//2.获得画布对应的画笔
		Graphics offScreenG = offScreenImage.getGraphics();
		Color c = offScreenG.getColor();
		//3.每次都初始化背景
		offScreenG.setColor(Color.WHITE);
		offScreenG.fillRect(0, 0, 800, 600);
		//4.重置画布画笔的颜色
		offScreenG.setColor(c);
		//5.画图
		this.paint(offScreenG);
		//6.将图片画到前景窗体里面
		g.drawImage(offScreenImage, 0, 0, null);
	}

	private class TankMove implements Runnable {

		@Override
		public void run() {
			try {
				for(int i=0; i<500; i++) {
					repaint();
					Thread.sleep(20);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
