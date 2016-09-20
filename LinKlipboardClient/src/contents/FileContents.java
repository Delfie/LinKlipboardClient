package contents;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

public class FileContents extends Contents implements Serializable {
	private String fileName;
	private long fileSize;
	private String filePath;

	public FileContents() {
		super();
		type = LinKlipboard.FILE_TYPE;
		//createSendFile();
	}
	
	public FileContents(File file) {
		fileName = file.getName();
		fileSize = file.length();
	}
	
	public FileContents(String sharer, String path) {
		super(sharer);
		type = LinKlipboard.FILE_TYPE;
	}

	@Override
	public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException {
		return null;
	}
	

	/** ���� ������ �̸��� ��ȯ */
	public String getFileName() {
		return this.fileName;
	}
	
	/** ���� ������ ũ�⸦ ��ȯ */
	public long getFileSize() {
		return this.fileSize;
	}
	
}