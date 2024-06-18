package boundary;

import controller.ClientController;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MessageFrame extends JFrame{
    private JTextArea chat = new JTextArea();
    private ClientController controller;
    private MessagePanel messagePanel;
    private TopPanel topPanel;
    private ContactPanel contactPanel;

    private AddFriendPanel addFriendPanel;

    private AddFriendFrame addFriendFrame;

    public MessageFrame(ClientController controller, ImageIcon pictureFile){
        super("Chat");
        this.controller = controller;
        this.setLayout(null);

        this.setSize(680, 580);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setEditable(false);

        messagePanel = new MessagePanel(controller, this);
        topPanel = new TopPanel(controller, pictureFile, this);
        contactPanel = new ContactPanel(this);

        this.add(messagePanel);
        this.add(topPanel);
        this.add(contactPanel);

        this.setResizable(false);
        this.setVisible(true);
    }

    public void openFriendPanel(){
        addFriendFrame = new AddFriendFrame(this);
    }

    public ArrayList<String> getOnlineUsers(){
        return controller.getOnlineUsers();
    }



    public void sendImage(File message, String name){
        messagePanel.displayImage(message, name);
    }

    public void displayText(String text, String sender){
        messagePanel.displayText(messagePanel.getTextPane(), text, sender);
    }

    public String getUserName(){
        return controller.getUserName();
    }

    public void updateContacts() {
        controller.updateContacts();
    }

    public String getCurrentUser() {
        return controller.getUserName();
    }

    public void addFriendToList(String friend) {
        controller.addFriendToList(friend);
    }

    public void displayContacts(ArrayList<String> list) {
        contactPanel.displayContacts(list);
    }
}
