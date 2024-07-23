package kr.or.ddit.udp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpFileSender {
	private DatagramSocket ds;
	private DatagramPacket dp;
	
	private InetAddress receiveAddr;
	private int port;
	private File file;
	
	public UdpFileSender(String receiveIp, int port) {
		try {
			receiveAddr = InetAddress.getByName(receiveIp);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.port = port;
		
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		file = new File("D:/D_setting/A_TeachingMaterial/05_JQuery/VSC/image/mang/mang5.jpg"); //보낼 데이터
		
		if(!file.exists()) {
			System.out.println("해당 파일이 존재하지 않습니다.");
			System.exit(0);
		}
	}
	
	public void start() {
		long fileSize = file.length();
		long totalReadBytes = 0;
		
		long startTime = System.currentTimeMillis();
		
		try {
			
			sendData("start".getBytes()); // 전송 시작을 알려주기 위한 문자열 전송
			
			sendData(file.getName().getBytes()); //파일명 전송
			
			sendData(String.valueOf(fileSize).getBytes()); //총 파일 크기  전송
			
			
			///////////////////////////////////////////////////////////////
			
			FileInputStream fis = new FileInputStream(file);
			
			byte[] buffer = new byte[1000]; // 버퍼의 사이즈가 1000
			
			while(true) {
				try {
					// 패킷 전송간의 시간 간격을 주기 위해서...
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				int readBytes = fis.read(buffer, 0, buffer.length);
				
				if(readBytes == -1) { // 다 읽은 경우 ...
					break;
				}
				
				sendData(buffer, readBytes); // 읽어온 바이트 수 계산>  파일 데이터 전송하기
				
				totalReadBytes += readBytes;
				
				System.out.println("진행 상태 : " + totalReadBytes + "/" + fileSize
									+ " Byts(s) (" + (totalReadBytes * 100 / fileSize) + "%)");
			}
			
			long endTime = System.currentTimeMillis();
			long diffTime = endTime - startTime;
			double transferSpeed = fileSize / diffTime;
			
			System.out.println("걸린 시간 : " + diffTime + " (ms)");
			System.out.println("평균 전송속도 : " + transferSpeed + " (Bytes/ms)");
			System.out.println("전송 완료 ...");
			
			fis.close();
			
				
		} catch(IOException ex) {
			
		}
	}
	
	/**
	 * 바이트개열 전송하기 위한 메서드
	 * @param buffer 전송할 데이터
	 * @param readBytes 실제 데이터 사이즈
	 */
	private void sendData(byte[] buffer, int readBytes) {
		try {
			dp = new DatagramPacket(buffer, readBytes, receiveAddr, port);
			ds.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 바이트배열 전송하기 위한 메서드
	 * @param buffer 전송할 데이터
	 */
	private void sendData(byte[] buffer) {
		sendData(buffer, buffer.length);
	}
	
	public static void main(String[] args) {
		new UdpFileSender("192.168.144.40", 8888).start();
	}
}
