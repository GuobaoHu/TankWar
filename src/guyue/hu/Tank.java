package guyue.hu;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank {
	public static final int TANK_SIZE = 30;
	public static final int X_STEP = 5;
	public static final int Y_STEP = 5;
	
	private int id;
	private int x;
	private int y;
	private Direction direction = Direction.STOP;
	private Direction ptDirection = Direction.D; //炮筒方向
	private boolean bU=false, bD=false, bL=false, bR=false;//上下左右4个按键是否按下的flag标记
	private TankClient tc = null;
	private boolean live = true;
	private boolean good = true;
	private static Random random = new Random();//随机数产生器，这种比Math类里面的要好，可以产生int类型
	private int step = 5 + random.nextInt(15);
	
	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isGood() {
		return good;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank(int tankLocationX, int tankLocationY) {
		this.x = tankLocationX;
		this.y = tankLocationY;
	}
	
	public Tank(int tankLocationX, int tankLocationY, boolean good, TankClient tc) {
		this(tankLocationX, tankLocationY);
		this.good = good;
		this.tc = tc;
	}

	//坦克开火
	public void fire() {
		int x = this.x + TANK_SIZE/2 - Bullet.SIZE/2;
		int y = this.y + TANK_SIZE/2 - Bullet.SIZE/2;
		Bullet b = new Bullet(x, y, ptDirection, this.tc, good);
		tc.getBullets().add(b);
	}
	
	//画出坦克自身
	public void draw(Graphics g) {
		if(this.isLive()) {
			Color defaultColor = g.getColor();
			if(good) {
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(Color.BLUE);
			}
			g.fillOval(x, y, TANK_SIZE, TANK_SIZE);
			g.setColor(defaultColor);
			g.drawString("id:" + id, x, y - 10);
			this.drawPT(g);
			this.move();
		}
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
	
	//根据按键是否被按下确定坦克的移动方向
	public void moveDirection() {
		Direction prevDir = direction;
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
		if(prevDir != direction) {
			TankMoveMsg msg = new TankMoveMsg(id, x, y, direction);
			tc.getNc().sendMsg(msg);
		}
	}
	
	//8个方向的移动
	public void move() {
		switch(direction) {
		case U :
			y -= Y_STEP;
			break;
		case D :
			y += Y_STEP;
			break;
		case L :
			x -= X_STEP;
			break;
		case R :
			x += X_STEP;
			break;
		case LU :
			x -= X_STEP;
			y -= Y_STEP;
			break;
		case RU :
			x += X_STEP;
			y -= Y_STEP;
			break;
		case LD :
			x -= X_STEP;
			y += Y_STEP;
			break;
		case RD :
			x += X_STEP;
			y += Y_STEP;
			break;
		case STOP :
			break;
		}
		
		if(x < 0) x = 0;
		if(x > TankClient.GAME_WIDTH - Tank.TANK_SIZE) x = TankClient.GAME_WIDTH - Tank.TANK_SIZE;
		if(y < 30) y = 30;
		if(y > TankClient.GAME_HEIGHT - Tank.TANK_SIZE) y = TankClient.GAME_HEIGHT - Tank.TANK_SIZE;
		/*下面给ptDirection赋值的代码加在move()方法里面的原因：
		1.move方法不是在主线程调用，是在重画线程里面调用
		2.重画线程调用的时候，每次重画之间间隔100ms
		3.在间隔的100ms内，direction的变化不是原子性操作，也就是说坦克的direction可以从（右下-->右-->停）
		4.那么我们在这个重画线程里面给ptDirection赋值的时候，上一次可能是右下，下一次就停了（停了不会改变ptDirection的值），这样就可以记录下右下的位置
		5.如果在主线程例如moveDirection（）方法里面给ptDirection赋值的话，我们不可能同时松开右下两个键，这样很难记录像右下这种组合位置*/
		if(direction != Direction.STOP) {
			ptDirection = direction;
		}
		if(!good) {
			Direction[] directions = Direction.values();
			if(step == 0) {
				step = 5 + random.nextInt(15);
				int i = random.nextInt(directions.length);
				direction = directions[i];
			}
			if(random.nextInt(30) > 28) {
				this.fire();
			}
			step --;
		}
		
	}
	
	//画炮筒
	public void drawPT(Graphics g) {
		switch(ptDirection) {
		case U :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x+TANK_SIZE/2, y);
			break;
		case D :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x+TANK_SIZE/2, y+TANK_SIZE);
			break;
		case L :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x, y+TANK_SIZE/2);
			break;
		case R :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x+TANK_SIZE, y+TANK_SIZE/2);
			break;
		case LU :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x, y);
			break;
		case RU :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x+TANK_SIZE, y);
			break;
		case LD :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x, y+TANK_SIZE);
			break;
		case RD :
			g.drawLine(x+TANK_SIZE/2, y+TANK_SIZE/2, x+TANK_SIZE, y+TANK_SIZE);
			break;
		}
	}
	
	//获取Tank的外包裹矩形
	public Rectangle getRect() {
		return new Rectangle(x, y, TANK_SIZE, TANK_SIZE);
	}
	
	
}
