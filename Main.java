
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
    static BufferedImage img;
    static BufferStrategy bufferStrategyCenter;
    static reconstructImage reconstructImage;
    static JFrame frame;


    static JSlider sliderFactor, redFactorSlider, greenFactorSlider, blueFactorSlider;
    static JSlider sliderAdder, redAdderSlider, greenAdderSlider, blueAdderSlider;


    static PlotRBGGraph plotRBGGraph;
//    static PlotGraph redGraph, greenGraph, blueGraph;

    private static byte[] picTopixels(BufferedImage img) throws IOException {

        byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        return pixels;
    }

    private static int[] bytesToints(byte[] b){
        int[] out = new int[b.length];

        for (int i = 0; i < b.length; i++) {
            out[i] = (int) b[i];
        }

        return out;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        frame = new JFrame("ImageAnalyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());


        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
//                super.componentResized(e);
                try {
                    drawReconstructedImage(reconstructImage, bufferStrategyCenter);
                }catch (Exception ignored){}

            }
        });
        Canvas c = new Canvas();
        frame.add(c, BorderLayout.CENTER);
        c.createBufferStrategy(1);
        bufferStrategyCenter = c.getBufferStrategy();


//        redGraph = new PlotGraph(null, Color.red);
//        greenGraph = new PlotGraph(null, Color.green);
//        blueGraph = new PlotGraph(null, Color.blue);


        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
        settings.setBackground(Color.darkGray);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.setBackground(Color.darkGray);
        settings.add(buttons);
        JButton btnSave = new JButton("Save");

        JButton btnOpen = new JButton("Open");
        btnOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Images", "jpg", "gif", "tif", "png", "jpeg");

            fileChooser.setFileFilter(filter);
            fileChooser.showOpenDialog(null);

            try {
                img = ImageIO.read(fileChooser.getSelectedFile());
            } catch (IOException e1) {
                e1.printStackTrace();

            }


            int pixColorIntegerFormat2D[][] = convertTo2DWithoutUsingGetRGB(img);
            reconstructImage = new reconstructImage(pixColorIntegerFormat2D, 1, 0);

            int[] pixsD1 = D2toD1Array(pixColorIntegerFormat2D);
            int[] rs = removeZeroesFromArray(redPixels(pixsD1));
            int[] gs = removeZeroesFromArray(greenPixels(pixsD1));
            int[] bs = removeZeroesFromArray(bluePixels(pixsD1));

            plotRBGGraph = new PlotRBGGraph(rs, gs, bs);
            drawReconstructedImage(reconstructImage, bufferStrategyCenter);
        });
        buttons.add(btnOpen);

        JButton btnGraph = new JButton("Graph");
        btnGraph.addActionListener(e->plotRBGGraph.repaint());
        buttons.add(btnGraph);

        frame.add(settings, BorderLayout.EAST);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(Color.DARK_GRAY);
        JPanel factorSlider = new JPanel();
        factorSlider.setBackground(Color.LIGHT_GRAY);
        factorSlider.setLayout(new BoxLayout(factorSlider, BoxLayout.Y_AXIS));
        factorSlider.add(new JLabel("Main factor:"));
        factorSlider.add(sliderFactor = new JSlider(JSlider.HORIZONTAL, -100, 100, 0));
        sliderFactor.setBackground(Color.GRAY);

        controls.add(factorSlider);

        JPanel adderSlider = new JPanel();
        adderSlider.setBackground(Color.LIGHT_GRAY);
        adderSlider.setLayout(new BoxLayout(adderSlider, BoxLayout.Y_AXIS));
        adderSlider.add(new JLabel("Main adder:"));
        adderSlider.add(sliderAdder = new JSlider(JSlider.HORIZONTAL, -20000, 20000, 0));
        sliderAdder.setBackground(Color.GRAY);
        controls.add(adderSlider);

        ButtonHandler buttonHandler = new ButtonHandler();
        sliderAdder.addChangeListener(buttonHandler);
        sliderFactor.addChangeListener(buttonHandler);

        controls.add(Box.createVerticalStrut(15));





