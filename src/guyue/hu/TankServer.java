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
	private List<Client> clients = new ArrayList<>();

	public static void main(String[] args) {
		new TankServer().launch();
	}
	
	public void launch() {
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
				dos.writeInt(id ++);//�˴��������������߳�ͬʱ���ʵ�
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
	}

	private class Client {
		private String ip;
		private int udpPort;

		public Client(String ip, int udpPort) {
			this.ip = ip;
			this.udpPort = udpPort;
		}
	}

}
