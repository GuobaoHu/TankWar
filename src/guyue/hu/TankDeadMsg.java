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
 * @date 2017年12月25日 下午4:16:33
 * @class describ:
 */
public class TankDeadMsg implements Message {
	private int msgType = Message.MSG_TANK_DEAD;
	private TankClient tc;
	private Tank t;
	
	public TankDeadMsg(TankClient tc) {
		this.tc = tc;
	}

	public TankDeadMsg(Tank t) {
		this.t = t;
	}

	@Override
	public void sendMsg(DatagramSocket ds, String ip, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//begin 写数据
		try {
			dos.writeInt(msgType);
			dos.writeInt(t.getId());
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
						
			for(int i=0; i<tc.getEnemyTanks().size(); i++) {
				Tank t = tc.getEnemyTanks().get(i);
				if(t.getId() == id) {
					t.setLive(false);
					tc.getEnemyTanks().remove(t);
				}
			}
			//end
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
