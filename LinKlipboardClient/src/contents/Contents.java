package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public abstract class Contents implements Serializable {

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