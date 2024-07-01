package entity;

import controller.ServerController;

public class MainServer {
    public static void main(String[] args) {
        ServerController serverController = new ServerController();
        serverController.run();


    }
}