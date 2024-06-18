package controller;

import boundary.MainframeLogPanel;
import entity.Client;
import shared_classes.textMessage.Message;
import shared_classes.user.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class ServerController extends Thread{
    private ServerSocket serverSocket;
    private ArrayList<User> activeUsers = new ArrayList<>();

    private UserManager userManager;


    public ServerController() {
        try{

            userManager = new UserManager("server/src/AllUsers.dat");
          //  String file = "AllUsers.dat";
          //  userNameReader.readFile(file);

            this.serverSocket = new ServerSocket(1000);

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
                    Client client = new Client(user, clientSocket, oos, ois);
                    client.put(user, client);

                    boolean created = userManager.checkIfExists(user.getUserName());

                    if(!created){
                        registerUser(user);
                    } else {
                        String name = user.getUserName();
                        logInUser(name);
                    }

                    updateUserList();


                    while (true){
                      //  Message message = (Message) ois.readObject();
                      //  handleMessage(message);


                        Object obj = ois.readObject();

                        if(obj instanceof Message){
                            Message message = (Message) obj;
                            handleMessage(message);
                        }


                   //     else {
                     //       oos.writeObject(obj);
                      //  }

                    }



                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e){
            }
        }

        public void handleMessage(Message message){
            User userReciever = message.getReciever();
            String name = userReciever.getUserName();

            User reciever = userManager.readUserFromFile(name);

            Client clientReciever = Client.get(reciever);
            ObjectOutputStream oos = clientReciever.getOos();

            if(clientReciever.online()){
                try {
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
