package user_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import contents.Contents;
import contents.FileContents;
import contents.ImageContents;
import contents.StringContents;
import datamanage.History;
import server_manager.LinKlipboard;

public class HistoryPanel extends JPanel {
	private ListPanel listPanel;
	private JButton receiveButton;
	
	public HistoryPanel() {
		setLayout(null);
		setSize(320, 360);
		
		initComponents();
	}
	
	private void initComponents() {
		listPanel = new ListPanel();
		listPanel.setLocation(25, 15);
		add(listPanel);
		
		receiveButton = new JButton("Receive");
		receiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = listPanel.getSelectedListIndex();
				System.out.println(index);
			}
		});
		receiveButton.setBounds(215, 290, 80, 23);
		add(receiveButton);
		
	}
}

class ListPanel extends Panel {
	private History history;
	private JList<Contents> listContents;
	private JScrollPane scrollPane;
	
	public ListPanel() {
		setLayout(new BorderLayout());
		setSize(270, 260);
		
		addContentsInHistory(); // 빼야됨
		
		initComponents();
	}
	
	private void initComponents() {
		listContents = createListContents();
		scrollPane = new JScrollPane(listContents);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void addContentsInHistory() {
    	history = new History();
    	
    	history.addSharedContentsInHistory(new StringContents("Dooy", "가나다라마바사아자차카타파"));
    	
    	
    	ImageIcon image1 = new ImageIcon("image/1.png");
    	history.addSharedContentsInHistory(new ImageContents("Hee", image1));
    	
    	File file1 = new File("C:\\Users\\Administrator\\Desktop\\d.hwp");
    	history.addSharedContentsInHistory(new FileContents("Delf", file1));
    	
    	File file2 = new File("C:\\Users\\Administrator\\Desktop\\q.txt");
    	history.addSharedContentsInHistory(new FileContents("Dooy", file2));
    	
    	ImageIcon image2 = new ImageIcon("image/3.png");
    	history.addSharedContentsInHistory(new ImageContents("Hee", image2));
    	
    	history.addSharedContentsInHistory(new StringContents("Delf", "데르프"));
    	
    	history.addSharedContentsInHistory(new StringContents("Dooy", "두이"));
    	
    	history.addSharedContentsInHistory(new StringContents("Hee", "히히"));
    }
	
	private JList<Contents> createListContents() {
        // create List model
        DefaultListModel<Contents> model = new DefaultListModel<>();
        
        System.out.println(history.getSharedContents().size());
        
        // add item to model
        for(int i=0; i<history.getSharedContents().size(); i++) {
        	 model.add(0, history.getSharedContents().elementAt(i));
        }
        
        // create JList with model
        JList<Contents> list = new JList<Contents>(model);
        
        // set cell renderer 
        list.setCellRenderer(new ContentsRenderer());
        
        return list;
	}
	
	public int getSelectedListIndex() {
		return history.getSizeOfContentsInHistory() - 1 -listContents.getSelectedIndex();
	}
	
	class ContentsRenderer extends JPanel implements ListCellRenderer<Contents> {
		
	    private JLabel lbSharer = new JLabel();
	    private JLabel lbType = new JLabel();
	    private JLabel lbContents = new JLabel();
	 
	    public ContentsRenderer() {
	        setLayout(new BorderLayout(10, 10));
	 
	        JPanel panelText = new JPanel(new GridLayout(0, 1));
	        
	        panelText.add(lbSharer);
	        panelText.add(lbType);
	        
	        add(lbContents, BorderLayout.EAST);
	        add(panelText, BorderLayout.CENTER);
	    }
	 
	    @Override
	    public Component getListCellRendererComponent(JList<? extends Contents> list, Contents contents, int index, boolean isSelected, boolean cellHasFocus) {
	 
	    	Font font1 = new Font("helvitica", Font.BOLD, 15);
	    	Font font2 = new Font("helvitica", Font.BOLD, 12);
	    	
	        lbSharer.setText(contents.getSharer());
	        lbSharer.setFont(font1);
	        
	        switch(contents.getType()) {
	        case LinKlipboard.STRING_TYPE:
	        	StringContents stringContenst = (StringContents)contents;
	        	lbType.setText("<String>");
	        	lbType.setFont(font2);
	        	if(stringContenst.getString().length() > 12) {
	        		lbContents.setText(stringContenst.getString().substring(0, 11) + "...");
	        	}
	        	else {
	        		lbContents.setText(stringContenst.getString());
	        	}
	        	lbContents.setIcon(null);
	        	lbContents.setFont(font1);
	        	break;
	        case LinKlipboard.IMAGE_TYPE:
	        	ImageContents imageContenst = (ImageContents)contents;
	        	lbType.setText("<Image>");
	        	lbType.setFont(font2);
	        	lbContents.setText(null);
	        	lbContents.setIcon(imageContenst.getResizingImageIcon());
	        	break;
	        case LinKlipboard.FILE_TYPE:
	        	FileContents fileContenst = (FileContents)contents;
	        	lbType.setText("<File>");
	        	lbType.setFont(font2);
	        	if(fileContenst.getFileName().length() > 12) {
	        		lbContents.setText(fileContenst.getFileName().substring(0, 11) + "...");
	        	}
	        	else {
	        		lbContents.setText(fileContenst.getFileName());
	        	}
	        	lbContents.setText(fileContenst.getFileName());
	        	lbContents.setIcon(null);
	        	lbContents.setFont(font1);
	        	break;
			}
	        

			lbSharer.setForeground(new Color(254, 97, 0));

			// set Opaque to change background color of JLabel
			lbSharer.setOpaque(true);
			lbType.setOpaque(true);
			lbContents.setOpaque(true);

			// when select item
			if (isSelected) {
				lbSharer.setBackground(list.getSelectionBackground());
				lbType.setBackground(list.getSelectionBackground());
				lbContents.setBackground(list.getSelectionBackground());
				setBackground(list.getSelectionBackground());
			} else { // when don't select
				lbSharer.setBackground(list.getBackground());
				lbType.setBackground(list.getBackground());
				lbContents.setBackground(list.getBackground());
				setBackground(list.getBackground());
			}

	        return this;
	    }
	}
}
