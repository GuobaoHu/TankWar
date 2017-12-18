package guyue.hu;

import java.io.*;
import java.net.*;

public class NetClient {
	private static int UDP_PORT_START = 11003;
	private int udpPort;
	private Socket s = null;
	private TankClient tc;
	private DatagramSocket ds;
	private TankNewMsg msg;
	/**
	 * �¼���һ��tank������Server����tank�������Ϣ
	 * 
	 * @param tc TankClient��
	 */
	public NetClient(TankClient tc) {
		 udpPort = UDP_PORT_START ++;
		 this.tc = tc;
		 this.msg = new TankNewMsg(tc);
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}


	public void connect(String host, int port) {
		//begin ������֮����UDP���������߳�
		new Thread(new ThreadRcv()).start();
		//end
		//begin ���ֻ��ƣ���Server�˷���udp�˿ںţ�������Server�˷���Ķ�һID
		try {
			s = new Socket(host, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.getMyTank().setId(id);
System.out.println("Connect server! server give me an ID:" + id);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//end
		
		//begin ͨ��UDP�������ӵ�Tank�������
		sendMsg(msg);
		//end
	}

	public void sendMsg(TankNewMsg msg) {
		msg.sendMsg(ds, "127.0.0.1", TankServer.UDP_PORT);
	}
	
	private class ThreadRcv implements Runnable {
		byte[] buf = new byte[1024];
		
		@Override
		public void run() {
			while(true) {
				
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					//ע���д��
					ThreadRcv.this.parse(dp);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void parse(DatagramPacket dp) {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buf, 0, dp.getLength()));
			/**
			 * �˴���ӻ���dis����ؽ�������
			 * 
			 * 
			 */
		}
		
	}
	
}
