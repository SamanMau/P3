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

    public MessagePanel(ClientController clientController, MessageFrame messageFrame){
        setLayout(null);

        this.setBounds(0, 60, 500, 500);
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
        inputText.setBounds(36, 434, 365, 30);
        inputText.setBackground(Color.white);

        inputText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputText.getText();
                String name = clientController.getUserName();
                displayText(outPutText, message, name);
                inputText.setText("");
                clientController.manageMessage(message);
            }
        });

        this.add(inputText);

        this.setVisible(true);
    }




    public void createImageButton(){
        enterPic = new JButton("Send image");
        enterPic.setBackground(Color.WHITE);
        enterPic.setBounds(400, 433, 101, 30);

        enterPic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openFileManager();
            }
        });
    }

    public JTextPane getTextPane(){
        return outPutText;
    }


    /*
    "textPane.getDocument" hämtar dokument objektet kopplat till vår textPane
    (outputText). Document klassen används för att lagra text. "document.getLength"
    hämtar storleken på texten som har lagrats i "document" objektet. message representerar
    den nya texten och vi skriver "\n" så att de näst kommande texten placeras på en ny rad.
    "null" har lagts eftersom vi inte vill specifiera någon specifik format på texten,
    exempelvis "italics".
     */
    public void displayText(JTextPane textPane, String message, String username) {
        Document document;
        document = textPane.getDocument();
        try {
            document.insertString(document.getLength(),username + " : " + message+"\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void displayImage(File image, String username){
        System.out.println(image);
        try {
            ImageIcon oldSize = new ImageIcon(ImageIO.read(image));
            Image thisImage = oldSize.getImage();
            Image changedSize = thisImage.getScaledInstance(150, 150, Image.SCALE_DEFAULT);

            ImageIcon newSize = new ImageIcon(changedSize);

            outPutText.insertIcon(newSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        displayText(outPutText, "", username);
    }


}
