package shared_classes.textMessage;

import java.io.*;
import java.util.ArrayList;
import shared_classes.user.User;
import javax.swing.*;

public class Message implements Serializable {
    private User sender;
    private ArrayList<User> receivers;
    private ImageIcon imageIcon;
    private String text;
    private String serverReceivedTime;
    private String userReceiverTime;
    private static final long serialVersionUID = 1L;

    public Message(User sender, ArrayList<User> receivers, String text, ImageIcon imageIcon){
        this.receivers = receivers;
        this.sender = sender;
        this.text = text;
        this.imageIcon = imageIcon;
    }

    public Message(User user, String message){
        this.sender = user;
        this.text = message;
    }

    public void setServerReceivedTime(String time){
        this.serverReceivedTime = time;
    }

    public void setUserReceiverTime(String time){
        this.userReceiverTime = time;
    }

    public String getUserReceiverTime(){
        return userReceiverTime;
    }

    public User getSender(){
        return this.sender;
    }

    public ArrayList<User> getReceivers(){
        return receivers;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public String getTextMessage() {
        return text;
    }

}
