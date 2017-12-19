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
	private Direction dir;
	
	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public TankMoveMsg(int id, Direction dir) {
		this.id = id;
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
			Direction dir = Direction.values()[dis.readInt()];
			if(id == tc.getMyTank().getId()) {
				return;
			}
			for(int i=0; i<tc.getEnemyTanks().size(); i++) {
				Tank t = tc.getEnemyTanks().get(i);
				if(id == t.getId()) {
					t.setDirection(dir);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
