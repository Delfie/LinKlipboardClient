package client_manager;

import java.io.File;
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
	
	private History history; // �����丮
	private static Contents latestContents; //�ֽŵ�����

	UserInterface screen; // ����� �������̽�(for ���� ���� ǥ��)
	StartToProgram startHandler; //���α׷� ���ۿ� ���� �ڵ鷯
	
	private static File fileReceiveFolder; // ���� FileContents�� �ӽ÷� ������ ����
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
		
		this.history = new History();
		
		createFileReceiveFolder(); // LinKlipboard folder ����
		
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
		
		this.history = new History();
		
		createFileReceiveFolder(); // LinKlipboard folder ����
		
		this.receiveContentsThread = new ReceiveContents(); // Contents�� �޴� ������ ����
		receiveContentsThread.start(); // ������ start
	}
	
	/** ���۹��� ������ ������ ����(LinKlipboard) ���� */
	private void createFileReceiveFolder() {
		fileReceiveFolder = new File(LinKlipboard.fileReceiveDir);

		// C:\\Program Files�� LinKlipboard������ �������� ������
		if (!fileReceiveFolder.exists()) {
			fileReceiveFolder.mkdir(); // ���� ����
			System.out.println("[FolderManager] C:\\Program Files�� LinKlipboard ���� ����");
		}
	}

	/** ���� ���� ���ϵ��� ����(������ ��츸 ����.) */
	public static void initDir() {
		File[] innerFile = fileReceiveFolder.listFiles(); // ���� �� �����ϴ� ������ innerFile�� ����

		for (File file : innerFile) { // innerFile�� ũ�⸸ŭ for���� ���鼭
			file.delete(); // ���� ����
			System.out.println("[FolderManager] C:\\Program Files\\LinKlipboard ���� ���� ���� ����");
		}

		// Dir�ȿ� ������ �ϳ��� �ִ� ��쿡 ��� ����
		// innerFile[0].delete();
	}

	// ������ư�� ������ �� �޼ҵ尡 ����
	/** �׷���� �޼ҵ� */
	public void createGroup() {
		this.startHandler = new StartToProgram(this);
		startHandler.createGroup();
	}

	// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
	/** �׷����� �޼ҵ� */
	public void joinGroup() {
		this.startHandler = new StartToProgram(this);
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
	
	// ���� �����丮 �׽�Ʈ
	/** Ŭ���̾�Ʈ�� ������ �ֱ� Contents�� �����丮�� ������ Contest�� ���� */
	public void settLatestContents() {
		this.latestContents = history.getlastContents();
	}
	
	
	/** ������ �����ϴ� Contents�� �޴� Ŭ���� 
	 * �����带 ��ӹ޾� �������� �������ִ� �޼����� ��ٸ���. */
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
			System.out.println("[ReceiveContents] ���� ���� ��");
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