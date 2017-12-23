package guyue.hu;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author guyue
 * @date 2017年12月23日 下午9:04:51
 * @class describ:
 */
public class BulletNewMsg implements Message {
	private int msgType = Message.MSG_BULLET_NEW;
	private TankClient tc;
	private Bullet bullet;
	
	public BulletNewMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public BulletNewMsg(TankClient tc, Bullet bullet) {
		this(tc);
		this.bullet = bullet;
	}

	@Override
	public void sendMsg(DatagramSocket ds, String ip, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//begin 写数据
		try {
			dos.writeInt(msgType);
			dos.writeInt(tc.getMyTank().getId());
			dos.writeInt(bullet.getX());
			dos.writeInt(bullet.getY());
			dos.writeInt(bullet.getDirection().ordinal());
			dos.writeBoolean(bullet.isGood());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//end
		
		//begin 将数据发送给Server
		byte[] buf = baos.toByteArray();
		DatagramPacket p = new DatagramPacket(buf, buf.length, 
				new InetSocketAddress(ip, port));
		try {
			ds.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//end
	}

	@Override
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if(id == tc.getMyTank().getId()) {
				return;
			}
						
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
			Bullet bullet = new Bullet(x, y, dir, tc,good);
			tc.getBullets().add(bullet);
			//end
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
