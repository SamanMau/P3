package boundary;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private StartFrame startFrame;
    private LoginPanel loginPanel;

    public LoginFrame(StartFrame startFrame){
        super("Login");
        setLayout(null);
        this.startFrame = startFrame;
        this.setSize(350, 300);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.GRAY);

        loginPanel = new LoginPanel(this);
        this.add(loginPanel);
        setVisible(true);
    }

    public void logIn(String name){
        startFrame.logIn(name);
        this.dispose();
    }
}
