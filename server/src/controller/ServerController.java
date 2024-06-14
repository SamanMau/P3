package controller;

import boundary.MainframeLogPanel;
import shared_classes.user.User;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerController extends Thread{
    private MainframeLogPanel mf = new MainframeLogPanel();
    private ServerSocket serverSocket;
    private ArrayList<User> activeUsers = new ArrayList<>();
    private User currentSender;
    private User currentReciever;
    boolean testVariable = true;

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

                    ImageIcon imageIcon = user.getUserImage();

                    if(imageIcon != null){
                        registerUser(user);
                    } else {
                        String name = user.getUserName();
                        logInUser(name);
                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e){
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
    }

}
