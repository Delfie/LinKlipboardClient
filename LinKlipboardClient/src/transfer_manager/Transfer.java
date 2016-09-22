package transfer_manager;

import java.net.Socket;

import client_manager.LinKlipboardClient;

/** ������ ���ۿ� ���� Ŭ���� */
public abstract class Transfer extends Thread{
	protected Socket socket; // ������ ������ ����
	protected LinKlipboardClient client;
	
	protected String response; // �����κ��� ���� ���� ����
	protected ResponseHandler responseHandler; // ���信 ���� ó��

	/** Transfer ������ */
	public Transfer(LinKlipboardClient client) {
		this.client = client;
	}
	
	/** ������ ���� Ŭ���̾�Ʈ�� ������ ��ٸ���. */
	abstract public void setConnection();
	
	/** ���� ������ �ݴ´�. */
	abstract public void closeSocket();
	
	/** ������ ���ϴ� ������ ȣ���Ѵ�. */
	abstract public void callServlet();
}
