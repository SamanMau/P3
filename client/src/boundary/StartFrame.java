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
        this.controller = controller;
        this.setSize(300, 300); // You can adjust the size as needed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.GRAY);

        NamePanel namePanel = new NamePanel(this, controller);
        this.add(namePanel);

        setVisible(true);
    }

    public void close(){
        this.dispose();
        controller.ChatFrame();
    }

}