package user_interface;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import client_manager.LinKlipboardClient;

public class UserInterfacePage2 extends BasePanel {
	private JLabel tableLabel;
	private JTabbedPane tableTabbedPane = new JTabbedPane();
	private JPanel connectionPanel;
	private JPanel historyPanel;
	private JPanel settingPanel;

	public UserInterfacePage2(LinKlipboardClient client) {
		super(client);

		connectionPanel = new ConnectionPanel(client);
		// historyPanel = new HistoryPanel(client);
		// settingPanel = new SettingPanel(client);

		setLayout(null);
		setSize(320, 400);

		UIManager.put("TabbedPane.selected", new Color(255, 132, 0));

		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		ImageIcon connectionImage = resizeConnectionImageIcon(new ImageIcon("image/connection.png"));
		ImageIcon selectedConnectionImage = resizeConnectionImageIcon(new ImageIcon("image/selectedConnection.png"));
		ImageIcon historyImage = resizeImageIcon(new ImageIcon("image/history.png"));
		ImageIcon selectedHistoryImage = resizeImageIcon(new ImageIcon("image/selectedHistory.png"));
		ImageIcon settingImage = resizeImageIcon(new ImageIcon("image/setting.png"));
		ImageIcon selectedSettingImage = resizeImageIcon(new ImageIcon("image/selectedSetting.png"));

		tableLabel = new JLabel(new ImageIcon("image/tableImage.png"));
		tableLabel.setOpaque(true);
		tableLabel.setBounds(0, 0, 320, 40);
		tableLabel.setVisible(true);
		add(tableLabel);

		tableTabbedPane = new JTabbedPane();
		tableTabbedPane.addTab("", selectedConnectionImage, connectionPanel);
		tableTabbedPane.addTab("", historyImage, historyPanel);
		tableTabbedPane.addTab("", settingImage, settingPanel);

		tableTabbedPane.setBounds(0, 40, 320, 360);
		tableTabbedPane.setBackground(new Color(255, 132, 0));
		tableTabbedPane.setOpaque(true);
		add(tableTabbedPane);

		tableTabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(tableTabbedPane.getSelectedIndex());
				int index = tableTabbedPane.getSelectedIndex();

				if (index == 0) {
					tableTabbedPane.setIconAt(index, selectedConnectionImage);
					tableTabbedPane.setIconAt(1, historyImage);
					tableTabbedPane.setIconAt(2, settingImage);
				} else if (index == 1) {
					tableTabbedPane.setIconAt(0, connectionImage);
					tableTabbedPane.setIconAt(index, selectedHistoryImage);
					tableTabbedPane.setIconAt(2, settingImage);
				} else {
					tableTabbedPane.setIconAt(0, connectionImage);
					tableTabbedPane.setIconAt(1, historyImage);
					tableTabbedPane.setIconAt(index, selectedSettingImage);
				}
			}
		});
	}

	public ImageIcon resizeConnectionImageIcon(ImageIcon imageIcon) {
		Image resizingImage = imageIcon.getImage(); // ImageIcon�� Image�� ��ȯ.
		resizingImage = resizingImage.getScaledInstance(75, 17, java.awt.Image.SCALE_SMOOTH); // resize
		ImageIcon resizingImageIcon = new ImageIcon(resizingImage); // Image�� ImageIcon ����

		return resizingImageIcon;
	}

	public ImageIcon resizeImageIcon(ImageIcon imageIcon) {
		Image resizingImage = imageIcon.getImage(); // ImageIcon�� Image�� ��ȯ.
		resizingImage = resizingImage.getScaledInstance(50, 20, java.awt.Image.SCALE_SMOOTH); // resize
		ImageIcon resizingImageIcon = new ImageIcon(resizingImage); // Image�� ImageIcon ����

		return resizingImageIcon;
	}
}
