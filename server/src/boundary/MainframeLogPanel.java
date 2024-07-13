package boundary;

import controller.ServerController;

import javax.swing.*;
import java.util.ArrayList;

public class MainframeLogPanel extends JFrame {
    private ServerController controller;
    private TimePanel panel;
    private LogPanel logPanel;

    public MainframeLogPanel(ServerController serverController){
        super("Server log");
        this.controller = serverController;
        this.setResizable(false);
        this.setSize(610, 520);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        logPanel = new LogPanel(this);
        panel = new TimePanel(this);

        this.add(logPanel);
        this.add(panel);

        this.setVisible(true);
    }

    public boolean checkDateValidity(String fromTimeText, String toTimeText, String pattern) {
        return controller.checkDateValidity(fromTimeText, toTimeText, pattern);
    }

    public void displayTraficLog(String fromTimeText, String toTimeText) {
        ArrayList<String> logList = controller.getTraficLogInterval(fromTimeText, toTimeText);

        if(logList != null){
            logPanel.displayLog(logList);
        }

    }
}
