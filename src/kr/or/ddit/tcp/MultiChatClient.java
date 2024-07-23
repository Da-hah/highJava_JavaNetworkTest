package kr.or.ddit.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MultiChatClient {
	
	public void start() {
		Socket socket = null;
		
		try {
			socket = new Socket("192.168.144.41", 7777);
			System.out.println("멀티챗 서버에 접속했습니다.");
			
			// 송신용 스레드 생성 및 실행 
			ClientSender sender = new ClientSender(socket);
			sender.start();
			
			// 수신용 스레드 생성 및 실행 
			ClientReceiver rec = new ClientReceiver(socket);
			rec.start();
			
		} catch (IOException ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
	}
	
	// 메시지 전송을 위한 스레드 클래스 
	class ClientSender extends Thread{
		private DataOutputStream dos;
		private Scanner scan;
		
		public ClientSender(Socket socket) {
			scan = new Scanner(System.in);
			
			try {
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				if(dos != null) {
					// 시작하자마자 자신의 대화명을 서버로 전송하기
					System.out.println("대화명 >> ");
					dos.writeUTF(scan.nextLine());
				}
				
				while(dos != null) {
					// 이제부터는 일반 채팅 메시지 전송하기
					dos.writeUTF(scan.nextLine());
				}
				
			} catch (IOException ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}
	}
	
	// 메시지 수신을 위한 스레드 클래스
	class ClientReceiver extends Thread{
		private DataInputStream dis;
		
		public ClientReceiver(Socket socket) {
			try {
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			while(dis != null) {
				try {
					// 서버로부터 받은 메시지 콘솔에 출력하기
					System.out.println(dis.readUTF());
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new MultiChatClient().start();
	}
}
