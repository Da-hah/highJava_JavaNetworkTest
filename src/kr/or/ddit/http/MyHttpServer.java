package kr.or.ddit.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.StringTokenizer;


/**
 * 간단한 웹서버 예제
 * @author PC-13 
 *
 */
public class MyHttpServer {
	private final int port = 80;
	private final String encoding = "UTF-8";
	
	public void start() {
		
		ServerSocket server = null;
		
		try {
			System.out.println("HTTP 서버가 시작되었습니다...");

			server = new ServerSocket(80);
			
			while(true) {

				Socket socket = server.accept();
				
				// Http 핸들러 호출 및 시작(스레드)
				Httphandler handler = new Httphandler(socket);
				handler.start();
				
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * HTTP 요청 처리를 위한 핸들러 스레드
	 * @author PC-13
	 *
	 */
	class Httphandler extends Thread{
		private final Socket socket;
		
		public Httphandler(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			
			BufferedOutputStream bos = null;
			BufferedReader br = null;
			
			try {
				
				bos = new BufferedOutputStream(socket.getOutputStream());
				br = new BufferedReader(
						new InputStreamReader(socket.getInputStream())); // byte 기반의 Stream을 문자 기반으로 변환 
				
				// Request Line
				String reqLine = br.readLine(); // 첫줄은 요청라인 ...
				//System.out.println("Request Line : " + reqLine);
				
				// 요청헤더 정보 파싱하기
				StringBuilder sb = new StringBuilder();
				
				while(true) {
					String str = br.readLine();
					
					if(str.equals("")) { // Empty Line인 경우
						break;
					}
					sb.append(str + "\n");
				}
				
				// sb 안에 있는 헤더정보 
				String reqHeaderStr = sb.toString();
				
				//System.out.println("요청헤더 정보 : " + reqHeaderStr);
				
				String reqPath = ""; // 요청경로를 담을 변수 생성 
				
				// 요청 페이지 정보 가져오기
				StringTokenizer st = new StringTokenizer(reqLine); 
				while(st.hasMoreTokens()) {
					String token = st.nextToken();
					
					if(token.startsWith("/")) {
						reqPath = token;
					}
				}
				/////////////////////////////////////////////////////////////
				
				// URL 디코딩 처리하기(한글깨짐 문제 해결하기 위해)
				reqPath = URLDecoder.decode(reqPath, encoding);
				
				String filePath = "./WebContent" + reqPath;
				
				System.out.println("filePath : " + filePath);
				
				// 해당 파일이름을 이용하여 Content-Type 정보 추출하기 
				String ContentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
				
				// css 파일인 경우 인식이 잘 안되서 추가함
				if(ContentType == null && filePath.endsWith(".css")) {
					ContentType = "text/css";
				}
				System.out.println("ContentType : " + ContentType);
				
				File file = new File(filePath);
				if(!file.exists()) {
					// 에러페이지 생성
					makeErrorPage(bos, 404, "Not Found");
					return;
				}
				
				byte[] body = makeResponseBody(filePath);
				
				byte[] header = makeResponseHeader(body.length, ContentType);
				
				///////////////////////////////////////////////////////////////
				
				bos.write(header); // 헤더 전송
				
				// 응답 내용 보내기 전에 반드시 Empty Line 보내야 한다. 
				bos.write("\r\n\r\n".getBytes());
				
				bos.write(body); // 응답 내용 전송 
				
				bos.flush(); // 강제 방출 
				
			} catch (IOException ex) {
				ex.printStackTrace();
				// TODO: handle exception
			} finally {
				try {
					socket.close(); // 소켓 닫기 (연결 끊기)
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * 여러 페이지 출력
		 * @param out 에러페이지 전송할 스트림 객체 
		 * @param statusCode 에러코드
		 * @param statusMsg 에러메시지
		 */
		private void makeErrorPage(BufferedOutputStream out, int statusCode, String statusMsg) {
			String statusLine = "HTTP/1.1" + " " + statusCode + " " + statusMsg;
			
			try {
				out.write(statusLine.getBytes());
				out.flush();
				
			} catch (IOException ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
		}

		/**
		 * 응답헤더 생성하기
		 * @param length 응답 내용 크기
		 * @param contentType 마임타입
		 * @return 응답헤더 바이트 배열
		 */
		private byte[] makeResponseHeader(int length, String contentType) {
			
			// 응답 첫 줄은 Status Line
			String header = "HTTP/1.1 200 OK\r\n"
					+ "Server: MyHttpServer 1.0\r\n"
					+ "Content-length: "+length+"\r\n"
					+ "Content-Type: "+contentType+"; charset =UTF-8";
			
			return header.getBytes(); // 문자열 데이터를 byte[]로 변환해서 리턴
		}

		/**
		 * 응답내용 생성하기
		 * @param file 응답으로 사용할 파일경로
		 * @return 응답내용 바이트 배열 
		 */
		private byte[] makeResponseBody(String filePath) {
			FileInputStream fis = null;
			byte[] data = null;
			
			try {
				File file = new File(filePath);
				
				data = new byte[(int) file.length()];
				
				fis = new FileInputStream(file);
				
				fis.read(data);
				
				
			} catch (IOException ex) {
				// TODO: handle exception
				ex.printStackTrace();
			
			} finally {
				
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return data;
		}
	}
	
	public static void main(String[] args) {
		new MyHttpServer().start();
	}
}
