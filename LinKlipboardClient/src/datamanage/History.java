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

	/** History 생성자 */
	public History() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
		historySize = LinKlipboard.HISTORY_DEFAULT;
	}

	/**
	 * 전달받은 latestContents를 Vector<Contents>에 넣고 Contents의 타입이 이미지 이면 Vector<ImageIcon> 에 resizing된 이미지를 넣음
	 * 
	 * @param latestContents
	 *            새로 공유받은 최신의 Contents
	 */
	public void addSharedContnestsInHistory(Contents latestContents) {
		removeContents();
		this.sharedContents.add(latestContents);
		setResizingImgContents(latestContents);
	}

	/** 히스토리의 데이터가 historySize만큼 차면 데이터 하나 삭제 */
	public void removeContents() {
		if (this.sharedContents.size() == this.historySize) {
			this.sharedContents.remove(0);
		}
	}

	/** Vector<Contents>에 있는 Contents들이 어떤 타입인지에 따라서 resizingImgContents 초기화 */
	public void InitResizingImgContents() {
		// Vector<Contents> 값을 돌면서 Vector<ImageIcon>를 채워 넣는다.
		for(int i=0; i<sharedContents.size(); i++){
			setResizingImgContents(sharedContents.elementAt(i)); 
		}
	}

	/** 전달받은 Contents가 어떤 타입인지에 따라서 resizingImgContents를 세팅 */
	public void setResizingImgContents(Contents Contents) {
		if (Contents.getType() == LinKlipboard.IMAGE_TYPE) {
			ImageContents ImageData = (ImageContents) Contents;
			this.resizingImgContents.add(ImageData.getResizingImageIcon());
		} else {
			this.resizingImgContents.add(null);
		}
	}

	/** 사용자가 원하는 히스토리의 크기 세팅 */
	public void setHistorySize(int historySize) {
		this.historySize = historySize;
	}
	
	/** Vector<Contents>와 Vector<ImageIcon>를 세팅 */
	public void setVector(Vector<Contents> sharedContents, Vector<ImageIcon> resizingImgContents) {
		this.sharedContents = sharedContents;
		this.resizingImgContents = resizingImgContents;
	}
	
	/** index에 해당하는 Contents를 반환 */
	public Contents getRequestContents(int index) {
		return sharedContents.elementAt(index);
	}

	/** 사용자의 히스토리 크기 반환 */
	public int getHistorySize() {
		return this.historySize;
	}

	/** Vector<Contents> sharedContents를 반환 */
	public Vector<Contents> getSharedContents() {
		return this.sharedContents;
	}

	/** Vector<ImageIcon> resizingImgContents를 반환 */
	public Vector<ImageIcon> getResizingImgContents() {
		return this.resizingImgContents;
	}
}

class HistoryViewr {
	public void setViewer() {

	}
}