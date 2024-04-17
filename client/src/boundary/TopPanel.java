package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.*;


public class TopPanel extends JPanel {
    private ClientController controller;
    private JLabel recieverIcon;
    private JLabel senderIcon;

    public TopPanel(ClientController clientController){
        setLayout(null);
        this.controller = clientController;
        this.setBounds(0, 0, 580, 120);
        this.setBackground(Color.LIGHT_GRAY);

        displayUserImage("shared_classes/defaultPic.jpg", "shared_classes/defaultPic.jpg");

        this.setVisible(true);
    }

    public void displayUserImage(String recieverPic, String picture){
        ImageIcon recieverOldSize = new ImageIcon(recieverPic);
        Image recieverImage = recieverOldSize.getImage();
        Image recieverChangedSize = recieverImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        ImageIcon reciever = new ImageIcon(recieverChangedSize);

        recieverIcon = new JLabel(reciever);
        recieverIcon.setBounds(25, 0, 60, 60);

        ImageIcon senderOldSize = new ImageIcon(picture);
        Image senderImage = senderOldSize.getImage();
        Image SenderChangedSize = senderImage.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        ImageIcon sender = new ImageIcon(SenderChangedSize);

        senderIcon = new JLabel(sender);
        senderIcon.setBounds(480, 0, 60, 60);

        this.add(recieverIcon);
        this.add(senderIcon);
    }

}
