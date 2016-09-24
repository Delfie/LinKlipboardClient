package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

public class SettingPanel extends JPanel {
	private ShowNicknamePanel showNicknamePanel = new ShowNicknamePanel();
	private ShortcutSetPanel shortcutSetPanel = new ShortcutSetPanel();

	private JCheckBox setNotification = new JCheckBox();
	private JCheckBox setReceiveContents = new JCheckBox();

	private JButton exitButton = new JButton();

	public SettingPanel() {
		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		showNicknamePanel.setBounds(15, 20, 280, 70);
		add(showNicknamePanel);

		shortcutSetPanel.setBounds(15, 115, 280, 70);
		add(shortcutSetPanel);

		setNotification.setText("Turn off the notification (Always recieved)");
		setNotification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setNotificationActionPerformed(evt);
			}
		});
		setNotification.setBounds(20, 210, 280, 20);
		add(setNotification);

		setReceiveContents.setText("Deny the recieve clipboard data");
		setReceiveContents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setReceiveContentsActionPerformed(evt);
			}
		});
		setReceiveContents.setBounds(20, 245, 280, 20);
		add(setReceiveContents);

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
		// TODO add your handling code here:
	}

}

class ShowNicknamePanel extends JPanel {
	private JLabel userNicknameLabel = new JLabel();

	public ShowNicknamePanel() {
		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createTitledBorder("Your Nickname"));

		userNicknameLabel.setText("jLabel1");
		userNicknameLabel.setBounds(50, 50, 100, 20);
		add(userNicknameLabel);

		GroupLayout showNicknamePanelLayout = new GroupLayout(this);
		this.setLayout(showNicknamePanelLayout);
		showNicknamePanelLayout
				.setHorizontalGroup(showNicknamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(showNicknamePanelLayout
								.createSequentialGroup().addGap(64, 64, 64).addComponent(userNicknameLabel,
										GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(75, Short.MAX_VALUE)));
		showNicknamePanelLayout
				.setVerticalGroup(showNicknamePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(showNicknamePanelLayout.createSequentialGroup().addContainerGap()
								.addComponent(userNicknameLabel, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
								.addContainerGap()));
	}
}

class ShortcutSetPanel extends JPanel {
	private JComboBox<String> firstShortcut = new JComboBox<>();
	private JLabel label = new JLabel();
	private JComboBox<String> secondShortcut = new JComboBox<>();

	public ShortcutSetPanel() {
		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {

		this.setBorder(BorderFactory.createTitledBorder("Shortcut Setting"));
		secondShortcut.setModel(new DefaultComboBoxModel<>(new String[] { "A", "B", "D", "E", "F", "G", "H", "I", "J",
				"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "W", "X", "Y", "Z" }));
		secondShortcut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				secondShortcutActionPerformed(evt);
			}
		});

		label.setText("+");

		firstShortcut.setModel(new DefaultComboBoxModel<>(new String[] { "Ctrl", "Alt" }));
		firstShortcut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				firstShortcutActionPerformed(evt);
			}
		});

		GroupLayout shortcutSetPanelLayout = new GroupLayout(this);
		this.setLayout(shortcutSetPanelLayout);
		shortcutSetPanelLayout.setHorizontalGroup(shortcutSetPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(shortcutSetPanelLayout.createSequentialGroup().addGap(66, 66, 66)
						.addComponent(firstShortcut, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(label)
						.addGap(18, 18, 18)
						.addComponent(secondShortcut, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		shortcutSetPanelLayout
				.setVerticalGroup(shortcutSetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(shortcutSetPanelLayout.createSequentialGroup().addContainerGap()
								.addGroup(shortcutSetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(firstShortcut, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label).addComponent(secondShortcut, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
	}

	private void secondShortcutActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void firstShortcutActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}
}