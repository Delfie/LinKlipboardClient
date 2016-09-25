package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import server_manager.LinKlipboard;
import start_manager.StartToProgram;
import transfer_manager.CommunicatingWithServer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class UserInterfaceManager extends JFrame {
	private LinKlipboardClient client;
	private CommunicatingWithServer communicatingWithServer = new CommunicatingWithServer(client);
	private TrayIconManager trayIcon = new TrayIconManager();

	private UserInterfacePage1 page1;
	private UserInterfacePage2 page2;

	private NicknameDialog inputNickNameDialog; // 사용자가 원하는 닉네임 입력 다이얼로그

	// private Container contentPane;

	private static final int startXOfFrame = 0;
	private static final int startYOfFrame = 0;
	private static final int endXOfFrame = 326;
	private static final int endYOfFrame = 430;
	private static final int endXOfPanel = 320;
	private static final int endYOfPanel = 400;

	private static final int buttonWidth = 80;
	private static final int buttonHeight = 23;

	public UserInterfaceManager() {
		setTitle("LinKlipboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(endXOfFrame, endYOfFrame);

		trayIcon.addTrayIconInSystemTray();
		setHooker();

		// setLayout(null);

		// //TEST
		 client = new LinKlipboardClient(this);
		 setUserInterfacePage2(client, trayIcon, this);
//
//		setUserInterfacePage1(client, this);
//		this.client = page1.getClient();
//
//		if (page1.getACCESS() == true) {
//			setUserInterfacePage2(client, trayIcon, this);
//			dealInputnickName(this.client.getNickName()); // 닉네임 설정
//		}

		setVisible(true);
		setResizable(false);
	}

	/** UserInterfacePage1, LinKlipboardClient를 생성하여 초기화 후 배치 */
	public void setUserInterfacePage1(LinKlipboardClient client, UserInterfaceManager main) {
		// page1 패널 생성
		client = new LinKlipboardClient(main);
		page1 = new UserInterfacePage1(client);

		main.setContentPane(page1);
	}

	/** UserInterfacePage2 생성 및 배치 */
	public void setUserInterfacePage2(LinKlipboardClient client, TrayIconManager trayIcon, UserInterfaceManager main) {
		// page2 패널 생성
		page2 = new UserInterfacePage2(client, trayIcon, main);

		main.setContentPane(page2);
	}

	/** 단축키(초기값[Ctrl + Q] / [Alt + Q])를 누르면 서버에 데이터 전송, 최신 데이터 수신 */
	private void setHooker() {
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				char secondShortcutForSend = client.getSecondShortcutForSend().charAt(0);
				int getKeyCodeForSend = (int) secondShortcutForSend; // int keyCode를 얻어와 event,VK_ 꼴로 만들어야함

				char secondShortcutForReceive = client.getSecondShortcutForReceive().charAt(0);
				int getKeyCodeForReceive = (int) secondShortcutForReceive;

				if (client.getFirstShortcutForSend().equals("Ctrl")) {
					if (event.isControlPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForSend) {
							System.out.println("[Ctrl + " + secondShortcutForSend + "] is detected.");
							// 전송한다.
							if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE) {
								communicatingWithServer.requestSendFileData();
							} else {
								communicatingWithServer.requestSendExpFileData();
							}
						}
					}
				} else if (client.getFirstShortcutForSend().equals("Alt")) {
					if (event.isMenuPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForSend) {
							System.out.println("[Ctrl + " + secondShortcutForSend + "] is detected.");
							// 전송한다.
							if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE) {
								communicatingWithServer.requestSendFileData();
							} else {
								communicatingWithServer.requestSendExpFileData();
							}
						}
					}
				} else {
					System.out.println("[setHookerForSend] 단축키 오류");
				}

				if (client.getFirstShortcutForReceive().equals("Ctrl")) {
					if (event.isControlPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
							System.out.println("[Ctrl + " + secondShortcutForReceive + "] is detected.");
							receiveData();
						}
					}
				} else if (client.getFirstShortcutForReceive().equals("Alt")) {
					if (event.isMenuPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
							System.out.println("[Alt + " + secondShortcutForReceive + "] is detected.");
							receiveData();
						}
					}
				} else {
					System.out.println("[setHookerForReceive] 단축키 오류");
				}
			}
		});
	}


	/** 최신으로 받은 Contents를 내 시스템 클립보드에 넣음 */
	public void receiveData() {
		client.settLatestContents();

		Contents latestContentsFromServer = client.getLatestContents();
		int latestContentsType = client.getLatestContents().getType();

		if (latestContentsType == LinKlipboard.FILE_TYPE) {
			communicatingWithServer.requestReceiveFileData();
		} else if (latestContentsType == LinKlipboard.STRING_TYPE) {
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
		} else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
		} else {
			System.out.println("[ConnectionPanel] File, String, Image 어디에도 속하지 않음");
		}
	}

	/** 다이얼로그에서 입력받은 닉네임을 처리 */
	public void dealInputnickName(String defaulNickname) {
		inputNickNameDialog = new NicknameDialog(this, "Set Nickname", defaulNickname);
		inputNickNameDialog.setSize(250, 130);
		inputNickNameDialog.setVisible(true);

		String nickname = inputNickNameDialog.getInput();
		new StartToProgram(client).requestChangeInfoToServer(nickname);

		// page2로 넘어간 후에 다이얼로그 띄우기
	}
	
	/** TrayIcon을 반환 */
	public TrayIconManager getTrayIcon(){
		return this.trayIcon;
	}

	public static void main(String[] args) {
		new UserInterfaceManager();
	}
}

class NicknameDialog extends JDialog {
	JLabel label = new JLabel("Please enter your nickname.");
	JLabel errorLabel = new JLabel();
	JTextField inputNicknameField = new JTextField();
	JButton okButton = new JButton("OK");

	public NicknameDialog(JFrame jf, String title, String defaulNickname) {
		super(jf, title, true);

		setLayout(null);

		label.setBounds(40, 10, 170, 20);
		// label.setBackground(Color.yellow);
		// label.setOpaque(true);
		add(label);
		
		errorLabel.setBounds(40, 70, 100, 20);
		errorLabel.setForeground(Color.red);
		add(errorLabel);

		inputNicknameField.setBounds(55, 40, 130, 25);
		inputNicknameField.setText(defaulNickname);
		add(inputNicknameField);

		okButton.setBounds(150, 70, 80, 23);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (inputNicknameField.getText().length() != 0) {
					errorLabel.setText("닉네임 필수 입력");
					setVisible(false);
				}
			}
		});
		add(okButton);

		setResizable(false);
	}

	/** 사용자가 입력한 닉네임 반환 */
	String getInput() {
		if (inputNicknameField.getText().length() == 0) {
			return null;
		} else {
			return inputNicknameField.getText();
		}
	}
}
