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


    // Anger mönstret med x antal siffror på år, månad, dag
    private String pattern = "\\d{4} - \\d{2} - \\d{2}";

    public TimePanel(MainframeLogPanel mainframe){
        this.setLayout(null);
        this.mainframe = mainframe;
        this.setBounds(0, 0, 500, 100);
        Color color = new Color(171, 196, 199);
        this.setBackground(color);
        setUp();
        addActionListeners();
    }

    public void setUp(){
        JLabel label1 = new JLabel("To see log traffic between two intervals,");
        JLabel label2 = new JLabel("specify time with the following pattern 'yyyy - mm - dd'");
        JLabel from = new JLabel("From: ");
        JLabel to = new JLabel("To: ");

        label1.setBounds(120, 5, 250, 25);
        label2.setBounds(90, 20, 320, 25);

        this.add(label1);
        this.add(label2);

        from.setBounds(30, 60, 100, 25);
        fromTime = new JTextField();
        fromTime.setBounds(80, 60, 80, 25);
        this.add(fromTime);
        this.add(from);

        to.setBounds(240, 60, 50, 25);
        toTime = new JTextField();
        toTime.setBounds(290, 60, 80, 25);
        this.add(toTime);
        this.add(to);

        display = new JButton("Display");
        display.setBounds(400, 80, 90, 20);
        display.setBackground(Color.WHITE);
        this.add(display);
    }

    public void addActionListeners(){
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromTimeText = fromTime.getText();
                String toTimeText = toTime.getText();

                boolean valid = mainframe.checkDateValidity(fromTimeText, toTimeText, pattern);

                if(!valid){
                    JOptionPane.showMessageDialog(null, "Invalid date");
                } else {
                    mainframe.manageTraficLogInterval(fromTimeText, toTimeText);
                }


            }
        });
    }
}
