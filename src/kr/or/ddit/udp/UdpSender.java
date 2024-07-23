package kr.or.ddit.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UdpSender {
	private DatagramSocket ds;
	private DatagramPacket dp;
	
	private byte[] msg; // 데이터 송수신을 위한 바이트배열
	
	public UdpSender(int port) {
		try {
			// 메시지 수신을 위한 포트번호 설정
			ds = new DatagramSocket(port); // 번호를 설정하지 않으면 랜덤으로 포트번호 생성
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void start() {
		while(true) {
			// 데이터 수신하기 위한 패킷을 생성한다.
			msg = new byte[1];
			
			dp = new DatagramPacket(msg, 1);
			
			System.out.println("패킷 수신을 대기 중 ... ");
			try {
				ds.receive(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("패킷 수신 완료 ... ");
			
			// 수신한 패킷으로부터 송신자의 IP주소 및 포트번호 가져온다.
			InetAddress addr = dp.getAddress();
			int port = dp.getPort();
			
			// 현재 시간을 시분초 형태([hh:mm:ss])로 전송한다.
			SimpleDateFormat sdf = new SimpleDateFormat("[hh:mm:ss]");
			String time = sdf.format(new Date());
			msg = time.getBytes(); // 시간데이터를 바이트 배열로 반환
			
			// 패킷을 생성해서 송신자에게 전송한다. 
			dp = new DatagramPacket(msg, msg.length, addr, port);
			try {
				ds.send(dp);
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			System.out.println("현재 시간 전송 완료... ");
		}
	}
	
	public static void main(String[] args) {
		new UdpSender(8888).start();;
	}
}
