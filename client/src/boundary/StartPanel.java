package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    private StartFrame startFrame;
    private JButton login;
    private JButton register;

    public StartPanel(StartFrame startFrame){
        setLayout(null);
        this.setBounds(0, 0, 350, 300);
        this.startFrame = startFrame;
        Color color = new Color(163, 200, 203);
        this.setBackground(color);

        setUp();

        addActionListener();

        this.add(login);
        this.add(register);
    }

    public void setUp(){

        login = new JButton("Log in");
        login.setBounds(95, 80, 120, 20);
        login.setBackground(Color.WHITE);

        register = new JButton("Register");
        register.setBounds(95, 140, 120, 20);
        register.setBackground(Color.WHITE);

    }

    public void addActionListener(){

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.openLoginFrame();
            }
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.openRegisterFrame();
            }
        });

    }

}
