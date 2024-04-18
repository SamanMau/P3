package boundary;

import javax.swing.*;
import java.awt.*;

public class MainframeLogPanel extends JFrame {
    private JTextArea log = new JTextArea();

    public MainframeLogPanel(){
        super("Server log");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        log.setEditable(false);
        log.setPreferredSize(new Dimension(400, 400));
        this.add(log, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void sendLogMessage(String message){
        log.append(message);
        log.append("\n");
    }


}
