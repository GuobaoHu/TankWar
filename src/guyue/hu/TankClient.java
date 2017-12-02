package guyue.hu;

import java.awt.*;
import java.awt.event.*;

/**
 * @author guyue
 * @date 2017年11月29日 下午9:21:28
 * @class describ:
 */
public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int TANK_SIZE = 30;
	public static final int MOVE_STEP = 5;
	public static final int REPAINT_SPEED = 20;
	private int tankLocationX = 100;
	private int tankLocationY = 100;
	private Image offScreenImage = null;
	private Tank myTank = new Tank(tankLocationX, tankLocationY, MOVE_STEP, TANK_SIZE); 
//	private Direction direction = Direction.DOWN;

	public static void main(String[] args) {
		new TankClient().launch();
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
		myTank.draw(g);
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

	//连续移动相关代码，贪食蛇可以参考该写法
	/*public void directionMove() {
		switch(direction) {
		case UP:
			tankLocationY = tankLocationY - MOVE_STEP;
			break;
		case DOWN:
			tankLocationY = tankLocationY + MOVE_STEP;
			break;
		case LEFT:
			tankLocationX = tankLocationX - MOVE_STEP;
			break;
		case RIGHT:
			tankLocationX = tankLocationX + MOVE_STEP;
			break;
		}
	}*/
	
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
			myTank.move(e);
			/*if(e.getKeyCode() == KeyEvent.VK_UP) {
				direction = Direction.UP;
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				direction = Direction.DOWN;
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				direction = Direction.LEFT;
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				direction = Direction.RIGHT;
			}*/
		}
		
	}
	
	//连续移动相关代码
	/*private enum Direction {
		UP,DOWN,LEFT,RIGHT
	}*/
}
