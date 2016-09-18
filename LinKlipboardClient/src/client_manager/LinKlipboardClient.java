package client_manager;

import start_manager.StartToProgram;
import transfer_manager.ReceiveDataToServer;
import transfer_manager.SendDataToServer;
import user_interface.UserInterface;

public class LinKlipboardClient {
	private static String groupName; // 그룹이름
	private String password; // 패스워드
	private String nickName; // 닉네임
	private static String fileName; // 전송받을 파일이름

	UserInterface screen; // 사용자 인터페이스(for 오류 정보 표시)
	StartToProgram startHandler; //프로그램 시작에 대한 핸들러
	SendDataToServer sendDataHandler;
	ReceiveDataToServer receiveDataHandler;

	
	/**
	 * LinKlipboardClient 생성자
	 * 
	 * @param groupName
	 * @param groupPassword
	 */
	public LinKlipboardClient(String groupName, String groupPassword) {
		System.out.println("<클라이언트 생성> groupName: " + groupName + " groupPassword: " + groupPassword);
		
		this.groupName = groupName;
		this.password = groupPassword;
		this.startHandler = new StartToProgram(this); //생성/접속할때만 생성하도록 하까?
	}

	/**
	 * LinKlipboardClient 생성자
	 * 
	 * @param groupName
	 * @param groupPassword
	 * @param screen
	 */
	public LinKlipboardClient(String groupName, String groupPassword, UserInterface screen) {
		System.out.println("<클라이언트 생성> groupName: " + groupName + " groupPassword: " + groupPassword);

		this.groupName = groupName;
		this.password = groupPassword;
		this.screen = screen;
		this.startHandler = new StartToProgram(this);
	}

	// 생성버튼을 누르면 이 메소드가 실행
	/** 그룹생성 메소드 */
	public void createGroup() {
		startHandler.createGroup();
	}

	// 접속버튼을 누르면 이 메소드가 실행
	/** 그룹접속 메소드 */
	public void joinGroup() {
		startHandler.joinGroup();
	}

	/** 그룹 정보를 초기화 */
	public void initGroupInfo() {
		this.groupName = null;
		this.password = null;
	}
	
	/** 사용자 인터페이스에 오류 정보 표시 */
	public void updateErrorState(String response) {
		this.screen.updateErrorState(response);
	}

	/** 클라이언트가 입력한 그룹이름 반환 */
	public static String getGroupName() {
		return groupName;
	}

	/** 클라이언트가 입력한 그룹패스워드 반환 */
	public String getGroupPassword() {
		return password;
	}

	/** 클라이언트가 입력한 닉네임 반환 */
	public String getNickName() {
		return nickName;
	}
	
	/** 클라이언트가 전송받을 파일 이름 반환 */
	public static String getFileName() {
		return fileName;
	}
	
	/** 클라이언트의 닉네임을 세팅 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/** 클라이언트가 전송받을 파일 이름을 세팅 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}