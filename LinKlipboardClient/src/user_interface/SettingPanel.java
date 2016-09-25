package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import client_manager.LinKlipboardClient;
import datamanage.History;

public class SettingPanel extends BasePanel {
	private SetHistorySizePanel setHistorySizePanel;
	private ShortcutSetPanel shortcutSetPanel;

	private JLabel showNickname = new JLabel("Your Nickname: ");
	private JLabel userNickname = new JLabel();

	private JCheckBox setNotification = new JCheckBox();

	public static boolean notification = true; // 도연 추가

	private JButton exitButton = new JButton();

	public SettingPanel(LinKlipboardClient client, TrayIconManager trayIcon, UserInterfaceManager main) {
		super(client, trayIcon, main);
		
		userNickname.setText(client.getNickName());
		setHistorySizePanel = new SetHistorySizePanel(client);
		shortcutSetPanel = new ShortcutSetPanel(client);

		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		showNickname.setBounds(20, 30, 95, 20);
		// showNickname.setBackground(Color.yellow);
		// showNickname.setOpaque(true);
		add(showNickname);

		userNickname.setBounds(115, 30, 180, 20);
		// userNickname.setBackground(Color.yellow);
		// userNickname.setOpaque(true);
		add(userNickname);

		setHistorySizePanel.setBounds(15, 70, 280, 70);
		add(setHistorySizePanel);

		shortcutSetPanel.setBounds(15, 150, 280, 95);
		add(shortcutSetPanel);

		setNotification.setText("Turn off the notification (Always recieved)");
		// 도연 추가
		setNotification.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (setNotification.isSelected()) {
					notification = false;
				} else {
					notification = true;
				}

				System.out.println("notification = " + notification);
			}
		});
		setNotification.setBounds(20, 260, 280, 20);
		add(setNotification);

		exitButton.setText("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exitButtonActionPerformed(evt);
			}
		});
		exitButton.setBounds(215, 290, 80, 23);
		add(exitButton);
	}

	private void setNotificationActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void setReceiveContentsActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void exitButtonActionPerformed(ActionEvent evt) {
		// contentPane을 page1으로 돌아가도록
		// 돌아가기전에 client정보 초기화 or 새로 생성??
	}
}

class SetHistorySizePanel extends BasePanel {
	private String[] posibleSize = { "10", "20", "30", "40" };
	private JComboBox setHistorySize = new JComboBox(posibleSize);

	public SetHistorySizePanel(LinKlipboardClient client) {
		super(client);
		setHistorySize.setSelectedItem("10");
		setSize(320, 360);
		initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder("Set My History Size"));

		setHistorySize.setBounds(50, 50, 50, 30);
		add(setHistorySize);
		
		setHistorySize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String item = (String) setHistorySize.getSelectedItem();
				int historySize = Integer.parseInt(item);
				System.out.println("historySize: " + historySize);
				
				History.setHistorySize(historySize);
				ListPanel.setMaxNumOfContents(historySize);
			}
		});
	}
}

class ShortcutSetPanel extends BasePanel {
	private JLabel sendLabel = new JLabel("send shortcut");
	private String[] firstString = new String[] { "Ctrl", "Alt" };
	private JComboBox firstShortcutForSend = new JComboBox(firstString);
	private JLabel label1 = new JLabel("+");
	private String[] secondStringForSend = new String[] { "A", "B", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
			"N", "O", "P", "Q", "R", "S", "T", "U", "W", "X", "Y", "Z" };
	private JComboBox secondShortcut = new JComboBox(secondStringForSend);

	private JLabel receiveLabel = new JLabel("receive shortcut");
	private JComboBox firstShortcutForReceive = new JComboBox(firstString);
	private JLabel label2 = new JLabel("+");
	private JComboBox secondShortcutForReceive = new JComboBox(secondStringForSend);

	public ShortcutSetPanel(LinKlipboardClient client) {
		super(client);
		
		firstShortcutForSend.setSelectedItem("Ctrl");
		secondShortcut.setSelectedItem("Q");
		firstShortcutForReceive.setSelectedItem("Alt");
		secondShortcutForReceive.setSelectedItem("Q");

		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder("Shortcut Setting"));

		sendLabel.setBounds(25, 20, 100, 30);
		add(sendLabel);

		firstShortcutForSend.setBounds(10, 50, 50, 30);
		add(firstShortcutForSend);

		label1.setBounds(65, 50, 50, 30);
		add(label1);

		secondShortcut.setBounds(80, 50, 50, 30);
		add(secondShortcut);

		receiveLabel.setBounds(165, 20, 100, 30);
		add(receiveLabel);

		firstShortcutForReceive.setBounds(150, 50, 50, 30);
		add(firstShortcutForReceive);

		label2.setBounds(205, 50, 50, 30);
		add(label2);

		secondShortcutForReceive.setBounds(220, 50, 50, 30);
		add(secondShortcutForReceive);
	}

	private void secondShortcutActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void firstShortcutActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}
}