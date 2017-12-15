package guyue.hu;

import java.io.*;
import java.net.*;

public class NetClient {
	private static int UDP_PORT_START = 11000;
	private int udpPort = UDP_PORT_START++;
	private Socket s = null;

	public void connect(String host, int port) {
		try {
			s = new Socket(host, port);
			DatagramSocket ds = new DatagramSocket(udpPort);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			System.out.println("Connect server!");
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
