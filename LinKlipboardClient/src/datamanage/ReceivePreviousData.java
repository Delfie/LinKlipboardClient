package datamanage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import server_manager.LinKlipboard;
import transfer_manager.FileReceiveDataToServer;

public class ReceivePreviousData {
	private Contents previousData; // 클립보드에 삽입할 데이터
	private History history; // 히스토리
	
	private int dataType; 
	private int listIndex;
	
	private FileContents fileContents;
	private FileReceiveDataToServer fileReceive;
	
	/** ReceivePreviousData 생성자 */
	public ReceivePreviousData() {
		System.out.println("디폴트 ReceivePreviousData 생성자 호출");
	}
	
	/** ReceivePreviousData 생성자 
	 * @param history 사용자가 가지고 있던 history 정보
	 * @param listIndex history에서 받기를 원하는 Contents의 index값 */
	public ReceivePreviousData(History history, int listIndex) {
		this.history = history;
		this.listIndex = listIndex;
		this.previousData = history.getRequestContents(listIndex); 
		this.dataType = previousData.getType();
		
		receiveDataInClipboard();
	}
	
	/** 요청한 Contents를 시스템 클립보드에 넣음 */
	public void receiveDataInClipboard() {
		switch(dataType) {
		case LinKlipboard.STRING_TYPE :
		case LinKlipboard.IMAGE_TYPE :
			ClipboardManager.writeClipboard(previousData, dataType); // 수신받을 데이터를 클립보드에 삽입
			break;
		case LinKlipboard.FILE_TYPE:
			fileContents = (FileContents)previousData; // 수신받을 파일의 이름을 알아내기 위해 Contents를 FileContents로 캐스팅
			fileReceive = new FileReceiveDataToServer(fileContents.getFileName()); // 파일이름을 인자로 전달해 FileReceiveDataToServer 객체 생성
			requestReceiveData(); // 서버에 index 보내고 응답 받고 파일 받아서 클립보드에 삽입
			break;
		}
	}

	/** 파일 데이터 수신 메소드(FileReceiveDataToServerInHistory 서블릿 호출) */
	public void requestReceiveData() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/FileReceiveDataToServerInHistory");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹이름)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName() + "&";
			header += "index=" + listIndex + "\r\n";
			
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
				fileReceive.start();
			}
			//delf
			
			//exceptionHandling(this.response); // heee
			
			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
