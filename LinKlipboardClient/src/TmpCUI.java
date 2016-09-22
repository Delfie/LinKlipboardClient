import java.util.Scanner;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
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

	/** 클라이언트 생성 */
	private void createClient(String groupName, String groupPassword) {
		client = new LinKlipboardClient(groupName, groupPassword);
	}

	/** 오류 정보 표시 */
	public void updateErrorState(String response) {
		// TEST
		System.out.println("updateErrorState: " + response);
	}

	/** 클라이언트 정보 초기화 */
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
		System.out.print("1. 생성 / 2. 접속\n>> ");
		switch (s.next()) {
		case "1":
			createGroup();
			break;
		case "2":
			joinGroup();
		}
		System.out.println("서버 접속 완료\n");
	}

	private void menu() {
		System.out.print("1. 전송 / 2. 수신\n>> ");
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
		default:
			break;
		}
	}
	
	public void fileSendDataToServer() {
		FileSendDataToServer sender = new FileSendDataToServer(client);
		sender.requestSendData();
	}

	public void sendData() {
		SendDataToServer sender = new SendDataToServer();
		sender.requestSendData();
	}
	
	
	public void receiveData() {
		/*
		 * 
		Contents latestContentsFromServer = client.getLatestContents();
		int latestContentsType = client.getLatestContents().getType();
		
		if(latestContentsType == LinKlipboard.FILE_TYPE){
			FileReceiveDataToServer receiver = new FileReceiveDataToServer(client);
			receiver.requestReceiveData();
			trayIcon.showMsg("파일도착!");
		}
		else if(latestContentsType == LinKlipboard.STRING_TYPE || latestContentsType == LinKlipboard.IMAGE_TYPE){
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
			trayIcon.showMsg("스트링/이미지 도착!");
		}
		else{
			System.out.println("[TmpCUI_receiveData]File, String, Image 어디에도 속하지 않음");
		}
		*/
		
		FileReceiveDataToServer receiver = new FileReceiveDataToServer(client);
		receiver.requestReceiveData();
		trayIcon.showMsg("파일도착!");
		
	}
	

	public void createGroup() {
		System.out.println("\n - 그룹 생성 - ");
		System.out.print("그룹 이름: ");
		String name = s.next();
		System.out.print("패스워드: ");
		String password = s.next();

		System.out.println("\n입력 확인: " + name + ", " + password + "\n");
		createClient(name, password);
		client.createGroup();
		ACCESS = true;
	}

	public void joinGroup() {
		System.out.println("\n - 그룹 생성 - ");
		System.out.print("그룹 이름: ");
		String name = s.next();
		System.out.print("패스워드: ");
		String password = s.next();

		System.out.println("\n입력 확인: " + name + ", " + password + "\n");
		createClient(name, password);
		client.joinGroup();
		ACCESS = true;
	}

	public void debugCreateGroup() {

	}

	public void debugJoinGroup() {

	}
}