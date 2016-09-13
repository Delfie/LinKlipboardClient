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
	
	public static Contents readClipboard() {
		Transferable contents = getSystmeClipboardContets();
		setDataFlavor(contents);
		Contents settingdObject = extractDataFromContents(contents);
		
		return settingdObject;
	}
	
	public static Transferable getSystmeClipboardContets() {
		return systemClipboard.getContents(null);
	}
	
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
	
	private static Contents extractDataFromContents(Transferable contents) {
		try {
			String extractString = null;
			//Image extractImage = null;
			ImageIcon extractImageIcon = null;
			Contents sendObject = null; // 실제 전송 데이터

			// 클립보드의의 내용을 추출
			if (type == LinKlipboard.STRING_TYPE) {
				extractString = (String) contents.getTransferData(DataFlavor.stringFlavor);
				sendObject = new StringContents(extractString);

			}
			else if (type == LinKlipboard.IMAGE_TYPE) {
				extractImageIcon = (ImageIcon) contents.getTransferData(DataFlavor.imageFlavor);
				//extractImageIcon = new ImageIcon(extractImage);
				sendObject = new ImageContents(extractImageIcon);
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

	public static void writeClipboard(Contents data, Clipboard clip, int dataType) {
		switch (dataType) {
		case LinKlipboard.STRING_TYPE:
			StringContents stringData = (StringContents) data;
			String tmpString = stringData.getString();
			StringSelection stringTransferable = new StringSelection(tmpString);
			systemClipboard.setContents(stringTransferable, null);
			break;
		case LinKlipboard.IMAGE_TYPE:
			ImageContents ImageData = (ImageContents) data;
			Image tmpImage = ImageData.getImage().getImage();
			ImageTransferable Imagetransferable = new ImageTransferable(tmpImage);
			systemClipboard.setContents(Imagetransferable, null);
			break;
		case LinKlipboard.FILE_TYPE:
			break;
		default:
			break;
		}
	}
	
	/** 전송가능한 Image 객체 */
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
