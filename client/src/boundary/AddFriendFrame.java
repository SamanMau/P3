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

    public void onlineUsersRequest(){
        messageFrame.onlineUsersRequest();
    }

    public void updateContacts() {
        messageFrame.updateContacts();
    }

    public void addNewFriendToContacts(String friend) {
        messageFrame.addNewFriendToContacts(friend);
    }

    public void sendMessageToOnlineUser(String text) {
        messageFrame.sendMessageToFriend(text);
    }

    public void clearFriends() {
        messageFrame.clearFriends();
    }

    public ArrayList<String> getOnlineUsers(){
        return messageFrame.getOnlineUsers();
    }
}
