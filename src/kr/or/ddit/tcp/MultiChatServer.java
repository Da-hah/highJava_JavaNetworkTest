package kr.or.ddit.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultiChatServer {
	// 대화명을 키, 해당 소켓을 값으로 하는 Map 객체 변수 선언 
	Map<String, Socket> clients;
	
	public MultiChatServer() {
		clients = Collections.synchronizedMap(new HashMap<String, Socket>());
	}
	
	public void serverStart() {
		ServerSocket server = null;
		Socket socket = null;
		
		try {
			server = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");
			
			while(true) {
				// 클라이언트의 접속을 대기
				socket = server.accept();
				
				System.out.println("[" + socket.getInetAddress() + " : " 
								+ socket.getPort() +"] 에서 접속하셨습니다.");
				
				// 사용자가 보내준 메시지를 받아서 처리하는 스레드 생성 및 실행
				ServerRecevier sr = new ServerRecevier(socket);
				sr.start();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 대화방, 즉 Map에 저장된 모든 접속한 사람들에게 안내메시지를 전송하기 위한 메서드  
	 * @param msg 전송할 메시지 
	 */
	public void sendMessage(String msg) {
		
		Iterator<String> it = clients.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				String name = it.next();
				
				// 해당 사용자에게 메시지 보내기 
				new DataOutputStream(clients.get(name).getOutputStream()).writeUTF(msg);
				
				
			} catch (IOException ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 대화방, 즉 Map에 저장된 모든 접속한 사람들에게 채팅메시지를 전송하기 위한 메서드  
	 * @param msg 채팅메시지
	 * @param from 메시지 보낸 대화명
	 */
	public void sendMessage(String msg, String from) {
		sendMessage("["+from + "] " + msg);
	}
	
	/**
	 * 서버에서 클라이언트로부터 수신한 메시지를 처리하기 위한 클래스
	 * Inner 클래스로 정의함.(Inner클래스에서는 부모클래스의 멤버들을 직접 접근할 수 있음)
	 * @author PC-13
	 *
	 */
	class ServerRecevier extends Thread{ // 내부클래스
		
		private Socket socket;
		private DataInputStream dis;
		private String name;
		
		public ServerRecevier(Socket socket) {
			this.socket = socket;
			
			try {
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				// 서버에서는 클라이언트가 보내온 최초의 메시지, 즉 대화명을 수신해야 한다.
				name = dis.readUTF();
				
				// 대화명을 받아서 다른 모든 클라이언트들에게 대화방 참여 메시지를 보낸다.
				sendMessage("#" + name +" 님이 입장했습니다");
				
				// 대화명을 사용하여 소켓객체를 Map에 저장한다.
				clients.put(name, socket);
				System.out.println("현재 서버 접속자 수는 " + clients.size() +"명 입니다.");
				
				// 이 이후의 처리는 반복문으로 처리한다.
				// 클라이언트가 보내온 메시지(대화내용)를 접속한 다른 모든 클라이언트들에게 보내준다. 
				while(dis != null) {
					sendMessage(dis.readUTF(), name);
				}
			} catch (IOException ex) {
				// TODO: handle exception
				ex.printStackTrace();
			} finally {
				// 이 부분이 실행된다는 것은 클라이언트와의 접속에 문제가 생겼다는 의미이다.
				// 그래서 접속종료 처리를한다.
				sendMessage(name + " 님이 나가셨습니다.");
				
				// Map에서 해당 사용자를 삭제한다.
				clients.remove(name);    
				
				System.out.println("[" + socket.getInetAddress() + " : " 
						+ socket.getPort() +"] 에서 종료하셨습니다.");  
				
				System.out.println("현재 서버 접속자 수는 " + clients.size() +"명 입니다.");    
			}
		}
	}
	public static void main(String[] args) {
		new MultiChatServer().serverStart();
	}
}

