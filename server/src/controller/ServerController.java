package controller;

import boundary.MainframeLogPanel;
import entity.UserNameReader;
import shared_classes.user.User;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServerController extends Thread{
    private MainframeLogPanel mf = new MainframeLogPanel();
    private ServerSocket serverSocket;
    private UserNameReader userNameReader;
    private ArrayList<User> activeUsers = new ArrayList<>();
    private User currentSender;
    private User currentReciever;
    boolean testVariable = true;


    public ServerController() {
        try{
            userNameReader = new UserNameReader(this);
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

        public ClientConnection(Socket clientSocket) {
            this.clientSocket = clientSocket;
            start();

        }

        @Override
        public void run(){
            try{
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                try {
                    String name = (String) ois.readObject();
                    checkUser(name);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                oos.flush();

            } catch (IOException e){

            }
        }
    }

    public void checkUser(String name){
        boolean exists = userNameReader.findUserName(name, "server/src/AllUsers.dat");

            if(exists){
                User user = userNameReader.getUserFromArray(name);
                activeUsers.add(user);
                System.out.println("Sender: " + user.getUserName()); //kommentera bort
            }

            else {
                System.out.println("Han finns inte");
                ImageIcon imageIcon = new ImageIcon("shared_classes/defaultPic.jpg");
                User newUser  = new User(name, imageIcon);
                System.out.println("ImageIcon: " + imageIcon.toString());
                userNameReader.newUserAdded(newUser);
                userNameReader.saveToFile("server/src/AllUsers.dat", newUser);
                activeUsers.add(newUser);

                currentSender = getCurrentSender(newUser); //kommentera bort

                System.out.println("Sender: " + newUser.getUserName()); //kommentera bort

            }

    }
}
