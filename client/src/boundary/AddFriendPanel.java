package boundary;

import javax.swing.*;

public class AddFriendPanel extends JPanel {
    private AddFriendFrame addFriendFrame;

    public AddFriendPanel(AddFriendFrame addFriendFrame){
        this.addFriendFrame = addFriendFrame;

        this.setBounds(0, 0, 400, 400);

        setUp();
    }

    public void setUp(){

        JLabel jLabel = new JLabel("TODO: Visa alla aktiva kontakter i form av jbuttons");
        JLabel jLabel1 = new JLabel("Man ska kunna trycka på en knapp, alltså en person, ");
        JLabel jLabel2 = new JLabel("och lägga till denna person som vän i kontakter.");

        jLabel.setBounds(30, 20, 200, 30);
        jLabel1.setBounds(30, 60, 200, 30);
        jLabel2.setBounds(30, 120, 200, 30);

        this.add(jLabel);
        this.add(jLabel1);
        this.add(jLabel2);
    }

}
