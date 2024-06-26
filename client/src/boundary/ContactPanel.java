package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        this.add(contactText);
        this.add(addnewFriend);
    }

    public void setUpContacts(){
        addnewFriend = new JButton("Add a new friend");
        addnewFriend.setBounds(5, 10, 130, 25);
        addnewFriend.setBackground(Color.WHITE);

        contactText = new JLabel("Contacts:");
        contactText.setBounds(5, 50, 60, 20);
        contactText.setForeground(Color.WHITE);

    }

    public void setActionListeners(){
        addnewFriend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageFrame.openFriendPanel();
            }
        });
    }

    public void displayContacts(ArrayList<String> list) {
        if(list != null){
            int amount = list.size();

            for(int i = 0; i < amount; i++){
                JButton button = new JButton(list.get(i));
                if(i == 0){
                    button.setBounds(20, 70, 120, 20);
                } else {
                    button.setBounds(20, (i + 1) * 50, 120, 20);
                }

                button.setBackground(Color.WHITE);

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String user = button.getText();
                        messageFrame.collectFriends(user);
                    }
                });

                this.add(button);
                repaint();
            }
        }


    }

}
