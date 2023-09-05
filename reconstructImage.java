import java.awt.*;
import java.awt.image.BufferedImage;

public class reconstructImage {
     double factor;
     int adder;
    private int[][] pixels;

    BufferedImage bufferedImage;

    public reconstructImage(int[][] pixels, double factor, int adder){
//        super("Reconstructed Image" + colorFactor);
        this.pixels = pixels;
        this.factor = factor;
        this.adder = adder;

//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(pixels[0].length, pixels.length);
//        setVisible(true);

        BufferedImage bufferedImage = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_ARGB);
        this.bufferedImage = bufferedImage;
    }

    public void setAdder(int adder) {
        this.adder = adder;

//        for (int row = 0; row < pixels.length; row++) {
//            for (int colon = 0; colon < pixels[row].length; colon++) {
//                bufferedImage.setRGB(colon, row, (int) ((pixels[row][colon]*this.factor)+this.adder));
//            }
//        }
    }

    public void setFactor(double factor) {
        if(factor<0)
        this.factor = 1/Math.abs(factor);
        else if(factor == 0)
            this.factor = 1;
        else
        this.factor = factor;

//        for (int row = 0; row < pixels.length; row++) {
//            for (int colon = 0; colon < pixels[row].length; colon++) {
//                bufferedImage.setRGB(colon, row, (int) ((pixels[row][colon]*this.factor)+this.adder));
//            }
//        }
    }

    public void setRedFactor(double factor){

        if(factor<0)
            factor = 1/(-factor);
        else if(factor == 0)
            factor=1;

        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                Color tempColor = new Color(pixels[row][colon]);
                int red = (int)(tempColor.getRed()*factor);
                int green = tempColor.getGreen();
                int blue = tempColor.getBlue();

                pixels[row][colon] = new Color(red, green, blue).getRGB();
            }
        }

    }

    public void setRedAdder(double adder){

        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                Color tempColor = new Color(pixels[row][colon]);
                int red = (int) (tempColor.getRed() + adder);
                int green = tempColor.getGreen();
                int blue = tempColor.getBlue();

                pixels[row][colon] = new Color(red, green, blue).getRGB();
            }
        }

    }

    public void setGreenFactor(double factor){

        if(factor<0)
            factor = 1/(-factor);
        else if(factor == 0)
            factor=1;

        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                Color tempColor = new Color(pixels[row][colon]);
                int red = tempColor.getRed();
                int green = (int) (tempColor.getGreen()*factor);
                int blue = tempColor.getBlue();

                pixels[row][colon] = new Color(red, green, blue).getRGB();
            }
        }

    }

    public void setGreenAdder(double adder){

        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                Color tempColor = new Color(pixels[row][colon]);
                int red = tempColor.getRed();
                int green = (int) (tempColor.getGreen()+adder);
                int blue = tempColor.getBlue();

                pixels[row][colon] = new Color(red, green, blue).getRGB();
            }
        }

    }

    public void setBlueFactor(double factor){

        if(factor<0)
            factor = 1/(-factor);
        else if(factor == 0)
            factor=1;

        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                Color tempColor = new Color(pixels[row][colon]);
                int red = tempColor.getRed();
                int green = tempColor.getGreen();
                int blue = (int) (tempColor.getBlue()*factor);

                pixels[row][colon] = new Color(red, green, blue).getRGB();
            }
        }

    }

    public void setBlueAdder(double adder){

        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                Color tempColor = new Color(pixels[row][colon]);
                int red = tempColor.getRed();
                int green = tempColor.getGreen();
                int blue = (int) (tempColor.getBlue()+adder);

                pixels[row][colon] = new Color(red, green, blue).getRGB();
            }
        }

    }

    public BufferedImage getBufferedImage() {
        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                bufferedImage.setRGB(colon, row, (int) ((pixels[row][colon]*this.factor)+this.adder));
            }
        }
        return bufferedImage;
    }

    public int[][] getPixels() {
        BufferedImage localBuffer = getBufferedImage();
        int[][] pixs = new int[pixels.length][pixels[0].length];
        for (int row = 0; row < pixels.length; row++) {
            for (int colon = 0; colon < pixels[row].length; colon++) {
                pixs[row][colon] = localBuffer.getRGB(colon, row);
            }
        }
        return pixs;
    }
}
