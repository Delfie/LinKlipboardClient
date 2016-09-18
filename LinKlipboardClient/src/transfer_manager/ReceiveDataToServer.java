package transfer_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
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

public class ReceiveDataToServer extends Thread {
	
	private Socket socket; // 서버와 연결할 소켓
	private String ipAddr = LinKlipboard.SERVER_IP;
	private final static int portNum = LinKlipboard.FTP_PORT;
	
	private String response; // 서버로부터 받은 응답 정보
	private LinKlipboardClient client;

	private ObjectInputStream in;
	private ResponseHandler responseHandler; // 응답에 대한 처리

	/** ReceiveDataToServer 생성자 */
	public ReceiveDataToServer() {
	}
	
	/** ReceiveDataToServer 생성자 */
	public ReceiveDataToServer(LinKlipboardClient client) {
		this.client = client;
	}
	
	/** 데이터 수신 메소드 */
	public void requestReceiveData() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReceiveDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹이름)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();
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
	 * @param response
	 *            클라이언트 요청에 대한 서버의 응답
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
			in = new ObjectInputStream(socket.getInputStream());
			System.out.println("연결 설정 끝"); //delf

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
			Contents receiveContents = (Contents) in.readObject(); // Contents
																	// 객체 수신
			ClipboardManager.writeClipboard(receiveContents, receiveContents.getType()); // 수신한
																							// 시스템
																							// 클립보드에
																							// 삽입
			System.out.println("데이터 받긴 받음"); //delf
			in.close();
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}