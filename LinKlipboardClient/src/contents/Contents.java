package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

// Å×½ºÆ®
public abstract class Contents implements Serializable {

	private static final long serialVersionUID = 4131370422438049456L;

	private int serialNum = LinKlipboard.NULL;

	protected String date;
	protected String sharer;
	protected int type;

	public Contents() {
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

}