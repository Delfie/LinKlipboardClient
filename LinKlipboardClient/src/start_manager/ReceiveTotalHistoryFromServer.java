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
//	/** ReceiveHistoryList ������ */
//	public ReceiveTotalHistoryFromServer() {
//		System.out.println("����Ʈ ReceiveHistoryList ������ ȣ��");
//	}
//	
//	/** ReceiveHistoryList ������ 
//	 * @param history �������� �Ѱ��� History ��ü */
//	public ReceiveTotalHistoryFromServer(History history) {
//		this.history = history;
//		
//		this.history.InitResizingImgContents();	
//		//join�� ���
//	}
//	
//	/** �����丮 ������Ʈ ��(�����丮 ���� ��ư�� ���� ������Ʈ) */
//	public void updateHistoryList(){
//		//������ �� history�� Vector<Contents>�� ���� resizingImgContents�� �ʱ�ȭ
//		history.InitResizingImgContents();		
//		
//		//1. �ܺη� ���ͼ� �����丮 ������ �Ѱ��ְ� ������� �����丮���� ������Ʈ... 
//		//(history setVector(getSharedContents(), getResizingImgContents())�޼ҵ� �̿�)
//		//2. ������� �����丮������ �̿��Ͽ� ui������Ʈ
//	}
//	
//	/** history�� Vector<Contents>�� ��ȯ */
//	public Vector<Contents> getSharedContents(){
//		return history.getSharedContents();
//	}
//	
//	/** history�� Vector<ImageIcon>�� ��ȯ */
//	public Vector<ImageIcon> getResizingImgContents(){
//		return history.getResizingImgContents();
//	}
//}
