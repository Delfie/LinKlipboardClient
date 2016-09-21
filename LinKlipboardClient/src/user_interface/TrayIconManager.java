package user_interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TrayIconManager {
	private final SystemTray systemTray; // �ý��� Ʈ����
	
	private Image image; // Ʈ���̾����� �̹���
	private PopupMenu popup; // Ʈ���̾����� ��Ŭ�� �޴�
	private MenuItem item; // Ʈ���̾����� ��Ŭ�� �޴� �׸�
	private final TrayIcon trayIcon; // Ʈ���̾�����

	public TrayIconManager() {
		systemTray = SystemTray.getSystemTray();
		image = Toolkit.getDefaultToolkit().getImage("image/LK.png"); 
		popup = new PopupMenu();
		trayIcon = new TrayIcon(image, "LinKlipboard", popup);
	}
	
	/** Ʈ���̾�����  */
	public void displayTrayIcon() {
		if (SystemTray.isSupported()) { // �ý��� Ʈ���̰� �����Ǹ�
			
			/* Ʈ���� ������ ���콺 ������ */
			MouseListener mouseListener = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 2) { // Ʈ���� �������� ���� Ŭ���ϸ�
						//displayMainApp(); // frame�� ������
					}
				}
			};

			setPopup();

			try {
				systemTray.add(trayIcon); // �ý��� Ʈ���̿� Ʈ���� ������ �߰�
				trayIcon.setImageAutoSize(true); // Ʈ���� ������ ũ�� �ڵ� ����
				trayIcon.addMouseListener(mouseListener); // Ʈ���� �����ܿ� ���콺 ������ �߰�
			} catch (AWTException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Tray unavailable");
		}
	}
	
	/** Ʈ���̾����� ��Ŭ�� �޴� ���� */
	public void setPopup() {
		
		item = new MenuItem("Main APP"); // frame ������
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//displayMainApp();
			}
		});
		popup.add(item);


		item = new MenuItem("Info"); // Info Message ���
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMsg("Info Title");
			}
		});
		popup.add(item);


		item = new MenuItem("Close"); // ���α׷� ����
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemTray.remove(trayIcon); // �ý���Ʈ���̿��� Ʈ���̾����� ����
				System.exit(0);
			}
		});
		popup.add(item);
	}
	
	/** Ʈ���̾����� �޽��� ���� */
	public void showMsg(String msg) {
		trayIcon.displayMessage(msg, "Info", TrayIcon.MessageType.INFO);
	}
}
