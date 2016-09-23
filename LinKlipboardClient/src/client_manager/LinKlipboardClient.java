package client_manager;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

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

	private static History history = new History(); // 히스토리
	private static Contents latestContents; // 최신데이터

	UserInterface screen; // 사용자 인터페이스(for 오류 정보 표시)
	StartToProgram startHandler; // 프로그램 시작에 대한 핸들러

	private static File fileReceiveFolder; // 받은 FileContents를 임시로 저장할 폴더
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

		createFileReceiveFolder(); // LinKlipboard folder 생성

		this.receiveContentsThread = new ReceiveContents(); // 최신 Contents를 받는 스레드 생성
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

		this.history = new History();

		createFileReceiveFolder(); // LinKlipboard folder 생성

		this.receiveContentsThread = new ReceiveContents(); // Contents를 받는 스레드 생성
		receiveContentsThread.start(); // 스레드 start
	}

	/** 전송받은 파일을 저장할 폴더(LinKlipboard) 생성 */
	private void createFileReceiveFolder() {
		fileReceiveFolder = new File(LinKlipboard.fileReceiveDir);

		// C:\\Program Files에 LinKlipboard폴더가 존재하지 않으면
		if (!fileReceiveFolder.exists()) {
			fileReceiveFolder.mkdir(); // 폴더 생성
			System.out.println("[FolderManager] C:\\Program Files에 LinKlipboard 폴더 생성");
		}
	}

	/** 폴더 안의 파일들을 삭제(파일인 경우만 생각.) */
	public static void initDir() {
		File[] innerFile = fileReceiveFolder.listFiles(); // 폴더 내 존재하는 파일을 innerFile에 넣음

		for (File file : innerFile) { // innerFile의 크기만큼 for문을 돌면서
			file.delete(); // 파일 삭제
			System.out.println("[FolderManager] C:\\Program Files\\LinKlipboard 폴더 안의 파일 삭제");
		}

		// Dir안에 파일이 하나만 있는 경우에 사용 가능
		// innerFile[0].delete();
	}

	// 생성버튼을 누르면 이 메소드가 실행
	/** 그룹생성 메소드 */
	public void createGroup() {
		new StartToProgram(this, "create");
	}

	// 접속버튼을 누르면 이 메소드가 실행
	/** 그룹접속 메소드 */
	public void joinGroup() {
		new StartToProgram(this, "join");
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
	public History getHistory() {
		return history;
	}

	/** 클라이언트의 닉네임을 세팅 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/** 클라이언트가 전송받을 파일 이름을 세팅 */
	public void setFileName(String fileName) {
		LinKlipboardClient.fileName = fileName;
	}

	/** 클라이언트 히스토리의 마지막 Contest를 가장 최근에 공유한 Contents로 세팅 */
	public void settLatestContents() {
		LinKlipboardClient.latestContents = history.getlastContents();
	}

	/** 클라이언트의 history를 세팅 */
	public static void setHistory() {
		history.setHistory();
	}

	/**
	 * 클라이언트의 history를 세팅
	 * 
	 * @param updateHistory
	 *            server에서 받아온 Vector<Contents>
	 */
	public static void setHistory(Vector<Contents> updateHistory) {
		history.setHistory(updateHistory);
	}

	/**
	 * 서버가 전송하는 Contents를 받는 클래스 스레드를 상속받아 서버에서 전달해주는 메세지를 기다린다.
	 */
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
				in = new ObjectInputStream(socket.getInputStream()); // 스트림 설정
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("[ReceiveContents] 연결 설정 끝");
		}

		@Override
		public void run() {
			while (true) {
				waitToServer();
				setConnection();
				try {
					latestContents = (Contents) in.readObject(); // Contents 객체수신
					history.addSharedContentsInHistory(latestContents); // 공유받은 최신Contents를 history에 추가
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