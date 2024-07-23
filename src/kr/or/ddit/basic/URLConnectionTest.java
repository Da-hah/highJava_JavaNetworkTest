package kr.or.ddit.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class URLConnectionTest {
	public static void main(String[] args) throws IOException {
		
		// URLConnection => 애플리케이션과 URL 간의 통신연결을 위한 추상클래스
		
		// 특정 서버 (ex : 네이버)에 접속하여 대문페이지 가져오기
		URL url = new URL("https://www.naver.com/index.html");
		 
		// URLConnection 객체 생성하기
		URLConnection urlConn = url.openConnection();
		
		// 기본적인 헤더정보 출력하기
		System.out.println("Content-Type : " + urlConn.getContentType());
		System.out.println("ContentEncoding : " + urlConn.getContentEncoding());
		
		// 응답데이터 출력하기 
		System.out.println("Content : " + urlConn.getContent()); // Stream 객체 
		System.out.println();
		
		// 전체 Header 정보 출력하기
		Map<String, List<String>> headerMap = urlConn.getHeaderFields();
		
		Iterator<String> iter = headerMap.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + " : " +headerMap.get(key));
		}
		System.out.println("------------------------------------------");
		
		InputStream is = (InputStream)urlConn.getContent();
		
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		
		int data =0;
		while((data = isr.read()) != -1) {
			System.out.print((char)data);
		}
	}
}
