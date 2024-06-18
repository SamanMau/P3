package entity;

import controller.ClientController;
import shared_classes.textMessage.Message;
import shared_classes.user.User;

public class MainClient {
    public static void main(String[] args) {
        ClientController clientController = new ClientController();



        /*
        new Thread(() -> {
            while (true) {
                Message message = clientController.recieveMessageFromClient();
                if (message != null) {
                    String name = clientController.getName();
                    User user2 = message.getReciever();
                    System.out.println("Reciever " + user2.getUserName());

                    if(name.equals(user2.getUserName())){
                        System.out.println(name + " och " + user2.getUserName());
                    }


                    System.out.println("Received: " + message.getTextMessage());
                }
            }
        }).start();


         */




    }
}