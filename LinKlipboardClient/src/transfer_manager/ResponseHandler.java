package transfer_manager;
import java.util.StringTokenizer;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;
import user_interface.UserInterface;

public class ResponseHandler {

	private String responseWholeMsg; // �������Լ� ������� ��ü�޼���

	private int errorCodeNum; // �����ڵ�
	private String errorMsg = null; // �����ڵ忡 ���� ����

	// �����ڵ� ������ ���� ����޼���(����Ʈ �г��� �Ǵ� ���۹��� �����̸�)
	private String latterMsg = null;

	private LinKlipboardClient client;

	
	/** ResponseHandler ������ */
	public ResponseHandler(String responseWholeMsg, LinKlipboardClient client) {
		this.responseWholeMsg = responseWholeMsg;
		this.client = client;
	}

	/** �Ѱܹ��� ��Ʈ�� ���� �ڵ带 �и��ϴ� �޼ҵ� */
	public void seperateErrorCode() {
		StringTokenizer tokens = new StringTokenizer(responseWholeMsg, LinKlipboard.RESPONSE_DELIMITER);

		errorCodeNum = Integer.parseInt(tokens.nextToken());
		setErrorMsg(errorCodeNum);
		
		//�ڿ� �߰��� String�� ������ 
		if(tokens.hasMoreTokens() == true){
			latterMsg = tokens.nextToken();
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
		case LinKlipboard.NULL:
			errorMsg = "NULL";
			break;
		default: 
			errorMsg = "�˼� ���� ����";
			break;
		}
	}

	/** ���� �� ���ӿ� ���� ���� �ڵ鷯 */
	public void responseHandlerForStart() {
		seperateErrorCode();

		// ���� errorCode�� ACCESS_PERMIT�̸� ����Ʈ �г����� set
		if (errorCodeNum == LinKlipboard.ACCESS_PERMIT) {
			setDefaultNickName(latterMsg);
			System.out.println(client.getGroupName() + "�� " + latterMsg + "��  ����");
		}
		// ���� errorCode�� ERROR�̸� errorMsg�� �������� set
		else {
			// ����� �������̽��� �������� ǥ��
			client.updateErrorState(errorMsg);
		}
	}
	
	/** ������ ���� �� ���ſ� ���� ���� �ڵ鷯 */
	public void responseHandlerForTransfer() {
		seperateErrorCode();

		// ���� errorCode�� READY_TO_TRANSFER�̸� ������ ����
		if (errorCodeNum == LinKlipboard.READY_TO_TRANSFER) {
			//���Ͽ���
			System.out.println("���� ����");
			//this.start(); //heee
			
			setFileName(latterMsg);
			System.out.println(client.getGroupName() + "�� " + latterMsg + "�� ���۹���");
		}
		// ���� errorCode�� ERROR�̸� errorMsg�� �������� set
		else {
			// ����� �������̽��� �������� ǥ��
			// client.updateErrorState(errorMsg);
		}
	}

	/** Ŭ���̾�Ʈ�� ����Ʈ �г����� ���� */
	public void setDefaultNickName(String nickName) {
		client.setNickName(nickName);
	}

	/** ���۹��� ������ �̸��� ���� */
	public void setFileName(String fileName){
		client.setFileName(fileName);
	}
}
