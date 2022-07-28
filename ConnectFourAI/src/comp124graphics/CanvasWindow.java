package comp124graphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;


public class CanvasWindow extends JPanel implements GraphicsObserver{

 
    protected JFrame windowFrame;


    private ConcurrentLinkedDeque<GraphicsObject> gObjects;

    public CanvasWindow(String title, int windowWidth, int windowHeight){
        setPreferredSize (new Dimension(windowWidth, windowHeight));
        setBackground (Color.white);

        gObjects = new ConcurrentLinkedDeque<GraphicsObject>();

        windowFrame = new JFrame (title);
        windowFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        windowFrame.getContentPane().add(this);
        windowFrame.pack();
        windowFrame.setVisible(true);
    }


    public void paintComponent (Graphics page) {
        super.paintComponent (page);

        Graphics2D gc = (Graphics2D)page;

        enableAntialiasing(gc);

        // Iterate over all of the graphical objects and draw them.
        Iterator<GraphicsObject> it = gObjects.iterator();
        while (it.hasNext()){
            it.next().draw(gc);
        }
    }

    public void add(GraphicsObject gObject){
        gObject.addObserver(this);
        gObjects.add(gObject);
        repaint();
    }

    public void add(GraphicsObject gObject, double x, double y){
        gObject.setPosition(x, y);
        this.add(gObject);
//        gObject.addObserver(this);
//        gObjects.add(gObject);
//        repaint();
    }

    public void remove(GraphicsObject gObject){
        gObject.removeObserver(this);
        gObjects.remove(gObject);
        repaint();
    }

    /**
     * Removes all of the objects currently drawn on the canvas
     */
    public void removeAll(){
        Iterator<GraphicsObject> it = gObjects.iterator();
        while(it.hasNext()){
            GraphicsObject obj = it.next();
            obj.removeObserver(this);
            it.remove();
        }
        repaint();
    }


    public void pause(long milliseconds){
        try{
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // Empty
        }
    }


    public void pause(double milliseconds){
        try {
            int millis = (int) milliseconds;
            int nanos = (int) Math.round((milliseconds - millis) * 1000000);
            Thread.sleep(millis, nanos);
        } catch (InterruptedException ex) {
			/* Empty */
        }
    }

    public GraphicsObject getElementAt(double x, double y){
        Graphics2D gc = (Graphics2D)this.getGraphics();
        Iterator<GraphicsObject> it = gObjects.descendingIterator();
        while(it.hasNext()){
            GraphicsObject obj = it.next();
            //if (obj.testHit(x, y, gc)){
            if (isGGat(obj, x, y) || obj.testHit(x, y, gc)){
                return obj;
            }
        }
        return null;
    }

    /*
       Checks to see if clicked within a GraphicsGroup object and returns true if so
     */
    private boolean isGGat(GraphicsObject obj, double x, double y) {
        if (obj instanceof GraphicsGroup) {
            java.awt.Rectangle rect = obj.getBounds();
            if (rect.getX() <= x && x<= (rect.getX() + rect.getWidth())) {
                if (rect.getY() <= y && y <= (rect.getY()+rect.getHeight())) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Enables antialiasing on the drawn shapes.
     * @param gc
     */
    private void enableAntialiasing(Graphics2D gc) {
        gc.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gc.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        gc.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
    }


    public void graphicChanged(GraphicsObject changedObject){
        repaint();
    }

    public void screenShot(String filename)
    {
        BufferedImage bImg = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D cg = bImg.createGraphics();
        this.paintAll(cg);
        try {
            if (ImageIO.write(bImg, "png", new File(filename)))
            {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public JFrame getWindowFrame(){
        return windowFrame;
    }

}


