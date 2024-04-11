package boundary;

import javax.swing.*;
import java.awt.*;

public class MainFrameStart extends JFrame {
    private JPanel namePanel;
    private JLabel title;

    public MainFrameStart() {
        super("Welcome!");
        this.setSize(300, 300); // You can adjust the size as needed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.GRAY);

        NamePanel namePanel = new NamePanel(this);
        this.add(namePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        MainFrameStart mainFrameStart = new MainFrameStart();
    }
}