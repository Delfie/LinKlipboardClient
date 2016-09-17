import java.awt.datatransfer.Clipboard;
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

public class ReceiveDataToServer extends Thread {
	private Socket socket;
	private String ipAddr = "";
	private int portNum = 20;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Clipboard systemClipboard; // �ڽ��� �ý��� Ŭ������

	public void requestReceiveData() {
		try {
			// ȣ���� ������ �ּ�
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Dooy/"); // ���� �ʿ�
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			
			// ������ ���� ������(�׷�����)
			String header = "groupName=" + LinKlipboardClient.getGroupName(); 
			System.out.println("���� ������ Ȯ��" + header);

			bout.write(header);
			bout.flush();
			bout.close();

			String tmp = null;
			String response = null;
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((tmp = bin.readLine()) != null) {
				response = tmp;
			}
			System.out.println("�����κ����� ����: " + response);

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
			//out = new ObjectOutputStream(socket.getOutputStream());
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
			Contents receiveContents =  (Contents) in.readObject(); // ���۰�ü ����
			ClipboardManager.writeClipboard(receiveContents, systemClipboard, receiveContents.getType()); // ������ �ý��� Ŭ�����忡 ����
			in.close();
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}