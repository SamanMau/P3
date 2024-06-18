package boundary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddFriendPanel extends JPanel {
    private AddFriendFrame addFriendFrame;
    private ArrayList<String> onlineUsers;

    private JButton save;

    public AddFriendPanel(AddFriendFrame addFriendFrame){
        this.setLayout(null);
        this.addFriendFrame = addFriendFrame;

        this.setBounds(0, 0, 400, 400);

        setUp();
    }

    public void setUp(){
        onlineUsers = addFriendFrame.getOnlineUsers();

        for(int i = 0; i < onlineUsers.size(); i++){
            JButton button = new JButton(onlineUsers.get(i));
            button.setBounds(90, i * 40, 150, 20);


            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addFriendFrame.addFriendToList(button.getText());
                    System.out.println("En vän tillagd");
                }
            });

            this.add(button);
        }

        this.save = new JButton("Save changes");
        save.setBounds(260, 20, 120, 20);

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFriendFrame.updateContacts();
                System.out.println("Saved changes");
            }
        });

        this.add(save);
    }

}
