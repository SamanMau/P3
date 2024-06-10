package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class StartFrame extends JFrame {
    private JPanel namePanel;
    private JLabel title;
    private ClientController controller;

    public StartFrame(ClientController controller) {
        super("Welcome!");
        setLayout(null);
        this.controller = controller;
        this.setSize(350, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.GRAY);

        StartPanel startPanel = new StartPanel(this, controller);
        this.add(startPanel);

        setVisible(true);
    }

    public String chooseProfilePic(){
        return controller.chooseProfilePic();
    }

}