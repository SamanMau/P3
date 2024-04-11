package entity;

import shared_classes.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserNameReader {
    private ArrayList<User> allUsers = new ArrayList<>();

    public boolean findUserName(String name){
        for (int i = 0; allUsers.size()> i; i++){
            if(Objects.equals(allUsers.get(i).getUserName(), name)){
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

    public User getUserFromArray(int index){
        return allUsers.get(index);
    }




}
