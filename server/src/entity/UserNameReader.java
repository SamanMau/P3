package entity;

import controller.ServerController;
import shared_classes.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserNameReader {
    private ArrayList<User> allUsers = new ArrayList<>();
    private ServerController serverController;

    public UserNameReader(ServerController serverController){
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
                if(line.contains(name)){
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


    /*
    public void readFile(String filename){
        User element;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            while (true){
                element = (User) in.readObject();
               // allUsers.add(element);
                serverController.names(element);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

     */

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
