package datamanage;

import java.util.Vector;
import javax.swing.ImageIcon;

import contents.Contents;
import contents.ImageContents;
import server_manager.LinKlipboard;

public class History {
	private Vector<Contents> sharedContents;
	private Vector<ImageIcon> resizingImgContents;
	
	// 생성자
	public History() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
	}
	
	// 전달받은 sharedContents를 Vector<Contents>에 넣고 Contents의 타입이 이미지 이면 Vector<ImageIcon> 에 resizing된 이미지를 넣음
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
	
	// Vector<Contents> sharedContents의 getter
	public Vector<Contents> getSharedContents() {
		return sharedContents;
	}
	
	// Vector<ImageIcon> resizingImgContents의 getter
	public Vector<ImageIcon> getResizingImgContents() {
		return resizingImgContents;
	}
}

class HistoryViewr {
	public void setViewer() {

	}
}