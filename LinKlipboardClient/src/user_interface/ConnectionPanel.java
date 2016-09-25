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

		// 서버와 응답을 처리하는 클래스
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

		sharedIcon.setIcon(new ImageIcon("image/sharedImage.png"));
		sharedIcon.setBounds(24, 220, 20, 20);
		// sharedIcon.setBackground(Color.GRAY);
		// sharedIcon.setOpaque(true);
		add(sharedIcon);
		
		// 최신 공유된 Contents가 없으면
		if (client.getLatestContents() == null) {
			sharedTimeLabel.setText("-------------------");
			sharedContentsInfoLabel.setText("최신 공유된 데이터가 없습니다.");
		} else {
			Contents latestContents = client.getLatestContents();
			String sharer = latestContents.getSharer();
			String dataType = null;
			String dataInfo = null;

			sharedTimeLabel.setText("[" + latestContents.getDate() + "]");

			if (latestContents.getType() == LinKlipboard.FILE_TYPE) {
				FileContents fc = new FileContents();
				fc = (FileContents) latestContents;
				dataType = "파일";
				dataInfo = fc.getFileName();
			} else if (latestContents.getType() == LinKlipboard.STRING_TYPE) {
				StringContents sc = new StringContents();
				sc = (StringContents) latestContents;
				dataType = "텍스트";
				dataInfo = sc.getString();
			} else if (latestContents.getType() == LinKlipboard.IMAGE_TYPE) {
				dataType = "이미지";
			} else {
				dataInfo = "지원하지 않는 컨텐츠";
			}
			
			if(sharer.length() > 7){
				sharer = dealLengthOfDataInfo(sharer, 7);
			}
			if(dataInfo.length() > 16){
				dataInfo = dealLengthOfDataInfo(dataInfo, 16);
			}

			sharedContentsInfoLabel
					.setText(client.getLatestContents().getSharer() + "님이 \\" + dataInfo + "\\ " + dataType + " 공유");
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

	/** 최신으로 공유된 Contents를 받아온다. */
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
			System.out.println("[ConnectionPanel] File, String, Image 어디에도 속하지 않음");
		}
	}

	/** 레이블의 크기를 넘어가면 ...으로 처리 */
	public String dealLengthOfDataInfo(String dataInfo, int cutSize) {
		return dataInfo.substring(0, cutSize) + ".."; 
	}
}
