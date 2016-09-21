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

import javax.swing.JOptionPane;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class FileReceiveDataToServer extends Thread {

	private Socket socket; // ������ ������ ����
	private String ipAddr = LinKlipboard.SERVER_IP;
	private final static int portNum = LinKlipboard.FTP_PORT;

	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	private LinKlipboardClient client;

	// ������ �а� �������� ���� ��Ʈ�� ����
	private FileOutputStream fos;
	private DataInputStream dis;

	private static File fileReceiveFolder;
	private final static String fileReceiveDir = "C:\\Program Files\\LinKlipboard";
	private String receiveFileName; // ���۹��� ������ �̸�
	private String receiveFilePath; // ���۹��� ������ ���

	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer() {
		this.receiveFileName = LinKlipboardClient.getFileName();
		this.receiveFilePath = fileReceiveDir + "\\" + this.receiveFileName;
	}

	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer(String fileName) {
		this.receiveFilePath = fileReceiveDir + "\\" + fileName;
	}

	/** FileReceiveDataToServer ������ */
	public FileReceiveDataToServer(LinKlipboardClient client) {
		this.client = client;
		this.receiveFileName = LinKlipboardClient.getFileName();
		this.receiveFilePath = fileReceiveDir + "\\" + this.receiveFileName;
	}

	/** ���� ������ ���� �޼ҵ� (FileReceiveDataToServer ���� ȣ��) */
	public void requestReceiveData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/FileReceiveDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[FileReceiveDataToServer] ���� ��ü ������ Ȯ��" + header); // delf

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

			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[FileReceiveDataToServer] ���� ����");
				this.start();
			}

			bin.close();
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
		if(response != null){
			responseHandler.responseHandlerForTransfer();
		}
		else{
			System.out.println("[FileReceiveDataToServer] Error!!!! ������ ���� response�� null��");
		}
	}

	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(ipAddr, portNum);
			// ��Ʈ�� ����
			dis = new DataInputStream(socket.getInputStream()); // ����Ʈ �迭�� �ޱ� ���� �����ͽ�Ʈ�� ����
			System.out.println("[FileReceiveDataToServer] ���� ���� ��"); // delf

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
			int byteSize = 65536;
			byte[] ReceiveByteArrayToFile = new byte[byteSize]; // ����Ʈ �迭 ����

			fos = new FileOutputStream(receiveFilePath); // ������ ��ο� ����Ʈ �迭�� �������� ���� ��Ʈ�� ����

			int EndOfFile = 0; // ������ ��(-1)�� �˸��� ���� ����
			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) { // ReceiveByteArrayToFile�� ũ���� 1024����Ʈ ��ŭ DataInputStream���� ����Ʈ�� �о� ����Ʈ �迭�� ����, EndOfFile���� 1024�� �������
																			// DataInputStream���� ����Ʈ�� �� �о�� ��(EndOfFile=-1 �� ��)���� �ݺ�
				fos.write(ReceiveByteArrayToFile, 0, EndOfFile); // ReceiveByteArrayToFile�� ����ִ� ����Ʈ�� 0~EndOfFile=1024 ��ŭ FileOutputStream���� ����
			}

			closeSocket();

			// reply = JOptionPane.showConfirmDialog(null, "������?", null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			//
			// if (reply == JOptionPane.YES_OPTION) {
			// // ���� ������ Ŭ�����忡 ����
			// setFileInClipboard(receiveFilePath + receiveFileName);
			// System.out.println("Ŭ������ ���� �Ϸ�");
			// }
			// else {
			// initDir();
			// }

			createFileReceiveFolder();
			initDir();

			setFileInClipboard(receiveFilePath);
			System.out.println("[FileReceiveDataToServer] Ŭ������ ���� �Ϸ�");

		} catch (IOException e) {
			closeSocket();
			return;
		}
	}

	/** ���۹��� ������ ������ ����(LinKlipboard) ���� */
	private void createFileReceiveFolder() {
		fileReceiveFolder = new File(fileReceiveDir);

		// C:\\Program Files�� LinKlipboard������ �������� ������
		if (!fileReceiveFolder.exists()) {
			fileReceiveFolder.mkdir(); // ���� ����
			System.out.println("C:\\Program Files�� LinKlipboard���� ����");
		} else {
			initDir();
		}
	}

	/** ���� ���� ���ϵ��� ����(������ ��츸 ����.) */
	private void initDir() {
		File[] innerFile = fileReceiveFolder.listFiles(); // ���� �� �����ϴ� ������ innerFile�� ����
		for (File file : innerFile) { // innerFile�� ũ�⸸ŭ for���� ���鼭
			file.delete(); // ���� ����
			System.out.println("C:\\Program Files\\LinKlipboard���� ���� ���� ����");
		}

		// Dir�ȿ� ������ �ϳ��� �ִ� ��쿡 ��� ����
		// innerFile[0].delete();
	}

	/** �����ִ� ������ ��� �ݴ´�. */
	private void closeSocket() {
		try {
			fos.close();
			dis.close();
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
}