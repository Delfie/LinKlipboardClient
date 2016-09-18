package transfer_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import server_manager.LinKlipboard;

public class SendDataToServer extends Thread {

	private Socket socket; // 서버와 연결할 소켓
	private String ipAddr = LinKlipboard.SERVER_IP;
	private final static int portNum = LinKlipboard.FTP_PORT;
	
	private String response; // 서버로부터 받은 응답 정보
	private LinKlipboardClient client;
	
	private ObjectOutputStream out;
	private ResponseHandler responseHandler; // 응답에 대한 처리
	
	/** SendDataToServer 생성자 */
	public SendDataToServer() {
	}	
	
	/** SendDataToServer 생성자 */
	public SendDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** 데이터 전송 메소드 */
	public void requestSendData() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹이름, (파일명 존재시 파일명전송))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String groupName = "groupName=" + LinKlipboardClient.getGroupName();
			// String fileName = "fileName=" + null; //수정. 파일명이 존재할 때 파일명을 넣는다.
			String header = groupName;
			// + "&" + fileName;
			System.out.println("보낼 전체 데이터 확인" + header); //delf

			bout.write(header);
			bout.flush();
			bout.close();

			// 서버로부터 받을 데이터(응답정보)
			/*
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;
			
			if ((response = bin.readLine()) != null) {
				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
				this.response = response;
			}
			System.out.println("서버로부터의 응답 데이터 확인: " + this.response); //delf
			bin.close();			
			
			exceptionHandling(this.response); 
			*/ 
			//heee
			
			String tmp = null;
			int response = LinKlipboard.NULL;
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((tmp = bin.readLine()) != null) {
				response = Integer.parseInt(tmp);
			}
			System.out.println("서버로부터의 응답 데이터 확인: " + response);
			
			if (response == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("소켓 연결");
				this.start();
			}
			//delf

			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 예외 처리
	 * @param response 클라이언트 요청에 대한 서버의 응답
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, client);
		responseHandler.responseHandlerForTransfer();
	}

	/** 서버와의 연결을 위한 소켓과 스트림 설정 */
	public void setConnection() {
		try {
			// 소켓 접속 설정
			socket = new Socket(ipAddr, portNum);
			// 스트림 설정
			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();
		try {
			Contents sendContents = ClipboardManager.readClipboard(); // 전송할 객체를
																		// 시스템
																		// 클립보드로부터
																		// 가져옴
			out.writeObject(sendContents); // Contents 객체 전송
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}