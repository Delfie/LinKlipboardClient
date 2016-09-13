package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

import javax.swing.ImageIcon;

import server_manager.LinKlipboard;

public abstract class Contents {

	protected Date date;
	protected String sharer;
	protected int type;

	public Contents(String sharer) {
		date = new Date();
		this.sharer = sharer;
	}

	public String getSharer() {
		return sharer;
	}

	public Date getDate() {
		return date;
	}

	public int getType() {
		return type;
	}

	/** �����͸� ���� 
	 * @param in ���� ���� ��Ʈ�� 
	 * @return �����͸� �����ϰ� �ڱ� �ڽ��� ��ȯ */
	abstract public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException;
}