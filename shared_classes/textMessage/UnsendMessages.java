package shared_classes.textMessage;

import shared_classes.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsendMessages {
    private HashMap<User, ArrayList<Message>> unsend;

    public synchronized void put(User user, Message message){
      //  unsend.put(user, message);
    }

}
