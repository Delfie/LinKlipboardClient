package start_manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import client_manager.LinKlipboardClient;
import contents.Contents;
import server_manager.LinKlipboard;
import transfer_manager.Transfer;

public class GetTotalHistoryFromServer extends Transfer {

	private ObjectInputStream in;

	private Vector<Contents> historyInServer; // 서버로부터 받을 Vector<Contents>

	/** GetTotalHistoryFromServer 생성자 */
	public GetTotalHistoryFromServer(LinKlipboardClient client) {
		super(client);
		this.start();
	}

	/** 서버와의 연결을 위한 소켓과 스트림 설정 */
	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
			// 스트림 설정
			in = new ObjectInputStream(socket.getInputStream());
			System.out.println("[GetTotalHistoryFromServer] 연결 설정 끝");

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
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();
		// 서버로부터 Vector<Contents>를 받아온다.
		try {
			System.out.println("[GetTotalHistoryFromServer] Vector<Contents> 수신 전");
			historyInServer = (Vector<Contents>) in.readObject();
			System.out.println("[GetTotalHistoryFromServer] Vector<Contents> 수신 후");
			System.out.println("[GetTotalHistoryFromServer]" + historyInServer.get(0).getType());

			// 클라이언트 히스토리에 세팅해준다.
			LinKlipboardClient.setHistory(historyInServer);

			closeSocket();

			System.out.println("[GetTotalHistoryFromServer] 클라이언트 히스토리 초기화 완료");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			closeSocket();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
			return;
		}
	}
}
