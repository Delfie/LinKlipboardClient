package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class UserInterfacePage1 extends JPanel {
	private JLabel mainImgLabel = new JLabel(); // 메인 이미지
	private JLabel groupNameLabel = new JLabel();
	private JLabel groupPWLabel = new JLabel();
	private JTextField groupNameField = new JTextField(); // 그룹이름 필드
	private JPasswordField groupPWField = new JPasswordField(); // 그룹패스워드 필드
	private JButton createButton = new JButton(); // 생성버튼
	private JButton joinButton = new JButton(); // 접속버튼

	/**
	 * Creates new form UserInterfacePage1
	 */
	public UserInterfacePage1() {
		setLayout(null);
		setSize(320, 400);

		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		// mainImgLabel.setIcon(new
		// ImageIcon("C:\\Users\\Administrator\\Desktop\\mainImg.PNG")); //
		// NOI18N
		mainImgLabel.setBackground(new Color(255, 132, 0));
		mainImgLabel.setOpaque(true);
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

		groupPWField.setText("");
		groupPWField.setBounds(135, 305, 150, 20);
		add(groupPWField);

		createButton.setText("Create");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createButtonActionPerformed(evt);
			}
		});
		createButton.setBounds(160 - 15 - 80, 355, 80, 23);
		add(createButton);

		joinButton.setText("Join");
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				joinButtonActionPerformed(evt);
			}
		});
		joinButton.setBounds(160 + 15, 355, 80, 23);
		add(joinButton);
	}

	protected void joinButtonActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub

	}

	private void createButtonActionPerformed(ActionEvent evt) {
		// TODO add your handling code here:
	}

}
