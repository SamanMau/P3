package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends JPanel {
    private JLabel recieverIcon;

    private ImageIcon pictureFile;

    private MessageFrame messageFrame;

    private JLabel userName;

    private JButton logOut;

    public TopPanel(ImageIcon pictureFile, MessageFrame messageFrame){
        setLayout(null);
        this.pictureFile = pictureFile;
        this.messageFrame = messageFrame;
        this.setBounds(0, 0, 500, 120);

        recieverIcon = new JLabel();
        this.setBackground(Color.LIGHT_GRAY);

        setUp(pictureFile);

        this.add(recieverIcon);
        this.add(userName);
        this.add(logOut);

        this.setVisible(true);
    }

    public void setUp(ImageIcon recieverPic){
        ImageIcon imageIcon = recieverPic;

        Image imge = imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        imageIcon.setImage(imge);

        recieverIcon.setIcon(imageIcon);
        recieverIcon.setBounds(20, 0, 60, 60);

        String name = messageFrame.getUserName();

        userName = new JLabel(name);
        userName.setBounds(90, 35, 100, 20);

        logOut = new JButton("Log out");
        logOut.setBackground(Color.WHITE);
        logOut.setBounds(400, 0, 100, 20);
        logOut.setFocusPainted(false);

        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageFrame.logOut();
            }
        });

    }

}
