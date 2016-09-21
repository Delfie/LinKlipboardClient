package start_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;
import transfer_manager.ResponseHandler;
import user_interface.UserInterface;

public class StartToProgram {	
	
	private String groupName; // 그룹이름
	private String password; // 패스워드
	private String response; // 서버로부터 받은 응답 정보
	
	private LinKlipboardClient client;
	private ResponseHandler responseHandler; // 응답에 대한 처리
	
	public StartToProgram(LinKlipboardClient client) {
		this.client = client;
		this.groupName = client.getGroupName();
		this.password = client.getGroupPassword(); 
		this.response = null;
	}

	// 생성버튼을 누르면 이 메소드가 실행
		/** 그룹생성 메소드 */
		public void createGroup() {
			sendGroupInfoToServer("/CreateGroup");
			exceptionHandling(response);
		}

		// 접속버튼을 누르면 이 메소드가 실행
		/** 그룹접속 메소드 */
		public void joinGroup() {
			sendGroupInfoToServer("/JoinGroup");
			exceptionHandling(response);
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

				// server에 그룹이름과 패스워드 전송(servlet이 받는 구분자: &)
				out.write("groupName=" + groupName + "&" + "password=" + password);
				out.flush();
				out.close();

				// 서버로부터 받을 데이터(응답정보)
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
		 * @param response
		 *            클라이언트 요청에 대한 서버의 응답
		 */
		public void exceptionHandling(String response) {
			responseHandler = new ResponseHandler(response, client);
			if(response != null){
				responseHandler.responseHandlerForStart();
			}
			else{
				System.out.println("[StartToProgram] Error!!!! 서버가 보낸 response가 null임");
			}
		}
		
		/** 서버로부터 받은 오류 정보를 반환 */
		public String getResponse(){
			return response;
		}
}
