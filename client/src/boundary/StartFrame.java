package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private JPanel namePanel;
    private JLabel title;
    private ClientController controller;

    public StartFrame(ClientController controller) {
        super("Welcome!");
        setLayout(null);
        this.controller = controller;
        this.setSize(350, 300);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.GRAY);

        StartPanel startPanel = new StartPanel(this, controller);
        this.add(startPanel);

        setVisible(true);
    }

    public ImageIcon chooseProfilePic(){
        return controller.chooseProfilePic();
    }

    public void openLoginFrame(){
        LoginFrame loginFrame = new LoginFrame(this);
    }

    public void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame(this);
    }

    public void createAccount(String name, ImageIcon imageIcon){
        controller.createAccount(name, imageIcon);
    }

    public void openChatFrame(ImageIcon userImage) {
        controller.openChatFrame(userImage);
    }

    public void logIn(String name){
        controller.logIn(name);
    }
}