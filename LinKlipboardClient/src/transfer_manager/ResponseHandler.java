package transfer_manager;
import java.util.StringTokenizer;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;
import user_interface.UserInterface;

public class ResponseHandler {

	private String responseWholeMsg; // 서버에게서 응답받은 전체메세지

	private int errorCodeNum; // 에러코드
	private String errorMsg = null; // 에러코드에 대한 정보

	// 두개를 합칠까...말까...
	private String nickName = null; //사용자의 디폴트 닉네임
	private String fileNeme = null; //전송할 파일이름

	private LinKlipboardClient client;
	private UserInterface screen;

	public ResponseHandler(String responseWholeMsg, LinKlipboardClient client, UserInterface screen) {
		this.responseWholeMsg = responseWholeMsg;
		this.client = client;
		this.screen = screen;
	}

	/** 넘겨받은 스트링 에러 코드를 분리하는 메소드 */
	public void seperateErrorCode() {
		StringTokenizer tokens = new StringTokenizer(responseWholeMsg, LinKlipboard.RESPONSE_DELIMITER);

		errorCodeNum = Integer.parseInt(tokens.nextToken());
		setErrorMsg(errorCodeNum);
		
		//뒤에 추가로 String이 있으면 
		if(tokens.hasMoreTokens() == true){
			nickName = tokens.nextToken();
		}
	}

	/** errorCodeNum와 일치하는 메세지를 set하는 메소드 */
	public void setErrorMsg(int errorCodeNum) {
		switch (errorCodeNum) {
		case LinKlipboard.ACCESS_PERMIT:
			errorMsg = "접속 허용";
			break;
		case LinKlipboard.ERROR_DUPLICATED_GROUPNAME:
			errorMsg = "그룹명 중복";
			break;
		case LinKlipboard.ERROR_NO_MATCHED_GROUPNAME:
			errorMsg = "그룹명 존재 안함";
			break;
		case LinKlipboard.ERROR_PASSWORD_INCORRECT:
			errorMsg = "password 불일치";
			break;
		case LinKlipboard.ERROR_SOCKET_CONNECTION:
			errorMsg = "소켓 연결 오류";
			break;
		case LinKlipboard.ERROR_DATA_TRANSFER:
			errorMsg = "데이터 송수신 오류";
			break;
		case LinKlipboard.ERROR_FULL_GROUP:
			errorMsg = "생성 가능 그룹 초과";
			break;
		case LinKlipboard.ERROR_FULL_CLIENT:
			errorMsg = "접속 가능 클라이언트 초과";
			break;
		case LinKlipboard.ERROR_TRYCATCH:
			errorMsg = "try catch 오류";
			break;
		case LinKlipboard.ERROR_DUPLICATED_IP:
			errorMsg = "중복된 ip 주소";
			break;
		default: 
			errorMsg = "알수 없는 오류";
			break;
		}
	}

	/** 응답핸들러 */
	public void responseHandler() {
		// 받은 response를 먼저 분리하여 저장한다.
		seperateErrorCode();

		// 만약 errorCode가 ACCESS_PERMIT이면 디폴트 닉네임을 set
		if (errorCodeNum == LinKlipboard.ACCESS_PERMIT) {
			setNickName(nickName);
			System.out.println(client.getGroupName() + "의 " + nickName + "가  접속");
		}
		// 만약 errorCode가 ERROR이면 errorMsg에 오류정보 set
		else {
			// 사용자 인터페이스에 에러상태 표시
			screen.updateErrorState(errorMsg);
		}
		// 만약 errorCode가 READY_TO_TRANSFER이면 소켓을 연다
	}

	/** 클라이언트의 닉네임을 세팅 */
	public void setNickName(String nickName) {
		client.setNickName(nickName);
	}

	/** 받을 파일의 이름을 세팅 */
}
