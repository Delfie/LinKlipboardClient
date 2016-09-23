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

public class StartToProgram {

	private LinKlipboardClient client;

	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	private String groupName; // �׷��̸�
	private String password; // �н�����
	private String orderMsg; // ����ڰ� ���ϴ� ���(create/join)

	/**
	 * StartToProgram ������
	 * 
	 * @param client
	 *            ���α׷��� �����ϴ� �����
	 */
	public StartToProgram(LinKlipboardClient client, String orderMsg) {
		this.client = client;
		this.groupName = LinKlipboardClient.getGroupName();
		this.password = client.getGroupPassword();
		this.orderMsg = orderMsg;
		this.response = null;

		startProgram();
	}

	public void startProgram() {
		// ������ư�� ������ �� �޼ҵ尡 ����
		if (orderMsg == "create") {
			sendGroupInfoToServer("/CreateGroup");
		}
		// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
		else if (orderMsg == "join") {
			sendGroupInfoToServer("/JoinGroup");
		}
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
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + groupName + "&" + "password=" + password;

			// server�� �׷��̸��� �н����� ����(servlet�� �޴� ������: &)
			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			bin.close();

			exceptionHandling(this.response);
			System.out.println(responseHandler.getErrorCodeNum());

			if (responseHandler.getErrorCodeNum() == LinKlipboard.ACCESS_PERMIT) {
				System.out.println("orderMsg: " + orderMsg);
				if (orderMsg.equals("create")) {
					System.out.println("���� ����");
					LinKlipboardClient.setHistory();
				} else if (orderMsg.equals("join")) {
					// ������ �ִ� Vector<Contents>�� �޴´�.
					new GetTotalHistoryFromServer();
				}
			}

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
		responseHandler = new ResponseHandler(response, client);
		if (response != null) {
			responseHandler.responseHandlerForStart();
		} else {
			System.out.println("[StartToProgram] Error!!!! ������ ���� response�� null��");
		}
	}

	// /** �����κ��� ���� ���� ������ ��ȯ */
	// public String getResponse() {
	// return response;
	// }
	// //UI�� �� �ʿ��ҵ�
}
