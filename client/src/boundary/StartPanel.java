package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StartPanel extends JPanel {
    private JLabel title;
    private JTextField enterName;
    private JButton sendName;
    private StartFrame startFrame;

    private String pictureFile;

    private JButton button;
    private ClientController clientController;

    public StartPanel(StartFrame startFrame, ClientController clientController){
        setLayout(null);
        this.setBounds(0, 0, 350, 300);
        this.clientController = clientController;
        this.startFrame = startFrame;
        title = new JLabel("Name:");
        title.setBounds(10, 80, 60, 20);

        enterName = new JTextField(15);
        enterName.setBounds(60, 80, 200, 25);

        sendName = new JButton("Send");
        sendName.setBounds(140, 130, 70, 25);

        button = new JButton("Choose image");
        button.setBounds(120, 30, 120, 25);

        addActionListener();

        this.add(title);
        this.add(enterName);
        this.add(sendName);
        this.add(button);
    }

    public void addActionListener(){
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
    }
}
