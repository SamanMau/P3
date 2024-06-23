package controller;

import boundary.MainframeLogPanel;
import entity.Client;
import shared_classes.textMessage.Message;
import shared_classes.user.User;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.chrono.MinguoEra;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerController extends Thread{
    private ServerSocket serverSocket;
    private ArrayList<User> activeUsers = new ArrayList<>();

    private UserManager userManager;
    private Client client;


    public ServerController() {
        try{

            userManager = new UserManager("server/src/AllUsers.dat");
          //  String file = "AllUsers.dat";
          //  userNameReader.readFile(file);

            this.serverSocket = new ServerSocket(1000);
            this.client = new Client();

        } catch (IOException e){
        }
    }

    public ArrayList<User> getActiveUsers(){
        return activeUsers;
    }

    @Override
    public void run(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                ClientConnection clientClientConnection = new ClientConnection(socket);
                //userConnected(clientClientConnection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void userConnected(User client){
        activeUsers.add(client);
    }

    public User getCurrentSender(User user){
        for(int i = 0; i < activeUsers.size(); i++){
            if(activeUsers.get(i).equals(user)){
                return activeUsers.get(i);
            }
        }

        return null;
    }

    public User getCurrentReciever(User user){
        for(int i = 0; i < activeUsers.size(); i++){
            if(activeUsers.get(i).equals(user)){
                return activeUsers.get(i);
            }
        }

        return null;
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


            if((textMessage != null) && textMessage.equals("Log out request")){
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
                    ObjectOutputStream oos = clientReciever.getOos();

                    if(clientReciever.online()){
                        try {

                          //  Message message3 = new Message(message.getSender(), receivers, message.getTextMessage(), message.getImage(), true);

                         //   oos.writeObject(message3);
                            oos.writeObject(message);
                            oos.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                // User user = message.getReciever();
                //   String name = user.getUserName();
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
