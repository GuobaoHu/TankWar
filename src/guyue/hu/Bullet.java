package guyue.hu;

import java.awt.*;
import guyue.hu.Tank.Direction;

public class Bullet {
	public static final int SIZE = 10;
	public static final int X_STEP = 10;
	public static final int Y_STEP = 10;
	private int locationX, locationY;
	private Tank.Direction direction;
	
	
	public Bullet(int locationX, int locationY, Direction direction) {
		this.locationX = locationX;
		this.locationY = locationY;
		this.direction = direction;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.RED);
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
	}
}
