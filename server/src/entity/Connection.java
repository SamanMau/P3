package entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread{
    private ServerSocket serverSocket;

    public Connection(int port){
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){
        while (true){
            try {
                Socket socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class ClientConnection extends Thread{
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Socket socket;

        public ClientConnection(Socket socket){

        }

        public void run(){

        }
    }
}
