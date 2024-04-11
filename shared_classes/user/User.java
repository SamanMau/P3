package shared_classes.user;

import javax.swing.*;
import java.util.ArrayList;

public class User {
    private String userName;
    private Icon userImage;
    private ArrayList<User> contacts;

    public User(String userName, Icon userImage){
        this.userImage = userImage;
        this.userName = userName;
    }

    public User(String userName){
        this.userName = userName;
    }

    public void addFriend(User user){
        contacts.add(user);
    }

    public void setUserImage(Icon image){
        this.userImage = image;
    }

    public Icon getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }
}
