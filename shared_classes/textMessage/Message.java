package shared_classes.textMessage;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import shared_classes.user.User;

import javax.imageio.ImageIO;
import javax.swing.*;



public class Message implements Serializable {
    private User sender;
    private ArrayList<User> recievers;
    private ImageIcon imageIcon;
    private String text;
    private String serverReceivedTime;
    private String receiverTime;
    private static final long serialVersionUID = 1L;

    private byte[] imageByteArray;


    public Message(User sender, ArrayList<User> recievers, String text, ImageIcon imageIcon){
        this.recievers = recievers;
        this.sender = sender;
        this.text = text;
    //    convertImageToByte(imageIcon);
        this.imageIcon = imageIcon;
    }

    /*
    public Message(User sender, ArrayList<User> recievers, String text, ImageIcon imageIcon, boolean sent){
        this.recievers = recievers;
        this.sender = sender;
        this.text = text;
        this.imageIcon = imageIcon;
    }

     */


    public void convertImageToByte(ImageIcon imageIcon){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(imageIcon);
            this.imageByteArray = baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageIcon getImage() {
        try {

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(imageByteArray));
            ImageIcon imageIconn = (ImageIcon) ois.readObject();
            this.imageIcon = imageIconn;
            return imageIcon;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }




    public byte[] getImageByteArray(){
        return imageByteArray;
    }

    /*
    public void getImageMessage() {
        try {

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(imageByteArray));
            ImageIcon imageIconn = (ImageIcon) ois.readObject();
            this.imageIcon = imageIconn;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

     */

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
