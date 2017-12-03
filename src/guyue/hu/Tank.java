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
	private Direction ptDirection = Direction.D; //��Ͳ����
	private boolean bU=false, bD=false, bL=false, bR=false;//��������4�������Ƿ��µ�flag���
	private TankClient tc = null;
	
	public Tank(int tankLocationX, int tankLocationY) {
		this.tankLocationX = tankLocationX;
		this.tankLocationY = tankLocationY;
	}
	
	public Tank(int tankLocationX, int tankLocationY, TankClient tc) {
		this(tankLocationX, tankLocationY);
		this.tc = tc;
	}

	//̹�˿���
	public void fire() {
		int x = tankLocationX + TANK_SIZE/2 - Bullet.SIZE/2;
		int y = tankLocationY + TANK_SIZE/2 - Bullet.SIZE/2;
		Bullet b = new Bullet(x, y, ptDirection);
		tc.getBullets().add(b);
	}
	
	//����̹������
	public void draw(Graphics g) {
		Color defaultColor = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(tankLocationX, tankLocationY, TANK_SIZE, TANK_SIZE);
		g.setColor(defaultColor);
		this.drawPT(g);
		this.move();
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
	
	//����̹���ƶ���8������
	enum Direction {
		U,D,L,R,LU,RU,LD,RD,STOP
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
		/*�����ptDirection��ֵ�Ĵ������move()���������ԭ��
		1.move�������������̵߳��ã������ػ��߳��������
		2.�ػ��̵߳��õ�ʱ��ÿ���ػ�֮����100ms
		3.�ڼ����100ms�ڣ�direction�ı仯����ԭ���Բ�����Ҳ����˵̹�˵�direction���Դӣ�����-->��-->ͣ��
		4.��ô����������ػ��߳������ptDirection��ֵ��ʱ����һ�ο��������£���һ�ξ�ͣ�ˣ�ͣ�˲���ı�ptDirection��ֵ���������Ϳ��Լ�¼�����µ�λ��
		5.��������߳�����moveDirection�������������ptDirection��ֵ�Ļ������ǲ�����ͬʱ�ɿ��������������������Ѽ�¼�������������λ��*/
		if(direction != Direction.STOP) {
			ptDirection = direction;
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
}
