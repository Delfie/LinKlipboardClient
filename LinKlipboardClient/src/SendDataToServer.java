import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import contents.Contents;


public class SendDataToServer extends Thread{
	private Socket socket;
	private String ipAddr = "";
	private int portNum = 20;
	private ObjectOutputStream out;
	//private ObjectInputStream in;

	
	public void requestReceiveData() {
		try {
			// ȣ���� ������ �ּ�
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Dooy/"); // ���� �ʿ�
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷�����)
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			out.write("groupName=" + LinKlipboardClient.getGroupName() + "\r\n");
			out.flush();
			out.close();

//			// �����κ��� ���� ������(��������)
//			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String response;
//
//			while ((response = in.readLine()) != null) {
//				dataType = Integer.parseInt(response);
//			}
//			in.close();

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
			//in = new ObjectInputStream(socket.getInputStream());

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
			Contents sendContents = ClipboardManager.readClipboard();
			out.writeObject(sendContents);
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}