package client_manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import contents.Contents;
import server_manager.LinKlipboard;
import user_interface.SettingPanel;

/**
 * 서버가 전송하는 Contents를 받는 클래스 스레드를 상속받아 서버에서 전달해주는 메세지를 기다린다.
 */
public class RceiveContents extends Thread {
	private ServerSocket listener;
	private Socket socket;
	private ObjectInputStream in; // Contents를 받기 위한 스트림
	
	private LinKlipboardClient client;
	
	public RceiveContents(LinKlipboardClient client) {
		this.client = client;
	}

	/** 서버의 연결을 기다리는 소켓 설정 */
	public void waitToServer() {
		try {
			System.out.println("[ReceiveContents] port번호: " + LinKlipboardClient.getPortNum());
			
			listener = new ServerSocket(LinKlipboardClient.getPortNum());
			socket = listener.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 서버와의 연결을 위한 스트림 설정 */
	public void setConnection() {
		try {
			in = new ObjectInputStream(socket.getInputStream()); // 스트림 설정
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[ReceiveContents] 연결 설정 끝");
	}

	@Override
	public void run() {
		while (true) {
			waitToServer();
			setConnection();
			try {
				client.latestContents = (Contents) in.readObject(); // Contents 객체수신
				int latestContentsType = client.latestContents.getType(); // Contents 객체의 타입

				System.out.println("최근에 받은 컨텐츠의 타입:" + latestContentsType);

				if (latestContentsType == LinKlipboard.NULL) {
					// sharer을 확인
					StringTokenizer tokens = new StringTokenizer(client.latestContents.getSharer(),
							LinKlipboard.RESPONSE_DELIMITER);
					String inoutClientInfo = tokens.nextToken();

					// join이면 Vector otherClients에 추가
					if (inoutClientInfo.equals("join")) {
						String inClientNickname = tokens.nextToken();

						System.out.println("join한 클라이언트의 닉네임:" + inClientNickname);

						client.getOtherClients().add(inClientNickname);
						client.getConnectionPanel().updateAccessGroup();
					}
					// exit이면 join이면 Vector otherClients에서 제거
					if (inoutClientInfo.equals("exit")) {
						String outClientNickname = tokens.nextToken();
						for (int i = 0; i < client.getOtherClients().size(); i++) {
							if (client.getOtherClients().get(i).equals(outClientNickname)) {
								client.getOtherClients().remove(i);
								client.getConnectionPanel().updateAccessGroup();
								return;
							}
						}
					}
				} else {
					client.getHistory().addSharedContentsInHistory(client.latestContents); // 공유받은 최신Contents를 history에 추가

					if (SettingPanel.notification) {// 도연 추가
						if (latestContentsType == LinKlipboard.FILE_TYPE) {
							client.getMain().getTrayIcon().showMsg("Shared <File> Contents");
						} else if (latestContentsType == LinKlipboard.STRING_TYPE) {
							client.getMain().getTrayIcon().showMsg("Shared <Text> Contents");
						} else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
							client.getMain().getTrayIcon().showMsg("Shared <Image> Contents");
						} else {
							System.out.println("[LinKlipboardClient_알림] File, String, Image 어디에도 속하지 않음");
						}
					}
				}

			} catch (ClassNotFoundException e) {
				this.start();
			} catch (IOException e) {
				this.start();
			}
		}
	}
}