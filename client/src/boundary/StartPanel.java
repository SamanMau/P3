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

    public StartPanel(StartFrame startFrame, ClientController clientController){
        setLayout(null);
        this.setBounds(0, 0, 350, 300);
        this.clientController = clientController;
        this.startFrame = startFrame;

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
        /*
        sendName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = enterName.getText();
                if(pictureFile != null){
                    clientController.sendName(name, pictureFile);
                    clientController.openChatFrame(pictureFile);
                } else {
                    JOptionPane.showMessageDialog(null, "You need to choose an image");
                }

            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pictureFile = startFrame.chooseProfilePic();
            }
        });

         */

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
