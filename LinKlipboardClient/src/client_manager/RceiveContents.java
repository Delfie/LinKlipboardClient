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
 * ������ �����ϴ� Contents�� �޴� Ŭ���� �����带 ��ӹ޾� �������� �������ִ� �޼����� ��ٸ���.
 */
public class RceiveContents extends Thread {
	private ServerSocket listener;
	private Socket socket;
	private ObjectInputStream in; // Contents�� �ޱ� ���� ��Ʈ��
	
	private LinKlipboardClient client;
	
	public RceiveContents(LinKlipboardClient client) {
		this.client = client;
	}

	/** ������ ������ ��ٸ��� ���� ���� */
	public void waitToServer() {
		try {
			System.out.println("[ReceiveContents] port��ȣ: " + LinKlipboardClient.getPortNum());
			
			listener = new ServerSocket(LinKlipboardClient.getPortNum());
			socket = listener.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** �������� ������ ���� ��Ʈ�� ���� */
	public void setConnection() {
		try {
			in = new ObjectInputStream(socket.getInputStream()); // ��Ʈ�� ����
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[ReceiveContents] ���� ���� ��");
	}

	@Override
	public void run() {
		while (true) {
			waitToServer();
			setConnection();
			try {
				client.latestContents = (Contents) in.readObject(); // Contents ��ü����
				int latestContentsType = client.latestContents.getType(); // Contents ��ü�� Ÿ��

				System.out.println("�ֱٿ� ���� �������� Ÿ��:" + latestContentsType);

				if (latestContentsType == LinKlipboard.NULL) {
					// sharer�� Ȯ��
					StringTokenizer tokens = new StringTokenizer(client.latestContents.getSharer(),
							LinKlipboard.RESPONSE_DELIMITER);
					String inoutClientInfo = tokens.nextToken();

					// join�̸� Vector otherClients�� �߰�
					if (inoutClientInfo.equals("join")) {
						String inClientNickname = tokens.nextToken();

						System.out.println("join�� Ŭ���̾�Ʈ�� �г���:" + inClientNickname);

						client.getOtherClients().add(inClientNickname);
						client.getConnectionPanel().updateAccessGroup();
					}
					// exit�̸� join�̸� Vector otherClients���� ����
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
					client.getHistory().addSharedContentsInHistory(client.latestContents); // �������� �ֽ�Contents�� history�� �߰�

					if (SettingPanel.notification) {// ���� �߰�
						if (latestContentsType == LinKlipboard.FILE_TYPE) {
							client.getMain().getTrayIcon().showMsg("Shared <File> Contents");
						} else if (latestContentsType == LinKlipboard.STRING_TYPE) {
							client.getMain().getTrayIcon().showMsg("Shared <Text> Contents");
						} else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
							client.getMain().getTrayIcon().showMsg("Shared <Image> Contents");
						} else {
							System.out.println("[LinKlipboardClient_�˸�] File, String, Image ��𿡵� ������ ����");
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