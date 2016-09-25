package user_interface;

import java.awt.Container;

import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class UserInterfaceManager extends JFrame {

	private UserInterfacePage1 page1 = new UserInterfacePage1();
	private UserInterfacePage2 page2 = new UserInterfacePage2();
	
	Container contentPane;
	
	private static final int startXOfFrame = 0;
	private static final int startYOfFrame = 0;
	private static final int endXOfFrame = 326;
	private static final int endYOfFrame = 430;
	private static final int endXOfPanel = 320;
	private static final int endYOfPanel = 400;
	
	private static final int buttonWidth = 80;
	private static final int buttonHeight = 23;
	
	public UserInterfaceManager() {
		setTitle("LinKlipboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(endXOfFrame, endYOfFrame);
		
		contentPane = getContentPane();
		//contentPane.setLayout(null);
		//contentPane.setLayout(new GroupLayout(this));

		//contentPane.add(page1);
		contentPane.add(page2);

		setVisible(true);
		setResizable(false);
	}
	
	public static void main(String[] args) {
		new UserInterfaceManager();
	}
}
