package shared_classes.textMessage;

import java.io.*;
import java.util.ArrayList;
import shared_classes.user.User;
import javax.swing.*;

public class Message implements Serializable {
    private User sender;
    private ArrayList<User> recievers;
    private ImageIcon imageIcon;
    private String text;
    private String serverReceivedTime;
    private String userReceiverTime;
    private static final long serialVersionUID = 1L;

    public Message(User sender, ArrayList<User> recievers, String text, ImageIcon imageIcon){
        this.recievers = recievers;
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

    public ArrayList<User> getRecievers(){
        return recievers;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public String getTextMessage() {
        return text;
    }

}
