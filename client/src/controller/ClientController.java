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

    /*
    This method creates an object of "MessageFrame"
    which is the chat frame itself, where clients
    can send messages. For each connection, we
    also need to read the current clients
    contacts.
     */
    public void openChatFrame(ImageIcon picture){
        messageFrame = new MessageFrame(this, picture);
        readContacts();
    }

    /*
    Same as the method above, only difference is that we dont
    need to read any contacts.
     */
    public void openChatFrameForRegister(ImageIcon pictureFile){
        messageFrame = new MessageFrame(this, pictureFile);
    }

    /*
    This method saves each clients contacts to a file, we put
    "append" as false because we want to overwrite the content
    in our file instead of putting new content at the end
    of the file. This method is called when a user logs out.
     */
    public synchronized void saveContacts(){
        HashMap<String, ArrayList<String>> currentContacts = getEveryContact();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("client/src/contacts.txt", false));

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

    /*
    This method is called when a clients adds a new friend.
    If a client has existing friends, we add the new friends
    to the existing list, else, we create a new element in
    our hashmap.

    The second for each loop checks that there are no
    duplicate contacts in a single list. For instance,
    "Benzema" can't have two friends of the exact same
    "Greken".
     */
    public void updateContacts(){
        HashMap<String, ArrayList<String>> currentContacts = getEveryContact();

        ArrayList<String> newFriends = contactsMap.get(userName);

        if(newFriends != null){

            if(currentContacts.containsKey(userName)){

                ArrayList<String> existingFriends = currentContacts.get(userName);

                for(int i = 0; i < newFriends.size(); i++){
                    existingFriends.add(newFriends.get(i));
                }

            } else {
                currentContacts.put(userName, newFriends);
            }

            /*
            We check if the current friend (i) is equal to
            the upcoming friend (x = i + 1) which would indicate
            that there are duplicates of the same person.
             */
            for(String friendOwner : currentContacts.keySet()){

                ArrayList<String> friendContacts = currentContacts.get(friendOwner);

                for(int i = 0; i < friendContacts.size(); i++){
                    String friend = friendContacts.get(i);

                    for(int x = i + 1; x < friendContacts.size(); x++){

                        if(friend.equals(friendContacts.get(x))){
                            friendContacts.remove(x);

                            x--; // x is reduced because the array is getting reduced.
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

            clearContactList(); //solved an unknown error.
        }



    }

    /*
    The first if statement checks if the current user already
    has a list of friends.
    */
    public synchronized void addNewFriendToContacts(String friend){
        if(!contactsMap.containsKey(userName)){
            contactsMap.put(userName, new ArrayList<>());
        }

        contactsMap.get(userName).add(friend);
    }

    public void clearContactList(){
        contactsMap = new HashMap<>();
    }

    /*
     row.split(":") splits a line in two parts, the part
     before ":" and the part after ":", this creates an array.
     trim(); removes unnecessary white space.
     */
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

    /*
    Reads every contact and displays the contactlist of the current user.
     */
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
    JFileChooser is a class which is used to open and save files on a computer.
    The method "showSaveDialog()" opens the file manager. showSaveDialog() returns
    an int. If a file was chosen, the number 0 is returned, else, 1 is returned.
     */
    public synchronized void openFileManager(){
        file = new JFileChooser();
        int action = file.showSaveDialog(null);

        if(action == 0){

            //gets the chosen file and its absolute path.
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


    /*
    This method sends a request to the server to return
    the list of online users.
     */
    public void onlineUsersRequest(){
        Message message = new Message(user, "Online user list request 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER");
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Returns the current time.
     */
    public synchronized String getReceiverTime(){
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

        if(action == 0){
            File chosenFile = new File(file.getSelectedFile().getAbsolutePath());
            String name = chosenFile.getAbsolutePath();

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

    /*
    Method called when a user wants to send an image.
     */
    public synchronized void manageImage(ImageIcon imageIcon, ArrayList<String> contacts) {
        ArrayList<User> receivers = new ArrayList<>();

        for(String username : contacts){
            User user1 = new User(username, null);
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

    /*
    Method called when a user wants to send an image
    combined with text content.
     */
    public synchronized void managePictureWithText(ImageIcon imageIcon, ArrayList<String> contacts, String text){
        ArrayList<User> receivers = new ArrayList<>();

        for(String username : contacts){
            User user1 = new User(username, null);
            receivers.add(user1);
        }

        Message message = new Message(user, receivers, text, imageIcon);

        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
    Returns the username of the current user.
     */
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

    /*
    This is a thread which monitors messages.
     */
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

                            /*
                            This if statement would indicate that a text message
                            with no image was sent.
                             */
                            if (textContent != null && imageIcon == null) {
                                String sender_name = message.getSender().getUserName();

                                if(sender_name.equals(userName)) {
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

                                    /*
                                    Checks if the current user is one of the receivers
                                    of a message. If true, then display the message.
                                     */
                                    for(User receiver : recievers){
                                        if(user.equals(receiver.getUserName())){
                                            String formattedTime = getReceiverTime();
                                            message.setUserReceiverTime(formattedTime);

                                            messageFrame.displayText(message.getTextMessage(), message.getSender().getUserName(), message.getUserReceiverTime());
                                        }
                                    }
                                }

                                /*
                                This else if statement would indicate that a message with
                                only a picture was sent, without text content.
                                 */
                            } else if((textContent == null) && message.getImageIcon() != null) {
                                String user = getUserName();
                                ArrayList<User> recievers = message.getReceivers();

                                for(User receiver : recievers){
                                    if(user.equals(receiver.getUserName())){
                                        String formattedTime = getReceiverTime();
                                        message.setUserReceiverTime(formattedTime);

                                        messageFrame.displayImage(message.getImageIcon(), message.getSender().getUserName(), message.getUserReceiverTime());
                                    }
                                }

                                /*
                                This else if statement would indicate that a message
                                was sent which included both text content and a picture.
                                 */
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

                            /*
                            If the read object was an arraylist, it means that the user
                            requested a list of the current online users.
                             */
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
