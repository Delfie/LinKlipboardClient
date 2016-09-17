import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import server_manager.LinKlipboard;

public class LinKlipboardClient {
	private static String groupName; // 그룹이름
	private String password; // 패스워드
	private String nickName; // 닉네임
	private String response; // 서버로부터 받은 오류 정보
	private String errorMessage; // 오류 정보 메세지

	UserInterface screen; // 사용자 인터페이스(for 오류 정보 표시)
	ResponseHandler responseHandler; // 응답에 대한 처리

	// delf
	/** LinKlipboardClient 생성자 */
	public LinKlipboardClient(String groupName, String groupPassword) {
		this.groupName = groupName;
		this.password = groupPassword;
		this.response = null;
		this.errorMessage = null;
	}
		
	/** LinKlipboardClient 생성자 */
	public LinKlipboardClient(String groupName, String groupPassword, UserInterface screen) {
		this.groupName = groupName;
		this.password = groupPassword;
		this.screen = screen;
		this.response = null;
		this.errorMessage = null;
	}

	// 생성버튼을 누르면 이 메소드가 실행
	/** 그룹생성 메소드 */
	public void createGroup() {
		// 1. 서버에 그룹정보 전송 후 response set하기
		sendGroupInfoToServer("/CreateGroup");
		// 2. response에 대한 error처리
		exceptionHandling(response);
	}

	// 접속버튼을 누르면 이 메소드가 실행
	/** 그룹접속 메소드 */
	public void joinGroup() {
		sendGroupInfoToServer("/JoinGroup");
		exceptionHandling(response);
	}

	/** 그룹 정보를 set하는 메소드 -> 생성자에서 처리가능? */
	// public void setGroupInfo(String groupName, String groupPassword) {
	// this.groupName = groupName;
	// this.groupPassword = groupPassword;
	// }

	/** 그룹 정보를 초기화 */
	public void initGroupInfo() {
		this.groupName = null;
		this.password = null;
	}

	/** 서버에서 보낸 오류정보를 초기화 */
	public void initResponse() {
		this.response = null;
		this.errorMessage = null;
	}

	/** 그룹 정보를 서버에 보내고 응답(response)받는 메소드 */
	public void sendGroupInfoToServer(String servletName) {
		String response = Integer.toString(LinKlipboard.ERROR_TRYCATCH);

		try {
			// 호출할 서블릿의 URL
			URL url = new URL(LinKlipboard.URL_To_CALL + servletName);
			URLConnection conn = url.openConnection();

			// servlet의 doPost호출
			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹정보)
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			
			// String info = "info=" + groupName + ":" + password;
			// out.write(info);

			// delf
			out.write("groupName=" + groupName + "&password=" + password);
			out.flush();
			out.close();

			// 서버로부터 받을 데이터(오류정보)
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			if ((response = in.readLine()) != null) {
				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
				this.response = response;
			}
			in.close();

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.response = response;
	}

	/**
	 * 예외 처리
	 * 
	 * @param response
	 *            클라이언트 요청에 대한 서버의 응답
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, this, this.screen);
		responseHandler.responseHandler();
	}

	/** 클라이언트가 입력한 그룹이름 반환 */
	public static String getGroupName() {
		return groupName;
	}

	/** 클라이언트가 입력한 그룹패스워드 반환 */
	public String getGroupPassword() {
		return password;
	}

	/** 클라이언트가 입력한 닉네임 반환 */
	public String getNickName() {
		return nickName;
	}

	/** 클라이언트의 닉네임을 세팅 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/** 오류 정보 메세지를 세팅 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}