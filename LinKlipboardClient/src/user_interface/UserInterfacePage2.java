package user_interface;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UserInterfacePage2 extends JPanel {
	private JLabel tableLabel = new JLabel();
	private JPanel connectionPanel = new ConnectionPanel();
	private JPanel historyPanel = new HistoryPanel();
	private JPanel settingPanel = new SettingPanel();
	private JTabbedPane tableTabbedPane = new JTabbedPane();

	public UserInterfacePage2() {
		setLayout(null);
		setSize(320, 400);

		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		tableLabel.setBackground(new Color(255, 132, 0));
		tableLabel.setOpaque(true);
		tableLabel.setBounds(0, 0, 320, 40);
		add(tableLabel);

		tableTabbedPane.addTab("connection", connectionPanel);
		tableTabbedPane.addTab("history", historyPanel);
		tableTabbedPane.addTab("setting", settingPanel);

		tableTabbedPane.setBounds(0, 40, 320, 360);
		add(tableTabbedPane);
	}

}
