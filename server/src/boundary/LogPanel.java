package boundary;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;

public class LogPanel extends JPanel {
    private MainframeLogPanel mainframe;
    private JTextPane outPutText;
    private JScrollPane scroll;
    private StyledDocument document;
    private Color color;

    public LogPanel(MainframeLogPanel mainframe){
        this.setLayout(null);
        this.mainframe = mainframe;
        this.setBounds(0, 100, 600, 400);
        color = new Color(163, 200, 203);
        this.setBackground(color);

        createTextPane();

        document = outPutText.getStyledDocument();
    }

    public void displayLog(ArrayList<String> logList){
        this.remove(scroll);
        this.remove(outPutText);
        createTextPane();
        repaint();
        for(String log : logList){
            try {
                document.insertString(document.getLength(), log + " \n", null);
                document.insertString(document.getLength(), "" + " \n", null);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createTextPane(){
        outPutText = new JTextPane();
        outPutText.setContentType("text/html");
        outPutText.setEditable(false);
        outPutText.setBounds(0, 0, 600, 450);
        outPutText.setBackground(color);
        this.add(outPutText);
        this.scroll = new JScrollPane(outPutText);
        scroll.setBounds(0, 0, 600, 400);
        this.add(scroll);

        document = outPutText.getStyledDocument();
    }
}
