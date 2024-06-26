package boundary;

import controller.ClientController;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.io.File;
import java.util.ArrayList;

public class MessageFrame extends JFrame{
    private JTextArea chat = new JTextArea();
    private ClientController controller;
    private MessagePanel messagePanel;
    private TopPanel topPanel;
    private ContactPanel contactPanel;

    private AddFriendFrame addFriendFrame;

    private ArrayList<String> friends;

    public MessageFrame(ClientController controller, ImageIcon pictureFile){
        super("Chat");
        this.controller = controller;
        this.friends = new ArrayList<>();
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

    public void openFileManager(){
        controller.openFileManager();
    }



    public void sendImage(File message, String name){
        messagePanel.displayImage(message, name);
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

    public void collectFriends(String name){
        if(!friends.contains(name)){
            friends.add(name);
        }

    }

    public ArrayList<String> getFriends(){
        return friends;
    }

    public void manageMessage(String message, ArrayList<String> contacts){
        controller.manageMessage(message, contacts);
    }

    public String getReceiverTime(){
        return controller.getReceiverTime();
    }

    public void removeChosenFriend(){
        friends = new ArrayList<>();
    }

    public void logOut(){
        controller.logOut();
        this.dispose();
    }


    public void managePicture(ImageIcon newSize, ArrayList<String> contacts) {
        controller.manageImage(newSize, contacts);
    }

    public void displayImage(ImageIcon imageIcon, String userName, String receiverTime) {
        messagePanel.displayFormattedImage(imageIcon, userName, receiverTime);

    }

    public void displayText(String text, String sender, String receiverTime){
        messagePanel.displayText(text, sender, receiverTime);
    }

    public void managePictureWithText(ImageIcon image, ArrayList<String> contacts, String message) {
        controller.managePictureWithText(image, contacts, message);
    }

    public void displayPictureWithText(ImageIcon imageIcon, String userName, String receiverTime, String message){
        messagePanel.displayPictureWithText(imageIcon, userName, receiverTime, message);
    }
}
