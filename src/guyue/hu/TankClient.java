package guyue.hu;

import java.awt.*;
import java.awt.event.*;

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
		this.setResizable(false);
		this.setTitle("Tank War");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
	}
}
