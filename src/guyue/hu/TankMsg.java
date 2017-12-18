package guyue.hu;

import java.io.*;
import java.net.*;

public class TankMsg {
	private Tank tank;
	private TankClient tc;

	public TankMsg(Tank tank, TankClient tc) {
		this.tank = tank;
		this.tc = tc;
	}
	
	public void sendMsg(DatagramSocket ds, String ip, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//begin д����
		try {
			dos.writeInt(tank.getId());
			dos.writeInt(tank.getX());
			dos.writeInt(tank.getY());
			dos.writeInt(tank.getDirection().ordinal());
			dos.writeBoolean(tank.isGood());
		} catch (IOException e) {
			e.printStackTrace();
		}
		//end
		
		//begin �����ݷ��͸�Server
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
			//begin ���ڽ��յ��������½�һ��̹��,�����뵽tc��enemytanks��
			Tank t = new Tank(x, y, good, tc);
			t.setDirection(dir);
			t.setId(id);
			tc.getEnemyTanks().add(t);
			//end
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
