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
	private static String groupName; // �׷��̸�
	private String password; // �н�����
	private String nickName; // �г���
	private String response; // �����κ��� ���� ���� ����
	private String errorMessage; // ���� ���� �޼���

	UserInterface screen; // ����� �������̽�(for ���� ���� ǥ��)
	ResponseHandler responseHandler; // ���信 ���� ó��

	// delf
	/** LinKlipboardClient ������ */
	public LinKlipboardClient(String groupName, String groupPassword) {
		this.groupName = groupName;
		this.password = groupPassword;
		this.response = null;
		this.errorMessage = null;
	}
		
	/** LinKlipboardClient ������ */
	public LinKlipboardClient(String groupName, String groupPassword, UserInterface screen) {
		this.groupName = groupName;
		this.password = groupPassword;
		this.screen = screen;
		this.response = null;
		this.errorMessage = null;
	}

	// ������ư�� ������ �� �޼ҵ尡 ����
	/** �׷���� �޼ҵ� */
	public void createGroup() {
		// 1. ������ �׷����� ���� �� response set�ϱ�
		sendGroupInfoToServer("/CreateGroup");
		// 2. response�� ���� erroró��
		exceptionHandling(response);
	}

	// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
	/** �׷����� �޼ҵ� */
	public void joinGroup() {
		sendGroupInfoToServer("/JoinGroup");
		exceptionHandling(response);
	}

	/** �׷� ������ set�ϴ� �޼ҵ� -> �����ڿ��� ó������? */
	// public void setGroupInfo(String groupName, String groupPassword) {
	// this.groupName = groupName;
	// this.groupPassword = groupPassword;
	// }

	/** �׷� ������ �ʱ�ȭ */
	public void initGroupInfo() {
		this.groupName = null;
		this.password = null;
	}

	/** �������� ���� ���������� �ʱ�ȭ */
	public void initResponse() {
		this.response = null;
		this.errorMessage = null;
	}

	/** �׷� ������ ������ ������ ����(response)�޴� �޼ҵ� */
	public void sendGroupInfoToServer(String servletName) {
		String response = Integer.toString(LinKlipboard.ERROR_TRYCATCH);

		try {
			// ȣ���� ������ URL
			URL url = new URL(LinKlipboard.URL_To_CALL + servletName);
			URLConnection conn = url.openConnection();

			// servlet�� doPostȣ��
			conn.setDoOutput(true);

			// ������ ���� ������(�׷�����)
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			
			// String info = "info=" + groupName + ":" + password;
			// out.write(info);

			// delf
			out.write("groupName=" + groupName + "&password=" + password);
			out.flush();
			out.close();

			// �����κ��� ���� ������(��������)
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			if ((response = in.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
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
	 * ���� ó��
	 * 
	 * @param response
	 *            Ŭ���̾�Ʈ ��û�� ���� ������ ����
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, this, this.screen);
		responseHandler.responseHandler();
	}

	/** Ŭ���̾�Ʈ�� �Է��� �׷��̸� ��ȯ */
	public static String getGroupName() {
		return groupName;
	}

	/** Ŭ���̾�Ʈ�� �Է��� �׷��н����� ��ȯ */
	public String getGroupPassword() {
		return password;
	}

	/** Ŭ���̾�Ʈ�� �Է��� �г��� ��ȯ */
	public String getNickName() {
		return nickName;
	}

	/** Ŭ���̾�Ʈ�� �г����� ���� */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/** ���� ���� �޼����� ���� */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}