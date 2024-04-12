package entity;

import shared_classes.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserNameReader {
    private ArrayList<User> allUsers = new ArrayList<>();

    public boolean findUserName(String name){
        for (int i = 0; i < allUsers.size(); i++){
            if(allUsers.get(i).equals(name)){
                return true;
            }
        }
        return false;
    }

    public void saveToFile(String filename){
        try{
            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            for (User allName : allUsers) {
                out.writeObject(allName);
            }
            out.close();
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile(String filename){
        User user = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            while (true){
                try {
                    user = (User) in.readObject();
                    allUsers.add(user);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUserFromArray(String name){
        for(int i = 0; i < allUsers.size(); i++){
            if(allUsers.get(i).getUserName().equals(name)){
                return allUsers.get(i);
            }
        }

        return null;
    }

    public void newUserAdded(User user){
        allUsers.add(user);
    }

}
