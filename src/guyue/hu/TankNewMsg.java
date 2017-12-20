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
		//begin д����
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
			/**
			 * enemyTanks�����Ƿ��Ѿ��и�ID��û����˵����������newMsg��̹�˻�δ�յ���
			 * ��ô������new��msg��Ӧ��̹��֮�󣬰ѱ�̹�˵���Ϣ����ȥ��msg��Ӧ��̹�ˣ�����Ҳ�����Լ���
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
				//û��enemyTanks����Ҳ���Ǳ��ͻ��˵���վ̹�ˣ���ô�ѱ��ͻ��˵���ս̹����Ϣ����ȥ
				TankNewMsg msg = new TankNewMsg(tc);
				tc.getNc().sendMsg(msg);
				
				int x = dis.readInt();
				int y = dis.readInt();
				Direction dir = Direction.values()[dis.readInt()];
				boolean good = dis.readBoolean();
				//begin ���ڽ��յ��������½�һ��̹��,�����뵽tc��enemytanks��
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
