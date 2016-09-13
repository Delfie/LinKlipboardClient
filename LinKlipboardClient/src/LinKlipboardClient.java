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
	
	private static String groupName; // �׷��̸�
	private String groupPassword; // �н�����
	private String nickName; //�г���
	private int response; //�����κ��� ���� ���� ����
	private String errorMessage; //���� ���� �޼���
	
	UserInterface screen; //����� �������̽�(for ���� ���� ǥ��)
	
	/** LinKlipboardClient ������ */
	public LinKlipboardClient(String groupName, String groupPassword, UserInterface screen) {
		this.groupName = groupName;
		this.groupPassword = groupPassword;
		this.screen = screen;
		this.response = LinKlipboard.NULL;
		this.errorMessage = null;
	}

	// ������ư�� ������ �� �޼ҵ尡 ����
	/** �׷���� �޼ҵ� */
	public void createGroup() {
		// 1. ������ �׷����� ���� �� response set�ϱ�
		sendGroupInfoToServer();
		// 2. response�� ���� erroró��
		exceptionHandling(response);
		// 3. ����� �������̽��� �������� ǥ��
		screen.updateErrorState(errorMessage);
	}

	// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
	/** �׷����� �޼ҵ� */
	public void joinGroup() {
		// 1. ������ �׷����� ���� �� response set�ϱ�
		sendGroupInfoToServer();
		// 2. response�� ���� erroró��
		exceptionHandling(response);
		// 3. ����� �������̽��� �������� ǥ��
		screen.updateErrorState(errorMessage);
	}

	/** �׷� ������ set�ϴ� �޼ҵ� -> �����ڿ��� ó������? */
	public void setGroupInfo(String groupName, String groupPassword) {
		this.groupName = groupName;
		this.groupPassword = groupPassword;
	}
	
	/** �׷� ������ �ʱ�ȭ�ϴ� �޼ҵ� */
	public void initGroupInfo(){
		this.groupName = null;
		this.groupPassword = null;
	}
	
	/** �������� ���� �������� �ʱ�ȭ */
	public void initResponse(){
		this.response = LinKlipboard.NULL;
		this.errorMessage = null;
	}

	/** �׷� ������ ������ ������ �޼ҵ� */
	public void sendGroupInfoToServer() {
		try {
			// ȣ���� ������ �ּ�
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Doooy/servlet/JoinGroup");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷�����)
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			out.write("groupName=" + groupName + "\r\n");
			out.write("groupPassword=" + groupPassword + "\r\n");
			out.flush();
			out.close();

			// �����κ��� ���� ������(��������)
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response;

			while ((response = in.readLine()) != null) {
				System.out.println(response);
				// response�� �������� Ȯ��
			}
			in.close();

			System.out.println("this.response: " + this.response);
			System.out.println("response: " + response);
			// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
			this.response = Integer.parseInt(response);
			

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// try catch�� ���� ����
		this.response =  LinKlipboard.ERROR_TRYCATCH;
	}

	/**
	 * ���� ó��
	 * @param response �׷���� �� ���ӿ� ���� ��������
	 */
	public void exceptionHandling(int response) {
		switch (response) {
		// 1. response==ok (�������� �Ѿ)
		case LinKlipboard.ACCESS_PERMIT:
			errorMessage = "���� ���";
			//���� �������� �Ѿ
			break;

		// 2. response==�׷���ߺ�
		case LinKlipboard.ERROR_DUPLICATED_GROUPNAME:
			errorMessage = "�׷�� �ߺ�";
			break;

		// 3. response==�׷���������
		case LinKlipboard.ERROR_NO_MATCHED_GROUPNAME:
			errorMessage = "�׷�� ���� ����";
			break;

		// 4. response==password����ġ
		case LinKlipboard.ERROR_PASSWORD_INCORRECT:
			errorMessage = "password ����ġ";
			break;

		// 6. response==���� ���� ����
		case LinKlipboard.ERROR_SOCKET_CONNECTION:
			errorMessage = "���� ���� ����";
			break;

		// 7. response==������ �ۼ��� ����
		case LinKlipboard.ERROR_DATA_TRANSFER:
			errorMessage = "������ �ۼ��� ����";
			break;

//		// 8. response==�׷��ο��ʰ�
//		case LinKlipboard.ACCESS_PERMIT:
//			errorMessage = "�׷� �ο��ʰ�";
//			break;
			
		// 9. response==try catch�� ���� ����
		case LinKlipboard.ERROR_TRYCATCH:
			errorMessage = "try catch ����";
			break;
		}
	}

	
	
	/** ������ ������ ���� */
	public void sendDateToServer(){
		
	}
	
	
	/** �������� ������ ���� */
	public void receiveDataToServer(){
		
	}
	
	
	/** �����丮���� ���ϴ� ������ ��û�ؼ� �ޱ� */
	public void receivePreviousData(){
		
	}
	

	/** �����丮 ��ü ���� ��û */
	public void receiveHistoryList(){
		
	}
	
	
	
	/** Ŭ���̾�Ʈ�� �Է��� �׷��̸� ��ȯ */
	public static String getGroupName() {
		return groupName;
	}

	/** Ŭ���̾�Ʈ�� �Է��� �׷��н����� ��ȯ */
	public String getGroupPassword() {
		return groupPassword;
	}

	// public String getNickName(){
	// return nickName;
	// }

	// public void setNickName(String nickName){
	// this.nickName = nickName;
	// }
}