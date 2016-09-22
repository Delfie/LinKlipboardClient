package transfer_manager;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class FileReceiveDataToServer_1 extends Transfer {

	// 파일을 읽고 쓰기위한 파일 스트림 설정
	private FileOutputStream fos;
	private DataInputStream dis;

	// 전송받을 파일의 경로
	private String receiveFilePath;

	/** FileReceiveDataToServer 생성자 */
	public FileReceiveDataToServer_1(LinKlipboardClient client) {
		super(client);
		this.start();
	}
	
	/** FileReceiveDataToServer 생성자 */
	public FileReceiveDataToServer_1(LinKlipboardClient client, String fileName) {
		super(client);
		this.receiveFilePath = LinKlipboard.fileReceiveDir + "\\" + fileName;
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboard.FTP_PORT);
			// 스트림 설정
			dis = new DataInputStream(socket.getInputStream()); // 바이트 배열을 받기 위한 데이터스트림 생성
			System.out.println("[FileReceiveDataToServer] 연결 설정 끝");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void closeSocket() {
		try {
			dis.close();
			fos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		setConnection();

		try {
			client.initDir();
			
			byte[] ReceiveByteArrayToFile = new byte[LinKlipboard.byteSize]; // 바이트 배열 생성

			System.out.println("[FileReceiveDataToServer] 지정 경로: " + receiveFilePath);
			fos = new FileOutputStream(receiveFilePath); // 지정한 경로에 바이트 배열을 쓰기위한 파일 스트림 생성

			int EndOfFile = 0; // 파일의 끝(-1)을 알리는 변수 선언
			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) { // ReceiveByteArrayToFile의 크기인 1024바이트 만큼 DataInputStream에서 바이트를 읽어 바이트 배열에 저장, EndOfFile에는 1024가 들어있음
																			// DataInputStream에서 바이트를 다 읽어올 때(EndOfFile=-1 일 때)까지 반복
				fos.write(ReceiveByteArrayToFile, 0, EndOfFile); // ReceiveByteArrayToFile에 들어있는 바이트를 0~EndOfFile=1024 만큼 FileOutputStream으로 보냄
			}

			closeSocket();
			
			setFileInClipboard(receiveFilePath);
			System.out.println("[FileReceiveDataToServer] 클립보드 삽입 완료");
		} catch (IOException e) {
			closeSocket();
			return;
		}
	}

	@Override
	public void callServlet() {
		// TODO Auto-generated method stub

	}

}
