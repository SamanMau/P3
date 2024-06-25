package controller;

import entity.Client;
import shared_classes.textMessage.Message;
import shared_classes.user.User;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ServerController extends Thread{
    private ServerSocket serverSocket;

    private UserManager userManager;
    private Client client;

    private HashMap<User, ArrayList<Message>> unsentMessages;

    private ArrayList<Message> newList;

    public ServerController() {
        try{

            userManager = new UserManager("server/src/AllUsers.dat");
          //  String file = "AllUsers.dat";
          //  userNameReader.readFile(file);

            this.serverSocket = new ServerSocket(1000);
            this.client = new Client();
            newList = new ArrayList<>();

        } catch (IOException e){
        }
    }

    @Override
    public void run(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                ClientConnection clientClientConnection = new ClientConnection(socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class ClientConnection extends Thread{ /*Kommer att hantera klient förfrågan. Den inre
    klassen finns eftersom vi vill hantera flera klienter samtidigt, och det hade varit
    ineffektivt att hantera detta i den yttre klassen. */

       // private LinkedBlockingQueue<Message>

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
                        }
                    }

                    updateUserList();

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

        public void handleMessage(Message message){
            String textMessage = message.getTextMessage();
            ImageIcon imageIcon = (ImageIcon) message.getImageIcon();


            if((textMessage != null) && textMessage.contains("Log out request")){
                User user = message.getSender();

                try {
                    client.remove(user);
                    updateUserList();
                    oos.writeObject(new Message(user, "Accepted"));
                    Message message1 = (Message) ois.readObject();


                    if(message1.getTextMessage().equals("Safe close")){
                        clientSocket.close();
                    }



                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }

            else{
                LocalDateTime currentTime = LocalDateTime.now(); //hämtar nuvarande datumet


                //"ofPattern" används för att skapa en specifik tidsformat.
                DateTimeFormatter formatCurrentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                 /*Vi formaterar den nuvarande tiden "currentTime" genom att använda mönstret vi skapade i
                 objektet formatCurrentTime.
                */
                String formattedTime = currentTime.format(formatCurrentTime);
                message.setServerReceivedTime(formattedTime);


                ArrayList<User> receivers = message.getRecievers();

                for(int i = 0; i < receivers.size(); i++){
                    String name = receivers.get(i).getUserName();

                    User reciever = userManager.readUserFromFile(name);

                    Client clientReciever = Client.get(reciever);

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
                    } else {
                        addUnsentMessageToFile(message);
                    }

                }

            }
        }

        /*
        "if(messageFile.length() > 0) används för att kontrollera att filen inte är tom. Om
        arrayen är tom och man läser från den, så får man error. Om arrayen nu är tom, så
        läggs meddelande objektet bara till i arrayen, därefter skriver man meddelande
        objekten från arrayen till filen igen. Man överskriver de gamla meddelanden
         filen varje gång ett nytt meddelande läggs till.
         */
        public void addUnsentMessageToFile(Message message) {
            List<Message> messageList = new ArrayList<>();

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

            messageList.add(message);

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

        public void readOfflineMessages() throws FileNotFoundException {
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
        public void manageOfflineMessages(ArrayList<Message> list){
            ArrayList<Message> messageList = list;
            HashMap<User,Client> clients = Client.getHashMap();

            for(int i = 0; i < messageList.size(); i++){
                Message currentMessage = messageList.get(i);
                ArrayList<User> receivers = currentMessage.getRecievers();

                for(int j = 0; j < receivers.size(); j++){
                    User user = receivers.get(j);

                    if(clients.containsKey(user)){
                        Client clientReciever = Client.get(user);

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

        public void removeUnsentMessage(Message message, User user, ArrayList<Message> list){
            for(int i = 0; i < list.size(); i++){
                Message checkMessage = list.get(i);

                if(checkMessage.equals(message)){
                    User sender = checkMessage.getSender();
                    ArrayList<User> receivers = checkMessage.getRecievers();
                    receivers.remove(user);
                    String text = checkMessage.getTextMessage();
                    ImageIcon imageIcon = checkMessage.getImageIcon();

                    Message modififed = new Message(sender, receivers, text, imageIcon);
                    newList.add(modififed);
                    break;
                }
            }

            clearOldFile();

            }


        public void clearOldFile(){
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

        public void logInUser(String name){
            User user = userManager.readUserFromFile(name);

            if(user != null){
                try {
                    oos.writeObject(user.getUserImage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        public void registerUser(User user){
            userManager.addUser(user);

        }

        /*
        "clients.keySet()" returnerar alla keys som finns i
        en hashmap.
         */
        public void updateUserList(){
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

        }
    }

}



