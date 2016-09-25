package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import client_manager.LinKlipboardClient;
import contents.Contents;

public class ConnectionPanel extends BasePanel {
	private JLabel accessGroupNameLabel = new JLabel(); // 자신이 속한 그룹명
	private JLabel accessCountLabel = new JLabel(); // 같은 그룹에 들어온 접속자 수

	private JScrollPane accessPersonScrollPane = new JScrollPane();
	private JList<String> accessPersonList = new JList<String>(); // 접속자 리스트

	private JLabel sharedIcon = new JLabel();
	private JLabel sharedTimeLabel = new JLabel();// 최신 공유한 시간 정보
	private JLabel sharedContentsInfoLabel = new JLabel(); // 최신 공유 Content정보

	private JButton receiveButton = new JButton();

	public ConnectionPanel(LinKlipboardClient client) {
		super(client);

		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		accessGroupNameLabel.setText(LinKlipboardClient.getGroupName());
		accessGroupNameLabel.setBounds(24, 20, 80, 20);
		// accessGroupNameLabel.setBackground(Color.GRAY);
		// accessGroupNameLabel.setOpaque(true);
		add(accessGroupNameLabel);

		accessCountLabel.setText("현재 " + client.getOtherClients().size() + "명 접속 중");
		accessCountLabel.setBounds(187, 20, 104, 20);
		// accessCountLabel.setBackground(Color.GRAY);
		// accessCountLabel.setOpaque(true);
		add(accessCountLabel);

		// client의 Vector<String>값을 저장
		DefaultListModel<String> model = new DefaultListModel<>();

		// add item to model
		for (int i = 0; i < client.getOtherClients().size(); i++) {
			model.add(0, client.getOtherClients().elementAt(i));
		}

		// create JList with model
		accessPersonList = new JList<String>(model);

		accessPersonScrollPane.setViewportView(accessPersonList);
		accessPersonScrollPane.setBounds(24, 50, 270, 150);
		add(accessPersonScrollPane);

		// accessCountLabel.setText("현재 " + accessPersonList.getModel().getSize() + "명 접속 중");

		sharedIcon.setText("IC");
		sharedIcon.setBounds(24, 220, 20, 20);
		// sharedIcon.setBackground(Color.GRAY);
		// sharedIcon.setOpaque(true);
		add(sharedIcon);

		sharedTimeLabel.setText("[2016-08-28 오후 2:51]");
		sharedTimeLabel.setBounds(50, 220, 150, 20);
		// sharedTimeLabel.setBackground(Color.GRAY);
		// sharedTimeLabel.setOpaque(true);
		add(sharedTimeLabel);

		sharedContentsInfoLabel.setText("delf님이 \"테스트 문장을 복사하였습... \" 공유");
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
