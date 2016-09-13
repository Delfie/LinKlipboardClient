import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class UserInterface extends JFrame {

	private LinKlipboardClient client;

	JLabel groupNameLabel = new JLabel("Group Name");
	JLabel groupPassWordLabel = new JLabel("Password");
	
	JTextField groupNameField = new JTextField();
	JTextField groupPassWordField = new JTextField();

	JLabel responseState = new JLabel("ERROR STATE"); //����Ȯ��Lable

	JButton createGroupBtn = new JButton("CREATE");
	JButton joinGroupBtn = new JButton("JOIN");
	
	public UserInterface() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setHooker();
		setComponents();
		setSize(340, 500);
		setVisible(true);
	}

	/** ����Ű(�ʱⰪ[Ctrl + T])�� ������ ������ ������ ����  */
	private void setHooker() {
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
		
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				if (event.isControlPressed()) {
					if(event.getVirtualKeyCode() == event.VK_T) {
						System.out.println("[Ctrl + T] is detected.");
						client.sendDateToServer();
					}
				}
			}
		});
	}

	/** components�� ��ġ ���� */
	private void setComponents() {
		setLayout(null);

		int X1 = 30;
		int Y1 = 10;
		int textHight = 20;
		int X2 = 40;
		int Y2 = 70;

		groupNameLabel.setBounds(X2, Y2, 150, 30);
		groupPassWordLabel.setBounds(X2, Y2 + 40, 150, 30);

		groupNameField.setBounds(X2 + 80, Y2, 150, 30);
		groupPassWordField.setBounds(X2 + 80, Y2 + 40, 150, 30);

		add(groupNameLabel);
		add(groupPassWordLabel);

		add(groupNameField);
		add(groupPassWordField);
		
		// ���� Ȯ�� Lable
		responseState.setBounds(X2+20, Y2+150, 200, 30);
		responseState.setBackground(Color.yellow);
		responseState.setOpaque(true);
		add(responseState);

		// ���� ��ư ����, ����
		createGroupBtn.setBounds(X1, 400, 100, 40);
		add(createGroupBtn);

		/** CREATE ��ư�� ���� ó�� */
		createGroupBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (groupNameField.getText().isEmpty() || groupPassWordField.getText().isEmpty()) {
					responseState.setText("�ʿ����� �Է�");
					return;
				} 
				
				//1. LinKlipboardClient ����
				createClient(groupNameField.getText(), groupPassWordField.getText());
				//2. �׷������ ��û
				client.createGroup();
			}
		});

		// ���� ��ư ����, ����
		joinGroupBtn.setBounds(X1+150, 400, 100, 40);
		add(joinGroupBtn);

		/** JOIN ��ư�� ���� ó�� */
		joinGroupBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (groupNameField.getText().isEmpty() || groupPassWordField.getText().isEmpty()) {
					responseState.setText("�ʿ����� �Է�");
					return;
				}

				//1. LinKlipboardClient ����
				createClient(groupNameField.getText(), groupPassWordField.getText());
				//2. �׷������� ��û
				client.joinGroup();
			}
		});
	}

	/** Ŭ���̾�Ʈ ���� ���� */
	private void createClient(String groupName, String groupPassword) {
		client = new LinKlipboardClient(groupName, groupPassword, this);
	}

	/** ���� ���� ǥ�� */
	public void updateErrorState(String response) {
		responseState.setText(response);
	}
	
	/** �Է� �ʵ� �ʱ�ȭ */
	public void initInputField(){
		groupNameField.setText("");
		groupPassWordField.setText("");
	}
	
	/** Ŭ���̾�Ʈ ���� �ʱ�ȭ */
	public void initClientInfo(LinKlipboardClient client){
		client.initGroupInfo();
		client.initResponse();
	}

	public static void main(String[] args) {
		new UserInterface();
	}

}




class LabelAndTextField {
	private JLabel lb;
	private JTextField tf;

	public LabelAndTextField(Object initLb, Object initTf) {
		lb = new JLabel();
	}
}

class TwoLabels {
	private JLabel first;
	private JLabel second;
	private int gap;

	public TwoLabels(String first, String second, int gap) {
		this.first = new JLabel(first);
		this.second = new JLabel(second);
		this.gap = gap;
	}

	public void initTwoLables(String first, String second, int gap) {
		this.first.setText(first);
		this.second.setText(second);
		this.gap = gap;
	}

	public void setFirstLabel(String first) {
		this.first.setText(first);
	}

	public void setSecondLabel(String second) {
		this.second.setText(second);
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

}