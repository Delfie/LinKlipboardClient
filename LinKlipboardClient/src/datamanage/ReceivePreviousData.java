package datamanage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import server_manager.LinKlipboard;
import transfer_manager.FileReceiveDataToServer;

public class ReceivePreviousData {
	private Contents previousData; // Ŭ�����忡 ������ ������
	private History history; // �����丮
	private int dataType; 
	private int listIndex;
	private FileContents fileContentes;
	private FileReceiveDataToServer fileReceive;
	
	ReceivePreviousData(History history, int listIndex) {
		this.history = history;
		this.listIndex = listIndex;
		this.previousData = history.getRequestContents(listIndex); // ����£O
		this.dataType = previousData.getType();
		
		receiveDataToServer();
	}
	
	public void receiveDataToServer() {
		switch(dataType) {
		case LinKlipboard.STRING_TYPE :
		case LinKlipboard.IMAGE_TYPE :
			ClipboardManager.writeClipboard(previousData, dataType); // ���Ź��� �����͸� Ŭ�����忡 ����
			break;
		case LinKlipboard.FILE_TYPE:
			fileContentes = (FileContents)previousData; // ���Ź��� ������ �̸��� �˾Ƴ��� ���� Contents�� FileContents�� ĳ����
			fileReceive = new FileReceiveDataToServer(fileContentes.getFileName()); // �����̸��� ���ڷ� ������ FileReceiveDataToServer ��ü ����
			requestReceiveData(); // ������ index ������ ���� �ް� ���� �޾Ƽ� Ŭ�����忡 ����
			break;
		}
	}

	public void requestReceiveData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/FileReceiveDataToServerInHistory");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName() + "&";
			header += "index=" + listIndex + "\r\n";
			
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
				fileReceive.start();
			}
			//delf
			
			//exceptionHandling(this.response); // heee
			
			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
