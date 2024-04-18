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
    MainframeLogPanel mf = new MainframeLogPanel();
    private ServerSocket serverSocket;
    private UserNameReader userNameReader;
    private ArrayList<ClientConnection> activeUsers = new ArrayList<>();

    public ServerController() {
        try{
            userNameReader = new UserNameReader(this);
          //  String file = "AllUsers.txt";
          //  userNameReader.readFile(file);

            this.serverSocket = new ServerSocket(1000);

        } catch (IOException e){
        }
    }

    @Override
    public void run(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                ClientConnection clientClientConnection = new ClientConnection(socket);
                userConnected(clientClientConnection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void userConnected(ClientConnection client){
        activeUsers.add(client);
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
        boolean exists = userNameReader.findUserName(name, "server/src/AllUsers.txt");
        if(exists){
            //User user = userNameReader.getUserFromArray(userName);
            System.out.println("I exist!");
        }


        else if(!exists){
            System.out.println("Han finns inte");
            ImageIcon imageIcon = new ImageIcon("shared_classes/defaultProfile.png");
            User user  = new User(name, imageIcon);
            userNameReader.newUserAdded(user);
            userNameReader.saveToFile("server/src/AllUsers.txt", user);
        }
    }
}
