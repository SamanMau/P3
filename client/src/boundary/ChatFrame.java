package boundary;

import controller.ClientController;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ChatFrame extends JFrame{
    private JTextArea chat = new JTextArea();
    private ClientController controller;
    private MessagePanel messagePanel;

    public ChatFrame(ClientController controller){
        super("Chat");
        this.controller = controller;
        this.setSize(580, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setEditable(false);
        chat.setPreferredSize(new Dimension(400, 400));
        messagePanel = new MessagePanel(controller);
        this.add(messagePanel);
        setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    /*
    public void sendImage(File message){
        messagePanel.displayImage(message);
    }

     */

}
