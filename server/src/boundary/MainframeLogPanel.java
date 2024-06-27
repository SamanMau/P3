package boundary;

import controller.ServerController;

import javax.swing.*;

public class MainframeLogPanel extends JFrame {
    private ServerController controller;

    public MainframeLogPanel(ServerController serverController){
        super("Server log");
        this.controller = serverController;
        this.setResizable(false);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        LogPanel logPanel = new LogPanel(this);
        TimePanel panel = new TimePanel(this);

        this.add(logPanel);
        this.add(panel);

        this.setVisible(true);
    }

    public void sendLogMessage(String message){
    }


    public boolean checkDateValidity(String fromTimeText, String toTimeText, String pattern) {
        return controller.checkDateValidity(fromTimeText, toTimeText, pattern);
    }

    public void manageTraficLogInterval(String fromTimeText, String toTimeText) {
        controller.manageTraficLogInterval(fromTimeText, toTimeText);
    }
}
