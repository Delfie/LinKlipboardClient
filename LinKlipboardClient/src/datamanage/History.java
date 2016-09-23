package datamanage;

import java.util.Vector;

import javax.swing.ImageIcon;

import contents.Contents;
import contents.ImageContents;
import server_manager.LinKlipboard;

public class History {
	private Vector<Contents> sharedContents;
	private Vector<ImageIcon> resizingImgContents;
	private int historySize = LinKlipboard.HISTORY_DEFAULT;; // ����ڰ� ���ϴ� �����丮 ũ��

	/** History ������ */
	public History() {
	}

	/**
	 * ���޹��� latestContents�� Vector<Contents>�� �ְ� Contents�� Ÿ���� �̹��� �̸� Vector<ImageIcon> �� resizing�� �̹����� ����
	 * 
	 * @param latestContents
	 *            ���� �������� �ֽ��� Contents
	 */
	public void addSharedContentsInHistory(Contents latestContents) {
		removeContents();
		this.sharedContents.add(latestContents);
		setResizingImgContents(latestContents);
	}

	/** �����丮�� �����Ͱ� historySize��ŭ ���� ������ �ϳ� ���� */
	public void removeContents() {
		if (this.sharedContents.size() == this.historySize) {
			this.sharedContents.remove(0);
		}
	}

	/** ���޹��� Contents�� � Ÿ�������� ���� resizingImgContents�� ���� */
	public void setResizingImgContents(Contents Contents) {
		if (Contents.getType() == LinKlipboard.IMAGE_TYPE) {
			ImageContents ImageData = (ImageContents) Contents;
			this.resizingImgContents.add(ImageData.getResizingImageIcon());
		} else {
			this.resizingImgContents.add(null);
		}
	}

	/** ���� Vector<Contents>�� sharedContents �ʱ�ȭ */
	public void initSharedContents(Vector<Contents> updateHistory) {
		sharedContents = updateHistory;
	}

	/** Vector<Contents>�� �ִ� Contents���� � Ÿ�������� ���� resizingImgContents �ʱ�ȭ */
	public void InitResizingImgContents() {
		// Vector<Contents> ���� ���鼭 Vector<ImageIcon>�� ä�� �ִ´�.
		for (int i = 0; i < sharedContents.size(); i++) {
			setResizingImgContents(sharedContents.elementAt(i));
		}
	}

	/** ����ڰ� ���ϴ� �����丮�� ũ�� ���� */
	public void setHistorySize(int historySize) {
		this.historySize = historySize;
	}

	// /** Vector<Contents>�� Vector<ImageIcon>�� ���� */
	// public void setVector(Vector<Contents> sharedContents, Vector<ImageIcon> resizingImgContents) {
	// this.sharedContents = sharedContents;
	// this.resizingImgContents = resizingImgContents;
	// }

	/** �����丮�� Contents�� ������ȣ�� ���� */
	public void setContentsSerialNum(Contents sharedContents, int serialNum) {
		sharedContents.setSerialNum(serialNum);
	}

	public void setHistory() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
	}

	public void setHistory(Vector<Contents> updateHistory) {
		initSharedContents(updateHistory);
		InitResizingImgContents();
	}

	/** index�� �ش��ϴ� Contents�� ��ȯ */
	public Contents getRequestContents(int index) {
		return sharedContents.elementAt(index);
	}

	/** �����丮�� ������ Contents�� ��ȯ */
	public Contents getlastContents() {
		return sharedContents.lastElement();
	}

	/** ����ڿ��� �������� �����丮 ũ�� ��ȯ */
	public int getHistorySize() {
		return this.historySize;
	}

	/** ������� ���� �����丮�� ����ִ� Contents ũ�� ��ȯ */
	public int getSizeOfContentsInHistory() {
		return this.sharedContents.size();
	}

	/** Vector<Contents> sharedContents�� ��ȯ */
	public Vector<Contents> getSharedContents() {
		return this.sharedContents;
	}

	/** Vector<ImageIcon> resizingImgContents�� ��ȯ */
	public Vector<ImageIcon> getResizingImgContents() {
		return this.resizingImgContents;
	}
}

class HistoryViewr {
	public void setViewer() {

	}
}