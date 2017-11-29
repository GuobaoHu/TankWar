package guyue.hu;

import java.awt.Frame;

/**
 * @author guyue
 * @date 2017年11月29日 下午9:21:28
 * @class describ:
 */
public class TankClient extends Frame {

	public static void main(String[] args) {
		new TankClient().launch();
	}
	
	public void launch() {
		this.setBounds(200, 100, 800, 600);
		this.setVisible(true);
	}
}
