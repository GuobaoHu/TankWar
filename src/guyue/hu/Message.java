package guyue.hu;

import java.io.DataInputStream;
import java.net.DatagramSocket;

/**
 * @author guyue
 * @date 2017年12月18日 下午9:20:32
 * @class describ:
 */
public interface Message {
	public static final int MSG_TANK_NEW = 1;
	public static final int MSG_TANK_MOVE = 2;
	public static final int MSG_BULLET_NEW = 3;
	
	public void sendMsg(DatagramSocket ds, String ip, int port);
	
	public void parse(DataInputStream dis);
}
