package shared_classes.user;

import javax.swing.*;
import java.util.ArrayList;

public class User {
    private String userName;
    private ImageIcon userImage;
    private ArrayList<User> contacts;

    public User(String userName, ImageIcon userImage){
        this.userImage = userImage;
        this.userName = userName;
    }

    public User(String userName){
        this.userName = userName;
    }

    public void addFriend(User user){
        contacts.add(user);
    }

    public void setUserImage(ImageIcon image){
        this.userImage = image;
    }

    public ImageIcon getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }
}
