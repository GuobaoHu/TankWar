package guyue.hu;

import java.io.*;
import java.net.*;

public class TankNewMsg implements Message {
	private TankClient tc;
	private int msgType = Message.MSG_TANK_NEW;

	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}
	
	public void sendMsg(DatagramSocket ds, String ip, int port) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		//begin 写数据
		try {
			dos.writeInt(msgType);
			dos.writeInt(tc.getMyTank().getId());
			dos.writeInt(tc.getMyTank().getX());
			dos.writeInt(tc.getMyTank().getY());
			dos.writeInt(tc.getMyTank().getDirection().ordinal());
			dos.writeBoolean(tc.getMyTank().isGood());
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
			if(id == tc.getMyTank().getId()) {
				return;
			}
			/**
			 * enemyTanks里面是否已经有该ID，没有则说明发过来的newMsg本坦克还未收到过
			 * 那么可以在new出msg对应的坦克之后，把本坦克的信息发出去给msg对应的坦克，让它也画出自己来
			 */
			boolean exist = false;
			for(int i=0; i<tc.getEnemyTanks().size(); i++) {
				Tank t = tc.getEnemyTanks().get(i);
				if(t.getId() == id) {
					exist = true;
					break;
				}
			}
			if(!exist) {
				//没在enemyTanks里面也不是本客户端的主站坦克，那么把本客户端的主战坦克信息发出去
				TankNewMsg msg = new TankNewMsg(tc);
				tc.getNc().sendMsg(msg);
				
				int x = dis.readInt();
				int y = dis.readInt();
				Direction dir = Direction.values()[dis.readInt()];
				boolean good = dis.readBoolean();
				//begin 基于接收到的数据新建一辆坦克,并加入到tc的enemytanks中
				Tank t = new Tank(x, y, good, tc);
				t.setDirection(dir);
				t.setId(id);
				tc.getEnemyTanks().add(t);
			}
			//end
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
