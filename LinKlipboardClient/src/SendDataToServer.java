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
			// 호출할 서블릿의 주소
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Dooy/"); // 수정 필요
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

			// delf: 서버에 보낼 데이터
			String header = "groupName=" + LinKlipboardClient.getGroupName() + "&"; // delf: 자신이 속한 그룹 이름
			header += "type=" + ClipboardManager.getClipboardDataTypeNow(); // delf: 보낼 데이터 타입
			System.out.println("보낸 데이터 확인" + header);
			//header += "type=" + ClipboardManager.setDataFlavor(ClipboardManager.getSystmeClipboardContets()) + "\r\n"; // 보낼 데이터 타입

			bout.write(header);
			bout.flush();
			bout.close();

			String tmp = null;
			String response = null;
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// delf
			/* 임시 수정 for test - 시작 */
			while ((tmp = bin.readLine()) != null) {
				response = tmp;
			}
			System.out.println("서버로부터의 응답: " + response);
			sendDataType = checkReadyToTrnasfer(response);
			
			if (sendDataType != LinKlipboard.NULL) {
				System.out.println("소켓 연결");
				this.start();
			}
			else {
				// 오류 처리
				System.out.println("오류: " + this.getClass());
				// response의 처리를 하나의 클래스에서 하는 것도 나쁘지 않을듯
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
			Contents sendContents = ClipboardManager.readClipboard(); // 전송할 객체를 SystemClipboard로부터 가져옴
			out.writeObject(sendContents); // 객체 전송
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