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
	private Direction ptDirection = Direction.D; //��Ͳ����
	private boolean bU=false, bD=false, bL=false, bR=false;//��������4�������Ƿ��µ�flag���
	private TankClient tc = null;
	private boolean live = true;
	private boolean good = true;
	private static Random random = new Random();//����������������ֱ�Math�������Ҫ�ã����Բ���int����
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

	//̹�˿���
	public void fire() {
		int x = this.x + TANK_SIZE/2 - Bullet.SIZE/2;
		int y = this.y + TANK_SIZE/2 - Bullet.SIZE/2;
		Bullet b = new Bullet(x, y, ptDirection, this.tc, good);
		tc.getBullets().add(b);
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
			g.fillOval(x, y, TANK_SIZE, TANK_SIZE);
			g.setColor(defaultColor);
			g.drawString("id:" + id, x, y - 10);
			this.drawPT(g);
			this.move();
		}
	}
	
	//��������
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
	
	//�����ͷ�
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
	
	//���ݰ����Ƿ񱻰���ȷ��̹�˵��ƶ�����
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
	
	//8��������ƶ�
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
			if(random.nextInt(30) > 28) {
				this.fire();
			}
			step --;
		}
		
	}
	
	//����Ͳ
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
	
	//��ȡTank�����������
	public Rectangle getRect() {
		return new Rectangle(x, y, TANK_SIZE, TANK_SIZE);
	}
	
	
}
