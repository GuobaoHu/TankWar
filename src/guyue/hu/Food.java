package guyue.hu;

import java.awt.*;

/**
 * @author guyue
 * @date 2017年12月7日 下午8:31:05
 * @class describ:
 */
public class Food {
	private int x, y, w, h;
	private TankClient tc;
	private int[][] positions = {{100,100}, {120,120}, {150,150}, {200,200}, {230,230}, 
			{260,260}, {280,280}, {300,300}, {320,320}, {350,350}, {370,370}, {400,400}};
	private int step = 0, counter = 0;
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Food(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(!live) {
			this.count();
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.CYAN);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		this.move();
		this.count();
	}
	
	public void move() {
		step ++;
		if(step == positions.length) {
			step = 0;
		}
		x = positions[step][0];
		y = positions[step][1];
	}
	
	public void count() {
		counter ++;
		if(counter == 300) {
			counter = 0;
			live = !live;
		}
	}
	
	public Rectangle getRect() {
		if(!live) return new Rectangle();
		return new Rectangle(x, y, w, h);
	}
	
}
