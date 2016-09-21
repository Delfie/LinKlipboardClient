package transfer_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import contents.Contents;
import contents.FileContents;
import server_manager.LinKlipboard;

public class SendDataToServer extends Thread {

	private Socket socket; // ������ ������ ����
	private String ipAddr = LinKlipboard.SERVER_IP;
	private final static int portNum = LinKlipboard.FTP_PORT;

	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	private LinKlipboardClient client;

	private ObjectOutputStream out;

	/** SendDataToServer ������ */
	public SendDataToServer() {
	}

	/** SendDataToServer ������ */
	public SendDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** ���ڿ�, �̹��� ������ ���� �޼ҵ� (SendDataToServer ���� ȣ��) */
	public void requestSendData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�, (���ϸ� ����� ���ϸ�����))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String groupName = "groupName=" + LinKlipboardClient.getGroupName();

			String header = groupName;

			System.out.println("[SendDataToServer] ���� ��ü ������ Ȯ��" + header); // delf

			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			System.out.println("[SendDataToServer] �����κ����� ���� ������ Ȯ��: " + this.response); // delf
			bin.close();

			exceptionHandling(this.response);

			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[SendDataToServer] ���� ����");
				this.start();
			}

			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ���� ó��
	 * 
	 * @param response
	 *            Ŭ���̾�Ʈ ��û�� ���� ������ ����
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, client);
		if(response != null){
			responseHandler.responseHandlerForStart();
		}
		else{
			System.out.println("[SendDataToServer] Error!!!! ������ ���� response�� null��");
		}
	}

	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(ipAddr, portNum);
			// ��Ʈ�� ����
			out = new ObjectOutputStream(socket.getOutputStream());

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
			Contents sendContents = ClipboardManager.readClipboard(); // ������ ��ü�� �ý��� Ŭ������κ��� ������
			out.writeObject(sendContents); // Contents ��ü ����
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}