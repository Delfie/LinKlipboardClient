package contents;

import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;

import server_manager.LinKlipboard;

public class ImageContents extends Contents implements Serializable {
	private ImageIcon imageData;

	public ImageContents() {
		super();
		type = LinKlipboard.IMAGE_TYPE;
	}
	
	public ImageContents(String sharer) {
		this();
		type = LinKlipboard.IMAGE_TYPE;
	}
	
	public ImageContents(ImageIcon image) {
		this();
		this.imageData = image;
	}
	
	public ImageContents(Image image) {
		this();
		this.imageData = new ImageIcon(image);
	}

	public ImageContents(String sharer, ImageIcon data) {
		super(sharer);
		type = LinKlipboard.IMAGE_TYPE;
		this.imageData = data;
	}

	public ImageIcon getImage() {
		return imageData;
	}
	
	public ImageIcon getResizingImageIcon() {
		Image resizingImage = imageData.getImage();  // ImageIcon을 Image로 변환.
		resizingImage = resizingImage.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // resize
		ImageIcon resizingImageIcon = new ImageIcon(resizingImage); // Image로 ImageIcon 생성
		
		return resizingImageIcon;
	}

	@Override
	public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException {
		imageData = (ImageIcon) in.readObject();
		return this;
	}
	
}