//        JPanel RedFactorSlider = new JPanel();
//        RedFactorSlider.setBackground(Color.PINK);
//        RedFactorSlider.setLayout(new BoxLayout(RedFactorSlider, BoxLayout.Y_AXIS));
//        RedFactorSlider.add(new JLabel("Red factor:"));
//        RedFactorSlider.add(redFactorSlider = new JSlider(JSlider.HORIZONTAL, -20, 20, 0));
//        redFactorSlider.setBackground(Color.GRAY);
//        controls.add(RedFactorSlider);
//
//        JPanel RedAdderSlider = new JPanel();
//        RedAdderSlider.setBackground(Color.PINK);
//        RedAdderSlider.setLayout(new BoxLayout(RedAdderSlider, BoxLayout.Y_AXIS));
//        RedAdderSlider.add(new JLabel("Red adder:"));
//        RedAdderSlider.add(redAdderSlider = new JSlider(JSlider.HORIZONTAL, -1000, 1000, 0));
//        redAdderSlider.setBackground(Color.GRAY);
//        controls.add(RedAdderSlider);
//
//
//        JPanel GreenFactorSlider = new JPanel();
//        GreenFactorSlider.setBackground(Color.GREEN);
//        GreenFactorSlider.setLayout(new BoxLayout(GreenFactorSlider, BoxLayout.Y_AXIS));
//        GreenFactorSlider.add(new JLabel("Green factor:"));
//        GreenFactorSlider.add(greenFactorSlider = new JSlider(JSlider.HORIZONTAL, -20, 20, 0));
//        greenFactorSlider.setBackground(Color.GRAY);
//        controls.add(GreenFactorSlider);
//
//        JPanel GreenAdderSlider = new JPanel();
//        GreenAdderSlider.setBackground(Color.GREEN);
//        GreenAdderSlider.setLayout(new BoxLayout(GreenAdderSlider, BoxLayout.Y_AXIS));
//        GreenAdderSlider.add(new JLabel("Green adder:"));
//        GreenAdderSlider.add(greenAdderSlider = new JSlider(JSlider.HORIZONTAL, -1000, 1000, 0));
//        greenAdderSlider.setBackground(Color.GRAY);
//        controls.add(GreenAdderSlider);
//
//
//        JPanel BlueFactorSlider = new JPanel();
//        BlueFactorSlider.setBackground(Color.CYAN);
//        BlueFactorSlider.setLayout(new BoxLayout(BlueFactorSlider, BoxLayout.Y_AXIS));
//        BlueFactorSlider.add(new JLabel("Blue factor:"));
//        BlueFactorSlider.add(blueFactorSlider = new JSlider(JSlider.HORIZONTAL, -20, 20, 0));
//        blueFactorSlider.setBackground(Color.GRAY);
//        controls.add(BlueFactorSlider);
//
//        JPanel BlueAdderSlider = new JPanel();
//        BlueAdderSlider.setBackground(Color.CYAN);
//        BlueAdderSlider.setLayout(new BoxLayout(BlueAdderSlider, BoxLayout.Y_AXIS));
//        BlueAdderSlider.add(new JLabel("Blue adder:"));
//        BlueAdderSlider.add(blueAdderSlider = new JSlider(JSlider.HORIZONTAL, -1000, 1000, 0));
//        greenAdderSlider.setBackground(Color.GRAY);
//        controls.add(BlueAdderSlider);
//
//        redAdderSlider.addChangeListener(buttonHandler);
//        redAdderSlider.addChangeListener(buttonHandler);
//
//        greenAdderSlider.addChangeListener(buttonHandler);
//        greenFactorSlider.addChangeListener(buttonHandler);
//
//        blueAdderSlider.addChangeListener(buttonHandler);
//        blueFactorSlider.addChangeListener(buttonHandler);
//
//
//

        settings.add(controls);


        frame.setVisible(true);








//        reconstructImage reconstructImage =
//        JPanel imagePlane = new JPanel();
//        imagePlane.set



//        for (byte b :
//                pixels) {
//            System.out.println(b);
//            System.out.println(Integer.toBinaryString(b));
//        }

//        for (byte p :
//                pixels) {
//            String bi = Integer.toBinaryString(p);
//            String hex = Integer.toHexString(p);
//
//            if(!hex.equals("ffffffff"))
//            System.out.println("Binary: " + bi + "\t\t\t" + "Hex: " + hex);
//        }

//        PictureFrame pictureFrame = new PictureFrame(img);
//        reconstructImage reconstructImage = new reconstructImage(pixColorIntegerFormat2D, 1);
//        reconstructImage reconstructImageq = new reconstructImage(pixColorIntegerFormat2D, 10);
//        reconstructImage reconstructImageqq = new reconstructImage(pixColorIntegerFormat2D, 100);

//        for (int p :
//                redPixels(pixColorIntegerFormat1D)) {
//            if(p!=0)
//            System.out.println(p);
//        }



//        PlotGraph plotGraphr = new PlotGraph(removeZeroesFromArray(redPixels(pixColorIntegerFormat1D)), Color.RED);
//        PlotGraph plotGraphg = new PlotGraph(removeZeroesFromArray(greenPixels(pixColorIntegerFormat1D)), Color.GREEN);
//        PlotGraph plotGraphb = new PlotGraph(removeZeroesFromArray(bluePixels(pixColorIntegerFormat1D)), Color.BLUE);


    }

