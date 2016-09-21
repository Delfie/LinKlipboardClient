package transfer_manager;

import java.awt.HeadlessException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.FileContents;
import server_manager.LinKlipboard;

public class FileSendDataToServer extends Thread {

	private Socket socket; // ������ ������ ����
	private LinKlipboardClient client;
	
	// ���濡�� ����Ʈ �迭�� �ְ� �ޱ����� ������ ��Ʈ�� ����
	private DataOutputStream dos;
	private FileInputStream fis;
	
	FileContents extractFile;
	File sendFile;
	
	/** FileSendDataToServer ������ */
	public FileSendDataToServer() {
	}	
	
	/** FileSendDataToServer ������ */
	public FileSendDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** ���� ������ ���� �޼ҵ� (FileSendDataToServer ���� ȣ��) */
	public void requestSendData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/FileSendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�, (���ϸ� ����� ���ϸ�����))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String groupName = "groupName=" + LinKlipboardClient.getGroupName();

			sendFile = new File(getFilePathInSystemClipboard());
			String fileName = "fileName=" + sendFile.getName();

			String header = groupName + "&" + fileName;
			System.out.println("���� ��ü ������ Ȯ��" + header); //delf

			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			/*
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;
			
			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			System.out.println("�����κ����� ���� ������ Ȯ��: " + this.response); //delf
			bin.close();			
			
			exceptionHandling(this.response); 
			*/ 
			//heee
			
			String tmp = null;
			int response = LinKlipboard.NULL;
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((tmp = bin.readLine()) != null) {
				response = Integer.parseInt(tmp);
			}
			System.out.println("�����κ����� ���� ������ Ȯ��: " + response);
			
			if (response == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("���� ����");
				this.start();
			}
			//delf

			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboard.FTP_PORT);
			
			// ��Ʈ�� ����
			dos = new DataOutputStream(socket.getOutputStream()); // ����Ʈ �迭�� ������ ���� �����ͽ�Ʈ�� ����

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();
		try {
			int byteSize = 65536;
			byte[] sendFileTobyteArray = new byte[byteSize]; // ����Ʈ �迭 ����

			fis = new FileInputStream(sendFile); // ���Ͽ��� �о���� ���� ��Ʈ�� ����

			int EndOfFile = 0; // ������ ��(-1)�� �˸��� ���� ����
			while ((EndOfFile = fis.read(sendFileTobyteArray)) != -1) { // sendFileTobyteArray�� ũ���� 1024����Ʈ ��ŭ ���Ͽ��� �о�� ����Ʈ �迭�� ����, EndOfFile���� 1024�� �������
																		// ������ ���� �ٴٸ���(EndOfFile=-1 �� ��)���� �ݺ�
				dos.write(sendFileTobyteArray, 0, EndOfFile); // sendFileTobyteArray�� ����ִ� ����Ʈ�� 0~EndOfFile=1024 ��ŭ DataOutputStream���� ����
			}

			closeSocket();
			
		} catch (IOException e1) {
			closeSocket();
			return;
		}

	}
	
	/** �����ִ� ������ ��� �ݴ´�. */
	private void closeSocket() {
		try {
			dos.close();
			fis.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** @return Ŭ�����忡 �ִ� ������ ��θ� */
	public static String getFilePathInSystemClipboard() {

		try {
			//�ý��� Ŭ�����忡�� ������ ����
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
}