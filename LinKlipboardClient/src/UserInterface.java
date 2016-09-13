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
//		// 라디오 버튼 생성
//		ButtonGroup g = new ButtonGroup();
//		scRadioBtn[SERVER] = new JRadioButton("Server");
//		scRadioBtn[CLIENT] = new JRadioButton("Client");
//		
//		// 라디오 버튼 크기, 위치 설정
//		scRadioBtn[SERVER].setBounds(X1, Y1, 70, textHight);
//		scRadioBtn[CLIENT].setBounds(X1 + 80, Y1, 70, textHight);
//
//		// 라디오버튼 리스너 생성
//		RadioBtnListener rl = new RadioBtnListener();
//		for (int i = 0; i < 2; i++) {
//			g.add(scRadioBtn[i]);
//			scRadioBtn[i].addItemListener(rl);
//			add(scRadioBtn[i]);
//		}
//
//		// start 버튼 생성, 부착
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
//		ipTextField.setEditable(false); // 편집 불가능
//
//		add(ipAddrLabel);
//		add(portNumLabel);
//
//		add(ipTextField);
//		add(portTextField);
//
//		/** start 버튼에 대한 처리 */
//		startConnection.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				int port = Integer.parseInt(portTextField.getText());
//
//				if (ipTextField.getText().isEmpty() || portTextField.getText().isEmpty()) {
//					connetionState.setText("필요정보 입력");
//					return;
//				} else {
//					// Server 선택시
//					if (scRadioBtn[SERVER].isSelected()) {
//						createServer(port);
//						// 만약 다시 클아이언트 라디오박스를 선택한다면
//						// Server의 생성자 안에서 만들어진 BindWait 스레드를 죽여줘야 함
//					} else if (scRadioBtn[CLIENT].isSelected()) {
//						String ip = ipTextField.getText();
//						createClient(ip, port);
//					} else {
//						connetionState.setText("server 또는 client 선택");
//					}
//				}
//			}
//		}); // 버튼 끝
//
//		// 연결 상태
//		stateInfo.setBounds(X1, 190, 100, textHight);
//		add(stateInfo);
//		connetionState.setBounds(X1 + 120, 185, 120, textHight + 10);
//		connetionState.setBackground(Color.LIGHT_GRAY);
//		connetionState.setOpaque(true);
//		add(connetionState, JLabel.CENTER);
//
//		// 클립보드 내용
//		ClipboardFormat.setBounds(X1, 240, 100, textHight);
//		add(ClipboardFormat);
//		ClipboardState.setBounds(X1 + 120, 235, 120, textHight + 10);
//		ClipboardState.setBackground(Color.LIGHT_GRAY);
//		ClipboardState.setOpaque(true);
//		add(ClipboardState, JLabel.CENTER);
//
//		// 클립보드 내용 확인 버튼
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
//					ClipboardState.setText("문자열");
//				} else if (clipboadNow.equals(DataFlavor.imageFlavor)) {
//					ClipboardState.setText("이미지");
//				} else if (clipboadNow.equals(DataFlavor.javaFileListFlavor)) {
//					ClipboardState.setText("파일");
//				} else {
//					ClipboardState.setText("not supported");
//				}
//			}
//		});
//
//		//전송 버튼
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
//		// 파일 전송 버튼
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
//		// 파일 경로 입력 텍스트필드
//		filePath.setBounds(X1 - 20, 300, 300, textHight);
//		add(filePath);
//	}
//
//	/** 서버소켓을 생성 */
//	private void createServer(int port) {
//		host = new LinKlipboardHost(port, this);
//	}
//
//	/** 클라이언트 소켓 생성 */
//	private void createClient(String ip, int port) {
//		host = new LinKlipboardHost(ip, port, this);
//	}
//
//	public void updateConnectState(String text) {
//		connetionState.setText(text);
//	}
//
//	/** 서버, 또는 클라이언트 역할을 선택하는 라디오 버튼 */
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
//				// 아무일도 안일어남
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