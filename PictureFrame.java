import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PictureFrame extends JFrame {
    BufferedImage image;

    public PictureFrame(BufferedImage img){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(img!=null)
            setSize(img.getWidth(), img.getHeight());
        else
            setSize(100, 100);

        image = img;
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(image!=null)
            g.drawImage(image, 0, 0, null);
        setVisible(true);

    }
}
