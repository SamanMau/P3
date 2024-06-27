package boundary;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {
    private MainframeLogPanel mainframe;
    private JTextPane outPutText;
    private JScrollPane scroll;

    public LogPanel(MainframeLogPanel mainframe){
        this.setLayout(null);
        this.mainframe = mainframe;
        this.setBounds(0, 100, 500, 400);
        Color color = new Color(163, 200, 203);
        this.setBackground(color);

        outPutText = new JTextPane();
        outPutText.setContentType("text/html");
        outPutText.setEditable(false);
        outPutText.setBounds(0, 0, 500, 450);
        outPutText.setBackground(color);

        this.add(outPutText);
        this.scroll = new JScrollPane(outPutText);
        scroll.setBounds(0, 0, 550, 400);
        this.add(scroll);

    }
}
