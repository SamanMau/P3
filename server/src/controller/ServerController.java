package controller;

import boundary.MainframeLogPanel;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerController extends Thread{
    MainframeLogPanel mf = new MainframeLogPanel();
    private ServerSocket serverSocket;
    private ArrayList<ClientConnection> activeUsers = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in); //test variabel
    private Thread thread;

    public ServerController() {
        try{
            this.serverSocket = new ServerSocket(1024);
            thread = new Thread();
            thread.start();
        } catch (IOException e){

        }

    }

    @Override
    public void run(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                ClientConnection clientClientConnection = new ClientConnection(socket);
                userConnected(clientClientConnection);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void userConnected(ClientConnection client){
        activeUsers.add(client);
    }

    private class ClientConnection extends Thread{ /*Kommer att hantera klient förfrågan. Den inre
    klassen finns eftersom vi vill hantera flera klienter samtidigt, och det hade varit
    ineffektivt att hantera detta i den yttre klassen. */

       // private LinkedBlockingQueue<Message>

        private Socket clientSocket;

        public ClientConnection(Socket clientSocket) {
            this.clientSocket = clientSocket;
            start();

        }

        @Override
        public void run(){
            try{
                System.out.println("Hej");
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                sendLogMessage();
                oos.flush();

            } catch (IOException e){

            }
        }

        public void sendLogMessage(){
            System.out.println("Enter name: ");
            String name = scanner.next();
            mf.sendLogMessage(name);
        }

    }

    public static void main(String[] args) {
        ServerController sc = new ServerController();
    }
}

