package client_manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import contents.Contents;
import datamanage.History;
import server_manager.LinKlipboard;
import start_manager.StartToProgram;
import user_interface.UserInterface;

public class LinKlipboardClient {
	private static String groupName; // �׷��̸�
	private String password; // �н�����
	private String nickName = null; // �г���
	
	private static String fileName = null; // ���۹��� �����̸�
	
	private History history; // �����丮(�̿�)
	private static Contents latestContents; //�ֽŵ�����(�̿�)

	UserInterface screen; // ����� �������̽�(for ���� ���� ǥ��)
	StartToProgram startHandler; //���α׷� ���ۿ� ���� �ڵ鷯
	
	ReceiveContents receiveContentsThread; // �����κ��� ���� Contents

	
	/**
	 * LinKlipboardClient ������
	 * 
	 * @param groupName
	 * @param groupPassword
	 */
	public LinKlipboardClient(String groupName, String groupPassword) {
		System.out.println("<Ŭ���̾�Ʈ ����> groupName: " + groupName + " groupPassword: " + groupPassword);
		
		this.groupName = groupName;
		this.password = groupPassword;
		this.startHandler = new StartToProgram(this); //����/�����Ҷ��� �����ϵ��� �ϱ�?
		this.history = new History();
		
		// ����
		this.receiveContentsThread = new ReceiveContents(); // Contents�� �޴� ������ ����
		receiveContentsThread.start(); // ������ start
	}

	/**
	 * LinKlipboardClient ������
	 * 
	 * @param groupName
	 * @param groupPassword
	 * @param screen
	 */
	public LinKlipboardClient(String groupName, String groupPassword, UserInterface screen) {
		System.out.println("<Ŭ���̾�Ʈ ����> groupName: " + groupName + " groupPassword: " + groupPassword);

		this.groupName = groupName;
		this.password = groupPassword;
		this.screen = screen;
		this.startHandler = new StartToProgram(this);
		this.history = new History();
		
		// ����
		this.receiveContentsThread = new ReceiveContents(); // Contents�� �޴� ������ ����
		receiveContentsThread.start(); // ������ start
	}

	// ������ư�� ������ �� �޼ҵ尡 ����
	/** �׷���� �޼ҵ� */
	public void createGroup() {
		startHandler.createGroup();
	}

	// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
	/** �׷����� �޼ҵ� */
	public void joinGroup() {
		startHandler.joinGroup();
	}

	/** �׷� ������ �ʱ�ȭ */
	public void initGroupInfo() {
		this.groupName = null;
		this.password = null;
	}
	
	/** ����� �������̽��� ���� ���� ǥ�� */
	public void updateErrorState(String response) {
		this.screen.updateErrorState(response);
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
	
	/** Ŭ���̾�Ʈ�� ���۹��� ���� �̸� ��ȯ */
	public static String getFileName() {
		return fileName;
	}
	
	/** Ŭ���̾�Ʈ�� ������ �ֱ� Contents ��ȯ */
	public Contents getLatestContents() {
		return latestContents;
	}
	
	/** Ŭ���̾�Ʈ�� history ��ȯ */
	public History getHistory(){
		return history;
	}
	
	/** Ŭ���̾�Ʈ�� �г����� ���� */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/** Ŭ���̾�Ʈ�� ���۹��� ���� �̸��� ���� */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/** ������ �����ϴ� Contents�� �ޱ� */
	class ReceiveContents extends Thread {
		private ServerSocket listener;
		private Socket socket;
		private ObjectInputStream in; // Contents�� �ޱ� ���� ��Ʈ��
		
		/** ������ ������ ��ٸ��� ���� ���� */
		public void waitToServer() {
			try {
				listener = new ServerSocket(LinKlipboard.FTP_PORT);
				socket = listener.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/** �������� ������ ���� ��Ʈ�� ���� */
		public void setConnection() {
			try {
				in = new ObjectInputStream(socket.getInputStream()); //��Ʈ�� ����
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("���� ���� ��");
		}
		
		@Override
		public void run() {
			while (true) {
				waitToServer();
				setConnection();
				try {
					latestContents = (Contents) in.readObject(); // Contents ��ü����
					history.addSharedContnestsInHistory(latestContents); // history�� �߰�
					// (�˸� ���� �߰�)
				} catch (ClassNotFoundException e) {
					this.start();
				} catch (IOException e) {
					this.start();
				}
			}
		}
		
	}
}