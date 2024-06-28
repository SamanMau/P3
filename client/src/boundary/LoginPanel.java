package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField enterName;
    private LoginFrame loginFrame;

    private JLabel enterNameInstruction;

    public LoginPanel(LoginFrame loginFrame){
        this.setLayout(null);
        this.setBounds(0, 0, 350, 300);
        this.loginFrame = loginFrame;
        Color color = new Color(163, 200, 203);
        this.setBackground(color);

        setUp();

        this.add(enterName);
        this.add(enterNameInstruction);

    }

    public void setUp(){
        enterNameInstruction = new JLabel("Enter name: ");
        enterNameInstruction.setBounds(15, 80, 100, 20);

        enterName = new JTextField();
        enterName.setBounds(100, 80, 180, 20);

        enterName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = enterName.getText();
                loginFrame.logIn(name);
            }
        });
    }
}