//    private static void regraphColorGraphs(int[][] pixs, PlotGraph r, PlotGraph g, PlotGraph b){
//        int[] array = D2toD1Array(pixs);
//
////        r.setPoints(removeZeroesFromArray(redPixels(array)));
////        g.setPoints(removeZeroesFromArray(greenPixels(array)));
////        b.setPoints(removeZeroesFromArray(bluePixels(array)));
//        r = new PlotGraph(removeZeroesFromArray(redPixels(array)), Color.red);
//        g = new PlotGraph(removeZeroesFromArray(greenPixels(array)), Color.green);
//        b = new PlotGraph(removeZeroesFromArray(bluePixels(array)), Color.blue);
//
////        frame.invalidate();
////        frame.validate();
////        frame.repaint();
//    }

    public static void updateColorGrapgs(PlotRBGGraph plotRBGGraph, int[][] pixs2D)
    {
        int[] pixsD1 = D2toD1Array(pixs2D);
        int[] rs = removeZeroesFromArray(redPixels(pixsD1));
        int[] gs = removeZeroesFromArray(greenPixels(pixsD1));
        int[] bs = removeZeroesFromArray(bluePixels(pixsD1));

        plotRBGGraph.newGraph(rs, gs, bs);

    }

    public static void drawReconstructedImage(reconstructImage reconstructImage, BufferStrategy bufferStrategy){
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        System.out.println(reconstructImage.factor + "            " + reconstructImage.adder);
        g.drawImage(reconstructImage.getBufferedImage(), 0, 0, frame.getWidth(), frame.getHeight(), null);
        bufferStrategy.show();
    }

    private static int[] D2toD1Array(int[][] d3Array){
        int[] out = new int[d3Array.length*d3Array[0].length];
        for(int i = 0; i < d3Array.length; i ++) {
            for(int s = 0; s < d3Array[i].length; s ++) {
                out[i+s] = d3Array[i][s];
            }
        }
        return out;
    }

    private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }


    private static int[] redPixels(int[] ColorInts)
    {
        int[] redPixels = new int[ColorInts.length];
        for (int i = 0; i < redPixels.length; i++) {
            redPixels[i] = new Color(ColorInts[i]).getRed();
        }
        return redPixels;
    }

    private static int[] greenPixels(int[] ColorInts)
    {
        int[] greenPixels = new int[ColorInts.length];
        for (int i = 0; i < greenPixels.length; i++) {
            greenPixels[i] = new Color(ColorInts[i]).getGreen();
        }
        return greenPixels;
    }

    private static int[] bluePixels(int[] ColorInts)
    {
        int[] bluePixels = new int[ColorInts.length];
        for (int i = 0; i < bluePixels.length; i++) {
            bluePixels[i] = new Color(ColorInts[i]).getBlue();
        }
        return bluePixels;
    }

    private static int[] removeZeroesFromArray(int[] array){
        ArrayList<Integer> outList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if(array[i]!=0)
                outList.add(array[i]);
        }

        //Integer array to int array
        int[] out = new int[outList.size()];
        for (int i = 0; i < out.length; i++) {
            out[i] = outList.get(i);
        }
        return out;
    }

}

class ButtonHandler implements ChangeListener{

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == Main.sliderAdder){
            Main.reconstructImage.setAdder(Main.sliderAdder.getValue());
        }
        else if(e.getSource() == Main.sliderFactor){
            Main.reconstructImage.setFactor(Main.sliderFactor.getValue());
        }/*
        else if(e.getSource() == Main.redAdderSlider){
            Main.reconstructImage.setRedAdder(Main.redAdderSlider.getValue());
        }
        else if(e.getSource() == Main.redFactorSlider){
            Main.reconstructImage.setRedFactor(Main.redFactorSlider.getValue());
        }
        else if(e.getSource() == Main.greenAdderSlider){
            Main.reconstructImage.setGreenAdder(Main.greenAdderSlider.getValue());
        }
        else if(e.getSource() == Main.greenFactorSlider){
            Main.reconstructImage.setGreenFactor(Main.greenFactorSlider.getValue());
        }
        else if(e.getSource() == Main.blueAdderSlider){
            Main.reconstructImage.setBlueAdder(Main.blueAdderSlider.getValue());
        }
        else if(e.getSource() == Main.blueFactorSlider){
            Main.reconstructImage.setBlueFactor(Main.blueFactorSlider.getValue());
        }*/


        int[][] pixs = Main.reconstructImage.getPixels();
        Main.updateColorGrapgs(Main.plotRBGGraph, pixs);

        Main.drawReconstructedImage(Main.reconstructImage, Main.bufferStrategyCenter);

        Main.plotRBGGraph.repaint();
    }
}
