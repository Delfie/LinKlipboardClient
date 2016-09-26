package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
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
	private LinKlipboardClient client = new LinKlipboardClient(this);
	private CommunicatingWithServer communicatingWithServer = new CommunicatingWithServer(client);
	private TrayIconManager trayIcon = new TrayIconManager(this);

	private UserInterfacePage1 page1 = new UserInterfacePage1(client, this, trayIcon);
	private UserInterfacePage2 page2;

	private NicknameDialog inputNickNameDialog; // ����ڰ� ���ϴ� �г��� �Է� ���̾�α�

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
		setSize(endXOfFrame, endYOfFrame);

		setting();

		trayIcon.addTrayIconInSystemTray();
		setHooker();
		// setHooker(client);

		this.setContentPane(page1);

		setVisible(true);
		setResizable(false);
	}

	public void setting() {
		client.setting(this.getHistoryPanel(), this.getConnectionPanel());
	}

	// ����TEST
	/** ����Ű(�ʱⰪ[Ctrl + Q] / [Alt + Q])�� ������ ������ ������ ����, �ֽ� ������ ���� */
	public void setHooker() {
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

		System.out.println("Global keyboard hook successfully started, press [escape] key to shutdown.");

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				if (event.isControlPressed()) {
					if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_Q) {
						System.out.println("[Ctrl + " + "Q" + "] is detected.");
						// �����Ѵ�.
						if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE) {
							communicatingWithServer.requestSendFileData();
						} else {
							communicatingWithServer.requestSendExpFileData();
						}
					}
				}
			}
		});
	}

	/** ����Ű(�ʱⰪ[Ctrl + Q] / [Alt + Q])�� ������ ������ ������ ����, �ֽ� ������ ���� */
	public void setHooker2() {
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				char secondShortcutForSend = LinKlipboardClient.getSecondShortcutForSend().charAt(0);
				int getKeyCodeForSend = (int) secondShortcutForSend; // int
																		// keyCode��
																		// ����
																		// event,VK_
																		// �÷�
																		// ��������

				char secondShortcutForReceive = LinKlipboardClient.getSecondShortcutForReceive().charAt(0);
				int getKeyCodeForReceive = (int) secondShortcutForReceive;

				if (LinKlipboardClient.getFirstShortcutForSend().equals("Ctrl")) {
					if (event.isControlPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForSend) {
							System.out.println("[Ctrl + " + secondShortcutForSend + "] is detected.");
							// �����Ѵ�.
							if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE) {
								communicatingWithServer.requestSendFileData();
							} else {
								communicatingWithServer.requestSendExpFileData();
							}
						}
					}
				} else if (LinKlipboardClient.getFirstShortcutForSend().equals("Alt")) {
					if (event.isMenuPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForSend) {
							System.out.println("[Ctrl + " + secondShortcutForSend + "] is detected.");
							// �����Ѵ�.
							if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE) {
								communicatingWithServer.requestSendFileData();
							} else {
								communicatingWithServer.requestSendExpFileData();
							}
						}
					}
				} else {
					System.out.println("[setHookerForSend] ����Ű ����");
				}

				if (LinKlipboardClient.getFirstShortcutForReceive().equals("Ctrl")) {
					if (event.isControlPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
							System.out.println("[Ctrl + " + secondShortcutForReceive + "] is detected.");
							receiveData();
						}
					}
				} else if (LinKlipboardClient.getFirstShortcutForReceive().equals("Alt")) {
					if (event.isMenuPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
							System.out.println("[Alt + " + secondShortcutForReceive + "] is detected.");
							receiveData();
						}
					}
				} else {
					System.out.println("[setHookerForReceive] ����Ű ����");
				}
			}
		});
	}

	/** �ֽ����� ���� Contents�� �� �ý��� Ŭ�����忡 ���� */
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
			System.out.println("[ConnectionPanel] File, String, Image ��𿡵� ������ ����");
		}
	}

	// /** ����Ű(�ʱⰪ[Ctrl + Q] / [Alt + Q])�� ������ ������ ������ ����, �ֽ� ������ ���� */
	// public static void setHooker(LinKlipboardClient client) {
	// GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
	// CommunicatingWithServer communicatingWithServer = new
	// CommunicatingWithServer(client);
	//
	// keyboardHook.addKeyListener(new GlobalKeyAdapter() {
	// @Override
	// public void keyPressed(GlobalKeyEvent event) {
	// char secondShortcutForSend =
	// LinKlipboardClient.getSecondShortcutForSend().charAt(0);
	// int getKeyCodeForSend = (int) secondShortcutForSend; // int
	// // keyCode��
	// // ����
	// // event,VK_
	// // �÷�
	// // ��������
	//
	// char secondShortcutForReceive =
	// LinKlipboardClient.getSecondShortcutForReceive().charAt(0);
	// int getKeyCodeForReceive = (int) secondShortcutForReceive;
	//
	// if (LinKlipboardClient.getFirstShortcutForSend().equals("Ctrl")) {
	// if (event.isControlPressed()) {
	// if (event.getVirtualKeyCode() == getKeyCodeForSend) {
	// System.out.println("[Ctrl + " + secondShortcutForSend + "] is
	// detected.");
	// // �����Ѵ�.
	// if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE)
	// {
	// communicatingWithServer.requestSendFileData();
	// } else {
	// communicatingWithServer.requestSendExpFileData();
	// }
	// }
	// }
	// } else if (LinKlipboardClient.getFirstShortcutForSend().equals("Alt")) {
	// if (event.isMenuPressed()) {
	// if (event.getVirtualKeyCode() == getKeyCodeForSend) {
	// System.out.println("[Ctrl + " + secondShortcutForSend + "] is
	// detected.");
	// // �����Ѵ�.
	// if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE)
	// {
	// communicatingWithServer.requestSendFileData();
	// } else {
	// communicatingWithServer.requestSendExpFileData();
	// }
	// }
	// }
	// } else {
	// System.out.println("[setHookerForSend] ����Ű ����");
	// }
	//
	// if (LinKlipboardClient.getFirstShortcutForReceive().equals("Ctrl")) {
	// if (event.isControlPressed()) {
	// if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
	// System.out.println("[Ctrl + " + secondShortcutForReceive + "] is
	// detected.");
	// receiveData(client);
	// }
	// }
	// } else if (LinKlipboardClient.getFirstShortcutForReceive().equals("Alt"))
	// {
	// if (event.isMenuPressed()) {
	// if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
	// System.out.println("[Alt + " + secondShortcutForReceive + "] is
	// detected.");
	// receiveData(client);
	// }
	// }
	// } else {
	// System.out.println("[setHookerForReceive] ����Ű ����");
	// }
	// }
	// });
	// }
	//
	// /** �ֽ����� ���� Contents�� �� �ý��� Ŭ�����忡 ���� */
	// public static void receiveData(LinKlipboardClient client) {
	// client.settLatestContents();
	// CommunicatingWithServer communicatingWithServer = new
	// CommunicatingWithServer(client);
	//
	// Contents latestContentsFromServer = client.getLatestContents();
	// int latestContentsType = client.getLatestContents().getType();
	//
	// if (latestContentsType == LinKlipboard.FILE_TYPE) {
	// communicatingWithServer.requestReceiveFileData();
	// } else if (latestContentsType == LinKlipboard.STRING_TYPE) {
	// ClipboardManager.writeClipboard(latestContentsFromServer,
	// latestContentsType);
	// } else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
	// ClipboardManager.writeClipboard(latestContentsFromServer,
	// latestContentsType);
	// } else {
	// System.out.println("[ConnectionPanel] File, String, Image ��𿡵� ������ ����");
	// }
	// }

	/** ���̾�α׿��� �Է¹��� �г����� ó�� */
	public void dealInputnickName(String defaulNickname, UserInterfacePage2 page2) {
		this.page2 = page2;
		inputNickNameDialog = new NicknameDialog(this, "Set Nickname", defaulNickname, client, page2);
		inputNickNameDialog.setSize(250, 130);
		inputNickNameDialog.setVisible(true);

		// String nickname = inputNickNameDialog.getInput();
		// client.setNickName(nickname);

		// new StartToProgram(client).requestChangeInfoToServer(nickname);

		// page2�� �Ѿ �Ŀ� ���̾�α� ����
	}

	/** TrayIcon�� ��ȯ */
	public TrayIconManager getTrayIcon() {
		return this.trayIcon;
	}

	public HistoryPanel getHistoryPanel() {
		return page1.getHistoryPanel();
	}

	public ConnectionPanel getConnectionPanel() {
		return page1.getConnectionPanel();
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
	// static boolean permit = false;

	public NicknameDialog(JFrame jf, String title, String defaulNickname, LinKlipboardClient client,
			UserInterfacePage2 page2) {
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
			@Override
			public void actionPerformed(ActionEvent evt) {

				System.out
						.println("nickname = " + inputNicknameField.getText() + inputNicknameField.getText().length());
				System.out.println("getInput" + getInput());

				if (getInput().length() == 0) {
					errorLabel.setText("�г��� �ʼ� �Է�");
				} else {
					// new
					// StartToProgram(client).requestChangeInfoToServer(getInput());
					// // �г��� �ߺ�ó��

					// ���εǸ�
					LinKlipboardClient.setNickName(getInput());

					client.getOtherClients().add(getInput()); // �ڽŵ� �߰�
					System.out.println("[page1] " + client.getOtherClients().size());
					page2.getConnectionPanel().update();
					jf.setContentPane(page2);
					setVisible(false);
				}
			}
		});
		add(okButton);

		setResizable(false);
	}

	/** ����ڰ� �Է��� �г��� ��ȯ */
	String getInput() {
		// if (inputNicknameField.getText().length() == 0) {
		// return null;
		// } else {
		return inputNicknameField.getText();
		// }
	}
}
