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
	
	/** 시스템 클립보드에서 Transferable객체를 읽어와 데이터 타입을 알아내고 Contents객체로 변환*/
	public static Contents readClipboard() {
		Transferable contents = getSystmeClipboardContets();
		setDataFlavor(contents);
		Contents settingdObject = extractDataFromContents(contents);
		
		return settingdObject;
	}
	
	/** 시스템 클립보드의 Transferable객체 리턴*/
	public static Transferable getSystmeClipboardContets() {
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard(); // delf
		return systemClipboard.getContents(null);
	}

	// delf 데르프
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

	/** 클립보드의 Transferable객체가 어떤 타입인지 리턴 */
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
	
	/** 클립보드의 Transferable객체를 전송객체로 바꿈*/
	private static Contents extractDataFromContents(Transferable contents) {
		try {
			String extractString = null;
			ImageIcon extractImage = null;
			Contents sendObject = null; // 실제 전송 데이터

			// 클립보드의의 내용을 추출
			if (type == LinKlipboard.STRING_TYPE) {
				extractString = (String) contents.getTransferData(DataFlavor.stringFlavor); // Transferable객체를 String으로 변환
				sendObject = new StringContents(extractString); // 클립보드로 부터 추출한 String으로 전송객체 생성

			}
			else if (type == LinKlipboard.IMAGE_TYPE) {
				extractImage = (ImageIcon) contents.getTransferData(DataFlavor.imageFlavor); // Transferable객체를 ImageIcon으로 변환
				sendObject = new ImageContents(extractImage); // 클립보드로 부터 추출한 ImageIcon으로 전송객체 생성
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

	/** 전송받은 Contents객체를 Transferable해서 클립보드에 삽입 */
	public static void writeClipboard(Contents data, Clipboard clip, int dataType) {
		switch (dataType) {
		case LinKlipboard.STRING_TYPE:
			StringContents stringData = (StringContents) data; // Contents를 stringContents로 다운캐스팅
			String tmpString = stringData.getString(); // StringContent의 String을 얻어옴
			StringSelection stringTransferable = new StringSelection(tmpString); // 클립보드에 넣을 수 있는 Transferable 객체 생성
			systemClipboard.setContents(stringTransferable, null); // 시스템 클립보드에 삽입
			break;
		case LinKlipboard.IMAGE_TYPE:
			ImageContents ImageData = (ImageContents) data; // Contents를 ImageContents로 다운캐스팅
			Image tmpImage = ImageData.getImage().getImage(); // ImageContent의 ImageIcon을 얻고  ImageIcon의 Image를 얻어옴
			ImageTransferable Imagetransferable = new ImageTransferable(tmpImage); // 클립보드에 넣을 수 있는 Transferable 객체 생성
			systemClipboard.setContents(Imagetransferable, null); // 시스템 클립보드에 삽입
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
