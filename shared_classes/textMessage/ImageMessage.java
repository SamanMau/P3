package shared_classes.textMessage;

import javax.swing.*;
import java.awt.*;

public class ImageMessage {
    Image image = new ImageIcon().getImage();

    public ImageMessage(Image image){
         this.image = image;
    }

    public ImageIcon sendImage(String path){
        ImageIcon ic = new ImageIcon(path);
        System.out.println(path);
        return ic;
    }

    public Image getImage() {
        return image;
    }
}
