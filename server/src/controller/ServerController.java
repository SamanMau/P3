package controller;

import boundary.MainframeLogPanel;
import entity.Client;
import shared_classes.textMessage.Message;
import shared_classes.user.User;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerController{
    private ServerSocket serverSocket;

    private UserManager userManager;
    private Client client;

    private ArrayList<Message> newList;
    private MainframeLogPanel mainframe;

    public ServerController() {
        try{

            this.mainframe = new MainframeLogPanel(this);
            userManager = new UserManager("server/src/AllUsers.dat");

            this.serverSocket = new ServerSocket(1000);
            this.client = new Client();
            newList = new ArrayList<>();


        }
        catch (IOException e){
        }
    }

    public void listenForConnections(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                ClientConnection clientClientConnection = new ClientConnection(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /*
    "DateTimeFormatter" is used to create a date with a specific pattern.
    We try parsing the sent time "fromTimeText" in the same pattern as
    "timeFormatter". If both were successful, return true, else, false.
    False indicates that a incorrect format was typed as input by the user.
     */
    public boolean checkDateValidity(String fromTimeText, String toTimeText, String pattern) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(pattern);

        try{
            LocalDate.parse(fromTimeText, timeFormatter);
            LocalDate.parse(toTimeText, timeFormatter);
            return true;
        } catch (DateTimeParseException e){
            return false;
        }

    }

    /*
    This method is used to display every log from our text file from a specified
    time. We create a pattern, "formatDate" and then we create LocalDateTime
    objects by parsing our parameters, "fromTimeText" and "toTimeText".

    We read every single log from our file "loggTrafik", for each log, we check if
    that log falls within the range of "fromDate" and "toDate". If that is the case,
    we add that log to our "logList".
     */
    public ArrayList<String> getTraficLogInterval(String fromTimeText, String toTimeText) {
        ArrayList<String> logList = new ArrayList<>();

        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime fromDate = LocalDateTime.parse(fromTimeText, formatDate);

        LocalDateTime toDate = LocalDateTime.parse(toTimeText, formatDate);

        try {
            FileReader fr = new FileReader("server/src/loggTrafik.txt");
            BufferedReader reader = new BufferedReader(fr);

            String row;

            while (!((row = reader.readLine()) == null)){

                try {
                    String[] splitRow = row.split(" : ");
                    String log = splitRow[0]; //contains the event itself.

                    String time = splitRow[1]; //contains the date the event happened.

                    //We parse the time from the event to our specified format.
                    LocalDateTime logDate = LocalDateTime.parse(time, formatDate);

                    //Checks if the date is within our range.
                    if(!logDate.isBefore(fromDate) && !logDate.isAfter(toDate)){
                        String text = log + "   " + time;
                        logList.add(text);
                    }


                } catch (ArrayIndexOutOfBoundsException e){
                }

            }

            if(logList.size() > 0){
                return logList;
            } else {
                JOptionPane.showMessageDialog(null, "No log was found during that period");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
           // return logList;
            e.printStackTrace();
        }

        return null;
    }

    /*
     This class represents a single client, represented by a thread.
     */
    private class ClientConnection extends Thread{
        private Socket clientSocket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        public ClientConnection(Socket clientSocket) {
            this.clientSocket = clientSocket;
            start();
        }

        /*
        If imageIcon is not null, it means that the user is new,
        and should be registered. If imageIcon is null, it means
        that the user already exists.
         */
        @Override
        public void run(){
            try{
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
                try {
                    User user = (User) ois.readObject();

                    ImageIcon imageIcon = user.getUserImage();

                    if(imageIcon != null){
                        registerUser(user);
                        Client client1 = new Client(user, clientSocket, oos, ois);
                        client.put(user, client1);
                        logTrafic(user.getUserName() + " has registered" , getCurrentTime());


                    } else {
                        boolean exists = userManager.checkIfExists(user.getUserName());
                        if(!exists){
                            Message message = new Message(user, "Can't log in");
                            message.setServerReceivedTime(getCurrentTime());
                            oos.writeObject(message);
                        } else {
                            String name = user.getUserName();
                            logInUser(name);
                            Client client1 = new Client(user, clientSocket, oos, ois);
                            client.put(user, client1);
                            logTrafic(user.getUserName() + " has logged in" , getCurrentTime());
                        }
                    }

                    int amountActive = updateUserList();
                    logTrafic("The online user list has been updated. " + amountActive + " people active", getCurrentTime());


                    newList = new ArrayList<>(); // löser problemet med att oskickade meddelanden visas om och om igen.
                    readOfflineMessages();


                    /*
                    The if statement "user1 != null" solved an unknown weird problem.
                     */
                    while (true){
                        Object obj = ois.readObject();

                        if(obj instanceof Message){
                            Message message = (Message) obj;

                            User user1 = message.getSender();

                            if(user1 != null){
                                handleMessage(message);
                            }
                        }
                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e){
            }
        }

        public synchronized void handleMessage(Message message){
            String textMessage = message.getTextMessage();

            message.setServerReceivedTime(getCurrentTime());


            /*
            This if statement handles the situation where a user wants to log out.
             */
            if((textMessage != null) && textMessage.contains("Log out request 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER")){
                User user = message.getSender();
                logTrafic(user.getUserName() + " wants to log out", getCurrentTime());


                try {
                    client.remove(user);
                    int amountActive = updateUserList();

                    Message message2 = new Message(user, "Accepted 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER");

                    message2.setServerReceivedTime(getCurrentTime());

                    oos.writeObject(message2);

                    Message message1 = (Message) ois.readObject();
                    message1.setServerReceivedTime(getCurrentTime());

                    if(message1.getTextMessage().equals("Safe close 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER")){
                        clientSocket.close();
                        logTrafic(user.getUserName() + " has disconnected" , getCurrentTime());

                        logTrafic("The online user list has been updated. " + amountActive + " people active", getCurrentTime());

                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }

            /*
            This else if statement handles the situation where a user wants
            to see which users are online.
             */
            else if(textMessage.equals("Online user list request 323fwed142erg32494903490fg425667h767468327:)78898AdEEeE342SHEKEER")){
                ArrayList<String> onlineList = getOnlineUsers();
                User user = message.getSender();
                Client client = Client.getClient(user);

                if(client != null){
                    ObjectOutputStream oos = client.getOos();
                    try {
                        oos.writeObject(onlineList);
                        oos.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            /*
            Handles normal message cases between users. This is responsible for sending new messages
             */
            else{
                ArrayList<User> receivers = message.getReceivers();

                ArrayList<User> sortedReceivers = new ArrayList<>();

                int offlineAmount = 0;

                for(int i = 0; i < receivers.size(); i++){
                    Client client = Client.getClient(receivers.get(i));
                    if(client != null){
                        sortedReceivers.add(receivers.get(i));
                    }
                }

                for(int i = 0; i < receivers.size(); i++){
                    Client client = Client.getClient(receivers.get(i));
                    if(client == null){
                        sortedReceivers.add(receivers.get(i));
                        offlineAmount++;
                    }
                }

                int canBreak = 0;

                for(int i = 0; i < sortedReceivers.size(); i++){

                    String name = sortedReceivers.get(i).getUserName();

                    User reciever = userManager.readUserFromFile(name);

                    Client clientReciever = Client.getClient(reciever);

                    if(clientReciever != null){
                        ObjectOutputStream oos = clientReciever.getOos();

                        if(clientReciever.online()){
                            try {
                                oos.writeObject(message);
                                oos.flush();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }

                    else {
                       // addUnsentMessageToFile(message);
                        canBreak++;


                        if(canBreak == offlineAmount){
                            addUnsentMessageToFile(message);
                            break;
                        }

                    }

                }

                StringBuilder text = new StringBuilder();

                if(receivers.size() == 1){
                    text.append(receivers.get(0).getUserName());
                } else {
                    for(int i = 0; i < receivers.size(); i++){
                        if(i + 1 == receivers.size()){
                            text.append("and " + receivers.get(i).getUserName());
                        } else {
                            text.append(receivers.get(i).getUserName()).append(", ");
                        }
                    }
                }

                logTrafic(message.getSender().getUserName() + " has sent a message to " + text , message.getServerReceiverTime());

            }
        }

        public String getCurrentTime(){
            LocalDateTime currentTime = LocalDateTime.now(); //Returns the current date.


            //"ofPattern" is used to create a specific time format, in this case "yyyy-MM-dd HH:mm"
            DateTimeFormatter formatCurrentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            /*
            We format the current time (currentTime" by using the pattern we created in "formatCurrentTime".
             */
            String formattedTime = currentTime.format(formatCurrentTime);

            return formattedTime;
        }

        /*
        Logs trafic.
         */
        public synchronized void logTrafic(String message, String time) {
            ArrayList<String> logList = new ArrayList<>();

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("server/src/loggTrafik.txt", true));
                writer.write(message + " : " + time);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        /*
        if(messageFile.length() > 0) is used to check that the file is not empty.
        If the array is empty and we try reading from it, errors will be thrown.
        If the array is not empty, the message objects will be put in the array.
        Then, we write the message objects from the array to the file again.
        We overwrite the old messages from the file.
         */
        public synchronized void addUnsentMessageToFile(Message message) {
            ArrayList<Message> messageList = new ArrayList<>();

            File messageFile = new File("server/src/offlineMessages.dat");

            if (messageFile.length() > 0) {
                try{
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageFile));

                    //Reads the message objects from the file and puts it in the array "messageList".
                    while (true){
                        try {
                            Message messageRead = (Message) ois.readObject();
                            messageRead.setServerReceivedTime(getCurrentTime());
                            messageList.add(messageRead);
                        } catch (EOFException | ClassNotFoundException e) {
                            break;
                        }
                    }

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            ArrayList<User> offlineUsers = message.getReceivers();

            ArrayList<User> filtered = getOfflineReceivers(offlineUsers);

            User sender = message.getSender();
            String text = message.getTextMessage();
            ImageIcon imageIcon = message.getImageIcon();

            Message modified = new Message(sender, filtered, text, imageIcon);


          //  messageList.add(message);
            messageList.add(modified);

            try{
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(messageFile));

                for(int i = 0; i < messageList.size(); i++){
                    oos.writeObject(messageList.get(i));
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


       // Returns an array of offline users
        public synchronized ArrayList<User> getOfflineReceivers(ArrayList<User> receivers){
            ArrayList<User> filtered = new ArrayList<>();

            for(int i = 0; i < receivers.size(); i++){
                Client client1 = Client.getClient(receivers.get(i));

                if(client1 == null){
                    filtered.add(receivers.get(i));
                }

            }

            return filtered;

        }

        /*
        This method puts unsent message objects in an array and forwards this array
        to other methods, such as "manageOfflineMessages()", which handles these
        messages more precisely.
         */
        public synchronized void readOfflineMessages() throws FileNotFoundException {
            ArrayList<Message> list = new ArrayList<>();

            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream("server/src/offlineMessages.dat"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            while (true){
                    try {
                        Message message = (Message) ois.readObject();
                        message.setServerReceivedTime(getCurrentTime());
                        list.add(message);
                    }   catch (IOException e) {
                        if (!list.isEmpty()) {
                            manageOfflineMessages(list);
                        }
                        break;
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
        }

        /*
        "messageList" contains every unsent message. For each message, we get their respective
        arraylist of receivers. For each user object in the receivers array, we check if that
        user is online or not. If he is online, we send him the message.
         */
        public synchronized void manageOfflineMessages(ArrayList<Message> list){
            ArrayList<Message> messageList = list;

            HashMap<User,Client> clients = Client.getHashMap();

            for(int i = 0; i < messageList.size(); i++){

                Message currentMessage = messageList.get(i);
                ArrayList<User> receivers = currentMessage.getReceivers();

                if(receivers != null){

                    for(int j = 0; j < receivers.size(); j++){
                        User user = receivers.get(j);

                        if(clients.containsKey(user)){
                            Client clientReciever = Client.getClient(user);

                            ObjectOutputStream oos = clientReciever.getOos();
                            try {
                                oos.writeObject(currentMessage);

                                removeUnsentMessage(currentMessage, user, messageList);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }


            }
        }

        /*
        Removes users from the sender list of a message so that once the user has received a message it won't receive it again
         */
        public synchronized void removeUnsentMessage(Message message, User user, ArrayList<Message> list){

            for(int i = 0; i < list.size(); i++){
                Message checkMessage = list.get(i);

                if(checkMessage.equals(message)){

                    User sender = checkMessage.getSender();

                    ArrayList<User> receivers = checkMessage.getReceivers();

                    receivers.remove(user);

                    String text = checkMessage.getTextMessage();

                    ImageIcon imageIcon = checkMessage.getImageIcon();

                    Message modififed = new Message(sender, receivers, text, imageIcon);

                    if(modififed.getReceivers().size() > 0){
                        newList.add(modififed);
                        break;
                    }
                }
            }

            clearOldFile();
        }

        /*
        Clears and recreates file to make sure no duplicates are created
         */
        public synchronized void clearOldFile(){
            try {

                // "rw" står för read/write vilket innebär att man kan läsa och skriva till filen.
                RandomAccessFile file = new RandomAccessFile(new File("server/src/offlineMessages.dat"), "rw");
                file.setLength(0);

                FileOutputStream fileOS = new FileOutputStream("server/src/offlineMessages.dat");
                ObjectOutputStream oos = new ObjectOutputStream(fileOS);

                for(int i = 0; i < newList.size(); i++){
                    Message message = newList.get(i);
                    oos.writeObject(message);
                }


            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*
        Uses the users name to log in
         */
        public synchronized void logInUser(String name){
            User user = userManager.readUserFromFile(name);

            if(user != null){
                try {
                    oos.writeObject(user.getUserImage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /*
        Registers a user
         */
        public synchronized void registerUser(User user){
            userManager.addUser(user);
        }

        /*
        Returns the number of active clients.
         */
        public synchronized int updateUserList(){
            ArrayList<String> userList = new ArrayList<>();
            HashMap<User,Client> clients = Client.getHashMap();

            for(User user : clients.keySet()){
                userList.add(user.getUserName());
            }

            writeOnlineUsersToFile(userList);

            ArrayList<String> newUserList;
            newUserList = getOnlineUsers();

            try {
                oos.writeObject(newUserList);
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return newUserList.size();

        }

        /*
        Returns an arraylist which contains the name of every active client.
         */
        public ArrayList<String> getOnlineUsers(){
            ArrayList<String> online = new ArrayList<>();
            try {
                FileReader fr = new FileReader("server/src/onlineUsers.txt");
                BufferedReader reader = new BufferedReader(fr);

                String line = "";

                while ((line = reader.readLine()) != null){
                    online.add(line);
                }

                reader.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return online;
        }

        /*
        This saves all online users to a file
         */
        public void writeOnlineUsersToFile(ArrayList<String> onlineUsers){
            try {
                FileWriter fw = new FileWriter("server/src/onlineUsers.txt");
                BufferedWriter writer = new BufferedWriter(fw);

                for(String online : onlineUsers){
                    writer.write(online);
                    writer.newLine();
                }

                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
