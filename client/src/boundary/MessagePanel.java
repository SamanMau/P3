package boundary;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MessagePanel extends JPanel {
    private JTextField inputText;
    private JLabel textInstruction;
    private JButton enterPic;
    private JTextPane outPutText;
    private JScrollPane scroll;

    private MessageFrame messageFrame;

    private StyledDocument document;

    private ImageIcon image;

    public MessagePanel(MessageFrame messageFrame){
        setLayout(null);

        this.setBounds(0, 60, 500, 500);
        this.messageFrame = messageFrame;
        Color color = new Color(163, 200, 203);
        this.setBackground(color);

        textInstruction = new JLabel("Type: ");
        textInstruction.setBounds(3, 433, 40, 30);
        this.add(textInstruction);

        createButtons();
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
        outPutText.setBackground(color);

        this.add(outPutText);
        this.scroll = new JScrollPane(outPutText);
        scroll.setBounds(0, 0, 550, 400);
        this.add(scroll);

        /*
        Den hämtar styleddocument objektet kopplat till vår textpane.
        styleddocument låter en ha större kontroll över ens GUI.
         */
        document = outPutText.getStyledDocument();



        inputText = new JTextField();
        inputText.setBounds(36, 434, 365, 30);
        inputText.setBackground(Color.white);

        inputText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> contacts = messageFrame.getFriends();
                String message = inputText.getText();

                if(contacts != null && !(contacts.isEmpty())){

                    if((message != null) && (!message.isEmpty()) && image != null){
                        messageFrame.managePictureWithText(image, contacts, message);
                        displayPictureWithText(image, messageFrame.getUserName(), messageFrame.getReceiverTime(), message);
                        inputText.setText("");

                        image = null;

                    } else if(message != null && (!message.isEmpty()) && image == null){
                        String name = messageFrame.getUserName();
                        displayText(message, name, messageFrame.getReceiverTime());
                        inputText.setText("");
                        messageFrame.manageMessage(message, contacts);
                    }

                    else if(image != null){
                        messageFrame.managePicture(image, contacts);
                        displayFormattedImage(image, messageFrame.getUserName(), messageFrame.getReceiverTime());
                        image = null;
                    }
                }

                messageFrame.removeChosenFriend();

            }
        });

        this.add(inputText);

        this.setVisible(true);
    }




    public void createButtons(){
        enterPic = new JButton("Send image");
        enterPic.setBackground(Color.WHITE);
        enterPic.setBounds(400, 433, 101, 30);

        enterPic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageFrame.openFileManager();
            }
        });
    }

    /*
    "textPane.getStyledDocument" hämtar dokument objektet kopplat till vår textPane
    (outputText). StyledDocument interfacet används för att lagra bland annat text.
    Den ger en större kotrnoll över ens GUI. "doc.getLength" hämtar storleken
    på innehållet (text eller bild) som har lagrats i "doc" objektet.
    "null" har lagts eftersom vi inte vill specifiera någon specifik format på texten,
    exempelvis "italics".
     */
    public void displayText(String message, String username, String receiverTime) {
        ArrayList<String> contacts = messageFrame.getFriends();

        if(contacts != null){
            try {
                String formattedText = username + " : " + message + " " + receiverTime + "\n";

                document.insertString(document.getLength(), formattedText, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public void displayPictureWithText(ImageIcon imageIcon, String userName, String receiverTime, String message){
        ArrayList<String> contacts = messageFrame.getFriends();

        if(contacts != null){
            try{
                Style style = document.addStyle("jpg", null);

                String formattedText = userName + " : " + message + " " + receiverTime + "\n";
                document.insertString(document.getLength(), formattedText, null);

                StyleConstants.setIcon(style, imageIcon);
                document.insertString(document.getLength(),  "\n", style);
                document.insertString(document.getLength(), "\n", null); //ger extra mellanrum.
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void displayImage(File image, String username){
        ArrayList<String> contacts = messageFrame.getFriends();

        if(contacts != null){
            try {
                ImageIcon oldSize = new ImageIcon(ImageIO.read(image));
                Image thisImage = oldSize.getImage();
                Image changedSize = thisImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

                ImageIcon newSize = new ImageIcon(changedSize);

                this.image = newSize;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /*
    Style används för att bland annat specifiera format på bilder. I detta fall laddar
    vi upp "jpg" bilder. Vi skapar en style och lägger in det i vår styledDocument objekt.
    "StyleConstants.setIcon();" associerar vår imageIcon med en specifik format, i detta fall
    jpg. doc.insertString(doc.getLength(),  "\n", style); lägger till bilden i GUI:t och gör
    append, alltså ny rad. vi har null på addStyle("jpg", null) eftersom vi ska skapa en ny stil.
     */
    public void displayFormattedImage(ImageIcon imageIcon, String userName, String receiverTime) {

        try{
            Style style = document.addStyle("jpg", null);

            StyleConstants.setIcon(style, imageIcon);
            document.insertString(document.getLength(), userName + " : " + receiverTime + "\n", null);
            document.insertString(document.getLength(),  "\n", style);
            document.insertString(document.getLength(), "\n", null); //ger extra mellanrum.
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}

