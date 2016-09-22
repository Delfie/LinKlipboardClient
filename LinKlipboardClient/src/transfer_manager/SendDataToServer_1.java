package transfer_manager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import server_manager.LinKlipboard;

public class SendDataToServer_1 extends Transfer{

	private ObjectOutputStream out;
	
	
	
	
	// 도연 히스토리 테스트
	private LinKlipboardClient client;

	/** FileSendDataToServer_1 생성자 */
	public SendDataToServer_1(LinKlipboardClient client) {
		super();
		this.client = client;
		this.start();
	}
	
	
	
	/** SendDataToServer_1 생성자 */
	public SendDataToServer_1() {
		super();
		this.start();
	}
	
	/** 서버와의 연결을 위한 소켓과 스트림 설정 */
	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboard.FTP_PORT);
			// 스트림 설정
			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 열려있는 소켓과 스트림을 모두 닫는다. */
	@Override
	public void closeSocket() {
		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		setConnection();
		try {
			Contents sendContents = ClipboardManager.readClipboard(); // 전송할 객체를 시스템 클립보드로부터 가져옴
			
			
			
			// 도연 히스토리 테스트
			client.getHistory().addSharedContnestsInHistory(sendContents);
			
			
			
			out.writeObject(sendContents); // Contents 객체 전송
			
			closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
		}
	}
}
