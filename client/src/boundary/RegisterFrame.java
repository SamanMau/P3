package boundary;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private StartFrame startFrame;
    private RegisterPanel registerPanel;

    public RegisterFrame(StartFrame startFrame){
        super("Register");
        setLayout(null);
        this.startFrame = startFrame;
        this.setSize(350, 300);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.GRAY);

        registerPanel = new RegisterPanel(startFrame, this);
        this.add(registerPanel);
        setVisible(true);
    }


    public void createAccount(String name, ImageIcon imageIcon){
        startFrame.createAccount(name, imageIcon);
    }

    public void openChatFrame(ImageIcon userImage) {
        startFrame.openChatFrame(userImage);
        this.dispose();
    }
}
