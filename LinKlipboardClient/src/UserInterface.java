//import java.awt.Color;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.StringTokenizer;
//
//import javax.swing.ButtonGroup;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JRadioButton;
//import javax.swing.JTextField;
//
//import lc.kra.system.keyboard.GlobalKeyboardHook;
//import lc.kra.system.keyboard.event.GlobalKeyAdapter;
//import lc.kra.system.keyboard.event.GlobalKeyEvent;
//
//public class UserInterface extends JFrame {
//
//	private LinKlipboardHost host;
//
//	private boolean connected = false;
//
//	JLabel titleNetworkSetting = new JLabel("Network Setting");
//
//	JRadioButton[] scRadioBtn = new JRadioButton[2];
//	final static int SERVER = 0;
//	final static int CLIENT = 1;
//
//	JLabel ipAddrLabel = new JLabel("IP Address");
//	JLabel portNumLabel = new JLabel("Port");
//
//	JTextField ipTextField = new JTextField();
//	JTextField portTextField = new JTextField("5000");
//
//	JButton startConnection = new JButton("Start");
//
//	JLabel titleClipboardDataTransferState = new JLabel("Clipboard Data Transfer State");
//	JLabel receiveState = new JLabel("Receive State");
//
//	JLabel stateInfo = new JLabel("Connetion State");
//	JLabel connetionState = new JLabel("");
//
//	JLabel ClipboardFormat = new JLabel("Clipboard Format");
//	JLabel ClipboardState = new JLabel("");
//
//	JLabel show = new JLabel("show contents");
//	JLabel send = new JLabel("SEND");
//
//	JButton showBtn = new JButton("SHOW");
//	JButton sendBtn = new JButton("SEND");
//
//	JButton sendFileBtn = new JButton("File Send");
//	JTextField filePath = new JTextField();
//
//	public UserInterface() {
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setHooker();
//		setComponents();
//		setSize(340, 500);
//		setVisible(true);
//	}
//
//	private void setHooker() {
//		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
//		
//		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
//			@Override
//			public void keyPressed(GlobalKeyEvent event) {
//				if (event.isControlPressed()) {
//					if(event.getVirtualKeyCode() == event.VK_T) {
//						System.out.println("[Ctrl + T] is detected.");
//						host.sendClipboardContents();
//					}
//				}
//			}
//		});
//	}
//
//	private void setComponents() {
//
//		//setLayout(new FlowLayout());
//		setLayout(null);
//
//		int X1 = 30;
//		int Y1 = 10;
//		int textHight = 20;
//		titleNetworkSetting.setBounds(X1, Y1, 100, textHight);
//
//		// ���� ��ư ����
//		ButtonGroup g = new ButtonGroup();
//		scRadioBtn[SERVER] = new JRadioButton("Server");
//		scRadioBtn[CLIENT] = new JRadioButton("Client");
//		
//		// ���� ��ư ũ��, ��ġ ����
//		scRadioBtn[SERVER].setBounds(X1, Y1, 70, textHight);
//		scRadioBtn[CLIENT].setBounds(X1 + 80, Y1, 70, textHight);
//
//		// ������ư ������ ����
//		RadioBtnListener rl = new RadioBtnListener();
//		for (int i = 0; i < 2; i++) {
//			g.add(scRadioBtn[i]);
//			scRadioBtn[i].addItemListener(rl);
//			add(scRadioBtn[i]);
//		}
//
//		// start ��ư ����, ����
//		startConnection.setBounds(X1 + 180, Y1 + 5, 80, 40);
//		add(startConnection);
//		int X2 = 40;
//		int Y2 = 70;
//
//		ipAddrLabel.setBounds(X2, Y2, 150, 30);
//		portNumLabel.setBounds(X2, Y2 + 40, 150, 30);
//
//		ipTextField.setBounds(X2 + 80, Y2, 150, 30);
//		portTextField.setBounds(X2 + 80, Y2 + 40, 150, 30);
//		ipTextField.setEditable(false); // ���� �Ұ���
//
//		add(ipAddrLabel);
//		add(portNumLabel);
//
//		add(ipTextField);
//		add(portTextField);
//
//		/** start ��ư�� ���� ó�� */
//		startConnection.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				int port = Integer.parseInt(portTextField.getText());
//
//				if (ipTextField.getText().isEmpty() || portTextField.getText().isEmpty()) {
//					connetionState.setText("�ʿ����� �Է�");
//					return;
//				} else {
//					// Server ���ý�
//					if (scRadioBtn[SERVER].isSelected()) {
//						createServer(port);
//						// ���� �ٽ� Ŭ���̾�Ʈ �����ڽ��� �����Ѵٸ�
//						// Server�� ������ �ȿ��� ������� BindWait �����带 �׿���� ��
//					} else if (scRadioBtn[CLIENT].isSelected()) {
//						String ip = ipTextField.getText();
//						createClient(ip, port);
//					} else {
//						connetionState.setText("server �Ǵ� client ����");
//					}
//				}
//			}
//		}); // ��ư ��
//
//		// ���� ����
//		stateInfo.setBounds(X1, 190, 100, textHight);
//		add(stateInfo);
//		connetionState.setBounds(X1 + 120, 185, 120, textHight + 10);
//		connetionState.setBackground(Color.LIGHT_GRAY);
//		connetionState.setOpaque(true);
//		add(connetionState, JLabel.CENTER);
//
//		// Ŭ������ ����
//		ClipboardFormat.setBounds(X1, 240, 100, textHight);
//		add(ClipboardFormat);
//		ClipboardState.setBounds(X1 + 120, 235, 120, textHight + 10);
//		ClipboardState.setBackground(Color.LIGHT_GRAY);
//		ClipboardState.setOpaque(true);
//		add(ClipboardState, JLabel.CENTER);
//
//		// Ŭ������ ���� Ȯ�� ��ư
//		showBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				if (ClipboardDatatTansferor.isSystemClipboardEmpty()) {
//					ClipboardState.setText("Empty");
//				}
//				DataFlavor clipboadNow = ClipboardDatatTansferor.whatKindofDataFlavorintSystemClipboardNow();
//
//				if (clipboadNow.equals(DataFlavor.stringFlavor)) {
//					ClipboardState.setText("���ڿ�");
//				} else if (clipboadNow.equals(DataFlavor.imageFlavor)) {
//					ClipboardState.setText("�̹���");
//				} else if (clipboadNow.equals(DataFlavor.javaFileListFlavor)) {
//					ClipboardState.setText("����");
//				} else {
//					ClipboardState.setText("not supported");
//				}
//			}
//		});
//
//		//���� ��ư
//		showBtn.setBounds(X1, 400, 100, 40);
//		add(showBtn);
//
//		sendBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				host.sendClipboardContents();
//			}
//		});
//
//		sendBtn.setBounds(X1 + 150, 400, 100, 40);
//		add(sendBtn);
//
//		// ���� ���� ��ư
//		sendFileBtn.setBounds(X1 + 150, 350, 100, 40);
//		add(sendFileBtn);
//
//		sendFileBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				host.fileSend();
//				filePath.setText("");
//			}
//		});
//
//		// ���� ��� �Է� �ؽ�Ʈ�ʵ�
//		filePath.setBounds(X1 - 20, 300, 300, textHight);
//		add(filePath);
//	}
//
//	/** ���������� ���� */
//	private void createServer(int port) {
//		host = new LinKlipboardHost(port, this);
//	}
//
//	/** Ŭ���̾�Ʈ ���� ���� */
//	private void createClient(String ip, int port) {
//		host = new LinKlipboardHost(ip, port, this);
//	}
//
//	public void updateConnectState(String text) {
//		connetionState.setText(text);
//	}
//
//	/** ����, �Ǵ� Ŭ���̾�Ʈ ������ �����ϴ� ���� ��ư */
//	class RadioBtnListener implements ItemListener {
//
//		@Override
//		public void itemStateChanged(ItemEvent e) {
//			if (e.getStateChange() == ItemEvent.DESELECTED) {
//				return;
//			}
//
//			if (scRadioBtn[SERVER].isSelected()) {
//				ipTextField.setEditable(false);
//				ipTextField.setText(LinKlipboardHost.getCurrentEnvironmentNetworkIp());
//
//			} else if (scRadioBtn[CLIENT].isSelected()) {
//				ipTextField.setEditable(true);
//				ipTextField.setText("");
//
//			} else {
//				// �ƹ��ϵ� ���Ͼ
//			}
//		}
//	}
//
//	public static void main(String[] args) {
//		new UserInterface();
//	}
//
//}
//
//class LabelAndTextField {
//	private JLabel lb;
//	private JTextField tf;
//
//	public LabelAndTextField(Object initLb, Object initTf) {
//		lb = new JLabel();
//	}
//}
//
//class TwoLabels {
//	private JLabel first;
//	private JLabel second;
//	private int gap;
//
//	public TwoLabels(String first, String second, int gap) {
//		this.first = new JLabel(first);
//		this.second = new JLabel(second);
//		this.gap = gap;
//	}
//
//	public void initTwoLables(String first, String second, int gap) {
//		this.first.setText(first);
//		this.second.setText(second);
//		this.gap = gap;
//	}
//
//	public void setFirstLabel(String first) {
//		this.first.setText(first);
//	}
//
//	public void setSecondLabel(String second) {
//		this.second.setText(second);
//	}
//
//	public void setGap(int gap) {
//		this.gap = gap;
//	}
//
//}