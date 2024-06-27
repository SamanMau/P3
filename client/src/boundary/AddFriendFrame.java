package boundary;

import javax.swing.*;
import java.util.ArrayList;

public class AddFriendFrame extends JFrame {
    private MessageFrame messageFrame;

    public AddFriendFrame(MessageFrame messageFrame){
        setLayout(null);
        this.setSize(400, 400);
        this.messageFrame = messageFrame;
        AddFriendPanel addFriendPanel = new AddFriendPanel(this);
        this.add(addFriendPanel);
        this.setVisible(true);
    }

    public ArrayList<String> getOnlineUsers(){
        return messageFrame.getOnlineUsers();
    }

    public void updateContacts() {
        messageFrame.updateContacts();
    }

    public String getCurrentUser(){
        return messageFrame.getCurrentUser();
    }

    public void addFriendToList(String friend) {
        messageFrame.addFriendToList(friend);
    }

    public void collectFriends(String text) {
        messageFrame.collectFriends(text);
    }
}
