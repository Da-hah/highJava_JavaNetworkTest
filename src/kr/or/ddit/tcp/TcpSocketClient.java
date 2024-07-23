package kr.or.ddit.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpSocketClient {
	public static void main(String[] args) throws IOException {
		String serverIp = "127.0.0.1";
		// 자기 자신 컴퓨터를 나타내는 방법 >> IP주소 혹은 localhost를 써도됨
		// IP 주소 : 127.0.0.1
		// host name : localhost
		
		System.out.println(serverIp + " 서버에 접속 중입니다.");
		
		// 소켓을 생성해서 서버에 연결을 요청한다.
		Socket socket = new Socket(serverIp, 7777);
		
		// 연결이 되면 이 후 부분이 실행된다. 
		System.out.println("서버와 연결 되었습니다.");
		
		// 서버에서 보내온 메시지 받기
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		System.out.println("서버로부터 받은 메시지 : " + dis.readUTF());
		
		System.out.println("클라이언트 소켓 연결 종료");
		
		dis.close();
		socket.close();
	}
}
