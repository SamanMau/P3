package shared_classes.Messages;

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
    private String receiverTime;
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

    public String getLogOutMessage(){
        return text;
    }

    public void setServerReceivedTime(String time){
        this.serverReceivedTime = time;
    }

    public void setReceiverTime(String time){
        this.receiverTime = time;
    }

    public String getReceiverTime(){
        return receiverTime;
    }

    public String getServerReceivedTime(){
        return serverReceivedTime;
    }

    public User getSender(){
        return sender;
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
