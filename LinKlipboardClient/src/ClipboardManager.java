import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.ImageIcon;
import contents.Contents;
import contents.ImageContents;
import contents.StringContents;
import server_manager.LinKlipboard;

public class ClipboardManager {
	private static Clipboard systemClipboard;
	private static int type;

	public ClipboardManager() {
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	/** �ý��� Ŭ�����忡�� Transferable��ü�� �о�� ������ Ÿ���� �˾Ƴ��� Contents��ü�� ��ȯ*/
	public static Contents readClipboard() {
		Transferable contents = getSystmeClipboardContets();
		setDataFlavor(contents);
		Contents settingdObject = extractDataFromContents(contents);
		
		return settingdObject;
	}
	
	/** �ý��� Ŭ�������� Transferable��ü ����*/
	public static Transferable getSystmeClipboardContets() {
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // delf
		return systemClipboard.getContents(null);
	}

	// delf ������
	public static int getClipboardDataTypeNow() {
		DataFlavor type = setDataFlavor(getSystmeClipboardContets());

		if (type.equals(DataFlavor.stringFlavor)) {
			return LinKlipboard.STRING_TYPE;
		} else if (type.equals(DataFlavor.imageFlavor)) {
			return LinKlipboard.IMAGE_TYPE;
		} else if (type.equals(DataFlavor.javaFileListFlavor)) {
			return LinKlipboard.FILE_TYPE;
		} else {
			return -1;
		}
	}

	/** Ŭ�������� Transferable��ü�� � Ÿ������ ���� */
	public static DataFlavor setDataFlavor(Transferable t) {
		DataFlavor[] flavors = t.getTransferDataFlavors();
		for (int i = 0; i < flavors.length; i++) {

			if (flavors[i].equals(DataFlavor.stringFlavor)) {
				type = LinKlipboard.STRING_TYPE;
				return DataFlavor.stringFlavor;
			}
			else if (flavors[i].equals(DataFlavor.imageFlavor)) {
				type = LinKlipboard.IMAGE_TYPE;
				return DataFlavor.imageFlavor;
			}
			else if (flavors[i].equals(DataFlavor.javaFileListFlavor)) {
				type = LinKlipboard.FILE_TYPE;
				return DataFlavor.javaFileListFlavor;
			}
			else {
			}
		}
		return null;
	}
	
	/** Ŭ�������� Transferable��ü�� ���۰�ü�� �ٲ�*/
	private static Contents extractDataFromContents(Transferable contents) {
		try {
			String extractString = null;
			ImageIcon extractImage = null;
			Contents sendObject = null; // ���� ���� ������

			// Ŭ���������� ������ ����
			if (type == LinKlipboard.STRING_TYPE) {
				extractString = (String) contents.getTransferData(DataFlavor.stringFlavor); // Transferable��ü�� String���� ��ȯ
				sendObject = new StringContents(extractString); // Ŭ������� ���� ������ String���� ���۰�ü ����

			}
			else if (type == LinKlipboard.IMAGE_TYPE) {
				extractImage = (ImageIcon) contents.getTransferData(DataFlavor.imageFlavor); // Transferable��ü�� ImageIcon���� ��ȯ
				sendObject = new ImageContents(extractImage); // Ŭ������� ���� ������ ImageIcon���� ���۰�ü ����
			}
			else {

			}

			return sendObject;

		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** ���۹��� Contents��ü�� Transferable�ؼ� Ŭ�����忡 ���� */
	public static void writeClipboard(Contents data, Clipboard clip, int dataType) {
		switch (dataType) {
		case LinKlipboard.STRING_TYPE:
			StringContents stringData = (StringContents) data; // Contents�� stringContents�� �ٿ�ĳ����
			String tmpString = stringData.getString(); // StringContent�� String�� ����
			StringSelection stringTransferable = new StringSelection(tmpString); // Ŭ�����忡 ���� �� �ִ� Transferable ��ü ����
			systemClipboard.setContents(stringTransferable, null); // �ý��� Ŭ�����忡 ����
			break;
		case LinKlipboard.IMAGE_TYPE:
			ImageContents ImageData = (ImageContents) data; // Contents�� ImageContents�� �ٿ�ĳ����
			Image tmpImage = ImageData.getImage().getImage(); // ImageContent�� ImageIcon�� ���  ImageIcon�� Image�� ����
			ImageTransferable Imagetransferable = new ImageTransferable(tmpImage); // Ŭ�����忡 ���� �� �ִ� Transferable ��ü ����
			systemClipboard.setContents(Imagetransferable, null); // �ý��� Ŭ�����忡 ����
			break;
		case LinKlipboard.FILE_TYPE:
			break;
		default:
			break;
		}
	}
	
	/** ���۰����� Image ��ü */
	static class ImageTransferable implements Transferable {
		private Image image;

		public ImageTransferable(Image image) {
			this.image = image;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (isDataFlavorSupported(flavor)) {
				return image;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == DataFlavor.imageFlavor;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}
	}

}
