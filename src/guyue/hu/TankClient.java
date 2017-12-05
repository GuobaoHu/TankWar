package guyue.hu;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * @author guyue
 * @date 2017年11月29日 下午9:21:28
 * @class describ:
 */
public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int REPAINT_SPEED = 100;
	private int tankLocationX = 100;
	private int tankLocationY = 100;
	private Image offScreenImage = null;
	private Tank myTank = new Tank(tankLocationX, tankLocationY, this); 
	private Tank enemyTank = new Tank(200, 300, this);
	private java.util.List<Bullet> bullets = new ArrayList<Bullet>();
	
	public static void main(String[] args) {
		new TankClient().launch();
	}
	
	public java.util.List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullet(java.util.List<Bullet> bullets) {
		this.bullets = bullets;
	}

	//启动
	public void launch() {
		this.setBounds(200, 100, GAME_WIDTH, GAME_HEIGHT);
		this.setResizable(false);
		this.setTitle("Tank War");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		new Thread(new TankMove()).start();
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		g.drawString("bullets:" + bullets.size(), 10, 40);
		for(int i=0; i<bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(!b.hitTank(enemyTank)) {
				bullets.get(i).draw(g);
			}
		}
		myTank.draw(g);
		enemyTank.draw(g);
//		this.directionMove();
	}
	
	//双缓冲解决闪烁问题
	@Override
	public void update(Graphics g) {
		//1.首先获得一张画布
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		//2.获得画布对应的画笔
		Graphics offScreenG = offScreenImage.getGraphics();
		Color c = offScreenG.getColor();
		//3.每次都初始化背景
		offScreenG.setColor(Color.WHITE);
		offScreenG.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
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
				while(true) {
					repaint();
					Thread.sleep(REPAINT_SPEED);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		
	}
	
	//连续移动相关代码
	/*private enum Direction {
		UP,DOWN,LEFT,RIGHT
	}*/
}
