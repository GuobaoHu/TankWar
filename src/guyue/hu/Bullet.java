package guyue.hu;

import java.awt.*;
import java.util.List;

public class Bullet {
	public static final int SIZE = 10;
	public static final int X_STEP = 10;
	public static final int Y_STEP = 10;
	private int locationX, locationY;
	private Direction direction;
	private TankClient tc;
	private boolean good = true;
	private static int kill = 0;
	
	public static int getKill() {
		return kill;
	}

	public Bullet(int locationX, int locationY, Direction direction) {
		this.locationX = locationX;
		this.locationY = locationY;
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
		g.fillOval(locationX, locationY, SIZE, SIZE);
		g.setColor(c);
		this.move();
	}
	
	public void move() {
		switch(direction) {
		case U :
			locationY -= Y_STEP;
			break;
		case D :
			locationY += Y_STEP;
			break;
		case L :
			locationX -= X_STEP;
			break;
		case R :
			locationX += X_STEP;
			break;
		case LU :
			locationX -= X_STEP;
			locationY -= Y_STEP;
			break;
		case RU :
			locationX += X_STEP;
			locationY -= Y_STEP;
			break;
		case LD :
			locationX -= X_STEP;
			locationY += Y_STEP;
			break;
		case RD :
			locationX += X_STEP;
			locationY += Y_STEP;
			break;
		}
		this.outRange();
	}
	
	//判断是否出界并作出处理
	public void outRange() {
		if(locationX < 0 || locationY < 0 || locationX > TankClient.GAME_WIDTH || locationY > TankClient.GAME_HEIGHT) {
			this.tc.getBullets().remove(this);
		}
	}
	
	//获取子弹的外包裹矩形
	public Rectangle getRect() {
		return new Rectangle(locationX, locationY, SIZE, SIZE);
	}
	
	//检查子弹撞墙的问题
	public boolean hitWall(Wall w) {
		if(this.getRect().intersects(w.getRect())) {
			tc.getBullets().remove(this);
			return true;
		}
		return false;
	}
	
	//检查子弹是否击中Tank
	public boolean hitTank(Tank t) {
		if(this.getRect().intersects(t.getRect()) && t.isLive() && (this.good != t.isGood())) {
			if(t.isGood()) {
				t.setLife(t.getLife() - 10);
				if(t.getLife() <= 0) {
					t.setLive(false);
				}
			} else {
				t.setLive(false);
			}
			tc.getBooms().add(new Boom(locationX, locationY, tc));
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
		if(tanks.size() < 6) {
			tc.addEnemy();
		}
		return false;
	}
}
