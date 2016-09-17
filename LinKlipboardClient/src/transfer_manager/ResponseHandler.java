package transfer_manager;
import java.util.StringTokenizer;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;
import user_interface.UserInterface;

public class ResponseHandler {

	private String responseWholeMsg; // �������Լ� ������� ��ü�޼���

	private int errorCodeNum; // �����ڵ�
	private String errorMsg = null; // �����ڵ忡 ���� ����

	// �ΰ��� ��ĥ��...����...
	private String nickName = null; //������� ����Ʈ �г���
	private String fileNeme = null; //������ �����̸�

	private LinKlipboardClient client;
	private UserInterface screen;

	public ResponseHandler(String responseWholeMsg, LinKlipboardClient client, UserInterface screen) {
		this.responseWholeMsg = responseWholeMsg;
		this.client = client;
		this.screen = screen;
	}

	/** �Ѱܹ��� ��Ʈ�� ���� �ڵ带 �и��ϴ� �޼ҵ� */
	public void seperateErrorCode() {
		StringTokenizer tokens = new StringTokenizer(responseWholeMsg, LinKlipboard.RESPONSE_DELIMITER);

		errorCodeNum = Integer.parseInt(tokens.nextToken());
		setErrorMsg(errorCodeNum);
		
		//�ڿ� �߰��� String�� ������ 
		if(tokens.hasMoreTokens() == true){
			nickName = tokens.nextToken();
		}
	}

	/** errorCodeNum�� ��ġ�ϴ� �޼����� set�ϴ� �޼ҵ� */
	public void setErrorMsg(int errorCodeNum) {
		switch (errorCodeNum) {
		case LinKlipboard.ACCESS_PERMIT:
			errorMsg = "���� ���";
			break;
		case LinKlipboard.ERROR_DUPLICATED_GROUPNAME:
			errorMsg = "�׷�� �ߺ�";
			break;
		case LinKlipboard.ERROR_NO_MATCHED_GROUPNAME:
			errorMsg = "�׷�� ���� ����";
			break;
		case LinKlipboard.ERROR_PASSWORD_INCORRECT:
			errorMsg = "password ����ġ";
			break;
		case LinKlipboard.ERROR_SOCKET_CONNECTION:
			errorMsg = "���� ���� ����";
			break;
		case LinKlipboard.ERROR_DATA_TRANSFER:
			errorMsg = "������ �ۼ��� ����";
			break;
		case LinKlipboard.ERROR_FULL_GROUP:
			errorMsg = "���� ���� �׷� �ʰ�";
			break;
		case LinKlipboard.ERROR_FULL_CLIENT:
			errorMsg = "���� ���� Ŭ���̾�Ʈ �ʰ�";
			break;
		case LinKlipboard.ERROR_TRYCATCH:
			errorMsg = "try catch ����";
			break;
		case LinKlipboard.ERROR_DUPLICATED_IP:
			errorMsg = "�ߺ��� ip �ּ�";
			break;
		default: 
			errorMsg = "�˼� ���� ����";
			break;
		}
	}

	/** �����ڵ鷯 */
	public void responseHandler() {
		// ���� response�� ���� �и��Ͽ� �����Ѵ�.
		seperateErrorCode();

		// ���� errorCode�� ACCESS_PERMIT�̸� ����Ʈ �г����� set
		if (errorCodeNum == LinKlipboard.ACCESS_PERMIT) {
			setNickName(nickName);
			System.out.println(client.getGroupName() + "�� " + nickName + "��  ����");
		}
		// ���� errorCode�� ERROR�̸� errorMsg�� �������� set
		else {
			// ����� �������̽��� �������� ǥ��
			screen.updateErrorState(errorMsg);
		}
		// ���� errorCode�� READY_TO_TRANSFER�̸� ������ ����
	}

	/** Ŭ���̾�Ʈ�� �г����� ���� */
	public void setNickName(String nickName) {
		client.setNickName(nickName);
	}

	/** ���� ������ �̸��� ���� */
}
