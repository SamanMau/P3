package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddFriendPanel extends JPanel {
    private AddFriendFrame addFriendFrame;
    private static ArrayList<String> onlineUsers;

    private JButton save;

    public AddFriendPanel(AddFriendFrame addFriendFrame){
        this.setLayout(null);
        this.addFriendFrame = addFriendFrame;
        Color color = new Color(163, 200, 203);
        this.setBackground(color);

        this.setBounds(0, 0, 400, 400);

        setUp();
    }

    public void setUp(){
        onlineUsers = new ArrayList<>();
        addFriendFrame.onlineUsersRequest();

        onlineUsers = addFriendFrame.getOnlineUsers();

        for(int i = 0; i < onlineUsers.size(); i++){
            JButton button = new JButton(onlineUsers.get(i));

            button.setBounds(90, i * 40, 150, 20);
            button.setBackground(Color.WHITE);


            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String text = button.getText();
                    addFriendFrame.addFriendToList(text);
                    addFriendFrame.sendMessageToFriend(text);
                }
            });

            this.add(button);
        }

        this.repaint();

        this.save = new JButton("Save changes");
        save.setBackground(Color.WHITE);
        save.setBounds(260, 20, 120, 20);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFriendFrame.updateContacts();
                addFriendFrame.clearFriends();
            }
        });

        this.add(save);
    }

}
