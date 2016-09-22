package transfer_manager;

import java.net.Socket;

import client_manager.LinKlipboardClient;

/** ������ ���ۿ� ���� Ŭ���� */
public abstract class Transfer extends Thread{
	protected Socket socket; // ������ ������ ����

	/** Transfer ������ */
	public Transfer() {
	}
	
	/** ������ ���� Ŭ���̾�Ʈ�� ������ ��ٸ���. */
	abstract public void setConnection();
	
	/** ���� ������ �ݴ´�. */
	abstract public void closeSocket();
	
}
