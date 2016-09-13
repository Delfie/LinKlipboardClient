import java.awt.datatransfer.Clipboard;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import server_manager.LinKlipboard;

public class LinKlipboardClient {
	private static String groupName; // 그룹이름
	private String groupPassword; // 패스워드
	// private String nickName; //닉네임

	// UserInterface screen; //사용자 인터페이스

	public static void main(String[] args) {
		LinKlipboardClient test = new LinKlipboardClient();
		test.inputGroupInfo();
		test.createGroup();
	}

	/** LinKlipboardClient 생성자 */
	public LinKlipboardClient() {
		groupName = null;
		groupPassword = null;
	}

	// 생성버튼을 누르면 이 메소드가 실행
	/** 그룹생성 메소드 */
	public void createGroup() {

	}

	// 접속버튼을 누르면 이 메소드가 실행
	/** 그룹접속 메소드 */
	public void joinGroup() {

	}

	/** 그룹 정보를 입력하는 메소드 */
	public void inputGroupInfo() {
		// 나중엔 textField에서 입력받은 것은 set하기
		// 임시로 console에서
		// 그룹이름, 그룹패스워드 입력받기
		System.out.print("그룹이름을 입력: ");
		Scanner s = new Scanner(System.in);
		groupName = s.next();

		System.out.print("그룹패스워드를 입력: ");
		groupPassword = s.next();
	}

	/** 그룹 정보를 초기화하는 메소드 */
	public void initGroupInfo() {
		groupName = null;
		groupPassword = null;
	}

	/** 그룹 정보를 서버에 보내는 메소드 */
	public String sendGroupInfoToServer() {
		try {
			// 호출할 서블릿의 주소
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Dooy/DoLogin");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹정보)
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			out.write("groupName=" + groupName + "\r\n");
			out.write("groupPassword=" + groupPassword + "\r\n");
			out.flush();
			out.close();

			// 서버로부터 받을 데이터(오류정보)
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response;

			while ((response = in.readLine()) != null) {
				System.out.println(response);
				// response로 오류정보 확인
			}
			in.close();

			// 서버에서 확인 후 클라이언트가 받은 결과 메세지
			return response;

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// try catch에 대한 오류
		return null;
	}

	/**
	 * 예외 처리
	 * 
	 * @param response
	 *            그룹생성 및 접속에 대한 예외종류
	 */
	public void exceptionHandling(String response) {

		int except = Integer.parseInt(response);

		// try catch에 대한 오류
		if (response == null) {

		}

		switch (except) {
		// 1. response==ok (다음으로 넘어감)
		case LinKlipboard.ACCESS_PERMIT:
			break;

		// 2. response==그룹명중복
		case LinKlipboard.ERROR_DUPLICATED_GROUPNAME:
			break;

		// 3. response==그룹명존재안함
		case LinKlipboard.ERROR_NO_MATCHED_GROUPNAME:
			break;

		// 4. response==password불일치
		case LinKlipboard.ERROR_PASSWORD_INCORRECT:
			break;

		// 6. response==소켓 연결 오류
		case LinKlipboard.ERROR_SOCKET_CONNECTION:
			break;

		// 7. response==데이터 송수신 오류
		case LinKlipboard.ERROR_DATA_TRANSFER:
			break;

		// // 8. response==그룹인원초과
		// case LinKlipboard.ACCESS_PERMIT:
		// break;
		}
	}

	// public void setNickName(String nickName){
	// this.nickName = nickName;
	// }

	// public String getNickName(){
	// return nickName;
	// }

	/** 클라이언트가 입력한 그룹이름 반환 */
	public static String getGroupName() {
		return groupName;
	}

	/** 클라이언트가 입력한 그룹패스워드 반환 */
	public String getGroupPassword() {
		return groupPassword;
	}
}
