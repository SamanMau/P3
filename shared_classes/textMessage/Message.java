package shared_classes.textMessage;

import java.util.ArrayList;
import java.time.LocalDateTime;
import shared_classes.user.User;
import javax.swing.*;


public class Message {
    private User sender;
    private ArrayList<User> recievers;
    private LocalDateTime timesent;
    private Icon imageIcon;
    private String text;

    Message(User sender, ArrayList<User> recievers, String text, Icon imageIcon){
        this.recievers = recievers;
        this.sender = sender;
        this.text = text;
        this.imageIcon = imageIcon;
        timesent = LocalDateTime.now();
    }

    public void SendMessage(Message message){

    }

    public Icon getImageIcon() {
        return imageIcon;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimesent() {
        return timesent;
    }
}
