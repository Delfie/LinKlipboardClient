package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ConnectionPanel extends JPanel {
	private JLabel accessGroupNameLabel = new JLabel(); // �ڽ��� ���� �׷��
	private JLabel accessCountLabel = new JLabel(); // ���� �׷쿡 ���� ������ ��

	private JScrollPane accessPersonScrollPane = new JScrollPane();
	private JList<String> accessPersonList = new JList<>(); // ������ ����Ʈ

	private JLabel sharedIcon = new JLabel();
	private JLabel sharedTimeLabel = new JLabel();// �ֽ� ������ �ð� ����
	private JLabel sharedContentsInfoLabel = new JLabel(); // �ֽ� ���� Content����

	private JButton receiveButton = new JButton();

	public ConnectionPanel() {
		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		accessGroupNameLabel.setText("Goddesses");
		accessGroupNameLabel.setBounds(24, 20, 80, 20);
//		accessGroupNameLabel.setBackground(Color.GRAY);
//		accessGroupNameLabel.setOpaque(true);
		add(accessGroupNameLabel);

		accessCountLabel.setText("���� 10�� ���� ��");
		accessCountLabel.setBounds(187, 20, 104, 20);
//		accessCountLabel.setBackground(Color.GRAY);
//		accessCountLabel.setOpaque(true);
		add(accessCountLabel);

		accessPersonList.setModel(new AbstractListModel<String>() {
			String[] accessNickname = { "student_1", "student_2", "delf", "heeeee", "doooy", " ", " " };

			public int getSize() {
				return accessNickname.length;
			}

			public String getElementAt(int i) {
				return accessNickname[i];
			}
		});
		accessPersonScrollPane.setViewportView(accessPersonList);
		accessPersonScrollPane.setBounds(24, 50, 270, 150);
		add(accessPersonScrollPane);

		sharedIcon.setText("IC");
		sharedIcon.setBounds(24, 220, 20, 20);
//		sharedIcon.setBackground(Color.GRAY);
//		sharedIcon.setOpaque(true);
		add(sharedIcon);

		sharedTimeLabel.setText("[2016-08-28 ���� 2:51]");
		sharedTimeLabel.setBounds(50, 220, 150, 20);
//		sharedTimeLabel.setBackground(Color.GRAY);
//		sharedTimeLabel.setOpaque(true);
		add(sharedTimeLabel);

		sharedContentsInfoLabel.setText("delf���� \"�׽�Ʈ ������ �����Ͽ���... \" ����");
		sharedContentsInfoLabel.setBounds(24, 250, 270, 20);
		sharedContentsInfoLabel.setBackground(Color.GRAY);
		sharedContentsInfoLabel.setOpaque(true);
		add(sharedContentsInfoLabel);

		receiveButton.setText("Receive");
		receiveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				receiveButtonActionPerformed(evt);
			}
		});
		receiveButton.setBounds(215, 290, 80, 23);
		add(receiveButton);
	}

	private void receiveButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

}
