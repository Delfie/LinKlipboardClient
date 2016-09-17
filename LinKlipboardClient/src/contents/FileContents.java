package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

public class FileContents extends Contents implements Serializable {
	private String fileName;

	public FileContents() {
		super("");
	}
	public FileContents(String sharer, String name) {
		super(sharer);
		type = LinKlipboard.FILE_TYPE;
		this.fileName = name;
	}

	public String getfilePath() {
		return fileName;
	}

	@Override
	public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
}