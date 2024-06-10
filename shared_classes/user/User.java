package shared_classes.user;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String userName;
    private ImageIcon userImage;
    private ArrayList<User> contacts;

    public User(String userName, ImageIcon userImage){
        this.userImage = userImage;
        this.userName = userName;
    }

    public User GetCurrentSender(User user){
        return null;
    }

    public void GetCurrentReciever(User user){
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + this.userName + '\'' +
                ", imageIcon=" + this.userImage +
                '}';
    }
}
