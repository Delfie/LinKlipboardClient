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
	// private String nickName; //�г���

	// UserInterface screen; //����� �������̽�

	public static void main(String[] args) {
		LinKlipboardClient test = new LinKlipboardClient();
		test.inputGroupInfo();
		test.createGroup();
	}

	/** LinKlipboardClient ������ */
	public LinKlipboardClient() {
		groupName = null;
		groupPassword = null;
	}

	// ������ư�� ������ �� �޼ҵ尡 ����
	/** �׷���� �޼ҵ� */
	public void createGroup() {

	}

	// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
	/** �׷����� �޼ҵ� */
	public void joinGroup() {

	}

	/** �׷� ������ �Է��ϴ� �޼ҵ� */
	public void inputGroupInfo() {
		// ���߿� textField���� �Է¹��� ���� set�ϱ�
		// �ӽ÷� console����
		// �׷��̸�, �׷��н����� �Է¹ޱ�
		System.out.print("�׷��̸��� �Է�: ");
		Scanner s = new Scanner(System.in);
		groupName = s.next();

		System.out.print("�׷��н����带 �Է�: ");
		groupPassword = s.next();
	}

	/** �׷� ������ �ʱ�ȭ�ϴ� �޼ҵ� */
	public void initGroupInfo() {
		groupName = null;
		groupPassword = null;
	}

	/** �׷� ������ ������ ������ �޼ҵ� */
	public String sendGroupInfoToServer() {
		try {
			// ȣ���� ������ �ּ�
			// URL("http://localhost:8080/LinKlipboardServerProJect/Servlet/CreateGroup");
			// URL url = new URL("http://113.198.84.51:8080/godhj/DoLogin");
			URL url = new URL("http://localhost:8080/Dooy/DoLogin");
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

			// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
			return response;

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// try catch�� ���� ����
		return null;
	}

	/**
	 * ���� ó��
	 * 
	 * @param response
	 *            �׷���� �� ���ӿ� ���� ��������
	 */
	public void exceptionHandling(String response) {

		int except = Integer.parseInt(response);

		// try catch�� ���� ����
		if (response == null) {

		}

		switch (except) {
		// 1. response==ok (�������� �Ѿ)
		case LinKlipboard.ACCESS_PERMIT:
			break;

		// 2. response==�׷���ߺ�
		case LinKlipboard.ERROR_DUPLICATED_GROUPNAME:
			break;

		// 3. response==�׷���������
		case LinKlipboard.ERROR_NO_MATCHED_GROUPNAME:
			break;

		// 4. response==password����ġ
		case LinKlipboard.ERROR_PASSWORD_INCORRECT:
			break;

		// 6. response==���� ���� ����
		case LinKlipboard.ERROR_SOCKET_CONNECTION:
			break;

		// 7. response==������ �ۼ��� ����
		case LinKlipboard.ERROR_DATA_TRANSFER:
			break;

		// // 8. response==�׷��ο��ʰ�
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

	/** Ŭ���̾�Ʈ�� �Է��� �׷��̸� ��ȯ */
	public static String getGroupName() {
		return groupName;
	}

	/** Ŭ���̾�Ʈ�� �Է��� �׷��н����� ��ȯ */
	public String getGroupPassword() {
		return groupPassword;
	}
}
