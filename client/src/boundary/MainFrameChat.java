package boundary;

import javax.swing.*;
import java.awt.*;

public class MainFrameChat extends JFrame{
    private JTextArea chat = new JTextArea();

    public MainFrameChat(){
        super("Chat");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setEditable(false);
        chat.setPreferredSize(new Dimension(400, 400));
        BottomPanel bp = new BottomPanel();
        this.add(bp, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void sendChatMessage(String message){
        chat.append(message);
        chat.append("\n");
    }

    public static void main(String[] args) {
        MainFrameChat mainFrameChat = new MainFrameChat();
    }

}
