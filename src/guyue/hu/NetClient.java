package guyue.hu;

import java.io.*;
import java.net.*;

public class NetClient {
	private static int UDP_PORT_START = 11000;
	private int udpPort;
	private Socket s = null;
	private TankClient tc;
	
	public NetClient(TankClient tc) {
		 udpPort = UDP_PORT_START ++;
		this.tc = tc;
	}

	public void connect(String host, int port) {
		try {
			s = new Socket(host, port);
//			DatagramSocket ds = new DatagramSocket(udpPort);
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
	}

}
