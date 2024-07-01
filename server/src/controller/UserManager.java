package controller;

import shared_classes.user.User;

import java.io.*;
import java.util.ArrayList;

public class UserManager {
    private String filePath;

    ArrayList<User> users;

    public UserManager(String filePath){
        this.filePath = filePath;
    }

    public synchronized void addUser(User user) {
        try{
            users = readEveryUserFromFile();
            users.add(user);
            overwriteAllUsers(users);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private synchronized ArrayList<User> readEveryUserFromFile() {
        try {
            ArrayList<User> users;

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));

            users = (ArrayList<User>) ois.readObject();

            return users;

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public synchronized void overwriteAllUsers(ArrayList<User> users) throws IOException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
            oos.writeObject(users);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public synchronized User readUserFromFile(String username) {
            ArrayList<User> users = readEveryUserFromFile();

            for(int i = 0; i < users.size(); i++){
                if(users.get(i).getUserName().equals(username)){
                    return users.get(i);
                }
            }

            return null;
    }

    public synchronized boolean checkIfExists(String username){
        ArrayList<User> users = readEveryUserFromFile();

        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getUserName().contains(username)){
                return true;
            }
        }

        return false;
    }

}
