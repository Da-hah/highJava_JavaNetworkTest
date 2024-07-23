package kr.or.ddit.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpFileServer {
/*
 	서버는 접속한 클라이언트가 요청한 파일이 있는지 확인 후 존재사면 해당 파일을 클라이언트에게 전송한다.
 */
	public void start() {
		ServerSocket server = null;
		
		try {
			
			server = new ServerSocket(7777);
			System.out.println("파일서버 준비 완료...");
			
			while(true) {
				Socket socket = server.accept();
				
				// 파일 전송 스레드 생성 및 실행
				FileSender vsender = new FileSender(socket);
			}
			
		} catch (IOException ex) {
			// TODO: handle exception
			ex.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class FileSender extends Thread{
		private Socket socket;
		private FileInputStream fis;
		private DataInputStream dis;
		private DataOutputStream dos;
		
		public FileSender(Socket socket) {
			this.socket = socket;
			
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}
		@Override
		public void run() {
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			
			try {
				String downDir = "d:/D_Oher/"; //이미지가 있는 폴더 위치
				File file = new File(downDir + dis.readUTF());
				System.out.println("요청 파일 존재 여부 체크 중...");
				
				if(!file.exists()) {
					System.out.println("요청 파일(" + file.getName() +") 존재하지 않습니다.");
					dos.writeUTF("요청파일(" + file.getName() +") 존재하지 않습니다.");
				} else {
					dos.writeUTF("OK"); // 요청 파일 존재함을 알려줌...
					System.out.println("요청 파일(" + file.getName() +") 전송 시작...");
					
					fis = new FileInputStream(file);
					
					bis = new BufferedInputStream(fis);
					bos = new BufferedOutputStream(socket.getOutputStream());
					
					int data = 0;
					while ((data = bis.read()) != 1) {
						bos.write(data);
						System.out.println("요청 파일(" + file.getName() +") 전송 완료...");
					}
				}
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} finally {
				try {
					bis.close();
					bos.close();
					dis.close();
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	public static void main(String[] args) {
		new TcpFileServer().start();
	}
}
