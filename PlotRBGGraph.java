import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class PlotRBGGraph extends JFrame {
    int[] reds;
    int[] blues;
    int[] greens;

    boolean EnableScale = true;
    int scaleY, scaleX = 1;

    Color colorOfPoint;

    Graphics2D g;

    public PlotRBGGraph(int[] rs, int[] gs, int[] bs){
        super("Color Graph");
        setResizable(false);
        setSize(255, 255*3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        reds =  rs;
        greens = gs;
        blues = bs;
    }

    @Override
    public void paint(Graphics gq) {
        super.paint(gq);
        g = (Graphics2D)gq;
        try {
            plotPoints(g);
        }catch (Exception ignored){}
    }

    public void newGraph(int[] rs, int[] gs, int[] bs)
    {
        reds = rs;
        greens = gs;
        blues = bs;
//        repaint();
    }


    private void plotPoints(Graphics2D g){
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, getWidth(), getHeight());

        ArrayList<Stack> stacksOfRedValues = stackValues(reds);
        ArrayList<Stack> stacksOfGreenValues = stackValues(greens);
        ArrayList<Stack> stacksOfBlueValues = stackValues(blues);

        Stack.findDenom();

        int Yposition = getHeight();

        //go through each stacks

        //blues
        g.setColor(Color.cyan);
        for (int i = 0; i < stacksOfBlueValues.size(); i++) {
            //draw each point in stack
            int highestBar = 0;
            for (int j = 0; j < stacksOfBlueValues.get(i).getN(); j++) {
                if(j<255){
                    g.drawRect(stacksOfBlueValues.get(i).value, Yposition, 1, 1);
                    Yposition--;
                }
            }
            Yposition+=stacksOfBlueValues.get(i).getN();
        }
        Yposition-=255;
        //greens
        g.setColor(Color.GREEN);
        for (int i = 0; i < stacksOfGreenValues.size(); i++) {
            //draw each point in stack
            for (int j = 0; j < stacksOfGreenValues.get(i).getN(); j++) {
                g.drawRect(stacksOfGreenValues.get(i).value, Yposition, 1, 1);
                Yposition--;
            }
            Yposition+=stacksOfGreenValues.get(i).getN();
        }
        Yposition-=255;
        //reds
        g.setColor(Color.pink);
        for (int i = 0; i < stacksOfRedValues.size(); i++) {
            //draw each point in stack
            for (int j = 0; j < stacksOfRedValues.get(i).getN(); j++) {
                g.drawRect(stacksOfRedValues.get(i).value, Yposition, 1, 1);
                Yposition--;
            }
            Yposition+=stacksOfRedValues.get(i).getN();
        }
    }

    private ArrayList<Stack> stackValues(int[] values){
        ArrayList<Stack> stacks = new ArrayList<>();

        for (int i = 0; i < values.length; i++) {
            boolean noValue = true;
            //check if these if array with selected value
            for (Stack s :
                    stacks) {
                if (s.value == values[i]) {
                    s.addVal();
                    noValue = false;
                }
            }
            if(noValue)
            stacks.add(new Stack(values[i], getHeight()));
        }
        return stacks;
    }

    private static int relativeVal(int val, int highesVal, int maxPromVal){
        return val*maxPromVal/highesVal;
    }

    private int[] multiplyValues(int[] values, double factor){
        int[] newValues = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            newValues[i] = (int) (values[i] * factor);
        }
        return newValues;
    }

    private int findHighestVal(int[] values){
        int[] newValues = multiplyValues(values, -1);
        int highest = 0;
        for (int v :
                newValues) {
            if (v > highest)
                highest = v;
        }

        return highest;

    }
}









class Stack {
    int n;
    int value;

    static int highestN = 0;
    static int denom;

    static int frameHeight;

    public Stack(int val, int height){
        frameHeight = height;
        value = val;
        n++;
    }

    public void addVal(){
        n++;
        if(n>highestN)
            highestN = n;

    }

    public static  void findDenom(){
            int tempDen = denom;
            if(tempDen==0)
                tempDen = 1;

            if(highestN/tempDen>frameHeight)
                proportionForAllStacks();
    }

    private static void proportionForAllStacks(){
        int tempN = highestN;

        while(tempN>frameHeight){
            denom++;
            tempN/=denom;
        }
    }

    public int getN() {

        if(denom == 0)
            return n;
        return n/1;

    }


}
