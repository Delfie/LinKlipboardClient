package user_interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TrayIconManager {
	private final SystemTray systemTray; // 시스템 트레이
	
	private Image image; // 트레이아이콘 이미지
	private PopupMenu popup; // 트레이아이콘 우클릭 메뉴
	private MenuItem item; // 트레이아이콘 우클릭 메뉴 항목
	private final TrayIcon trayIcon; // 트레이아이콘

	public TrayIconManager() {
		systemTray = SystemTray.getSystemTray();
		image = Toolkit.getDefaultToolkit().getImage("image/LK.png"); 
		popup = new PopupMenu();
		trayIcon = new TrayIcon(image, "LinKlipboard", popup);
	}
	
	/** 트레이아이콘  */
	public void displayTrayIcon() {
		if (SystemTray.isSupported()) { // 시스템 트레이가 지원되면
			
			/* 트레이 아이콘 마우스 리스너 */
			MouseListener mouseListener = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 2) { // 트레이 아이콘을 더블 클릭하면
						//displayMainApp(); // frame을 보여줌
					}
				}
			};

			setPopup();

			try {
				systemTray.add(trayIcon); // 시스템 트레이에 트레이 아이콘 추가
				trayIcon.setImageAutoSize(true); // 트레이 아이콘 크기 자동 조절
				trayIcon.addMouseListener(mouseListener); // 트레이 아이콘에 마우스 리스너 추가
			} catch (AWTException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Tray unavailable");
		}
	}
	
	/** 트레이아이콘 우클릭 메뉴 설정 */
	public void setPopup() {
		
		item = new MenuItem("Main APP"); // frame 보여줌
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//displayMainApp();
			}
		});
		popup.add(item);


		item = new MenuItem("Info"); // Info Message 띄움
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMsg("Info Title");
			}
		});
		popup.add(item);


		item = new MenuItem("Close"); // 프로그램 종료
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				systemTray.remove(trayIcon); // 시스템트레이에서 트레이아이콘 제거
				System.exit(0);
			}
		});
		popup.add(item);
	}
	
	/** 트레이아이콘 메시지 띄우기 */
	public void showMsg(String msg) {
		trayIcon.displayMessage(msg, "Info", TrayIcon.MessageType.INFO);
	}
}
