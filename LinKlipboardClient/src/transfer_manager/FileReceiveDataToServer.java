package transfer_manager;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class FileReceiveDataToServer extends Thread {

	private Socket socket; // 서버와 연결할 소켓
	private String ipAddr = LinKlipboard.SERVER_IP;
	private final static int portNum = LinKlipboard.FTP_PORT;

	private String response; // 서버로부터 받은 응답 정보
	private ResponseHandler responseHandler; // 응답에 대한 처리

	private LinKlipboardClient client;

	// 파일을 읽고 쓰기위한 파일 스트림 설정
	private FileOutputStream fos;
	private DataInputStream dis;

	private static File fileReceiveFolder;
	private final static String fileReceiveDir = "C:\\Program Files\\LinKlipboard";
	private String receiveFileName; // 전송받을 파일의 이름
	private String receiveFilePath; // 전송받을 파일의 경로

	/** FileReceiveDataToServer 생성자 */
	public FileReceiveDataToServer() {
		this.receiveFileName = LinKlipboardClient.getFileName();
		this.receiveFilePath = fileReceiveDir + "\\" + this.receiveFileName;
	}

	/** FileReceiveDataToServer 생성자 */
	public FileReceiveDataToServer(String fileName) {
		this.receiveFilePath = fileReceiveDir + "\\" + fileName;
	}

	/** FileReceiveDataToServer 생성자 */
	public FileReceiveDataToServer(LinKlipboardClient client) {
		this.client = client;
		this.receiveFileName = LinKlipboardClient.getFileName();
		this.receiveFilePath = fileReceiveDir + "\\" + this.receiveFileName;
	}

	/** 파일 데이터 수신 메소드 (FileReceiveDataToServer 서블릿 호출) */
	public void requestReceiveData() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/FileReceiveDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹이름)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[FileReceiveDataToServer] 보낼 전체 데이터 확인" + header); // delf

			bout.write(header);
			bout.flush();
			bout.close();

			// 서버로부터 받을 데이터(응답정보)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
				this.response = response;
			}
			System.out.println("[FileReceiveDataToServer] 서버로부터의 응답 데이터 확인: " + this.response); // delf
			bin.close();

			exceptionHandling(this.response);

			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[FileReceiveDataToServer] 소켓 연결");
				this.start();
			}

			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 예외 처리
	 * 
	 * @param response
	 *            클라이언트 요청에 대한 서버의 응답
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, client);
		if(response != null){
			responseHandler.responseHandlerForTransfer();
		}
		else{
			System.out.println("[FileReceiveDataToServer] Error!!!! 서버가 보낸 response가 null임");
		}
	}

	/** 서버와의 연결을 위한 소켓과 스트림 설정 */
	public void setConnection() {
		try {
			// 소켓 접속 설정
			socket = new Socket(ipAddr, portNum);
			// 스트림 설정
			dis = new DataInputStream(socket.getInputStream()); // 바이트 배열을 받기 위한 데이터스트림 생성
			System.out.println("[FileReceiveDataToServer] 연결 설정 끝"); // delf

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();
		try {
			int byteSize = 65536;
			byte[] ReceiveByteArrayToFile = new byte[byteSize]; // 바이트 배열 생성

			fos = new FileOutputStream(receiveFilePath); // 지정한 경로에 바이트 배열을 쓰기위한 파일 스트림 생성

			int EndOfFile = 0; // 파일의 끝(-1)을 알리는 변수 선언
			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) { // ReceiveByteArrayToFile의 크기인 1024바이트 만큼 DataInputStream에서 바이트를 읽어 바이트 배열에 저장, EndOfFile에는 1024가 들어있음
																			// DataInputStream에서 바이트를 다 읽어올 때(EndOfFile=-1 일 때)까지 반복
				fos.write(ReceiveByteArrayToFile, 0, EndOfFile); // ReceiveByteArrayToFile에 들어있는 바이트를 0~EndOfFile=1024 만큼 FileOutputStream으로 보냄
			}

			closeSocket();

			// reply = JOptionPane.showConfirmDialog(null, "받을래?", null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			//
			// if (reply == JOptionPane.YES_OPTION) {
			// // 받은 파일을 클립보드에 삽입
			// setFileInClipboard(receiveFilePath + receiveFileName);
			// System.out.println("클립보드 삽입 완료");
			// }
			// else {
			// initDir();
			// }

			createFileReceiveFolder();
			initDir();

			setFileInClipboard(receiveFilePath);
			System.out.println("[FileReceiveDataToServer] 클립보드 삽입 완료");

		} catch (IOException e) {
			closeSocket();
			return;
		}
	}

	/** 전송받은 파일을 저장할 폴더(LinKlipboard) 생성 */
	private void createFileReceiveFolder() {
		fileReceiveFolder = new File(fileReceiveDir);

		// C:\\Program Files에 LinKlipboard폴더가 존재하지 않으면
		if (!fileReceiveFolder.exists()) {
			fileReceiveFolder.mkdir(); // 폴더 생성
			System.out.println("C:\\Program Files에 LinKlipboard폴더 생성");
		} else {
			initDir();
		}
	}

	/** 폴더 안의 파일들을 삭제(파일인 경우만 생각.) */
	private void initDir() {
		File[] innerFile = fileReceiveFolder.listFiles(); // 폴더 내 존재하는 파일을 innerFile에 넣음
		for (File file : innerFile) { // innerFile의 크기만큼 for문을 돌면서
			file.delete(); // 파일 삭제
			System.out.println("C:\\Program Files\\LinKlipboard폴더 안의 파일 삭제");
		}

		// Dir안에 파일이 하나만 있는 경우에 사용 가능
		// innerFile[0].delete();
	}

	/** 열려있는 소켓을 모두 닫는다. */
	private void closeSocket() {
		try {
			fos.close();
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 전송가능한 File 객체 */
	static class FileTransferable implements Transferable {
		private ArrayList<File> listOfFiles;

		public FileTransferable(ArrayList<File> listOfFiles) {
			this.listOfFiles = listOfFiles;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.javaFileListFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.javaFileListFlavor.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			return listOfFiles;
		}
	}

	/**
	 * 지정한 경로에 위치한 파일을 시스템 클립보드에 복사한다.
	 * 
	 * @param receiveFilePath
	 *            파일을 저장할 경로
	 */
	public void setFileInClipboard(String receiveFilePath) {
		File file = new File(receiveFilePath);
		ArrayList<File> listOfFiles = new ArrayList<File>();
		listOfFiles.add(file);

		FileTransferable ft = new FileTransferable(listOfFiles);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
				System.out.println("Lost ownership");
			}
		});
	}
}