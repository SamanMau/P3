package controller;

import boundary.ChatFrame;
import boundary.StartFrame;
import entity.WrongFormat;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientController {
    private String ipAdress = "127.0.0.1";
    private int port = 1000;
    private StartFrame startView;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private JFileChooser file;
    private ChatFrame chatFrame;

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

    public void ChatFrame(){
        chatFrame = new ChatFrame(this);
    }

    /*
    JFileChooser är en klass som används för att öppna och spara en fil lokalt på datorn.
    Metoden "showOpenDialog" öppnar filen .vi har ingen parent component, därför sätter vi null.
    showOpenDialog returnerar en int. Om man väljer en fil, returnerar den 0. Om man inte
    väljer något, returneras 1.

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
                    chatFrame.sendImage(chosenFile);
                }
            } catch (WrongFormat e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        }
    }

     */


    public void sendName(String name){
        try {
            oos.writeObject(name);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
