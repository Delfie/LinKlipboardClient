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
import server_manager.LinKlipboard;


public class SendDataToServer extends Thread{
	private Socket socket; //������ ������ ����
	private String ipAddr = LinKlipboard.SERVER_IP;
	private final static int portNum = LinKlipboard.FTP_PORT;
	private ObjectOutputStream out;

	public void requestReceiveData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			// ������ ���� ������(�׷��̸�, (���ϸ�))
			String header = "groupName=" + LinKlipboardClient.getGroupName() + "&"; // delf: �ڽ��� ���� �׷� �̸�
			header += "type=" + ClipboardManager.getClipboardDataTypeNow(); // delf: ���� ������ Ÿ��
			System.out.println("���� ������ Ȯ��" + header);
			//header += "type=" + ClipboardManager.setDataFlavor(ClipboardManager.getSystmeClipboardContets()) + "\r\n"; // ���� ������ Ÿ��

			bout.write(header);
			bout.flush();
			bout.close();

			String tmp = null;
			int response = LinKlipboard.NULL;
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// delf
			/* �ӽ� ���� for test - ���� */
			while ((tmp = bin.readLine()) != null) {
				response = Integer.parseInt(tmp);
			}
			System.out.println("�����κ����� ����: " + response);
						
			if (response == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("���� ����");
				this.start();
			}
			System.out.println("while pass: " + tmp);
			/* �ӽ� ���� for test - �� */

			bin.close();

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
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
			Contents sendContents = ClipboardManager.readClipboard(); // ������ ��ü�� SystemClipboard�κ��� ������
			out.writeObject(sendContents); // ��ü ����
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}