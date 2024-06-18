package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    private JLabel title;
    private JTextField enterName;
    private JButton sendName;
    private StartFrame startFrame;

    private String pictureFile;

    private JButton button;
    private ClientController clientController;

    private JButton login;
    private JButton register;

    private JButton newUser;

    public StartPanel(StartFrame startFrame, ClientController clientController){
        setLayout(null);
        this.setBounds(0, 0, 350, 300);
        this.clientController = clientController;
        this.startFrame = startFrame;

        setUp();

        addActionListener();

        this.add(login);
        this.add(register);
        this.add(newUser);
    }

    public void setUp(){

        login = new JButton("Log in");
        login.setBounds(95, 80, 120, 20);
        login.setBackground(Color.WHITE);

        register = new JButton("Register");
        register.setBounds(95, 140, 120, 20);
        register.setBackground(Color.WHITE);

        newUser = new JButton("Create new connection");
        newUser.setBounds(75, 180, 170, 20);
        newUser.setBackground(Color.WHITE);

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

        newUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startFrame.newConnection();
            }
        });
    }
}
