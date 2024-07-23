package kr.or.ddit.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpReceiver {
	private DatagramSocket ds;
	private DatagramPacket dp;
	
	private byte[] msg;
	
	public UdpReceiver() {
		msg = new byte[100];
		
		try {
			// 포트 번호를 명시하지 않으면, 이용가능한 임의의 포트번호로 할당됨
			ds = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() throws IOException {
		InetAddress addr = InetAddress.getByName("192.168.144.41");
		
		dp = new DatagramPacket(msg, 1, addr, 8888); // 상대방에게 ip주소와 포트번호를 알려주기 위함 
		ds.send(dp);
		
		///////////////////////////////////////////////////////////////
		
		dp = new DatagramPacket(msg, msg.length);
		ds.receive(dp);
		
		System.out.println("수신한 현재 시간 정보 => " + new String(dp.getData()));
		
		ds.close(); // 소켓종료
	}
	
	public static void main(String[] args) throws Exception{
		new UdpReceiver().start();
	}
}
