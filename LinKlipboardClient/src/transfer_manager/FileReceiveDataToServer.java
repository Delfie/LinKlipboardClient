package transfer_manager;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class FileReceiveDataToServer extends Thread {

	private Socket socket; // ������ ������ ����
	private LinKlipboardClient client;
	
	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	// ������ �а� �������� ���� ��Ʈ�� ����
	private FileOutputStream fos;
	private DataInputStream dis;

	private String receiveFilePath; // ���۹��� ������ ���

	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer() {
	}

	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer(LinKlipboardClient client, String fileName) {
		this.client = client;
		this.receiveFilePath = LinKlipboard.fileReceiveDir + "\\" + fileName;
	}

	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** ���� ������ ���� �޼ҵ� (ReceiveDataToServer ���� ȣ��) */
	public void requestReceiveData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReceiveDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[FileReceiveDataToServer] ���� ��ü ������ Ȯ��" + header);

			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			System.out.println("[FileReceiveDataToServer] �����κ����� ���� ������ Ȯ��: " + this.response); // delf
			bin.close();

			exceptionHandling(this.response);
			setFilePath();

			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[FileReceiveDataToServer] ���� ����");
				this.start();
			}

			System.out.println("bin.close ��");
			bin.close();
			System.out.println("bin.close ��");
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ���� ó��
	 * 
	 * @param response
	 *            Ŭ���̾�Ʈ ��û�� ���� ������ ����
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, client);
		if (response != null) {
			responseHandler.responseHandlerForTransfer();
		} else {
			System.out.println("[FileReceiveDataToServer] Error!!!! ������ ���� response�� null��");
		}
	}

	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
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
	public void run() {
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
		System.out.println("run ��~");
	}

	/** �����ִ� ������ ��� �ݴ´�. */
	private void closeSocket() {
		try {
			System.out.println("[FileReceiveDataToServer] closeSocket���� ");
			dis.close();
			fos.close();
			socket.close();
			System.out.println("[FileReceiveDataToServer] closeSocket���� �� �κ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** ���۰����� File ��ü */
	static class FileTransferable implements Transferable {
		private ArrayList<File> listOfFiles;

		public FileTransferable(ArrayList<File> listOfFiles) {
			this.listOfFiles = listOfFiles;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.javaFileListFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.javaFileListFlavor.equals(flavor);
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			return listOfFiles;
		}
	}

	/**
	 * ������ ��ο� ��ġ�� ������ �ý��� Ŭ�����忡 �����Ѵ�.
	 * 
	 * @param receiveFilePath
	 *            ������ ������ ���
	 */
	public void setFileInClipboard(String receiveFilePath) {
		File file = new File(receiveFilePath);
		ArrayList<File> listOfFiles = new ArrayList<File>();
		listOfFiles.add(file);

		FileTransferable ft = new FileTransferable(listOfFiles);

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, new ClipboardOwner() {
			@Override
			public void lostOwnership(Clipboard clipboard, Transferable contents) {
				System.out.println("Lost ownership");
			}
		});
	}

	/** �����κ��� ���� �����̸����� ���ϰ�θ� �ٽ� ���� */
	public void setFilePath() {
		this.receiveFilePath = LinKlipboard.fileReceiveDir + "\\" + LinKlipboardClient.getFileName();
	}
}