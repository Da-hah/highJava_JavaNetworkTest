package kr.or.ddit.tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketServer {
	
	public static void main(String[] args) throws IOException {
		// 소켓이란? 두 호스트 간에 통신을 하기 위한 양 끝단(EndPoint)을 말한다.
		// TCP 소켓 통신을 하기 위해 ServerSocket 객체 생성하기
		ServerSocket server = new ServerSocket(7777); // 포트번호 >> 7777
		System.out.println("서버가 소켓접속을 기다립니다...");
		
		// accept() 메서드는 클라이언트에서 접속 요청이 올 때까지 계속 기다린다.
		// 연결 요청이 오면 Socket객체를 생성해서 클라이언트와의 통신에 사용한다.
		Socket socket = server.accept();
		
		//-----------------------------------------------------
		// 이 이후는 클라이언트의 연결된 후의 작업을 진행하면 된다.
		System.out.println("접속한 클라이언트 정보");
		System.out.println("주소 : " + socket.getInetAddress());
		
		// 클라이언트에 메시지 보내기
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		dos .writeUTF("어서오세요, 반갑습니다.");
		
		System.out.println("클라이언트에 메시지를 보냈습니다.");
		
		dos.close();
		server.close(); // 서버 닫기
		
	}
}
