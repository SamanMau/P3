package boundary;

import controller.ClientController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MessagePanel extends JPanel {
    private JTextField enterText;
    private ClientController controller;
    private JLabel textInstruction;
    private JButton enterPic;
    private JTextPane textPane;

    public MessagePanel(ClientController clientController){
        this.setBounds(0, 0, 580, 500);
        setLayout(null);
        this.controller = clientController;

        textInstruction = new JLabel("Type: ");
        textInstruction.setBounds(3, 433, 40, 30);
        this.add(textInstruction);

    //    createImageButton();
        this.add(enterPic);

        /*
        Likasom JTextArea kan en JTextPane skicka meddelanden i form av String.
        Men det som är speciellt med JTextPane är att den kan också displaya bilder,
        det är just därför vi ska använda den.
         */
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setBounds(0, 0, 485, 433);
        textPane.setBackground(Color.GRAY);
        this.add(textPane);


        enterText = new JTextField();
        enterText.setBounds(36, 434, 426, 30);
        enterText.setBackground(Color.white);
        this.add(enterText);

        this.setVisible(true);

        enterText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = enterText.getText();
                String name = "Saman";
                displayText(message);
                enterText.setText("");
            }
        });
    }

    /*
    public void createImageButton(){
        enterPic = new JButton("Send image");
        enterPic.setBounds(460, 433, 101, 30);

        enterPic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openFileManager();
            }
        });
    }

     */


    public void displayText(String text){
        textPane.setText(text);
        textPane.setText("\n");
    }

    /*
    public void displayImage(File image){
        System.out.println(image);
        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(image));
            textPane.insertIcon(imageIcon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        textPane.setText("\n");
    }

     */

}
