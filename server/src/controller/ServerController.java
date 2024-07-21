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


        } catch (IOException e){
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
    En DateTimeFormatter skapas med ett datum mönster. Vi har en try catch för att
    se om det blir error eller inte, om det är error, innebär det att användaren
    skrev in fel datum mönster.
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
    DateTimeFormatter formaterar ett mönstrer för datum. LocalDate används för att
    omvandla vår sträng till en LocalDate objekt så att vi kan hämta året, månaden,
    och dagen individuellt.
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
                    String log = splitRow[0];

                    String time = splitRow[1];

                    LocalDateTime logDate = LocalDateTime.parse(time, formatDate);

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

    private class ClientConnection extends Thread{ /*Kommer att hantera klient förfrågan. Den inre
    klassen finns eftersom vi vill hantera flera klienter samtidigt, och det hade varit
    ineffektivt att hantera detta i den yttre klassen. */
        
        private Socket clientSocket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        public ClientConnection(Socket clientSocket) {
            this.clientSocket = clientSocket;
            start();
        }

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
                        boolean created = userManager.checkIfExists(user.getUserName());
                        if(!created){
                            Message message = new Message(user, "Can't log in");
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


            if((textMessage != null) && textMessage.contains("Log out request")){
                User user = message.getSender();
                logTrafic(user.getUserName() + " wants to log out", getCurrentTime());


                try {
                    client.remove(user);
                    updateUserList();
                    oos.writeObject(new Message(user, "Accepted"));
                    Message message1 = (Message) ois.readObject();


                    if(message1.getTextMessage().equals("Safe close")){
                        clientSocket.close();
                        logTrafic(user.getUserName() + " has disconnected" , getCurrentTime());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            } else if(textMessage.equals("Online user list request")){
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

                String formattedTime = getCurrentTime();
                message.setServerReceivedTime(formattedTime);

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
            LocalDateTime currentTime = LocalDateTime.now(); //hämtar nuvarande datumet


            //"ofPattern" används för att skapa en specifik tidsformat.
            DateTimeFormatter formatCurrentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                 /*Vi formaterar den nuvarande tiden "currentTime" genom att använda mönstret vi skapade i
                 objektet formatCurrentTime.
                */
            String formattedTime = currentTime.format(formatCurrentTime);

            return formattedTime;
        }

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
        "if(messageFile.length() > 0) används för att kontrollera att filen inte är tom. Om
        arrayen är tom och man läser från den, så får man error. Om arrayen nu är tom, så
        läggs meddelande objektet bara till i arrayen, därefter skriver man meddelande
        objekten från arrayen till filen igen. Man överskriver de gamla meddelanden
         filen varje gång ett nytt meddelande läggs till.
         */
        public synchronized void addUnsentMessageToFile(Message message) {
            ArrayList<Message> messageList = new ArrayList<>();

            File messageFile = new File("server/src/offlineMessages.dat");

            if (messageFile.length() > 0) {
                try{
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(messageFile));

                    while (true){
                        try {
                            Message messageRead = (Message) ois.readObject();
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

            ArrayList<User> filtered = filterArray(offlineUsers);

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

        public synchronized ArrayList<User> filterArray(ArrayList<User> receivers){
            ArrayList<User> filtered = new ArrayList<>();

            for(int i = 0; i < receivers.size(); i++){
                Client client1 = Client.getClient(receivers.get(i));

                if(client1 == null){
                    filtered.add(receivers.get(i));
                }

            }

            return filtered;

        }


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
        messageList är alla oskciakde meddelanden. För varje oskickat
        meddelande, så hämtar vi dess arraylist av receivers. För varje
        user objekt i receiver listan, måste vi kontrollera om det user objektet
        finns aktiv i vår hashmap.
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

        public synchronized void registerUser(User user){
            userManager.addUser(user);
        }

        /*
        "clients.keySet()" returnerar alla keys som finns i
        en hashmap.
         */
        /*
        public synchronized int updateUserList(){
            ArrayList<String> userList = new ArrayList<>();
            HashMap<User,Client> clients = Client.getHashMap();

            for(User user : clients.keySet()){
                userList.add(user.getUserName());
            }

            try {
                oos.writeObject(userList);
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return userList.size();

        }

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
