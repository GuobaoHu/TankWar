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
 * @date 2017年12月18日 下午9:29:14
 * @class describ:
 */
public class TankMoveMsg implements Message {
	private int msgType = Message.MSG_TANK_MOVE;
	private TankClient tc;
	private int id;
	private int x, y;
	private Direction dir;
	
	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public TankMoveMsg(int id, int x, int y, Direction dir) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	@Override
	public void sendMsg(DatagramSocket ds, String ip, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//begin 写数据
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(x);
			dos.writeInt(y);
			dos.writeInt(dir.ordinal());
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
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			if(id == tc.getMyTank().getId()) {
				return;
			}
			for(int i=0; i<tc.getEnemyTanks().size(); i++) {
				Tank t = tc.getEnemyTanks().get(i);
				if(id == t.getId()) {
					t.setX(x);
					t.setY(y);
					t.setDirection(dir);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
