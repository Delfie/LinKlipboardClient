import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import contents.Contents;
import server_manager.LinKlipboard;


public class SendDataToServer extends Thread{
	private Socket socket;
	private String ipAddr = "";
	private int portNum = 20;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int sendDataType;

	// delf
	private final static int PERMIT_CODE = 0;
	private final static int TYPE = 1;
	
	public void requestReceiveData() {
		try {
			// ȣ���� ������ �ּ�
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Dooy/"); // ���� �ʿ�
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			// delf: ������ ���� ������
			String header = "groupName=" + LinKlipboardClient.getGroupName() + "&"; // delf: �ڽ��� ���� �׷� �̸�
			header += "type=" + ClipboardManager.getClipboardDataTypeNow(); // delf: ���� ������ Ÿ��
			System.out.println("���� ������ Ȯ��" + header);
			//header += "type=" + ClipboardManager.setDataFlavor(ClipboardManager.getSystmeClipboardContets()) + "\r\n"; // ���� ������ Ÿ��

			bout.write(header);
			bout.flush();
			bout.close();

			String tmp = null;
			String response = null;
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// delf
			/* �ӽ� ���� for test - ���� */
			while ((tmp = bin.readLine()) != null) {
				response = tmp;
			}
			System.out.println("�����κ����� ����: " + response);
			sendDataType = checkReadyToTrnasfer(response);
			
			if (sendDataType != LinKlipboard.NULL) {
				System.out.println("���� ����");
				this.start();
			}
			else {
				// ���� ó��
				System.out.println("����: " + this.getClass());
				// response�� ó���� �ϳ��� Ŭ�������� �ϴ� �͵� ������ ������
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
			in = new ObjectInputStream(socket.getInputStream());

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

	// delf
	public int converterReceiveData(String data) {
		return Integer.parseInt(data);
	}

	// delf
	public int[] converterAndSplipReceiveData(String data) {
		String[] info = data.split(";");
		int[] res = { Integer.parseInt(info[0]), Integer.parseInt(info[1]) };
		return res;
	}

	// delf
	public int checkReadyToTrnasfer(String data) {
		int[] info = converterAndSplipReceiveData(data);
		int ready = info[PERMIT_CODE];
		if (ready == LinKlipboard.READY_TO_TRANSFER) {
			try {
				return info[TYPE];
			} catch (ArrayIndexOutOfBoundsException e) {
				return LinKlipboard.NULL;
			}
		}
		return LinKlipboard.NULL;
	}
}