package entity;

import shared_classes.user.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {

    private HashMap<User,Client> clients = new HashMap<>();

    public synchronized void put(User user,Client client) {
        clients.put(user,client);
    }
    public synchronized Client get(User user) {
        return get(user);
    }
// fler synchronized-metoder som behövs
}

