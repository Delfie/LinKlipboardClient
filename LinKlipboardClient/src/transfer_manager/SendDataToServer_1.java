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
	
	
	
	
	// ���� �����丮 �׽�Ʈ
	private LinKlipboardClient client;

	/** FileSendDataToServer_1 ������ */
	public SendDataToServer_1(LinKlipboardClient client) {
		super();
		this.client = client;
		this.start();
	}
	
	
	
	/** SendDataToServer_1 ������ */
	public SendDataToServer_1() {
		super();
		this.start();
	}
	
	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboard.FTP_PORT);
			// ��Ʈ�� ����
			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** �����ִ� ���ϰ� ��Ʈ���� ��� �ݴ´�. */
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
			Contents sendContents = ClipboardManager.readClipboard(); // ������ ��ü�� �ý��� Ŭ������κ��� ������
			
			
			
			// ���� �����丮 �׽�Ʈ
			client.getHistory().addSharedContnestsInHistory(sendContents);
			
			
			
			out.writeObject(sendContents); // Contents ��ü ����
			
			closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
		}
	}
}
