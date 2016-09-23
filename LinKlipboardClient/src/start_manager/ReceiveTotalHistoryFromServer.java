//package start_manager;
//
//import java.util.Vector;
//
//import javax.swing.ImageIcon;
//
//import contents.Contents;
//import datamanage.History;
//
//public class ReceiveTotalHistoryFromServer {
//	private History history;
//	
//	/** ReceiveHistoryList 생성자 */
//	public ReceiveTotalHistoryFromServer() {
//		System.out.println("디폴트 ReceiveHistoryList 생성자 호출");
//	}
//	
//	/** ReceiveHistoryList 생성자 
//	 * @param history 서버에서 넘겨준 History 객체 */
//	public ReceiveTotalHistoryFromServer(History history) {
//		this.history = history;
//		
//		this.history.InitResizingImgContents();	
//		//join한 경우
//	}
//	
//	/** 히스토리 업데이트 시(히스토리 보기 버튼을 눌러 업데이트) */
//	public void updateHistoryList(){
//		//서버가 준 history의 Vector<Contents>에 따른 resizingImgContents을 초기화
//		history.InitResizingImgContents();		
//		
//		//1. 외부로 나와서 히스토리 정보들 넘겨주고 사용자의 히스토리정보 업데이트... 
//		//(history setVector(getSharedContents(), getResizingImgContents())메소드 이용)
//		//2. 사용자의 히스토리정보를 이용하여 ui업데이트
//	}
//	
//	/** history의 Vector<Contents>를 반환 */
//	public Vector<Contents> getSharedContents(){
//		return history.getSharedContents();
//	}
//	
//	/** history의 Vector<ImageIcon>를 반환 */
//	public Vector<ImageIcon> getResizingImgContents(){
//		return history.getResizingImgContents();
//	}
//}
