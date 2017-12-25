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
 * @date 2017年12月25日 下午5:52:59
 * @class describ:
 */
public class BulletDeadMsg implements Message {
	private int msgType = Message.MSG_BULLET_DEAD;
	private TankClient tc;
	private Bullet b;
	
	public BulletDeadMsg(Bullet b) {
		this.b = b;
	}

	public BulletDeadMsg(TankClient tc) {
		this.tc = tc;
	}

	@Override
	public void sendMsg(DatagramSocket ds, String ip, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//begin 写数据
		try {
			dos.writeInt(msgType);
			dos.writeInt(b.getTankId());
			dos.writeInt(b.getUid());
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
			int tankId = dis.readInt();
			int uid = dis.readInt();			
			for(int i=0; i<tc.getBullets().size(); i++) {
				Bullet b = tc.getBullets().get(i);
				if(b.getTankId() == tankId && b.getUid() == uid) {
					tc.getBooms().add(new Boom(b.getX(), b.getY(), tc));
					tc.getBullets().remove(b);
				}
			}
			//end
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
