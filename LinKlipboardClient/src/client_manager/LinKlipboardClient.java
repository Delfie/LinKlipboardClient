package client_manager;

import start_manager.StartToProgram;
import transfer_manager.ReceiveDataToServer;
import transfer_manager.SendDataToServer;
import user_interface.UserInterface;

public class LinKlipboardClient {
	private static String groupName; // �׷��̸�
	private String password; // �н�����
	private String nickName; // �г���
	private static String fileName; // ���۹��� �����̸�

	UserInterface screen; // ����� �������̽�(for ���� ���� ǥ��)
	StartToProgram startHandler; //���α׷� ���ۿ� ���� �ڵ鷯
	SendDataToServer sendDataHandler;
	ReceiveDataToServer receiveDataHandler;

	
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
	
	/** Ŭ���̾�Ʈ�� �г����� ���� */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/** Ŭ���̾�Ʈ�� ���۹��� ���� �̸��� ���� */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}