package boundary;

import controller.ClientController;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MessagePanel extends JPanel {
    private JTextField inputText;
    private ClientController controller;
    private JLabel textInstruction;
    private JButton enterPic;
    private JTextPane outPutText;
    private JScrollPane scroll;

    public MessagePanel(ClientController clientController){
        this.setBounds(0, 0, 580, 500);
        setLayout(null);
        this.controller = clientController;

        textInstruction = new JLabel("Type: ");
        textInstruction.setBounds(3, 433, 40, 30);
        this.add(textInstruction);

        createImageButton();
        this.add(enterPic);

        /*
        Likasom JTextArea kan en JTextPane skicka meddelanden i form av String.
        Men det som är speciellt med JTextPane är att den kan också displaya bilder,
        det är just därför vi ska använda den.
         */
        outPutText = new JTextPane();
        outPutText.setContentType("text/html");
        outPutText.setEditable(false);
        outPutText.setBounds(0, 0, 485, 433);
        outPutText.setBackground(Color.white);
        this.add(outPutText);

        this.scroll = new JScrollPane(outPutText);
        scroll.setBackground(Color.red);
        scroll.setBounds(0, 0, 550, 400);
        this.add(scroll);


        inputText = new JTextField();
        inputText.setBounds(36, 434, 426, 30);
        inputText.setBackground(Color.white);

        inputText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputText.getText();
                displayText(outPutText, message);
                inputText.setText("");
            }
        });

        this.add(inputText);

        this.setVisible(true);
    }




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


    //behöver förklaring.
    public void displayText(JTextPane textPane, String newText) {
        Document document = textPane.getDocument();
        try {
            document.insertString(document.getLength(),newText+"\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void displayImage(File image){
        System.out.println(image);
        try {
            ImageIcon imageIcon = new ImageIcon(ImageIO.read(image));
            outPutText.insertIcon(imageIcon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        displayText(outPutText, "");
    }

}
