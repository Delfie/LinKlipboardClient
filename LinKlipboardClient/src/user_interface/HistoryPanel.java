package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HistoryPanel extends JPanel {
	private JList<String> historyList = new JList<>();
	private JScrollPane historyScrollPane = new JScrollPane();

	private JButton receiveButton = new JButton();

	public HistoryPanel() {
		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		// 도연이껄로 수정
		historyList.setModel(new AbstractListModel<String>() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		historyScrollPane.setViewportView(historyList);
		historyScrollPane.setBounds(15, 15, 285, 270);
		add(historyScrollPane);

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
