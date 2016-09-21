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
	private static String groupName; // 그룹이름
	private String password; // 패스워드
	private String nickName = null; // 닉네임
	
	private static String fileName = null; // 전송받을 파일이름
	
	private History history; // 히스토리(미완)
	private static Contents latestContents; //최신데이터(미완)

	UserInterface screen; // 사용자 인터페이스(for 오류 정보 표시)
	StartToProgram startHandler; //프로그램 시작에 대한 핸들러
	
	ReceiveContents receiveContentsThread; // 서버로부터 받을 Contents

	
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
		this.history = new History();
		
		// 도연
		this.receiveContentsThread = new ReceiveContents(); // Contents를 받는 스레드 생성
		receiveContentsThread.start(); // 스레드 start
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
		this.history = new History();
		
		// 도연
		this.receiveContentsThread = new ReceiveContents(); // Contents를 받는 스레드 생성
		receiveContentsThread.start(); // 스레드 start
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
	
	/** 클라이언트가 공유한 최근 Contents 반환 */
	public Contents getLatestContents() {
		return latestContents;
	}
	
	/** 클라이언트의 history 반환 */
	public History getHistory(){
		return history;
	}
	
	/** 클라이언트의 닉네임을 세팅 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	/** 클라이언트가 전송받을 파일 이름을 세팅 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/** 서버가 전송하는 Contents를 받기 */
	class ReceiveContents extends Thread {
		private ServerSocket listener;
		private Socket socket;
		private ObjectInputStream in; // Contents를 받기 위한 스트림
		
		/** 서버의 연결을 기다리는 소켓 설정 */
		public void waitToServer() {
			try {
				listener = new ServerSocket(LinKlipboard.FTP_PORT);
				socket = listener.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/** 서버와의 연결을 위한 스트림 설정 */
		public void setConnection() {
			try {
				in = new ObjectInputStream(socket.getInputStream()); //스트림 설정
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("연결 설정 끝");
		}
		
		@Override
		public void run() {
			while (true) {
				waitToServer();
				setConnection();
				try {
					latestContents = (Contents) in.readObject(); // Contents 객체수신
					history.addSharedContnestsInHistory(latestContents); // history에 추가
					// (알림 띄우기 추가)
				} catch (ClassNotFoundException e) {
					this.start();
				} catch (IOException e) {
					this.start();
				}
			}
		}
		
	}
}