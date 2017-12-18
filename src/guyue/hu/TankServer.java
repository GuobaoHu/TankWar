package guyue.hu;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class TankServer {
	/**
	 * ��ÿ���ͻ��˶�һ�޶���ID������ʹ��java.util.UUID
	 */
	private static int id = 100;
	public static final int TCP_PORT = 10000;
	public static final int UDP_PORT = 8888;
	private List<Client> clients = new ArrayList<>();

	public static void main(String[] args) {
		new TankServer().launch();
	}
	
	public void launch() {
		//begin ר���̴߳���UDP
		new Thread(new UDPThread()).start();
		//end
		
		//begin TCP���Ӽ�����
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(TCP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//�ͻ�������
		while (true) {
			Socket s = null;
			try {
				s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
	System.out.println("A client connected! Address:" + s.getInetAddress() + " TCP port:" + s.getPort() + "---UDP port:" + udpPort);
				Client c = new Client(s.getInetAddress().getHostAddress(), udpPort);
				clients.add(c);
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());
				dos.writeInt(id ++);//�˴��������������߳�ͬʱ���ʵ�,�����������ͬ��������
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(s != null) {
					try {
						s.close();
						s = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		//end
	}

	private class UDPThread implements Runnable {
		private DatagramSocket ds = null;
		private byte[] buf = new byte[1024];
		@Override
		public void run() {
			try {
				//begin ���տͻ�������
				ds = new DatagramSocket(UDP_PORT);
				while(ds != null) {
					DatagramPacket dp = new DatagramPacket(buf, buf.length);
					ds.receive(dp);
System.out.println("server has received a DatagramPacket!");
				//end
				//begin ��ÿ���ͻ��˷�������
					for(int i=0; i<clients.size(); i++) {
						Client c = clients.get(i);
						dp.setSocketAddress(new InetSocketAddress(c.getIp(), c.getUdpPort()));
						ds.send(dp);
					}
				//end
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private class Client {
		private String ip;
		private int udpPort;

		public Client(String ip, int udpPort) {
			this.ip = ip;
			this.udpPort = udpPort;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getUdpPort() {
			return udpPort;
		}

		public void setUdpPort(int udpPort) {
			this.udpPort = udpPort;
		}
		
	}

}
