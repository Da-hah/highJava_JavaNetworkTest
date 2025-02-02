package kr.or.ddit.tcp;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpChatClient {
	public static void main(String[] args) throws IOException {
		
		Socket socket = new Socket("192.168.144.40", 7777);
		
		System.out.println("챗서버에 연결되었습니다.");
		Sender sender = new Sender(socket);
		sender.start();
		
		Receiver recv = new Receiver(socket);
		recv.start();
		
	}
}
