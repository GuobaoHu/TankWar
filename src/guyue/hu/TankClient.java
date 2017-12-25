package guyue.hu;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * @author guyue
 * @date 2017年11月29日 下午9:21:28
 * @class describ:
 */
public class TankClient extends Frame {
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	public static final int REPAINT_SPEED = 100;
	private int tankLocationX = 100;
	private int tankLocationY = 100;
	private Image offScreenImage = null;
	private Tank myTank = new Tank(tankLocationX, tankLocationY, true, this); 
	private List<Tank> enemyTanks = new ArrayList<Tank>();
	private List<Bullet> bullets = new ArrayList<Bullet>();
	private List<Boom> booms = new ArrayList<Boom>();
	private static Random random = new Random();
	private NetClient nc = new NetClient(this);
	private ConDialog conDlg;
	
	public static void main(String[] args) {
		new TankClient().launch();
	}
	
	public NetClient getNc() {
		return nc;
	}

	public void setNc(NetClient nc) {
		this.nc = nc;
	}

	public Tank getMyTank() {
		return myTank;
	}

	public java.util.List<Bullet> getBullets() {
		return bullets;
	}

	public List<Boom> getBooms() {
		return booms;
	}

	public List<Tank> getEnemyTanks() {
		return enemyTanks;
	}

	//启动
	public void launch() {
		this.setBounds(200, 100, GAME_WIDTH, GAME_HEIGHT);
		this.setResizable(false);
		this.setTitle("Tank War");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new KeyMonitor());
		new Thread(new TankMove()).start();
//		nc.connect("127.0.0.1", TankServer.TCP_PORT);
//		this.addEnemy();
		this.setVisible(true);
	}

	//添加敌方Tank,添加在界面的随机位置
	/*public void addEnemy() {
		for(int i=0; i<10; i++) {
			enemyTanks.add(new Tank(random.nextInt(GAME_WIDTH - Tank.TANK_SIZE), 30 + random.nextInt(GAME_HEIGHT - Tank.TANK_SIZE - 30), false, this));
		}
	}*/
	
	@Override
	public void paint(Graphics g) {
		g.drawString("bullets:" + bullets.size(), 10, 40);
		g.drawString("booms count:" + booms.size(), 10, 60);
		g.drawString("enemys count:" + enemyTanks.size(), 100, 40);
		g.drawString("kills count:" + Bullet.getKill(), 100, 60);
		
		for(int i=0; i<bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(!b.hitTank(myTank)) {
				bullets.get(i).draw(g);
			}
		}
		
		for(int i=0; i<booms.size(); i++) {
			Boom boom = booms.get(i);
			boom.draw(g);
		}
		myTank.draw(g);
		for(int i=0; i<enemyTanks.size(); i++) {
			enemyTanks.get(i).draw(g);
		}
//		this.directionMove();
	}
	
	//双缓冲解决闪烁问题
	@Override
	public void update(Graphics g) {
		//1.首先获得一张画布
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		//2.获得画布对应的画笔
		Graphics offScreenG = offScreenImage.getGraphics();
		Color c = offScreenG.getColor();
		//3.每次都初始化背景
		offScreenG.setColor(Color.WHITE);
		offScreenG.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		//4.重置画布画笔的颜色
		offScreenG.setColor(c);
		//5.画图
		this.paint(offScreenG);
		//6.将图片画到前景窗体里面
		g.drawImage(offScreenImage, 0, 0, null);
	}

	private class TankMove implements Runnable {

		@Override
		public void run() {
			try {
				while(true) {
					repaint();
					Thread.sleep(REPAINT_SPEED);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_C) {
				conDlg = new ConDialog();
				conDlg.setVisible(true);
			} else {
				myTank.keyPressed(e);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
		
	}
	
	private class ConDialog extends Dialog {
		private TextField ipField = new TextField("127.0.0.1", 20);
		private TextField portField = new TextField("" + TankServer.TCP_PORT, 5);
		private TextField myUportField = new TextField("12000", 5);
		private Button button = new Button("连接");
		
		public ConDialog() {
			super(TankClient.this, true);
			this.launch();
		}
		
		public void launch() {
			this.setLocation(200, 200);
			this.setLayout(new FlowLayout());
			this.add(new Label("Host IP:"));
			this.add(ipField);
			this.add(new Label("Host Port:"));
			this.add(portField);
			this.add(new Label("My UDP Port:"));
			this.add(myUportField);
			this.add(button);
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					ConDialog.this.setVisible(false);
				}
			});
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String ip = ipField.getText().trim();
					int hostPort = Integer.parseInt(portField.getText().trim());
					int udpPort = Integer.parseInt(myUportField.getText().trim());
					nc.setUdpPort(udpPort);
					nc.setIp(ip);
					nc.connect(ip, hostPort);
					ConDialog.this.setVisible(false);
				}
			});
			this.pack();
		}
	}
}
