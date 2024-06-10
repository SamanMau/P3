package boundary;

import controller.ClientController;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MessageFrame extends JFrame{
    private JTextArea chat = new JTextArea();
    private ClientController controller;
    private MessagePanel messagePanel;
    private TopPanel topPanel;
    private ContactPanel contactPanel;

    private AddFriendPanel addFriendPanel;

    private AddFriendFrame addFriendFrame;

    public MessageFrame(ClientController controller, String pictureFile){
        super("Chat");
        this.controller = controller;
        this.setLayout(null);

        this.setSize(680, 580);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setEditable(false);

        messagePanel = new MessagePanel(controller, this);
        topPanel = new TopPanel(controller, pictureFile, this);
        contactPanel = new ContactPanel(this);

        this.add(messagePanel);
        this.add(topPanel);
        this.add(contactPanel);

        this.setResizable(false);
        this.setVisible(true);
    }

    public void openFriendPanel(){
        addFriendFrame = new AddFriendFrame(this);
    }



    public void sendImage(File message, String name){
        messagePanel.displayImage(message, name);
    }

    public String chooseProfilePic(){
        return controller.chooseProfilePic();
    }


}
