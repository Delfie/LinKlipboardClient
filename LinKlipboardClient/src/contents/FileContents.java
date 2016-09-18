package contents;

import java.awt.HeadlessException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import client_manager.ClipboardManager;
import server_manager.LinKlipboard;

public class FileContents extends Contents implements Serializable {
	private File sendFile;
	private String filePath;
	private String fileName;
	private long fileSize;

	public FileContents() {
		super();
		type = LinKlipboard.FILE_TYPE;
		this.filePath = getFilePathInSystemClipboard();
		createSendFile();
	}
	
	public FileContents(String sharer, String path) {
		super(sharer);
		type = LinKlipboard.FILE_TYPE;
		this.filePath = path;
	}

	public String getfilePath() {
		return filePath;
	}

	@Override
	public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException {
		return null;
	}
	
	/** @return 클립보드에 있는 파일의 경로명 */
	public static String getFilePathInSystemClipboard() {

		try {
			//시스템 클립보드에서 내용을 추출
			Transferable contents = ClipboardManager.getSystmeClipboardContets();
			
			String fileTotalPath = contents.getTransferData(ClipboardManager.setDataFlavor(contents)).toString();
		
			// 경로명만 얻어오기 위해 양 끝의 []를 제거
			return fileTotalPath.substring(1, fileTotalPath.length() - 1);
			
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 지정된 파일 경로에 파일을 생성 */
	public void createSendFile() {
		sendFile = new File(filePath); // 파일 생성
	}
	
	/** 보낼 파일을 반환 */
	public File getSendFile() {
		return this.sendFile;
	}
	
	/** 보낼 파일의 이름을 반환 */
	public String getFileName() {
		fileName = sendFile.getName(); // 파일 이름 받아오기
		return this.fileName;
	}
	
	/** 보낼 파일의 크기를 반환 */
	public long getFileSize() {
		fileSize = sendFile.length(); // 파일 크기 받아오기
		return this.fileSize;
	}
	
}