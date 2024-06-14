package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {
    private StartFrame startFrame;

    private JTextField enterName;

    private JLabel enterNameInstruction;

    private JButton choosePic;

    private RegisterFrame registerFrame;

    private JButton createAcc;

    private ImageIcon userImage;

    public RegisterPanel(StartFrame startFrame, RegisterFrame registerFrame){
        this.setLayout(null);
        this.setBounds(0, 0, 350, 300);
        this.registerFrame = registerFrame;
        this.startFrame = startFrame;

        setUp();
        addActionListeners();

        this.add(enterName);
        this.add(enterNameInstruction);
        this.add(choosePic);
        this.add(createAcc);
    }

    public void setUp(){
        enterName = new JTextField();
        enterName.setBounds(100, 80, 180, 20);

        enterNameInstruction = new JLabel("Enter name: ");
        enterNameInstruction.setBounds(15, 80, 100, 20);

        choosePic = new JButton("Choose profile picture");
        choosePic.setBackground(Color.WHITE);
        choosePic.setBounds(100, 40, 180, 20);

        createAcc = new JButton("Create account");
        createAcc.setBackground(Color.WHITE);
        createAcc.setBounds(120, 120, 120, 20);
    }

    public void addActionListeners(){

        choosePic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userImage = startFrame.chooseProfilePic();
            }
        });

        createAcc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = enterName.getText();
                if(userImage != null){
                    registerFrame.createAccount(name, userImage);
                    registerFrame.openChatFrame(userImage);

                } else {
                    JOptionPane.showMessageDialog(null, "You need to choose an image");
                }
            }
        });
    }

}
