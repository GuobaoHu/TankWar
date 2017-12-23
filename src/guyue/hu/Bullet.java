package guyue.hu;

import java.awt.*;
import java.util.List;

public class Bullet {
	public static final int SIZE = 10;
	public static final int X_STEP = 10;
	public static final int Y_STEP = 10;
	private int x, y;
	private Direction direction;
	private TankClient tc;
	private boolean good = true;
	private static int kill = 0;
	
	public static int getKill() {
		return kill;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Bullet(int x, int y, Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public Bullet(int locationX, int locationY, Direction direction, TankClient tc, boolean good) {
		this(locationX, locationY, direction);
		this.good = good;
		this.tc = tc;
	}

	public void draw(Graphics g) {
		Color c = g.getColor();
		if(good) {
		g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}
		g.fillOval(x, y, SIZE, SIZE);
		g.setColor(c);
		this.move();
	}
	
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
		}
		this.outRange();
	}
	
	//判断是否出界并作出处理
	public void outRange() {
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			this.tc.getBullets().remove(this);
		}
	}
	
	//获取子弹的外包裹矩形
	public Rectangle getRect() {
		return new Rectangle(x, y, SIZE, SIZE);
	}
	
	//检查子弹是否击中Tank
	public boolean hitTank(Tank t) {
		if(this.getRect().intersects(t.getRect()) && t.isLive() && (this.good != t.isGood())) {
			t.setLive(false);
			tc.getBooms().add(new Boom(x, y, tc));
			tc.getBullets().remove(this);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank tank = tanks.get(i);
			if(this.hitTank(tank)) {
				kill ++;
				tanks.remove(tank);
				return true;
			}
		}
		/*if(tanks.size() < 6) {
			tc.addEnemy();
		}*/
		return false;
	}
}
