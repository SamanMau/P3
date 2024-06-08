package controller;

import shared_classes.user.User;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class UserManager {
    private ArrayList<User> allUsers = new ArrayList<>();
    private ServerController serverController;

    public UserManager(ServerController serverController){
        this.serverController = serverController;
    }


    public boolean findUserName(String name, String file){
        String userName = name;
        FileReader fileReader;

        String line = "";
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null){
                if(line.contains(userName)){
                    return true;
                }
            }

            return false;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: Emil jobba här!
    public User readFile(String filename, String username){
        User user;
        String line = "";
        FileReader fileReader;
        try {
            fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null){
                if(line.contains(username)){
                    // user = bufferedReader.read();
                    //  System.out.println("User: " + user);
                    return null;
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void saveToFile(String filename, User user){
        try{
            /*
            booleska värdet "true" i FileOutPutStream gör så att varje ny skrivning
            går till en ny rad. På så sätts skrivs saker inte över varandra.
             */
            FileOutputStream fos = new FileOutputStream(filename, true);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(user);

            oos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageIcon readImage(String filename, String username){
        User user;
        String line = "";
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            while ((line = in.readLine()) != null){
                if(line.contains(username)){
                    user = (User) in.readObject();
                    ImageIcon icon = user.getUserImage();
                    return icon;
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;

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
