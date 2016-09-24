package user_interface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class SettingPanel extends JPanel {
	
    // Variables declaration - do not modify                     
	private JButton exitButton;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JPanel jPanel4;
    private JCheckBox setNotification;
    private JCheckBox setReceiveContents;

    private JPanel shortcutSetPanel;
    // End of variables declaration

    /**
     * Creates new form SettingPanel
     */
    public SettingPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        shortcutSetPanel = new JPanel();
        jComboBox2 = new JComboBox<>();
        jLabel5 = new JLabel();
        jComboBox1 = new JComboBox<>();
        setNotification = new JCheckBox();
        setReceiveContents = new JCheckBox();
        jPanel4 = new JPanel();
        jLabel6 = new JLabel();
        exitButton = new JButton();

        setLayout(new BorderLayout());

        this.setPreferredSize(new Dimension(300, 320));

        shortcutSetPanel.setBorder(BorderFactory.createTitledBorder(" Shortcut Setting"));

        jComboBox2.setModel(new DefaultComboBoxModel<>(new String[] { "A", "B", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "W", "X", "Y", "Z" }));
        jComboBox2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel5.setText("+");

        jComboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "Ctrl", "Alt" }));
        jComboBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        GroupLayout shortcutSetPanelLayout = new GroupLayout(shortcutSetPanel);
        shortcutSetPanel.setLayout(shortcutSetPanelLayout);
        shortcutSetPanelLayout.setHorizontalGroup(
            shortcutSetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shortcutSetPanelLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        shortcutSetPanelLayout.setVerticalGroup(
            shortcutSetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(shortcutSetPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(shortcutSetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setNotification.setText("Turn off the notification (Always recieved)");
        setNotification.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setNotificationActionPerformed(evt);
            }
        });

        setReceiveContents.setText("Deny the recieve clipboard data");
        setReceiveContents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setReceiveContentsActionPerformed(evt);
            }
        });

        jPanel4.setBorder(BorderFactory.createTitledBorder(" Shortcut Setting"));

        jLabel6.setText("+");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jLabel6)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        exitButton.setText("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        GroupLayout thisLayout = new GroupLayout(this);
        this.setLayout(thisLayout);
        thisLayout.setHorizontalGroup(
            thisLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(thisLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(setReceiveContents)
                    .addComponent(exitButton, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                    .addComponent(setNotification, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(shortcutSetPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 21, Short.MAX_VALUE))
        );
        thisLayout.setVerticalGroup(
            thisLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(thisLayout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(shortcutSetPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(setNotification)
                .addGap(10, 10, 10)
                .addComponent(setReceiveContents)
                .addGap(26, 26, 26)
                .addComponent(exitButton)
                .addGap(18, 18, 18))
        );

        add(this, BorderLayout.CENTER);
    }// </editor-fold>                        

    private void jComboBox2ActionPerformed(ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void jComboBox1ActionPerformed(ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void setNotificationActionPerformed(ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void setReceiveContentsActionPerformed(ActionEvent evt) {                                                   
        // TODO add your handling code here:
    }                                                  

    private void exitButtonActionPerformed(ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          


                   
}