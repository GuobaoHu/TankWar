package guyue.hu;

import java.io.*;
import java.net.*;

public class TankMsg {
	private Tank tank;

	public TankMsg(Tank tank) {
		this.tank = tank;
	}
	
	public void sendMsg(DatagramSocket ds, String ip, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//begin 写数据
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

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();
System.out.println("id:" + id + "-x:" + x + "-y:" + y + "-dir:" + dir + "-good:" + good);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
