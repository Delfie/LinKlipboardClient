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
	
	/** @return Ŭ�����忡 �ִ� ������ ��θ� */
	public static String getFilePathInSystemClipboard() {

		try {
			//�ý��� Ŭ�����忡�� ������ ����
			Transferable contents = ClipboardManager.getSystmeClipboardContets();
			
			String fileTotalPath = contents.getTransferData(ClipboardManager.setDataFlavor(contents)).toString();
		
			// ��θ� ������ ���� �� ���� []�� ����
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
	
	/** ������ ���� ��ο� ������ ���� */
	public void createSendFile() {
		sendFile = new File(filePath); // ���� ����
	}
	
	/** ���� ������ ��ȯ */
	public File getSendFile() {
		return this.sendFile;
	}
	
	/** ���� ������ �̸��� ��ȯ */
	public String getFileName() {
		fileName = sendFile.getName(); // ���� �̸� �޾ƿ���
		return this.fileName;
	}
	
	/** ���� ������ ũ�⸦ ��ȯ */
	public long getFileSize() {
		fileSize = sendFile.length(); // ���� ũ�� �޾ƿ���
		return this.fileSize;
	}
	
}