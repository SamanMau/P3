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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/*
Klassen ansvarar för att acceptera klient förfrågningar.
Klient förfrågningar hanteras genom klassen ClientHandler.
 */
public class ClientController {
    private String ipAdress = "127.0.0.1";

    private int port = 1000;

    private StartFrame startFrame;

    private ObjectInputStream ois;

    private ObjectOutputStream oos;

    private JFileChooser file;

    private MessageFrame messageFrame;

    private String userName;

    private User user;

    private MonitorMessage monitorMessage;

    private HashMap<String, ArrayList<String>> contactsMap;

    private Socket socket;

    public ClientController(){
        contactsMap = new HashMap<>();
        try {
            socket = new Socket(ipAdress, port);
            InputStream inputStream = socket.getInputStream();
            this.ois = new ObjectInputStream(inputStream);
            OutputStream outputStream = socket.getOutputStream();
            this.oos = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        startFrame = new StartFrame(this);


    }

    public void openChatFrame(ImageIcon picture){
        messageFrame = new MessageFrame(this, picture);
        readContacts();
    }

    public void openChatFrameForRegister(ImageIcon pictureFile){
        messageFrame = new MessageFrame(this, pictureFile);
    }

    public void saveContacts(){
        HashMap<String, ArrayList<String>> currentContacts = getEveryContact();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("client/src/contacts.txt", false)); //true

            for(String person : currentContacts.keySet()){

                writer.write(person + ": ");

                ArrayList<String> friendContacts = currentContacts.get(person);

                for(int i = 0; i < friendContacts.size(); i++){
                    if(i + 1 == friendContacts.size()){
                        writer.write(friendContacts.get(i));
                    } else {
                        writer.write(friendContacts.get(i));
                        writer.write(", ");

                    }
                }

                writer.newLine();

            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateContacts(){
        HashMap<String, ArrayList<String>> currentContacts = getEveryContact();

        ArrayList<String> newFriends = contactsMap.get(userName);

        if(currentContacts.containsKey(userName)){
            ArrayList<String> existingFriends = currentContacts.get(userName);

            for(int i = 0; i < newFriends.size(); i++){
                existingFriends.add(newFriends.get(i));
            }

          //  currentContacts.put(userName, existingFriends);

        } else {
            currentContacts.put(userName, newFriends);
        }

        for(String pp : currentContacts.keySet()){

            ArrayList<String> friendContacts = currentContacts.get(pp);

            for(int i = 0; i < friendContacts.size(); i++){
                String friend = friendContacts.get(i);

                for(int x = i + 1; x < friendContacts.size(); x++){
                    if(friend.equals(friendContacts.get(x))){
                        friendContacts.remove(x);

                        x--; //minskar x eftersom arrayen har minskats.
                    }
                }

            }

        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("client/src/contacts.txt", false)); //true

            for(String person : currentContacts.keySet()){

                writer.write(person + ": ");

                ArrayList<String> friendContacts = currentContacts.get(person);

                for(int i = 0; i < friendContacts.size(); i++){
                    if(i + 1 == friendContacts.size()){
                        writer.write(friendContacts.get(i));
                    } else {
                        writer.write(friendContacts.get(i));
                        writer.write(", ");

                    }
                }

                writer.newLine();

            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        readContacts();

        clearContactList();

    }

    /*
    Den första if satsen kontrollerar om en person redan
    har en lista eller inte. Detta hjälper till att förhindra att
    alldeles för många element skapas.
    */
    public synchronized void addFriendToList(String friend){
        if(!contactsMap.containsKey(userName)){
            contactsMap.put(userName, new ArrayList<>());
        }

        contactsMap.get(userName).add(friend);
    }

    public void clearContactList(){
        contactsMap = new HashMap<>();
    }

    public HashMap<String, ArrayList<String>> getEveryContact(){
        HashMap<String, ArrayList<String>> allFriends = new HashMap<>();

        try {
            FileReader fr = new FileReader("client/src/contacts.txt");
            BufferedReader reader = new BufferedReader(fr);

            String row;

            while (!((row = reader.readLine()) == null)){

                try {
                    String[] splitRow = row.split(":");

                    String friendOwner = splitRow[0].trim();

                    String[] friends = splitRow[1].split(",");

                    ArrayList<String> updatedList = new ArrayList<>();

                    for (String friend : friends) {
                        updatedList.add(friend.trim());
                    }

                    allFriends.put(friendOwner, updatedList);

                } catch (ArrayIndexOutOfBoundsException e){
                }

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return allFriends;
    }

    public ArrayList<String> getOnlineUsers(){
        return monitorMessage.getOnlineUsers();
    }

    public synchronized void readContacts(){

        try {
            FileReader fr = new FileReader("client/src/contacts.txt");
            BufferedReader reader = new BufferedReader(fr);

            String row;

            while (!((row = reader.readLine()) == null)){

                try {
                    String[] splitRow = row.split(":");

                    String friendOwner = splitRow[0].trim();

                    String[] friends = splitRow[1].split(",");

                    ArrayList<String> updatedContactList = new ArrayList<>();

                    for (String friend : friends) {
                        updatedContactList.add(friend.trim());
                    }

                    contactsMap.put(friendOwner, updatedContactList);

                } catch (ArrayIndexOutOfBoundsException e){
                }

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> list = contactsMap.get(userName);

        messageFrame.displayContacts(list);

    }

    /*
    JFileChooser är en klass som används för att öppna och spara en fil lokalt på datorn.
    Metoden "showOpenDialog" öppnar filen .vi har ingen parent component, därför sätter vi null.
    showOpenDialog returnerar en int. Om man väljer en fil, returnerar den 0. Om man inte
    väljer något, returneras 1.
     */
    public synchronized void openFileManager(){
        file = new JFileChooser();
        int action = file.showSaveDialog(null);


      //  Hämtar den valda filen och dess absoluta sökväg, alltså den sökvägen som finns
      //  lokalt på ens dator.

        if(action == 0){
            File chosenFile = new File(file.getSelectedFile().getAbsolutePath());
            String name = chosenFile.getName();

            try{
                if(!(name.endsWith("jpg"))){
                    throw new WrongFormat("You need to choose a PNG picture.");
                }

                else {
                    messageFrame.sendImage(chosenFile, userName);
                }
            } catch (WrongFormat e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        }
    }

    public void onlineUsersRequest(){
        Message message = new Message(user, "Online user list request 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER");
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    //    return monitorMessage.getOnlineUsers();
    }

    public String getReceiverTime(){
        LocalDateTime currentTime = LocalDateTime.now();

        //"ofPattern" används för att skapa en specifik tidsformat.
        DateTimeFormatter formatCurrentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        /*Vi formaterar den nuvarande tiden "currentTime" genom att använda mönstret vi skapade i
             objektet formatCurrentTime.
         */
        String formattedTime = currentTime.format(formatCurrentTime);

        return formattedTime;
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
                if(!(name.endsWith("jpg"))){
                    throw new WrongFormat("You need to choose a PNG picture.");
                }

                else{
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

    public synchronized void manageImage(ImageIcon imageIcon, ArrayList<String> contacts) {
        ArrayList<User> receivers = new ArrayList<>();

        for(String contact : contacts){
            User user1 = new User(contact, null);
            receivers.add(user1);
        }

        Message image = new Message(user, receivers, null, imageIcon);

        try {
            oos.writeObject(image);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void managePictureWithText(ImageIcon imageIcon, ArrayList<String> contacts, String text){
        ArrayList<User> receivers = new ArrayList<>();

        for(String contact : contacts){
            User user1 = new User(contact, null);
            receivers.add(user1);
        }

        Message message = new Message(user, receivers, text, imageIcon);

        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized String getUserName(){
        return this.userName;
    }

    public synchronized void createAccount(String name, ImageIcon imageIcon) {
        this.user = new User(name, imageIcon);
        this.userName = name;
        try {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        monitorMessage = new MonitorMessage(socket, true);
        monitorMessage.start();

        startFrame.closeInterface();
        MainClient.main(new String[0]);
    }


    public synchronized void logOut(){
        Message logOut = new Message(user, "Log out request 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER");
        try {
            oos.writeObject(logOut);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void logIn(String userName){
        this.user = new User(userName, null);
        this.userName = userName;

        try {
            oos.writeObject(user);

            Object obj = ois.readObject();

            if (!(obj instanceof ImageIcon)) {
                JOptionPane.showMessageDialog(null, "Account does not exist");
                startFrame.closeInterface();
                MainClient.main(new String[0]);

            } else {
                startFrame.closeInterface();
                MainClient.main(new String[0]);

                ImageIcon userImage = (ImageIcon) obj;
                monitorMessage = new MonitorMessage(socket, true);
                monitorMessage.start();

                openChatFrame(userImage);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public synchronized void manageMessage(String text, ArrayList<String> contacts){
        messageFrame.clearFriends();

        ArrayList<User> receivers = new ArrayList<>();

        for(String contact : contacts){
            User user1 = new User(contact, null);
            receivers.add(user1);
        }

        Message message = new Message(user, receivers, text, null);
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private class MonitorMessage extends Thread{
        private static ArrayList<String> onlineUsers;
        private Socket socket;
        private boolean isRunning;

        public MonitorMessage(Socket socket, boolean isRunning){
            this.socket = socket;
            this.isRunning = true;
        }

        @Override
        public void run(){
                try {
                    while (isRunning) {
                        Object obj = ois.readObject();

                        if(obj instanceof Message){
                            Message message = (Message) obj;
                            String textContent = message.getTextMessage();
                            ImageIcon imageIcon = message.getImageIcon();

                            if (textContent != null && imageIcon == null) {
                                String name = message.getSender().getUserName();

                                if(name.equals(userName)){
                                    String text = message.getTextMessage();

                                    if(text.equals("Accepted 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER")){
                                        saveContacts();
                                        socket.close();
                                        break;
                                    }
                                }

                                else {
                                    String user = getUserName();
                                    ArrayList<User> recievers = message.getReceivers();

                                    for(User receiver : recievers){
                                        if(user.equals(receiver.getUserName())){
                                            String formattedTime = getReceiverTime();
                                            message.setUserReceiverTime(formattedTime);

                                            messageFrame.displayText(message.getTextMessage(), message.getSender().getUserName(), message.getUserReceiverTime());
                                        }
                                    }
                                }

                            } else if((textContent == null) && message.getImageIcon() != null) {
                                String user = getUserName();
                                ArrayList<User> recievers = message.getReceivers();

                              //  message.getImageMessage();

                                for(User receiver : recievers){
                                    if(user.equals(receiver.getUserName())){
                                        String formattedTime = getReceiverTime();
                                        message.setUserReceiverTime(formattedTime);

                                        messageFrame.displayImage(message.getImageIcon(), message.getSender().getUserName(), message.getUserReceiverTime());
                                    }
                                }
                            } else if(textContent != null && imageIcon != null){
                                String user = getUserName();
                                ArrayList<User> recievers = message.getReceivers();

                                for(User receiver : recievers){
                                    if(user.equals(receiver.getUserName())){
                                        String formattedTime = getReceiverTime();
                                        message.setUserReceiverTime(formattedTime);

                                        messageFrame.displayPictureWithText(message.getImageIcon(), message.getSender().getUserName(), message.getUserReceiverTime(), textContent);
                                    }
                                }
                            }

                        } else if(obj instanceof ArrayList<?>){
                            Message message1 = new Message(null, "Safe close 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER");
                            oos.writeObject(message1);
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
