package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

// �׽�Ʈ
public abstract class Contents implements Serializable {

	private int serialNum = LinKlipboard.NULL;

	protected String date;
	protected String sharer;
	protected int type;

	public Contents() {
		// date = Logger.now();
	}

	public Contents(String sharer) {
		this();
		this.sharer = sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}

	public int getSerialNum() {
		return serialNum;
	}

	public String getSharer() {
		return sharer;
	}

	public String getDate() {
		return date;
	}

	public int getType() {
		return type;
	}

	/**
	 * �����͸� ����
	 * 
	 * @param in
	 *            ���� ���� ��Ʈ��
	 * @return �����͸� �����ϰ� �ڱ� �ڽ��� ��ȯ
	 */
	abstract public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException;
}