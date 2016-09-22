package transfer_manager;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class FileReceiveDataToServer_1 extends Transfer {

	// ������ �а� �������� ���� ��Ʈ�� ����
	private FileOutputStream fos;
	private DataInputStream dis;

	// ���۹��� ������ ���
	private String receiveFilePath;

	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer_1(LinKlipboardClient client) {
		super(client);
		this.start();
	}
	
	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer_1(LinKlipboardClient client, String fileName) {
		super(client);
		this.receiveFilePath = LinKlipboard.fileReceiveDir + "\\" + fileName;
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboard.FTP_PORT);
			// ��Ʈ�� ����
			dis = new DataInputStream(socket.getInputStream()); // ����Ʈ �迭�� �ޱ� ���� �����ͽ�Ʈ�� ����
			System.out.println("[FileReceiveDataToServer] ���� ���� ��");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void closeSocket() {
		try {
			dis.close();
			fos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		setConnection();

		try {
			client.initDir();
			
			byte[] ReceiveByteArrayToFile = new byte[LinKlipboard.byteSize]; // ����Ʈ �迭 ����

			System.out.println("[FileReceiveDataToServer] ���� ���: " + receiveFilePath);
			fos = new FileOutputStream(receiveFilePath); // ������ ��ο� ����Ʈ �迭�� �������� ���� ��Ʈ�� ����

			int EndOfFile = 0; // ������ ��(-1)�� �˸��� ���� ����
			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) { // ReceiveByteArrayToFile�� ũ���� 1024����Ʈ ��ŭ DataInputStream���� ����Ʈ�� �о� ����Ʈ �迭�� ����, EndOfFile���� 1024�� �������
																			// DataInputStream���� ����Ʈ�� �� �о�� ��(EndOfFile=-1 �� ��)���� �ݺ�
				fos.write(ReceiveByteArrayToFile, 0, EndOfFile); // ReceiveByteArrayToFile�� ����ִ� ����Ʈ�� 0~EndOfFile=1024 ��ŭ FileOutputStream���� ����
			}

			closeSocket();
			
			setFileInClipboard(receiveFilePath);
			System.out.println("[FileReceiveDataToServer] Ŭ������ ���� �Ϸ�");
		} catch (IOException e) {
			closeSocket();
			return;
		}
	}

	@Override
	public void callServlet() {
		// TODO Auto-generated method stub

	}

}
