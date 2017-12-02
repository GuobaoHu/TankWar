package guyue.hu;

import java.awt.*;
import java.awt.event.*;

public class Tank {
	private int tankLocationX;
	private int tankLocationY;
	private int moveStep;
	private int tankSize;
	
	public Tank(int tankLocationX, int tankLocationY, int moveStep, int tankSize) {
		this.tankLocationX = tankLocationX;
		this.tankLocationY = tankLocationY;
		this.moveStep = moveStep;
		this.tankSize = tankSize;
	}
	
	//画出坦克自身
	public void draw(Graphics g) {
		Color defaultColor = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(tankLocationX, tankLocationY, tankSize, tankSize);
		g.setColor(defaultColor);
	}
	
	//坦克移动
	public void move(KeyEvent e) {
		int keyValue = e.getKeyCode();
		switch(keyValue) {
		case KeyEvent.VK_UP :
			tankLocationY -= moveStep;
			break;
		case KeyEvent.VK_DOWN :
			tankLocationY += moveStep;
			break;
		case KeyEvent.VK_LEFT :
			tankLocationX -= moveStep;
			break;
		case KeyEvent.VK_RIGHT :
			tankLocationX += moveStep;
			break;
		}
	}
	
}
