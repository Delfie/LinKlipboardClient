package transfer_manager;

import java.awt.HeadlessException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.FileContents;
import server_manager.LinKlipboard;

public class CommunicatingWithServer {

	protected LinKlipboardClient client;

	protected String response; // �����κ��� ���� ���� ����
	protected ResponseHandler responseHandler; // ���信 ���� ó��
	
	private static File sendFile;
	
	
	
	
	// ���� �����丮 �׽�Ʈ
	private static FileContents fileContents;

	
	
	
	/** CommunicatingWithServer ������ */
	public CommunicatingWithServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** ���� ������ ���� �޼ҵ� (ReceiveDataToServer ���� ȣ��) */
	public void requestReceiveFileData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReceiveDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[requestReceiveFileData] ���� ��ü ������ Ȯ��" + header);

			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			System.out.println("[requestReceiveFileData] �����κ����� ���� ������ Ȯ��: " + this.response);
			bin.close();

			exceptionHandling(this.response);
			FileReceiveDataToServer_1.setFilePath();

			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[requestReceiveFileData] ���� ����");
				new FileReceiveDataToServer_1();
			}

			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** ���� ������ ���� �޼ҵ� (SendDataToServer ���� ȣ��) */
	public void requestSendFileData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�, (���ϸ� ����� ���ϸ�����))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String groupName = "groupName=" + LinKlipboardClient.getGroupName();

			sendFile = new File(getFilePathInSystemClipboard());
			String fileName = "fileName=" + sendFile.getName();
			
			
			
			
			// ���� �����丮 �׽�Ʈ
			fileContents = new FileContents(sendFile);

			
			
			
			String header = groupName + "&" + fileName;
			System.out.println("[requestSendFileData] ���� ��ü ������ Ȯ��" + header); 

			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			System.out.println("[requestSendFileData] �����κ����� ���� ������ Ȯ��: " + this.response); 
			bin.close();

			exceptionHandling(this.response);
			
			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[requestSendFileData] ���� ����");
				//new FileSendDataToServer_1();
				
				// ���� �����丮 �׽�Ʈ
				new FileSendDataToServer_1(client);
			}
			bin.close();
			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/** ���ڿ�, �̹��� ������ ���� �޼ҵ� (SendDataToServer ���� ȣ��) */
	public void requestSendExpFileData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�, (���ϸ� ����� ���ϸ�����))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[requestSendExpFileData] ���� ��ü ������ Ȯ��" + header);
			
			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			System.out.println("[requestSendExpFileData] �����κ����� ���� ������ Ȯ��: " + this.response);
			bin.close();

			exceptionHandling(this.response);

			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[requestSendExpFileData] ���� ����");
				//new SendDataToServer_1();
				
				// ���� �����丮 �׽�Ʈ
				new SendDataToServer_1(client);
			}

			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
			responseHandler.responseHandlerForTransfer();
		} else {
			System.out.println("[exceptionHandling] Error!!!! ������ ���� response�� null��");
		}
	}
	
	/** @return Ŭ�����忡 �ִ� ������ ��θ� */
	public static String getFilePathInSystemClipboard() {

		try {
			// �ý��� Ŭ�����忡�� ������ ����
			Transferable contents = ClipboardManager.getSystmeClipboardContets();

			String fileTotalPath = contents.getTransferData(ClipboardManager.setDataFlavor(contents)).toString();

			// ��θ� ������ ���� �� ���� []�� ����
			return fileTotalPath.substring(1, fileTotalPath.length() - 1);

		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Ŭ���̾�Ʈ�� ������ ���� ���� ������ ���� */
	public static File getSendFile(){
		return sendFile;
	}
	
	// ���� �����丮 �׽�Ʈ
	public static FileContents getFileContents(){
		return fileContents;
	}
}
