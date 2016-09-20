package datamanage;

import java.util.Vector;
import javax.swing.ImageIcon;

import contents.Contents;
import contents.ImageContents;
import server_manager.LinKlipboard;

public class History {
	private Vector<Contents> sharedContents;
	private Vector<ImageIcon> resizingImgContents;
	
	// ������
	public History() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
	}
	
	// ���޹��� sharedContents�� Vector<Contents>�� �ְ� Contents�� Ÿ���� �̹��� �̸� Vector<ImageIcon> �� resizing�� �̹����� ����
	public void addSharedContnestsInHistory(Contents sharedContents) {
		if(this.sharedContents.size() == 10)
			this.sharedContents.remove(0);
		
		this.sharedContents.add(sharedContents);
		
		if(sharedContents.getType() == LinKlipboard.IMAGE_TYPE) {
			ImageContents ImageData = (ImageContents) sharedContents;
			this.resizingImgContents.add(ImageData.getResizingImageIcon());
		}
		else
			this.resizingImgContents.add(null);
		
	}
	
	// Vector<Contents> sharedContents�� getter
	public Vector<Contents> getSharedContents() {
		return sharedContents;
	}
	
	// Vector<ImageIcon> resizingImgContents�� getter
	public Vector<ImageIcon> getResizingImgContents() {
		return resizingImgContents;
	}
}

class HistoryViewr {
	public void setViewer() {

	}
}