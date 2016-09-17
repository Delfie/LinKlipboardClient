import java.util.Scanner;

import client_manager.LinKlipboardClient;
import transfer_manager.SendDataToServer;

public class TmpCUI {

	private LinKlipboardClient client;

	public static Scanner s = new Scanner(System.in);

	private static boolean ACCESS;

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
		client.initResponse();
	}

	public static void main(String[] args) {
		new TmpCUI().run();
	}

	public void run() {
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
		System.out.print("1. ���� / 2. ����\n>> ");
		switch (s.next()) {
		case "1":
			sendData();
			break;
			
		case "2":
			break;
		default:
			break;
		}
	}
	
	public void sendData() {
		SendDataToServer sender = new SendDataToServer();
		sender.requestReceiveData();
	}
	
	public void receiveData() {
		
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
}