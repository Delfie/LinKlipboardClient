package datamanage;

import contents.Contents;
import server_manager.LinKlipboard;

public class ReceivePreviousData {
	private Contents previousData;
	private int dataType;
	
	public ReceivePreviousData(Contents previousData) {
		this.previousData = previousData;
		dataType = previousData.getType();
	}
	
	public void receiveDataToServer(int dataType) {
		switch(dataType) {
		case LinKlipboard.STRING_TYPE :
		case LinKlipboard.IMAGE_TYPE :
			
			break;
		case LinKlipboard.FILE_TYPE:
			
			break;
		}
	}

	public Contents getPreviousData() {
		return previousData;
	}
}
