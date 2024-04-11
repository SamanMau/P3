package entity;
import shared_classes.textMessage.*;
import shared_classes.user.*;
import java.util.ArrayList;

import java.util.HashMap;


// Klar
public class UnsentMessages {

    private HashMap<User,ArrayList<Message>> unsent = new HashMap<>();

    public synchronized void put(User user,Message message) {
        ArrayList<Message> messages = unsent.get(user);
        if(messages == null){
            messages = new ArrayList();
        }
        messages.add(message);
        unsent.put(user,messages);
    }


    public synchronized ArrayList<Message> get(User user) {

       return unsent.get(user);
    }





}
