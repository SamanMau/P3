package shared_classes.textMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import shared_classes.user.User;
import javax.swing.*;



public class Message implements Serializable {
    private User sender;
    private User reciever;
    private ArrayList<User> recievers;
    private LocalDateTime timesent;
    private Icon imageIcon;
    private String text;

    public Message(User sender, User reciever, String text, Icon imageIcon){
        this.reciever = reciever;
        this.sender = sender;
        this.text = text;
        this.imageIcon = imageIcon;
        timesent = LocalDateTime.now();
    }

    public User getSender(){
        return sender;
    }

    public User getReciever(){
        return reciever;
    }

    public Icon getImageIcon() {
        return imageIcon;
    }

    public String getTextMessage() {
        return text;
    }

    public LocalDateTime getTimesent() {
        return timesent;
    }
}
