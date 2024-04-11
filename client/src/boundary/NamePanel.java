package boundary;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NamePanel extends JPanel {
    private JLabel title;
    private JTextField enterName;
    private JButton send;
    private MainFrameStart ms;

    public NamePanel(MainFrameStart ms){
        this.ms = ms;
        title = new JLabel("Name:");
        enterName = new JTextField(15);
        send = new JButton("Send");

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = enterName.getText();
                System.out.println(name + " Klass: Namepanel");
            }
        });

        this.add(title);
        this.add(enterName);
        this.add(send);
        this.setVisible(true);
    }

}
