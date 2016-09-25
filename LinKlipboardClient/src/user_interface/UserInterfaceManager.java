package user_interface;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import client_manager.LinKlipboardClient;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import start_manager.StartToProgram;
import transfer_manager.CommunicatingWithServer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class UserInterfaceManager extends JFrame {
	private LinKlipboardClient client = new LinKlipboardClient();

	private UserInterfacePage1 page1 = new UserInterfacePage1(client);
	private UserInterfacePage2 page2 = new UserInterfacePage2(client);
	private TrayIconManager trayIcon = new TrayIconManager();

	private NicknameDialog inputNickNameDialog; // ����ڰ� ���ϴ� �г��� �Է� ���̾�α�

	Container contentPane;

	// �������� ���� ó�� Ŭ����
	private CommunicatingWithServer communicatingWithServer = new CommunicatingWithServer(client);

	public static Scanner s = new Scanner(System.in);

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

		contentPane = getContentPane();
		// contentPane.setLayout(null);

		contentPane.add(page1);
		this.client = page1.getClient();
		
		// contentPane.add(page2);
		// dealInputnickName(this.client.getNickName());

		setVisible(true);
		setResizable(false);
	}

	/** ����Ű(�ʱⰪ[Ctrl + T])�� ������ ������ ������ ���� */
	private void setHooker() {
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				if (event.isControlPressed()) {
					if (event.getVirtualKeyCode() == event.VK_T) {
						System.out.println("[Ctrl + T] is detected.");
						// client.sendDateToServer();
						// ������ Ŭ����
					}
				}
			}
		});
	}

	public static void main(String[] args) {
		new UserInterfaceManager();
	}

	/** ���̾�α׿��� �Է¹��� �г����� ó�� */
	public void dealInputnickName(String defaulNickname) {
		inputNickNameDialog = new NicknameDialog(this, "Set Nickname", defaulNickname);
		inputNickNameDialog.setSize(250, 130);
		inputNickNameDialog.setVisible(true);

		String nickname = inputNickNameDialog.getInput();
		new StartToProgram(client).requestChangeInfoToServer(nickname);

		// page2�� �Ѿ �Ŀ� ���̾�α� ����
	}
}

class NicknameDialog extends JDialog {
	JLabel label = new JLabel("Please enter your nickname.");
	JTextField inputNicknameField = new JTextField();
	JButton okButton = new JButton("OK");

	public NicknameDialog(JFrame jf, String title, String defaulNickname) {
		super(jf, title, true);

		setLayout(null);

		label.setBounds(40, 10, 170, 20);
		// label.setBackground(Color.yellow);
		// label.setOpaque(true);
		add(label);

		inputNicknameField.setBounds(55, 40, 130, 25);
		inputNicknameField.setText(defaulNickname);
		add(inputNicknameField);

		okButton.setBounds(150, 70, 80, 23);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});
		add(okButton);

		setResizable(false);
	}

	/** ����ڰ� �Է��� �г��� ��ȯ */
	String getInput() {
		if (inputNicknameField.getText().length() == 0) {
			return null;
		} else {
			return inputNicknameField.getText();
		}
	}
}
