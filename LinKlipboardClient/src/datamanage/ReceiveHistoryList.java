package datamanage;

import java.util.Vector;

import javax.swing.ImageIcon;

import contents.Contents;

public class ReceiveHistoryList {
	private History history;
	//private Contents latestContents; // ���� �ֱٿ� ������ Contents
	
	/** ReceiveHistoryList ������ */
	public ReceiveHistoryList() {
		System.out.println("����Ʈ ReceiveHistoryList ������ ȣ��");
	}
	
	/** ReceiveHistoryList ������ 
	 * @param history �������� �Ѱ��� History ��ü */
	public ReceiveHistoryList(History history) {
		this.history = history;
		
		updateHistoryList();
	}
	
	/** �����丮 ������Ʈ ��(�����丮 ���� ��ư�� ���� ������Ʈ)  
	 * History ��ü�� �Ѱܹ޾Ƽ� set����� �ǳ�???????????(no!!! setHistory��) */
	public void updateHistoryList(){
		//������ �� history�� Vector<Contents>�� ���� resizingImgContents�� �ʱ�ȭ
		history.InitResizingImgContents();		
		//ui������Ʈ
		//�ܺη� ���ͼ� ������� �����丮���� ������Ʈ... (history setSharedContents()�޼ҵ� �̿�)
	}
	
	/** sharedContents ���ε� �� �������� �ֽ� Contents�� �Ѱܹ��� */
	public void getlatestContents(Contents latestContents){
		//�ֽ����� ���� Contents�� Vector�� ����
		history.addSharedContnestsInHistory(latestContents);
		//ui������Ʈ
	}
	
	/** history�� Vector<Contents>�� ��ȯ */
	public Vector<Contents> getSharedContents(){
		return history.getSharedContents();
	}
	
	/** history�� Vector<ImageIcon>�� ��ȯ */
	public Vector<ImageIcon> getResizingImgContents(){
		return history.getResizingImgContents();
	}
	
	//ui������Ʈ?
	//history�� �Ѱ��ְ�... ui���� ó��?
}
