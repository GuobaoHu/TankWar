package guyue.hu;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class TankServer {
	public static final int TCP_PORT = 10000;
	private List<Client> clients = new ArrayList<>();

	public static void main(String[] args) {
		new TankServer().launch();
	}
	
	public void launch() {
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while (true) {
				Socket s = ss.accept();
				System.out.println("A client connected! Address-" + s.getInetAddress() + " port-" + s.getPort());
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				Client c = new Client(s.getInetAddress().getHostAddress(), udpPort);
				clients.add(c);
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
