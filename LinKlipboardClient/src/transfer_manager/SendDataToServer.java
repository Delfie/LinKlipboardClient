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
	private Socket socket; //서버와 연결할 소켓
	private String ipAddr = LinKlipboard.SERVER_IP;
	private final static int portNum = LinKlipboard.FTP_PORT;
	private ObjectOutputStream out;

	public void requestReceiveData() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			// 서버에 보낼 데이터(그룹이름, (파일명))
			String header = "groupName=" + LinKlipboardClient.getGroupName() + "&"; // delf: 자신이 속한 그룹 이름
			header += "type=" + ClipboardManager.getClipboardDataTypeNow(); // delf: 보낼 데이터 타입
			System.out.println("보낸 데이터 확인" + header);
			//header += "type=" + ClipboardManager.setDataFlavor(ClipboardManager.getSystmeClipboardContets()) + "\r\n"; // 보낼 데이터 타입

			bout.write(header);
			bout.flush();
			bout.close();

			String tmp = null;
			int response = LinKlipboard.NULL;
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// delf
			/* 임시 수정 for test - 시작 */
			while ((tmp = bin.readLine()) != null) {
				response = Integer.parseInt(tmp);
			}
			System.out.println("서버로부터의 응답: " + response);
						
			if (response == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("소켓 연결");
				this.start();
			}
			System.out.println("while pass: " + tmp);
			/* 임시 수정 for test - 끝 */

			bin.close();

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
			Contents sendContents = ClipboardManager.readClipboard(); // 전송할 객체를 SystemClipboard로부터 가져옴
			out.writeObject(sendContents); // 객체 전송
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}