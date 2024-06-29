package shared_classes.user;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class User implements Serializable {
    private String userName;
    private ImageIcon userImage;
    private static final long serialVersionUID = 1L;

    private byte[] imageByteArray;

    public User(String userName, ImageIcon userImage){
        convertImageToByte(userImage);

        this.userName = userName;
    }

    public void convertImageToByte(ImageIcon imageIcon){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(imageIcon);

            this.imageByteArray = baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int hashCode() {
        return userName.hashCode();
    }
    
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof User)
            return userName.equals(((User)obj).getUserName());
        return false;
    }

    //deserialiserar bilden så att man kan läsa den.
    public ImageIcon getUserImage() {
        try {

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(imageByteArray));
            ImageIcon imageIcon = (ImageIcon) ois.readObject();
            return imageIcon;

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return this.userName + this.userImage;
    }
}
