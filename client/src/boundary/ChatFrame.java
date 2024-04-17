package boundary;

import controller.ClientController;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ChatFrame extends JFrame{
    private JTextArea chat = new JTextArea();
    private ClientController controller;
    private MessagePanel messagePanel;
    private TopPanel topPanel;

    public ChatFrame(ClientController controller){
        super("Chat");
        this.controller = controller;
        this.setLayout(null);

        this.setSize(580, 580);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setEditable(false);
        chat.setPreferredSize(new Dimension(400, 400));

        messagePanel = new MessagePanel(controller);
        topPanel = new TopPanel(controller);

        this.add(messagePanel);
        this.add(topPanel);

        this.setResizable(false);
        this.setVisible(true);
    }


    public void sendImage(File message){
        messagePanel.displayImage(message);
    }

}
