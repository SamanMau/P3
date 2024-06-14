package controller;

import boundary.MessageFrame;
import boundary.StartFrame;
import entity.WrongFormat;
import shared_classes.user.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

/*
Klassen ansvarar för att acceptera klient förfrågningar.
Klient förfrågningar hanteras genom klassen ClientHandler.
 */
public class ClientController {
    private String ipAdress = "127.0.0.1";
    private int port = 1000;
    private StartFrame startView;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private JFileChooser file;
    private MessageFrame messageFrame;

    private String userName;

    private User user;

    public ClientController(){
        startView = new StartFrame(this);
        try {
            Socket socket = new Socket(ipAdress, port);
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void openChatFrame(ImageIcon pictureFile){
        messageFrame = new MessageFrame(this, pictureFile);
    }


    /*
    JFileChooser är en klass som används för att öppna och spara en fil lokalt på datorn.
    Metoden "showOpenDialog" öppnar filen .vi har ingen parent component, därför sätter vi null.
    showOpenDialog returnerar en int. Om man väljer en fil, returnerar den 0. Om man inte
    väljer något, returneras 1.
     */
    public void openFileManager(){
        file = new JFileChooser();
        int action = file.showSaveDialog(null);


      //  Hämtar den valda filen och dess absoluta sökväg, alltså den sökvägen som finns
      //  lokalt på ens dator.

        if(action == 0){
            File chosenFile = new File(file.getSelectedFile().getAbsolutePath());
            String name = chosenFile.getName();

            try{
                if(!(name.endsWith("jpg") || (name.endsWith("jpeg")))){
                    throw new WrongFormat("You need to either choose a JPEG or PNG picture.");
                }

                else if((name.endsWith("jpg") || (name.endsWith("jpeg")))){
                    messageFrame.sendImage(chosenFile, userName);
                }
            } catch (WrongFormat e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        }
    }

    public ImageIcon chooseProfilePic(){
        file = new JFileChooser();
        int action = file.showSaveDialog(null);


        //  Hämtar den valda filen och dess absoluta sökväg, alltså den sökvägen som finns
        //  lokalt på ens dator.

        if(action == 0){
            File chosenFile = new File(file.getSelectedFile().getAbsolutePath());
            String name = chosenFile.getAbsolutePath();
        //    String name = chosenFile.getName();


            try{
                if(!(name.endsWith("jpg") || (name.endsWith("jpeg")))){
                    throw new WrongFormat("You need to either choose a JPEG or PNG picture.");
                }

                else if((name.endsWith("jpg") || (name.endsWith("jpeg")))){
                    ImageIcon imageIcon = new ImageIcon(name);
                    return imageIcon;
                }
            } catch (WrongFormat e){
                JOptionPane.showMessageDialog(null, e.getMessage());
                chooseProfilePic();
            }

        }

        return null;
    }

    public void sendName(String name, String pictureFile){
        try {

            this.userName = name;

            oos.writeObject(name);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName(){
        return this.userName;
    }

    public void createAccount(String name, ImageIcon imageIcon) {
        User user = new User(name, imageIcon);
        this.userName = name;
        try {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logIn(String userName){
        User user = new User(userName, null);
        this.userName = userName;

        try {
            oos.writeObject(user);
            ImageIcon userImage = (ImageIcon) ois.readObject();
            openChatFrame(userImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
