package guyue.hu;

import java.io.IOException;
import java.net.*;

public class TankServer {
	public static final int TCP_PORT = 10000;
	
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			while(true) {
				Socket s = ss.accept();
System.out.println("A client connected! Address-" + s.getInetAddress() + " port-" + s.getPort());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
