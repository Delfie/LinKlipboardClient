package datamanage;

import contents.Contents;

public class ReceiveHistoryList {
	private History history;
	private Contents latestContents;
	
	/** ReceiveHistoryList ������ */
	public ReceiveHistoryList() {
		System.out.println("����Ʈ ReceiveHistoryList ������ ȣ��");
	}
	
	/** ReceiveHistoryList ������ 
	 * @param history �������� �Ѱ��� History ��ü */
	public ReceiveHistoryList(History history) {
		this.history = history;
		this.latestContents = null;
	}
	
	/** �����丮 ������Ʈ ��  
	 * History ��ü�� �Ѱܹ޾Ƽ� set����� �ǳ�???????????(no!!! setHistory��) */
	public void updateHistoryList(){
		//������ �� history�� Vector<Contents>�� ���� resizingImgContents�� �ʱ�ȭ
		history.InitResizingImgContents();		
		//ui������Ʈ
	}
	
	/** sharedContents ���ε� �� �������� �ֽ� Contents�� �Ѱܹ��� */
	public void getlatestContents(Contents latestContents){
		//�ֽ����� ���� Contents�� Vector�� ����
		history.addSharedContnestsInHistory(latestContents);
		//ui������Ʈ
	}
	
	/** history�� ���� */
	public void setHistory(History history){
		this.history = history;
	}
	
	/** �ֽ� ������(latestContents)�� ���� */
	public void setLatestContents(Contents latestContents){
		this.latestContents = latestContents;
	}
	
	//ui������Ʈ?
	//history�� �Ѱ��ְ�... ui���� ó��?
}
