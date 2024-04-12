package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NamePanel extends JPanel {
    private JLabel title;
    private JTextField enterName;
    private JButton send;
    private StartFrame ms;
    private ClientController clientController;

    public NamePanel(StartFrame ms, ClientController clientController){
        this.clientController = clientController;
        this.ms = ms;
        title = new JLabel("Name:");
        enterName = new JTextField(15);
        send = new JButton("Send");

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = enterName.getText();
                clientController.sendName(name);
                ms.close();
            }
        });

        this.add(title);
        this.add(enterName);
        this.add(send);
        this.setVisible(true);
    }
}
