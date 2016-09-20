package datamanage;

import contents.Contents;

public class ReceiveHistoryList {
	private History history;
	private Contents latestContents;
	
	/** ReceiveHistoryList 생성자 */
	public ReceiveHistoryList() {
		System.out.println("디폴트 ReceiveHistoryList 생성자 호출");
	}
	
	/** ReceiveHistoryList 생성자 
	 * @param history 서버에서 넘겨준 History 객체 */
	public ReceiveHistoryList(History history) {
		this.history = history;
		this.latestContents = null;
	}
	
	/** 히스토리 업데이트 시  
	 * History 객체를 넘겨받아서 set해줘야 되나???????????(no!!! setHistory해) */
	public void updateHistoryList(){
		//서버가 준 history의 Vector<Contents>에 따른 resizingImgContents을 초기화
		history.InitResizingImgContents();		
		//ui업데이트
	}
	
	/** sharedContents 업로드 시 서버에서 최신 Contents를 넘겨받음 */
	public void getlatestContents(Contents latestContents){
		//최신으로 받은 Contents를 Vector에 넣음
		history.addSharedContnestsInHistory(latestContents);
		//ui업데이트
	}
	
	/** history를 세팅 */
	public void setHistory(History history){
		this.history = history;
	}
	
	/** 최신 데이터(latestContents)를 세팅 */
	public void setLatestContents(Contents latestContents){
		this.latestContents = latestContents;
	}
	
	//ui업데이트?
	//history를 넘겨주고... ui에서 처리?
}
