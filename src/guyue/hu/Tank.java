package guyue.hu;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tank {
	public static final int TANK_SIZE = 30;
	public static final int X_STEP = 5;
	public static final int Y_STEP = 5;
	
	private int tankLocationX;
	private int tankLocationY;
	private Direction direction = Direction.STOP;
	private Direction ptDirection = Direction.D; //��Ͳ����
	private boolean bU=false, bD=false, bL=false, bR=false;//��������4�������Ƿ��µ�flag���
	private TankClient tc = null;
	private boolean live = true;
	private boolean good = true;
	private static Random random = new Random();//����������������ֱ�Math�������Ҫ�ã����Բ���int����
	private int step = 5 + random.nextInt(15);
	private int oldX, oldY;
	private int life = 100;
	private BloodBar blood = new BloodBar();
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
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
		this.tankLocationX = tankLocationX;
		this.tankLocationY = tankLocationY;
		this.oldX = tankLocationX;
		this.oldY = tankLocationY;
	}
	
	public Tank(int tankLocationX, int tankLocationY, boolean good, TankClient tc) {
		this(tankLocationX, tankLocationY);
		this.good = good;
		this.tc = tc;
	}

	//̹�˿���
	public void fire() {
		int x = tankLocationX + TANK_SIZE/2 - Bullet.SIZE/2;
		int y = tankLocationY + TANK_SIZE/2 - Bullet.SIZE/2;
		Bullet b = new Bullet(x, y, ptDirection, this.tc, good);
		tc.getBullets().add(b);
	}
	
	public void fire(Direction direction) {
		int x = tankLocationX + TANK_SIZE/2 - Bullet.SIZE/2;
		int y = tankLocationY + TANK_SIZE/2 - Bullet.SIZE/2;
		Bullet b = new Bullet(x, y, direction, this.tc, good);
		tc.getBullets().add(b);
	}
	
	//̹��superFire
	public void superFire() {
		Direction[] directions  = Direction.values();
		for(int i=0; i<8; i++) {
			fire(directions[i]);
		}
	}
	
	//����̹������
	public void draw(Graphics g) {
		if(this.isLive()) {
			Color defaultColor = g.getColor();
			if(good) {
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(Color.BLUE);
			}
			g.fillOval(tankLocationX, tankLocationY, TANK_SIZE, TANK_SIZE);
			g.setColor(defaultColor);
			this.drawPT(g);
			if(good) {
				blood.draw(g);
			}
			this.move();
		}
	}
	
	//��������
	public void keyPressed(KeyEvent e) {
		int keyValue = e.getKeyCode();
		switch(keyValue) {
		case KeyEvent.VK_F2 :
			if(!live) {
				live = true;
				life = 100;
			}
			break;
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
	
	//�����ͷ�
	public void keyReleased(KeyEvent e) {
		int keyValue = e.getKeyCode();
		switch (keyValue) {
		case KeyEvent.VK_1 :
			this.fire();
			break;
		case KeyEvent.VK_2 :
			this.superFire();
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
	
	//���ݰ����Ƿ񱻰���ȷ��̹�˵��ƶ�����
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
	
	//8��������ƶ�
	public void move() {
		oldX = tankLocationX;
		oldY = tankLocationY;
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
		
		if(tankLocationX < 0) tankLocationX = 0;
		if(tankLocationX > TankClient.GAME_WIDTH - Tank.TANK_SIZE) tankLocationX = TankClient.GAME_WIDTH - Tank.TANK_SIZE;
		if(tankLocationY < 30) tankLocationY = 30;
		if(tankLocationY > TankClient.GAME_HEIGHT - Tank.TANK_SIZE) tankLocationY = TankClient.GAME_HEIGHT - Tank.TANK_SIZE;
		/*�����ptDirection��ֵ�Ĵ������move()���������ԭ��
		1.move�������������̵߳��ã������ػ��߳��������
		2.�ػ��̵߳��õ�ʱ��ÿ���ػ�֮����100ms
		3.�ڼ����100ms�ڣ�direction�ı仯����ԭ���Բ�����Ҳ����˵̹�˵�direction���Դӣ�����-->��-->ͣ��
		4.��ô����������ػ��߳������ptDirection��ֵ��ʱ����һ�ο��������£���һ�ξ�ͣ�ˣ�ͣ�˲���ı�ptDirection��ֵ���������Ϳ��Լ�¼�����µ�λ��
		5.��������߳�����moveDirection�������������ptDirection��ֵ�Ļ������ǲ�����ͬʱ�ɿ��������������������Ѽ�¼�������������λ��*/
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
			if(random.nextInt(12) > 8) {
				this.fire();
			}
			step --;
		}
		
	}
	
	//����Ͳ
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
	
	//��ȡTank�����������
	public Rectangle getRect() {
		return new Rectangle(tankLocationX, tankLocationY, TANK_SIZE, TANK_SIZE);
	}
	
	//��ǽ��ײʱ�ص���һ����λ��
	public void stay() {
		tankLocationX = oldX;
		tankLocationY = oldY;
	}
	
	//����Ƿ���ǽ��ײ
	public boolean hitWall(Wall w) {
		if(this.getRect().intersects(w.getRect())) {
			this.stay();
			return true;
		}
		return false;
	}
	
	//�����̹���Ƿ���ײ
	public boolean hitTanks(java.util.List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this.live && t.isLive() && (this != t) && 
					this.getRect().intersects(t.getRect())) {
				this.stay();
				t.stay();
				return true;
			}
		}
		return false;
	}
	
	//��ʳ��
	public boolean eatFood(Food food) {
		if(good && live && food.isLive() && this.getRect().intersects(food.getRect())) {
			food.setLive(false);
			life = 100;
			return true;
		}
		return false;
	}
	
	//����̹�˵�Ѫ��
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.GREEN);
			g.drawRect(tankLocationX, tankLocationY-15, TANK_SIZE, 10);
			int width = TANK_SIZE * life/100;
			if(life > 50) {
				g.setColor(Color.GREEN);
			} else if(life > 20) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.RED);
			}
			g.fillRect(tankLocationX, tankLocationY-15, width, 10);
			g.setColor(c);
		}
	}
}
