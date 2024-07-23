package kr.or.ddit.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpChatServer {
	public static void main(String[] args) {
	/*
	 	서버 소켓을 만들고, 클라이언트가 접속하면 만들어진 소켓을 사용하여 클라이언트와 데이터를 주고받는다.
	 */
		ServerSocket server = null;
		Socket socket = null;
		
		try {
			server = new ServerSocket(7777);
			System.out.println("채팅서버 대기 중 ... ");
			socket = server.accept();
			System.out.println("클라이언트와 연결되었습니다.");
			Sender sender = new Sender(socket);
			sender.start();
			
			Receiver recv = new Receiver(socket);
			recv.start();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
