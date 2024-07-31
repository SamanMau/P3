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

    /*
    A JTextPane was used to display both text and images.
    setContentType decides what type of content will be
    displayed. We use both text and HTML. HTML is used
    for different text formats.
    We use a document object because it gives us more
    control over our textpane.
     */
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

        outPutText = new JTextPane();
        outPutText.setContentType("text/html");
        outPutText.setEditable(false);
        outPutText.setBounds(0, 0, 480, 433);
        outPutText.setBackground(color);

        this.add(outPutText);
        this.scroll = new JScrollPane(outPutText);
        scroll.setBounds(0, 0, 550, 400);
        this.add(scroll);

        document = outPutText.getStyledDocument();

        inputText = new JTextField();
        inputText.setBounds(40, 434, 350, 30);
        inputText.setBackground(Color.white);

        manageTextPane();

        this.add(inputText);

        this.setVisible(true);
    }

    /*
    The first if statement checks that there are people
    who will get the specific message. The second if
    statement checks if the message contains both
    text and a picture. The first else if
    statement checks if the message only contains
    text, and no image. The last if statement
    checks if only a picture was sent.
     */
    public void manageTextPane(){
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
    }

    public void createButtons(){
        enterPic = new JButton("Send image");
        enterPic.setBackground(Color.WHITE);
        enterPic.setBounds(390, 433, 116, 30);

        enterPic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageFrame.openFileManager();
            }
        });
    }

    /*
    "insertString" is used to display contenct. "doc.getLength" gets the size
    of the content. The last parameter "null" is used because we do not want
    to specify any specific format on the text, for instance "italics".
    "clearContactButtons" removes the chosen friends.
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

        messageFrame.clearContactButtons();
       // messageFrame.removeChosenFriend();
    }


    /*
    "Style" is used to specify the format on a picture, "jpg". We create a style
    and put it in our StyledDocument object. "StyleConstants.setIcon" associates
    our chosen imageicon to our made style. To display the picture, we use
    "insertString()" method and send in "style" as last argument.
    The last insertString method is used to give extra space, if the last
    parameter in the last "insertString" was "style", then the picture would
    have been displayed twice.
     */
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

        messageFrame.clearContactButtons();
    }

    /*
    "ImageIO.read(file)" reads the image from the file. Then, we
    retrieve the image object from the "oldSize" imageIcon.
    Then we change the size of the image (changedSize), and
    use this object to create our imageIcon.

     */
    public void displayImage(File file, String username){
        ArrayList<String> contacts = messageFrame.getFriends();

        if(contacts != null){
            try {

                ImageIcon oldSize = new ImageIcon(ImageIO.read(file));
                Image image = oldSize.getImage();
                Image changedSize = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

                ImageIcon newSize = new ImageIcon(changedSize);

                this.image = newSize;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        messageFrame.clearContactButtons();

    }

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

        messageFrame.clearContactButtons();
    }
}

