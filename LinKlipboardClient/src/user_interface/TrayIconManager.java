package user_interface;

import java.awt.*;
import java.awt.event.*;

public class TrayIconManager {
	private final SystemTray systemTray = SystemTray.getSystemTray(); // �ý���Ʈ���� ����

	private Image trayIconImage; // Ʈ���̾����� �̹���
	private PopupMenu trayIconMenu; // Ʈ���̾����� ��Ŭ�� �޴�
	private MenuItem menuItem; // Ʈ���̾����� ��Ŭ�� �޴� �׸�
	private TrayIcon trayIcon; // Ʈ���̾�����

	public TrayIconManager() {
		trayIconImage = Toolkit.getDefaultToolkit().getImage("image/LK.png"); // Ʈ���̾����� �̹���
		trayIconMenu = new PopupMenu();
		trayIcon = new TrayIcon(trayIconImage, "LinKlipboard", trayIconMenu);
	}

	/** Ʈ���̾������� �ý���Ʈ���̿� �߰� */
	public void addTrayIconInSystemTray() {
		if (SystemTray.isSupported()) { // �ý��� Ʈ���̰� �����Ǹ�
			setMouseEvent();
			setMenu();

			try {
				systemTray.add(trayIcon); // �ý��� Ʈ���̿� Ʈ���� ������ �߰�
				trayIcon.setImageAutoSize(true); // Ʈ���� ������ ũ�� �ڵ� ����
				// trayIcon.addMouseListener(mouseListener); // Ʈ���� �����ܿ� ���콺 ������ �߰�
			} catch (AWTException e) {
				e.printStackTrace();
			}

		} else {
			System.err.println("Tray unavailable");
		}
	}

	/** Ʈ���̾����� ���콺 �̺�Ʈ ���� */
	public void setMouseEvent() {
		// /* Ʈ���� ������ ���콺 ������ */
		// MouseListener mouseListener = new MouseAdapter() {
		// public void mousePressed(MouseEvent e) {
		// if (e.getClickCount() == 2) { // Ʈ���� �������� ���� Ŭ���ϸ�
		// //displayMainApp(); // frame�� ������
		// }
		// }
		// };
	}

	/** Ʈ���̾����� ��Ŭ�� �޴� ���� */
	public void setMenu() {

		// menuItem = new MenuItem("Main APP"); // frame ������
		// menuItem.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// //displayMainApp();
		// }
		// });
		// trayIconMenu.add(item);

		menuItem = new MenuItem("Close"); // ���α׷� ����
		menuItem.addActionListener(new ActionListener() { // Close �޴��� ���� �׼� ������
			public void actionPerformed(ActionEvent e) {
				systemTray.remove(trayIcon); // �ý���Ʈ���̿��� Ʈ���̾����� ����
				System.exit(0); // ���α׷� ����
			}
		});

		trayIconMenu.add(menuItem);
	}

	/** Ʈ���̾����� �޽��� ���� */
	public void showMsg(String msg) {
		trayIcon.displayMessage("*���������� ����*", msg, TrayIcon.MessageType.INFO);
	}
}
