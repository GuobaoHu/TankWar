package guyue.hu;

import java.awt.*;
import java.awt.event.*;

/**
 * @author guyue
 * @date 2017��11��29�� ����9:21:28
 * @class describ:
 */
public class TankClient extends Frame {
	private int tankLocationX = 100;
	private int tankLocationY = 100;
	private Image offScreenImage = null;

	public static void main(String[] args) {
		new TankClient().launch();
	}
	
	//����
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
		new Thread(new TankMove()).start();
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		Color defaultColor = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillOval(tankLocationX, tankLocationY, 30, 30);
		g.setColor(defaultColor);
		tankLocationX = tankLocationX + 5;
	}
	
	//˫��������˸����
	@Override
	public void update(Graphics g) {
		//1.���Ȼ��һ�Ż���
		if(offScreenImage == null) {
			offScreenImage = this.createImage(800, 600);
		}
		//2.��û�����Ӧ�Ļ���
		Graphics offScreenG = offScreenImage.getGraphics();
		Color c = offScreenG.getColor();
		//3.ÿ�ζ���ʼ������
		offScreenG.setColor(Color.WHITE);
		offScreenG.fillRect(0, 0, 800, 600);
		//4.���û������ʵ���ɫ
		offScreenG.setColor(c);
		//5.��ͼ
		this.paint(offScreenG);
		//6.��ͼƬ����ǰ����������
		g.drawImage(offScreenImage, 0, 0, null);
	}

	private class TankMove implements Runnable {

		@Override
		public void run() {
			try {
				for(int i=0; i<500; i++) {
					repaint();
					Thread.sleep(20);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
