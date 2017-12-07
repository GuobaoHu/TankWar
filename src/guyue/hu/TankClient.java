package guyue.hu;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * @author guyue
 * @date 2017年11月29日 下午9:21:28
 * @class describ:
 */
public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int REPAINT_SPEED = 100;
	private int tankLocationX = 500;
	private int tankLocationY = 100;
	private Image offScreenImage = null;
	private Tank myTank = new Tank(tankLocationX, tankLocationY, true, this); 
	private List<Tank> enemyTanks = new ArrayList<Tank>();
	private List<Bullet> bullets = new ArrayList<Bullet>();
	private List<Boom> booms = new ArrayList<Boom>();
	private static Random random = new Random();
	private Wall w1 = new Wall(100, 300, 50, 200, this);
	private Wall w2 = new Wall(400, 400, 200, 50, this);
	private Food food = new Food(110, 100, 15, 15, this);
	
	public static void main(String[] args) {
		new TankClient().launch();
	}
	
	public java.util.List<Bullet> getBullets() {
		return bullets;
	}

	public List<Boom> getBooms() {
		return booms;
	}

	public List<Tank> getEnemyTanks() {
		return enemyTanks;
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
		this.addEnemy();
		this.setVisible(true);
	}

	//添加敌方Tank,添加在界面的随机位置
	public void addEnemy() {
		for(int i=0; i<10; i++) {
			Tank t = new Tank(random.nextInt(GAME_WIDTH - Tank.TANK_SIZE), 
					30 + random.nextInt(GAME_HEIGHT - Tank.TANK_SIZE - 30), false, this);
			while(t.getRect().intersects(w1.getRect()) || t.getRect().intersects(w2.getRect())) {
				t = new Tank(random.nextInt(GAME_WIDTH - Tank.TANK_SIZE), 
						30 + random.nextInt(GAME_HEIGHT - Tank.TANK_SIZE - 30), false, this);
			}
			enemyTanks.add(t);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawString("bullets:" + bullets.size(), 10, 40);
		g.drawString("booms count:" + booms.size(), 10, 60);
		g.drawString("enemys count:" + enemyTanks.size(), 100, 40);
		g.drawString("kills count:" + Bullet.getKill(), 100, 60);
		g.drawString("My tank life:" + myTank.getLife(), 10, 80);
		
		for(int i=0; i<bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(!b.hitTanks(enemyTanks) && !b.hitTank(myTank) && !b.hitWall(w1) && !b.hitWall(w2)) {
				bullets.get(i).draw(g);
			}
		}
		
		for(int i=0; i<booms.size(); i++) {
			Boom boom = booms.get(i);
			boom.draw(g);
		}
		myTank.draw(g);
		food.draw(g);
		myTank.eatFood(food);
		w1.draw(g);
		w2.draw(g);
		for(int i=0; i<enemyTanks.size(); i++) {
			Tank enemyTank = enemyTanks.get(i);
			enemyTank.hitWall(w1);
			enemyTank.hitWall(w2);
			enemyTank.hitTanks(enemyTanks);
			enemyTank.draw(g);
		}
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
}
