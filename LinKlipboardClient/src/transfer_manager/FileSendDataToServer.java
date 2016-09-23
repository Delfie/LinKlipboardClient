package transfer_manager;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server_manager.LinKlipboard;

public class FileSendDataToServer extends Transfer {

	// 상대방에게 바이트 배열을 주고 받기위한 데이터 스트림 설정
	private DataOutputStream dos;
	private FileInputStream fis;

	/** FileSendDataToServer 생성자 */
	public FileSendDataToServer() {
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
			dos = new DataOutputStream(socket.getOutputStream()); // 바이트 배열을 보내기 위한 데이터스트림 생성

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
			dos.close();
			fis.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();

		try {
			byte[] sendFileTobyteArray = new byte[LinKlipboard.byteSize]; // 바이트 배열 생성
			int EndOfFile = 0; // 파일의 끝(-1)을 알리는 변수 선언

			// 희정
			fis = new FileInputStream(CommunicatingWithServer.getSendFile()); // 파일에서 읽어오기 위한 스트림 생성

			/*
			 * sendFileTobyteArray의 크기인 1024바이트 만큼 파일에서 읽어와 바이트 배열에 저장, EndOfFile에는 1024가 들어있음 파일의 끝에 다다를때(EndOfFile=-1 일 때)까지 반복
			 */
			while ((EndOfFile = fis.read(sendFileTobyteArray)) != -1) {
				// sendFileTobyteArray에 들어있는 바이트를 0~EndOfFile=1024 만큼 DataOutputStream으로 보냄
				dos.write(sendFileTobyteArray, 0, EndOfFile);
			}

			closeSocket();

		} catch (IOException e1) {
			closeSocket();
			return;
		}
	}
}
