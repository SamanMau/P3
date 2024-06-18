package controller;

import boundary.MessageFrame;
import boundary.StartFrame;
import entity.MainClient;
import entity.WrongFormat;
import shared_classes.textMessage.Message;
import shared_classes.user.User;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

   // private ArrayList<String> onlineUsers;

    private MonitorMessage monitorMessage;

    private HashMap<String, ArrayList<String>> contactList;

    public ClientController(){
        startView = new StartFrame(this);
        contactList = new HashMap<>();
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

    public void updateContacts() {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("client/src/contacts.txt", true));
            for(String person : contactList.keySet()){
                writer.write(person + ": ");


                ArrayList<String> friendContacts = contactList.get(person);


                for(String friend : friendContacts){
                    writer.write(friend);
                    writer.write(", ");
                }
                writer.newLine();

            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        readContacts();

    }

    /*
    Den första if satsen kontrollerar om en person redan
    har en lista eller inte. Detta hjälper till att förhindra att
    alldeles för många element skapas.
    */
    public void addFriendToList(String friend){
        if(!contactList.containsKey(userName)){
            contactList.put(userName, new ArrayList<>());
            contactList.get(userName).add(friend);
        } else {
            contactList.get(userName).add(friend);
        }
    }

    public void readContacts(){
        try {
            FileReader fr = new FileReader("client/src/Contacts.txt");
            BufferedReader reader = new BufferedReader(fr);

            String row;

            while (!((row = reader.readLine()) == null)){
                String[] splitRow = row.split(": ");
                String personName = splitRow[0];

                String[] friendList = splitRow[1].split(", ");

                ArrayList<String> updatedList = new ArrayList<>();

                for(int i = 0; i < friendList.length; i++){
                    updatedList.add(friendList[i]);
                }

                contactList.put(personName, updatedList);

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> list = contactList.get(userName);
        
        messageFrame.displayContacts(list);


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

    public ArrayList<String> getOnlineUsers(){

        return monitorMessage.getOnlineUsers();
    }

    public String getName(){
        return this.userName;
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

    public void newConnection(){
        MainClient.main(new String[0]);
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
        this.user = new User(name, imageIcon);
        this.userName = name;
        try {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        monitorMessage = new MonitorMessage();
        monitorMessage.start();
    }

    public void logIn(String userName){
        this.user = new User(userName, null);
        this.userName = userName;

        try {
            oos.writeObject(user);

            ImageIcon userImage = (ImageIcon) ois.readObject();
            openChatFrame(userImage);

            monitorMessage = new MonitorMessage();
            monitorMessage.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void manageMessage(String text){
        User reciever = new User("LELA", null);
        Message message = new Message(user, reciever, text, null);

        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Message recieveMessageFromClient(){
        try {
            Message message = (Message) ois.readObject();

            if(message != null){
                return message;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private class MonitorMessage extends Thread{
        private static ArrayList<String> onlineUsers;

        public MonitorMessage(){
            System.out.println("ggg");
        }

        @Override
        public void run(){
                try {
                    while (true) {
                        Object obj = ois.readObject();

                        if(obj instanceof Message){
                            Message message = (Message) obj;

                            if (message != null) {
                                String name = user.getUserName();
                                User reciever = message.getReciever();

                                if(name.equals(reciever.getUserName())){
                                    messageFrame.displayText(message.getTextMessage(), message.getSender().getUserName());
                                }
                            }
                        } else if(obj instanceof ArrayList<?>){
                            onlineUsers = (ArrayList<String>) obj;
                        }

                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

        }

        public ArrayList<String> getOnlineUsers(){
            return onlineUsers;
        }
    }


}
