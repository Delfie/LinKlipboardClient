import java.awt.datatransfer.Clipboard;
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

public class ReceiveDataToServer extends Thread {
	private Socket socket;
	private String ipAddr = "";
	private int portNum = 20;
	//private ObjectOutputStream out;
	private ObjectInputStream in;
	private Clipboard systemClipboard; // 자신의 시스템 클립보드

	public void requestReceiveData() {
		try {
			// 호출할 서블릿의 주소
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Dooy/"); // 수정 필요
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹정보)
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			out.write("groupName=" + LinKlipboardClient.getGroupName() + "\r\n");
			out.flush();
			out.close();

//			// 서버로부터 받을 데이터(오류정보)
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
			// 소켓 접속 설정
			socket = new Socket(ipAddr, portNum);

			// 스트림 설정
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
			Contents receiveContents = (Contents) in.readObject();
			ClipboardManager.writeClipboard(receiveContents, systemClipboard, receiveContents.getType());
			in.close();
			socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}