package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactPanel extends JPanel {
    private MessageFrame messageFrame;
    private JLabel contactText;

    private JButton addnewFriend;

    public ContactPanel(MessageFrame messageFrame){
        setLayout(null);
        this.setBounds(505, 0, 160, 500);
        this.messageFrame = messageFrame;
        this.setBackground(Color.GRAY);

        setUpContacts();
        setActionListeners();
    }

    public void setUpContacts(){
        addnewFriend = new JButton("Add a new friend");
        addnewFriend.setBounds(5, 10, 130, 25);
        addnewFriend.setBackground(Color.WHITE);

        contactText = new JLabel("Contacts:");
        contactText.setBounds(5, 50, 60, 20);
        contactText.setForeground(Color.WHITE);

        this.add(contactText);
        this.add(addnewFriend);
    }

    public void setActionListeners(){
        addnewFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageFrame.openFriendPanel();
            }
        });
    }

}
