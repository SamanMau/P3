package boundary;

import javax.swing.*;

public class AddFriendFrame extends JFrame {

    public AddFriendFrame(MessageFrame messageFrame){
        setLayout(null);
        this.setSize(400, 400);
        AddFriendPanel addFriendPanel = new AddFriendPanel(this);
        this.add(addFriendPanel);
        this.setVisible(true);
    }
}
