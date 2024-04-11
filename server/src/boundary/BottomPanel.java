package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BottomPanel extends JPanel {
    private JTextField jTextField;
    private JButton sendButton;

    public BottomPanel(){
        this.setPreferredSize(new Dimension(100, 100));
        jTextField = new JTextField(20);
        jTextField.setBackground(Color.red);
        createButton();
        this.setVisible(true);

        jTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("message: " + jTextField.getText());
            }
        });
    }

    public void createButton(){
        sendButton = new JButton("Send message");
        sendButton.setBackground(Color.gray);
        sendButton.setBounds(10, 10, 10, 10);
        sendButton.setFocusPainted(false);
    }

}
