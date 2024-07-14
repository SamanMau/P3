package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private ClientController controller;
    private StartPanel startPanel;
    private LoginFrame loginFrame;
    private RegisterFrame registerFrame;

    public StartFrame(ClientController controller) {
        super("Welcome!");
        setLayout(null);
        this.controller = controller;
        this.setSize(350, 300);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.GRAY);

        startPanel = new StartPanel(this);
        this.add(startPanel);

        setVisible(true);
    }

    public ImageIcon chooseProfilePic(){
        return controller.chooseProfilePic();
    }

    public void openLoginFrame(){
        loginFrame = new LoginFrame(this);
    }

    public void openRegisterFrame() {
        registerFrame = new RegisterFrame(this);
    }


    public void createAccount(String name, ImageIcon imageIcon){
        controller.createAccount(name, imageIcon);
    }

    public void openChatFrame(ImageIcon userImage) {
      //  controller.openChatFrame(userImage);
        controller.openChatFrameForRegister(userImage);
    }

    public void logIn(String name){
        controller.logIn(name);
    }

    public void closeInterface() {
        this.dispose();
    }

}