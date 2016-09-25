package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client_manager.LinKlipboardClient;

public class UserInterfacePage1 extends BasePanel {
	private UserInterfaceManager main;
	private UserInterfacePage2 page2;
	
	private JLabel mainImgLabel = new JLabel(); // ���� �̹���
	private JLabel groupNameLabel = new JLabel();
	private JLabel groupPWLabel = new JLabel();
	private JTextField groupNameField = new JTextField(); // �׷��̸� �ʵ�
	private JPasswordField groupPassWordField = new JPasswordField(); // �׷��н����� �ʵ�
	private JButton createButton = new JButton(); // ������ư
	private JButton joinButton = new JButton(); // ���ӹ�ư
	private JLabel responseState = new JLabel(); // ����Ȯ�� Label

	private String groupName; // ����ڰ� �Է��� �׷��̸�
	private String groupPW; // ����ڰ� �Է��� �׷��н�����
	
	public UserInterfacePage1(LinKlipboardClient client, UserInterfaceManager main) {
		super(client);

		this.main = main;
		
		setLayout(null);
		setSize(320, 400);
		
		initField();
		client.setScreen(this);

		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		mainImgLabel.setIcon(new ImageIcon("image/mainImage.png")); 
//		mainImgLabel.setBackground(new Color(255, 132, 0));
//		mainImgLabel.setOpaque(true);
		mainImgLabel.setBounds(0, 0, 320, 250);
		add(mainImgLabel);

		groupNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		groupNameLabel.setText("Group Name");
		groupNameLabel.setBounds(25, 275, 100, 20);
		// groupNameLabel.setBackground(Color.GRAY);
		// groupNameLabel.setOpaque(true);
		add(groupNameLabel);

		groupPWLabel.setHorizontalAlignment(SwingConstants.CENTER);
		groupPWLabel.setText("Group Password");
		groupPWLabel.setBounds(25, 305, 100, 20);
		// groupPWLabel.setBackground(Color.GRAY);
		// groupPWLabel.setOpaque(true);
		add(groupPWLabel);

		groupNameField.setText("");
		groupNameField.setBounds(135, 275, 150, 20);
		add(groupNameField);

		groupPassWordField.setText("");
		groupPassWordField.setBounds(135, 305, 150, 20);
		add(groupPassWordField);
		
		

		responseState.setText("");
		responseState.setBounds(87, 335, 150, 20);
		responseState.setHorizontalAlignment(JLabel.CENTER);
//		responseState.setBackground(Color.GRAY);
		responseState.setOpaque(true);
		add(responseState);

		createButton.setText("Create");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createButtonActionPerformed(evt);
			}
		});
		createButton.setBounds(160 - 15 - 80, 365, 80, 23);
		add(createButton);

		joinButton.setText("Join");
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				joinButtonActionPerformed(evt);
			}
		});
		joinButton.setBounds(160 + 15, 365, 80, 23);
		add(joinButton);
	}

	/**
	 * �׷��� ����, �����ϱ� ���� ������ ��� �Է��ߴ��� Ȯ��. ��� �Է� ������ return true;
	 */
	private boolean checkAllInputData() {
		if (groupNameField.getText().length() == 0 || groupPassWordField.getPassword().length == 0) {
			responseState.setText("�ʿ� ���� �Է�");
			return false;
		}
		return true;
	}

	/** �Է� �ʵ带 �ʱ�ȭ */
	public void initField() {
		groupNameField.setText("");
		groupPassWordField.setText("");
	}

	// ��� ����(�ܺο��� ���?) -> LinKlipboardClient�� �� Panel�� �Ѱ��ְ� �����ڵ�������
	/** �������̽��� ���� ���� ǥ�� */
	public void updateErrorState(String errorMsg) {
		responseState.setText(errorMsg);
	}

	private void createButtonActionPerformed(ActionEvent evt) {
		if (checkAllInputData() == true) {
			groupName = groupNameField.getText();
			groupPW = new String(groupPassWordField.getPassword());

			// 1. �׷����� ����
			client.setGroupInfo(groupName, groupPW);
			// 2. �׷������ ��û
			client.createGroup();
			// 3. �Է�â �ʱ�ȭ
			initField();
			
			main.dealInputnickName(this.client.getNickName()); // �г��� ����
			client.getOtherClients().add(this.client.getNickName()); //�ڽŵ� �߰�
			page2 = new UserInterfacePage2(client, trayIcon, main);
			System.out.println("[page1] " + client.getOtherClients().size());
			main.setContentPane(page2);
			
		}
	}

	protected void joinButtonActionPerformed(ActionEvent evt) {
		if (checkAllInputData() == true) {
			groupName = groupNameField.getText();
			groupPW = new String(groupPassWordField.getPassword());

			// 1. �׷����� ����
			client.setGroupInfo(groupName, groupPW);
			// 2. �׷������� ��û
			client.joinGroup();
			// 3. �Է�â �ʱ�ȭ
			initField();
			
			main.dealInputnickName(this.client.getNickName()); // �г��� ����
			client.getOtherClients().add(this.client.getNickName()); //�ڽŵ� �߰�
			main.setContentPane(page2);
		}
	}
}

