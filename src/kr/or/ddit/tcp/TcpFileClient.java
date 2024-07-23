package kr.or.ddit.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TcpFileClient {
/*
 	클라이언트는 서버에 접속하여 파일을 요청 후 서버가 보내주는 파일을 다운 받는다.
 */
	private Socket socket;
	private FileOutputStream fos;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Scanner scan;
	
	public TcpFileClient() {
		scan = new Scanner(System.in);
	}
	
	public void start() {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		
		try {
			socket = new Socket("192.168.144.41", 7777);

			//소켓 접속이 성공하면 받고 싶은 파일명을 전송한다.
			System.out.println("파일명 >>> ");
			String fileName = scan.next();
			
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(fileName);
			
			// 서버로부터 파일에 대한 결과를 받아온다.
			dis = new DataInputStream(socket.getInputStream());
			String resultMsg = dis.readUTF();
			
			if(resultMsg.equals("OK")) { // == 파일이 있음 
				File downDir = new File("d:/D_Other/down_files");
				
				if(!downDir.exists()) {
					downDir.mkdir();
				}
				
				File file = new File(downDir, fileName);
				
				fos = new FileOutputStream(file);
				
				bis = new BufferedInputStream(socket.getInputStream());
				bos = new BufferedOutputStream(fos);
				
				int data = 0;
				while((data= bis.read()) != -1) {
					bos.write(data);
				}
				System.out.println("파일 다운로드 완료...");
			} else {
				System.out.println(resultMsg);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} finally {
			
			try {
				
				bis.close();
				bos.close();
				socket.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String[] args) {
		new TcpFileClient().start();
	}
}
