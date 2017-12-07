package guyue.hu;

import java.awt.*;

public class Boom {
	private int x, y;
	private TankClient tc;
	private boolean live = true;
	private int diameters[] = {6, 15, 20, 30, 40, 50, 60, 25, 10, 2};
	private int step = 0;
	
	public Boom(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	//»­³ö±¬Õ¨¹ý³Ì
	public void draw(Graphics g) {
		if(!live) {
			tc.getBooms().remove(this);
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.fillOval(x, y, diameters[step], diameters[step]);
		g.setColor(c);
		step ++;
		if(step == diameters.length) {
			live = false;
			step = 0;
		}
	}
	
}
