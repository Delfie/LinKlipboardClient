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
	
	private String groupName; // �׷��̸�
	private String password; // �н�����
	private String response; // �����κ��� ���� ���� ����
	
	private LinKlipboardClient client;
	private ResponseHandler responseHandler; // ���信 ���� ó��
	
	public StartToProgram(LinKlipboardClient client) {
		this.client = client;
		this.groupName = client.getGroupName();
		this.password = client.getGroupPassword(); 
		this.response = null;
	}

	// ������ư�� ������ �� �޼ҵ尡 ����
		/** �׷���� �޼ҵ� */
		public void createGroup() {
			sendGroupInfoToServer("/CreateGroup");
			exceptionHandling(response);
		}

		// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
		/** �׷����� �޼ҵ� */
		public void joinGroup() {
			sendGroupInfoToServer("/JoinGroup");
			exceptionHandling(response);
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

				// server�� �׷��̸��� �н����� ����(servlet�� �޴� ������: &)
				out.write("groupName=" + groupName + "&" + "password=" + password);
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
		 * @param response
		 *            Ŭ���̾�Ʈ ��û�� ���� ������ ����
		 */
		public void exceptionHandling(String response) {
			responseHandler = new ResponseHandler(response, client);
			if(response != null){
				responseHandler.responseHandlerForStart();
			}
			else{
				System.out.println("[StartToProgram] Error!!!! ������ ���� response�� null��");
			}
		}
		
		/** �����κ��� ���� ���� ������ ��ȯ */
		public String getResponse(){
			return response;
		}
}
