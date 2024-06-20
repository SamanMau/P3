package entity;

import shared_classes.textMessage.Message;
import shared_classes.user.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
    private static HashMap<User,Client> clients = new HashMap<>();
    private User user;
    private Socket clientSocket;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private boolean connected;

    public Client(User user, Socket clientSocket, ObjectOutputStream oos, ObjectInputStream ois){
        this.user = user;
        this.clientSocket = clientSocket;
        this.connected = true;

        this.oos = oos;
        this.ois = ois;
    }

    public Client(){

    }

    public void remove(User user){
        clients.remove(user);
    }

    public static HashMap<User,Client> getHashMap(){
        return clients;
    }

    public synchronized void put(User user,Client client) {
        clients.put(user,client);
    }

    public synchronized ObjectOutputStream getOos(){
        return oos;
    }

    public static synchronized Client get(User user) {
        return clients.get(user);
    }

    public Message messageRecieve(){
        try {
            Message message = (Message) ois.readObject();
            return message;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean online(){
        return connected;
    }

    public void sendMessage(Message message){
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public synchronized void signOutClient(User user){
        Client client = clients.get(user);
        this.connected = false;
        clients.remove(user);

        if(!this.clientSocket.isClosed()){
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

