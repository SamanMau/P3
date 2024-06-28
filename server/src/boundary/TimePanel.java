package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimePanel extends JPanel {
    private MainframeLogPanel mainframe;
    private JTextField fromTime;
    private JButton display;
    private JTextField toTime;

    public TimePanel(MainframeLogPanel mainframe){
        this.setLayout(null);
        this.mainframe = mainframe;
        this.setBounds(0, 0, 600, 100);
        Color color = new Color(171, 196, 199);
        this.setBackground(color);
        setUp();
        addActionListeners();
    }

    public void setUp(){
        JLabel label1 = new JLabel("To see log traffic between two intervals,");
        JLabel label2 = new JLabel("specify time with the following pattern 'yyyy-MM-dd HH:mm'");
        JLabel from = new JLabel("From: ");
        JLabel to = new JLabel("To: ");

        label1.setBounds(170, 5, 250, 25);
        label2.setBounds(120, 20, 355, 25);

        this.add(label1);
        this.add(label2);

        from.setBounds(90, 60, 100, 25);
        fromTime = new JTextField();
        fromTime.setBounds(135, 60, 110, 25);
        this.add(fromTime);
        this.add(from);

        to.setBounds(300, 60, 50, 25);
        toTime = new JTextField();
        toTime.setBounds(330, 60, 110, 25);
        this.add(toTime);
        this.add(to);

        display = new JButton("Display");
        display.setBounds(500, 80, 90, 20);
        display.setBackground(Color.WHITE);
        this.add(display);
    }

    public void addActionListeners(){
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromTimeText = fromTime.getText();
                String toTimeText = toTime.getText();

                boolean valid = mainframe.checkDateValidity(fromTimeText, toTimeText, "yyyy-MM-dd HH:mm");

                if(!valid){
                    JOptionPane.showMessageDialog(null, "Invalid date");
                } else {
                   mainframe.manageTraficLogInterval(fromTimeText, toTimeText);
                }
            }
        });
    }
}
