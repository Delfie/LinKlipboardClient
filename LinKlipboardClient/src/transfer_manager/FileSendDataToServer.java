package transfer_manager;

import java.awt.HeadlessException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import contents.FileContents;
import server_manager.LinKlipboard;

public class FileSendDataToServer extends Thread {

	private Socket socket; // 서버와 연결할 소켓
	private LinKlipboardClient client;
	
	// 상대방에게 바이트 배열을 주고 받기위한 데이터 스트림 설정
	private DataOutputStream dos;
	private FileInputStream fis;
	
	FileContents extractFile;
	File sendFile;
	
	/** FileSendDataToServer 생성자 */
	public FileSendDataToServer() {
	}	
	
	/** FileSendDataToServer 생성자 */
	public FileSendDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** 파일 데이터 전송 메소드 (FileSendDataToServer 서블릿 호출) */
	public void requestSendData() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/FileSendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹이름, (파일명 존재시 파일명전송))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String groupName = "groupName=" + LinKlipboardClient.getGroupName();

			sendFile = new File(getFilePathInSystemClipboard());
			String fileName = "fileName=" + sendFile.getName();

			String header = groupName + "&" + fileName;
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

	/** 서버와의 연결을 위한 소켓과 스트림 설정 */
	public void setConnection() {
		try {
			// 소켓 접속 설정
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboard.FTP_PORT);
			
			// 스트림 설정
			dos = new DataOutputStream(socket.getOutputStream()); // 바이트 배열을 보내기 위한 데이터스트림 생성

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
			int byteSize = 65536;
			byte[] sendFileTobyteArray = new byte[byteSize]; // 바이트 배열 생성

			fis = new FileInputStream(sendFile); // 파일에서 읽어오기 위한 스트림 생성

			int EndOfFile = 0; // 파일의 끝(-1)을 알리는 변수 선언
			while ((EndOfFile = fis.read(sendFileTobyteArray)) != -1) { // sendFileTobyteArray의 크기인 1024바이트 만큼 파일에서 읽어와 바이트 배열에 저장, EndOfFile에는 1024가 들어있음
																		// 파일의 끝에 다다를때(EndOfFile=-1 일 때)까지 반복
				dos.write(sendFileTobyteArray, 0, EndOfFile); // sendFileTobyteArray에 들어있는 바이트를 0~EndOfFile=1024 만큼 DataOutputStream으로 보냄
			}

			closeSocket();
			
		} catch (IOException e1) {
			closeSocket();
			return;
		}

	}
	
	/** 열려있는 소켓을 모두 닫는다. */
	private void closeSocket() {
		try {
			dos.close();
			fis.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** @return 클립보드에 있는 파일의 경로명 */
	public static String getFilePathInSystemClipboard() {

		try {
			//시스템 클립보드에서 내용을 추출
			Transferable contents = ClipboardManager.getSystmeClipboardContets();
			
			String fileTotalPath = contents.getTransferData(ClipboardManager.setDataFlavor(contents)).toString();
		
			// 경로명만 얻어오기 위해 양 끝의 []를 제거
			return fileTotalPath.substring(1, fileTotalPath.length() - 1);
			
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}