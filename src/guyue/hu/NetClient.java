package guyue.hu;

import java.io.IOException;
import java.net.*;

public class NetClient {
	public void connect(String host, int port) {
		try {
			Socket s = new Socket(host, port);
System.out.println("Connect server!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
