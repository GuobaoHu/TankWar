package guyue.hu;

import java.awt.Frame;

/**
 * @author guyue
 * @date 2017��11��29�� ����9:21:28
 * @class describ:
 */
public class BattleFiled extends Frame {

	public static void main(String[] args) {
		new BattleFiled().launch();
	}
	
	public void launch() {
		this.setBounds(200, 100, 800, 600);
		this.setVisible(true);
	}
}
