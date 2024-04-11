package controller;

import java.io.IOException;
import java.net.Socket;

public class ClientController {
    private String ipAdress = "127.0.0.1";
    private int port = 1024;

    public ClientController(){
        try {
            Socket socket = new Socket(ipAdress, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ClientController cc = new ClientController();
    }

}
