package datamanage;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;

import contents.Contents;
import contents.ImageContents;
import server_manager.LinKlipboard;

public class History {
	private Vector<Contents> sharedContents;
	private Vector<ImageIcon> resizingImgContents;
	private int historySize;

	/** History ������ */
	public History() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
		historySize = LinKlipboard.HISTORY_DEFAULT;
	}

	/**
	 * ���޹��� latestContents�� Vector<Contents>�� �ְ� Contents�� Ÿ���� �̹��� �̸� Vector<ImageIcon> �� resizing�� �̹����� ����
	 * 
	 * @param latestContents
	 *            ���� �������� �ֽ��� Contents
	 */
	public void addSharedContnestsInHistory(Contents latestContents) {
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

	/** Vector<Contents>�� �ִ� Contents���� � Ÿ�������� ���� resizingImgContents �ʱ�ȭ */
	public void InitResizingImgContents() {
		// Vector<Contents> ���� ���鼭 Vector<ImageIcon>�� ä�� �ִ´�.
		for(int i=0; i<sharedContents.size(); i++){
			setResizingImgContents(sharedContents.elementAt(i)); 
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

	/** ����ڰ� ���ϴ� �����丮�� ũ�� ���� */
	public void setHistorySize(int historySize) {
		this.historySize = historySize;
	}
	
	/** Vector<Contents>�� Vector<ImageIcon>�� ���� */
	public void setVector(Vector<Contents> sharedContents, Vector<ImageIcon> resizingImgContents) {
		this.sharedContents = sharedContents;
		this.resizingImgContents = resizingImgContents;
	}
	
	/** index�� �ش��ϴ� Contents�� ��ȯ */
	public Contents getRequestContents(int index) {
		return sharedContents.elementAt(index);
	}

	/** ������� �����丮 ũ�� ��ȯ */
	public int getHistorySize() {
		return this.historySize;
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