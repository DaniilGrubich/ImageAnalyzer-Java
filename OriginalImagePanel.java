import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Observer;

public class OriginalImagePanel extends JPanel {
    BufferedImage bufferedImage;
    Image image;
    public OriginalImagePanel(BufferedImage img){
        setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
        bufferedImage = img;

        image = bufferedImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_AREA_AVERAGING);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, (getHeight() - image.getHeight(null)) / 2, Color.DARK_GRAY, null);
    }
}
