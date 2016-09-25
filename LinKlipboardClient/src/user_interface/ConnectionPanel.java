package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import contents.StringContents;
import server_manager.LinKlipboard;
import transfer_manager.CommunicatingWithServer;

public class ConnectionPanel extends BasePanel {
	private JLabel accessGroupNameLabel = new JLabel(); // �ڽ��� ���� �׷��
	private JLabel accessCountLabel = new JLabel(); // ���� �׷쿡 ���� ������ ��

	private JScrollPane accessPersonScrollPane = new JScrollPane();
	private JList<String> accessPersonList = new JList<String>(); // ������ ����Ʈ

	private JLabel sharedIcon = new JLabel();
	private JLabel sharedTimeLabel = new JLabel();// �ֽ� ������ �ð� ����
	private JLabel sharedContentsInfoLabel = new JLabel(); // �ֽ� ���� Content����

	private JButton receiveButton = new JButton();

	public ConnectionPanel(LinKlipboardClient client) {
		super(client);

		// ������ ������ ó���ϴ� Ŭ����
		communicatingWithServer = new CommunicatingWithServer(client);

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

		System.out.println("[Connect] " + client.getOtherClients().size() );
		accessCountLabel.setText("���� " + client.getOtherClients().size() + "�� ���� ��");
		accessCountLabel.setBounds(187, 20, 104, 20);
		// accessCountLabel.setBackground(Color.GRAY);
		// accessCountLabel.setOpaque(true);
		add(accessCountLabel);

		// client�� Vector<String>���� ����
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

		sharedIcon.setIcon(new ImageIcon("image/sharedImage.png"));
		sharedIcon.setBounds(24, 220, 20, 20);
		// sharedIcon.setBackground(Color.GRAY);
		// sharedIcon.setOpaque(true);
		add(sharedIcon);
		
		// �ֽ� ������ Contents�� ������
		if (client.getLatestContents() == null) {
			sharedTimeLabel.setText("-------------------");
			sharedContentsInfoLabel.setText("�ֽ� ������ �����Ͱ� �����ϴ�.");
		} else {
			Contents latestContents = client.getLatestContents();
			String sharer = latestContents.getSharer();
			String dataType = null;
			String dataInfo = null;

			sharedTimeLabel.setText("[" + latestContents.getDate() + "]");

			if (latestContents.getType() == LinKlipboard.FILE_TYPE) {
				FileContents fc = new FileContents();
				fc = (FileContents) latestContents;
				dataType = "����";
				dataInfo = fc.getFileName();
			} else if (latestContents.getType() == LinKlipboard.STRING_TYPE) {
				StringContents sc = new StringContents();
				sc = (StringContents) latestContents;
				dataType = "�ؽ�Ʈ";
				dataInfo = sc.getString();
			} else if (latestContents.getType() == LinKlipboard.IMAGE_TYPE) {
				dataType = "�̹���";
			} else {
				dataInfo = "�������� �ʴ� ������";
			}
			
			if(sharer.length() > 7){
				sharer = dealLengthOfDataInfo(sharer, 7);
			}
			if(dataInfo.length() > 16){
				dataInfo = dealLengthOfDataInfo(dataInfo, 16);
			}

			sharedContentsInfoLabel
					.setText(client.getLatestContents().getSharer() + "���� \\" + dataInfo + "\\ " + dataType + " ����");
		}

		sharedTimeLabel.setBounds(50, 220, 150, 20);
//		sharedTimeLabel.setBackground(Color.GRAY);
//		sharedTimeLabel.setOpaque(true);
		add(sharedTimeLabel);
		
		sharedContentsInfoLabel.setBounds(24, 250, 270, 20);
		sharedContentsInfoLabel.setHorizontalAlignment(JLabel.CENTER);
//		sharedContentsInfoLabel.setBackground(Color.GRAY);
//		sharedContentsInfoLabel.setOpaque(true);
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

	/** �ֽ����� ������ Contents�� �޾ƿ´�. */
	private void receiveButtonActionPerformed(ActionEvent evt) {
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

	/** ���̺��� ũ�⸦ �Ѿ�� ...���� ó�� */
	public String dealLengthOfDataInfo(String dataInfo, int cutSize) {
		return dataInfo.substring(0, cutSize) + ".."; 
	}
}
