package boundary;

import controller.ClientController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartPanel extends JPanel {
    private JLabel title;
    private JTextField enterName;
    private JButton sendName;
    private StartFrame ms;
    private ClientController clientController;

    public StartPanel(StartFrame ms, ClientController clientController){
        this.clientController = clientController;
        this.ms = ms;
        title = new JLabel("Name:");
        enterName = new JTextField(15);
        sendName = new JButton("Send");

        sendName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = enterName.getText();
                clientController.sendName(name);
                ms.close();
            }
        });

        this.add(title);
        this.add(enterName);
        this.add(sendName);
        this.setVisible(true);
    }
}
