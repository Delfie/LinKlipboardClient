package datamanage;

import java.util.Vector;

import javax.swing.ImageIcon;

import contents.Contents;

public class ReceiveHistoryList {
	private History history;
	
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
	
	/** �����丮 ������Ʈ ��(�����丮 ���� ��ư�� ���� ������Ʈ) */
	public void updateHistoryList(){
		//������ �� history�� Vector<Contents>�� ���� resizingImgContents�� �ʱ�ȭ
		history.InitResizingImgContents();		
		
		//1. �ܺη� ���ͼ� �����丮 �Ѱ��ְ� ������� �����丮���� ������Ʈ... 
		//(history setVector(getSharedContents(), getResizingImgContents())�޼ҵ� �̿�)
		//2. ������� �����丮������ �̿��Ͽ� ui������Ʈ
	}
	
	/** sharedContents ���ε� �� �������� �ֽ� Contents�� �Ѱܹ��� */
	public void getlatestContents(Contents latestContents){
		//�ֽ����� ���� Contents�� Vector�� ����
		history.addSharedContnestsInHistory(latestContents);
		
		// 1. �ܺη� ���ͼ� �����丮 �Ѱ��ְ� ������� �����丮���� ������Ʈ... 
		// (history setVector(getSharedContents(), getResizingImgContents())�޼ҵ� �̿�)
		// 2. ������� �����丮������ �̿��Ͽ� ui������Ʈ
	}
	
	/** history�� ���� */
	public void setHistory(History history){
		this.history = history;
	}
	
	/** history�� Vector<Contents>�� ��ȯ */
	public Vector<Contents> getSharedContents(){
		return history.getSharedContents();
	}
	
	/** history�� Vector<ImageIcon>�� ��ȯ */
	public Vector<ImageIcon> getResizingImgContents(){
		return history.getResizingImgContents();
	}
}
