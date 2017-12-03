package guyue.hu;

import java.awt.*;
import java.awt.event.*;

public class Tank {
	public static final int TANK_SIZE = 30;
	public static final int X_STEP = 5;
	public static final int Y_STEP = 5;
	
	private int tankLocationX;
	private int tankLocationY;
	private Direction direction = Direction.STOP;
	private Direction ptDirection = Direction.D; //炮筒方向
	private boolean bU=false, bD=false, bL=false, bR=false;//上下左右4个按键是否按下的flag标记
	private TankClient tc = null;
	
	public Tank(int tankLocationX, int tankLocationY) {
		this.tankLocationX = tankLocationX;
		this.tankLocationY = tankLocationY;
	}
	
	public Tank(int tankLocationX, int tankLocationY, TankClient tc) {
		this(tankLocationX, tankLocationY);
		this.tc = tc;
	}

	//坦克开火
	public void fire() {
		int x = tankLocationX + TANK_SIZE/2 - Bullet.SIZE/2;
		int y = tankLocationY + TANK_SIZE/2 - Bullet.SIZE/2;
		Bullet b = new Bullet(x, y, ptDirection);
		tc.getBullets().add(b);
	}
	
	//画出坦克自身
	public void draw(Graphics g) {
		Color defaultColor = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(tankLocationX, tankLocationY, TANK_SIZE, TANK_SIZE);
		g.setColor(defaultColor);
		this.drawPT(g);
		this.move();
	}
	
	//按键按下
	public void keyPressed(KeyEvent e) {
		int keyValue = e.getKeyCode();
		switch(keyValue) {
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		}
		this.moveDirection();
	}
	
	//按键释放
	public void keyReleased(KeyEvent e) {
		int keyValue = e.getKeyCode();
		switch (keyValue) {
		case KeyEvent.VK_1 :
			this.fire();
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		}
		this.moveDirection();
	}
	
	//定义坦克移动的8个方向
	enum Direction {
		U,D,L,R,LU,RU,LD,RD,STOP
	}
	
	//根据按键是否被按下确定坦克的移动方向
	public void moveDirection() {
		if(bU && !bD && !bL && !bR) {
			direction = Direction.U;
		} else if(!bU && bD && !bL && !bR) {
			direction = Direction.D;
		} else if(!bU && !bD && bL && !bR) {
			direction = Direction.L;
		} else if(!bU && !bD && !bL && bR) {
			direction = Direction.R;
		} else if(bU && !bD && bL && !bR) {
			direction = Direction.LU;
		} else if(bU && !bD && !bL && bR) {
			direction = Direction.RU;
		} else if(!bU && bD && bL && !bR) {
			direction = Direction.LD;
		} else if(!bU && bD && !bL && bR) {
			direction = Direction.RD;
		} else {
			direction = Direction.STOP;
		}
	}
	
	//8个方向的移动
	public void move() {
		switch(direction) {
		case U :
			tankLocationY -= Y_STEP;
			break;
		case D :
			tankLocationY += Y_STEP;
			break;
		case L :
			tankLocationX -= X_STEP;
			break;
		case R :
			tankLocationX += X_STEP;
			break;
		case LU :
			tankLocationX -= X_STEP;
			tankLocationY -= Y_STEP;
			break;
		case RU :
			tankLocationX += X_STEP;
			tankLocationY -= Y_STEP;
			break;
		case LD :
			tankLocationX -= X_STEP;
			tankLocationY += Y_STEP;
			break;
		case RD :
			tankLocationX += X_STEP;
			tankLocationY += Y_STEP;
			break;
		case STOP :
			break;
		}
		/*下面给ptDirection赋值的代码加在move()方法里面的原因：
		1.move方法不是在主线程调用，是在重画线程里面调用
		2.重画线程调用的时候，每次重画之间间隔100ms
		3.在间隔的100ms内，direction的变化不是原子性操作，也就是说坦克的direction可以从（右下-->右-->停）
		4.那么我们在这个重画线程里面给ptDirection赋值的时候，上一次可能是右下，下一次就停了（停了不会改变ptDirection的值），这样就可以记录下右下的位置
		5.如果在主线程例如moveDirection（）方法里面给ptDirection赋值的话，我们不可能同时松开右下两个键，这样很难记录像右下这种组合位置*/
		if(direction != Direction.STOP) {
			ptDirection = direction;
		}
	}
	
	//画炮筒
	public void drawPT(Graphics g) {
		switch(ptDirection) {
		case U :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX+TANK_SIZE/2, tankLocationY);
			break;
		case D :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE);
			break;
		case L :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX, tankLocationY+TANK_SIZE/2);
			break;
		case R :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX+TANK_SIZE, tankLocationY+TANK_SIZE/2);
			break;
		case LU :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX, tankLocationY);
			break;
		case RU :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX+TANK_SIZE, tankLocationY);
			break;
		case LD :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX, tankLocationY+TANK_SIZE);
			break;
		case RD :
			g.drawLine(tankLocationX+TANK_SIZE/2, tankLocationY+TANK_SIZE/2, tankLocationX+TANK_SIZE, tankLocationY+TANK_SIZE);
			break;
		}
	}
}
