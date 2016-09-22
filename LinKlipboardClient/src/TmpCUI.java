import java.util.Scanner;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import contents.StringContents;
import server_manager.LinKlipboard;
import transfer_manager.FileReceiveDataToServer;
import transfer_manager.FileSendDataToServer;
import transfer_manager.SendDataToServer;
import user_interface.TrayIconManager;

public class TmpCUI {

	private LinKlipboardClient client;

	public static Scanner s = new Scanner(System.in);

	private static boolean ACCESS;
	
	private TrayIconManager trayIcon;

	public TmpCUI() {
	}

	/** Ŭ���̾�Ʈ ���� */
	private void createClient(String groupName, String groupPassword) {
		client = new LinKlipboardClient(groupName, groupPassword);
	}

	/** ���� ���� ǥ�� */
	public void updateErrorState(String response) {
		// TEST
		System.out.println("updateErrorState: " + response);
	}

	/** Ŭ���̾�Ʈ ���� �ʱ�ȭ */
	public void initClientInfo(LinKlipboardClient client) {
		client.initGroupInfo();
	}

	public static void main(String[] args) {
		new TmpCUI().run();
		
		
	}

	public void run() {
		trayIcon = new TrayIconManager();
		trayIcon.addTrayIconInSystemTray();
		
		ACCESS = false;
		System.out.println("\n[[this is program for debuging client side]]");

		while (true) {
			if (ACCESS == false) {
				accessServer();
			}
			else {
				menu();
			}
		}
	}

	private void accessServer() {
		System.out.print("1. ���� / 2. ����\n>> ");
		switch (s.next()) {
		case "1":
			createGroup();
			break;
		case "2":
			joinGroup();
		}
		System.out.println("���� ���� �Ϸ�\n");
	}

	private void menu() {
		System.out.print("1. ���� / 2. ���� / 3. �����丮 ��� ���� / 4. �����丮�� ���ϴ� ������ �ޱ�\n>> ");
		switch (s.next()) {
		case "1":
			if(ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE){
				fileSendDataToServer();
			}
			else{
				sendData();
			}
			break;
			
		case "2":
			receiveData();
			break;
			
		case "3":
			printHistoryList();
			break;
			
		case "4":
			printHistoryList();
			
			System.out.println("--index : ");
			int index = s.nextInt();
			 receiveDataInHistory(index);
			
		default:
			break;
		}
	}
	
	public void fileSendDataToServer() {
		FileSendDataToServer sender = new FileSendDataToServer(client);
		sender.requestSendData();
	}

	public void sendData() {
		SendDataToServer sender = new SendDataToServer(client);
		sender.requestSendData();
	}
	
	
	public void receiveData() {
		
		client.settLatestContents();
		
		Contents latestContentsFromServer = client.getLatestContents();
		int latestContentsType = client.getLatestContents().getType();
		
		System.out.println("���� ������ Ÿ�� : " + latestContentsType);
		
		if(latestContentsType == LinKlipboard.FILE_TYPE){
			FileReceiveDataToServer receiver = new FileReceiveDataToServer(client);
			receiver.requestReceiveData();
			trayIcon.showMsg("���ϵ���!");
		}
		else if(latestContentsType == LinKlipboard.STRING_TYPE || latestContentsType == LinKlipboard.IMAGE_TYPE){
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
			trayIcon.showMsg("��Ʈ��/�̹��� ����!");
		}
		else{
			System.out.println("[TmpCUI_receiveData]File, String, Image ��𿡵� ������ ����");
		}
		
		
//		FileReceiveDataToServer receiver = new FileReceiveDataToServer(client);
//		receiver.requestReceiveData();
//		trayIcon.showMsg("���ϵ���!");
		
	}
	

	public void createGroup() {
		System.out.println("\n - �׷� ���� - ");
		System.out.print("�׷� �̸�: ");
		String name = s.next();
		System.out.print("�н�����: ");
		String password = s.next();

		System.out.println("\n�Է� Ȯ��: " + name + ", " + password + "\n");
		createClient(name, password);
		client.createGroup();
		ACCESS = true;
	}

	public void joinGroup() {
		System.out.println("\n - �׷� ���� - ");
		System.out.print("�׷� �̸�: ");
		String name = s.next();
		System.out.print("�н�����: ");
		String password = s.next();

		System.out.println("\n�Է� Ȯ��: " + name + ", " + password + "\n");
		createClient(name, password);
		client.joinGroup();
		ACCESS = true;
	}
	
	public void debugCreateGroup() {

	}

	public void debugJoinGroup() {

	}
	
	public void printHistoryList() {
		for(int i=0; i<client.getHistory().getSharedContents().size(); i++) {
			Contents contentsListInHistory = client.getHistory().getRequestContents(i);
			System.out.print("[" + i + "] ");
			
			switch(contentsListInHistory.getType()) {
			case LinKlipboard.STRING_TYPE:
				StringContents strContents = (StringContents)contentsListInHistory;
				System.out.print(" <String> ");
				System.out.print(strContents.getString());
				break;
			case LinKlipboard.FILE_TYPE:
				FileContents fileContents = (FileContents)contentsListInHistory;
				System.out.print(" <File> ");
				System.out.print(fileContents.getFileName());
				break;
			}
			
			System.out.println();
		}
	}
	
	public void receiveDataInHistory(int index) {
		
		Contents indexContents = client.getHistory().getRequestContents(index);
		int indexContentsType = indexContents.getType();
		
		System.out.println("���� ������ Ÿ�� : " + indexContentsType);
		
		/*
		if(indexContentsType == LinKlipboard.FILE_TYPE){
			FileReceiveDataToServer receiver = new FileReceiveDataToServer(client);
			receiver.requestReceiveData();
			trayIcon.showMsg("���ϵ���!");
		}
		else*/ if(indexContentsType == LinKlipboard.STRING_TYPE || indexContentsType == LinKlipboard.IMAGE_TYPE){
			ClipboardManager.writeClipboard(indexContents, indexContentsType);
			trayIcon.showMsg("��Ʈ��/�̹��� ����!");
		}
		else{
			System.out.println("[TmpCUI_receiveData]File, String, Image ��𿡵� ������ ����");
		}
	}
	
}