package comp124graphics;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;


public class GraphicsGroup extends GraphicsObject implements GraphicsObserver {

 
    private ConcurrentLinkedDeque<GraphicsObject> gObjects;


    private double x;


    private double y;

    private java.awt.Rectangle bounds;


    private BufferedImage imgBuffer;
    private Graphics2D subCanvas;

    public GraphicsGroup(double x, double y){
        this.x = x;
        this.y = y;
        gObjects = new ConcurrentLinkedDeque<GraphicsObject>();
        bounds = new java.awt.Rectangle(0,0,-1, -1);
    }


     public GraphicsGroup() {
        this(0.0,0.0);
    }

    public void add(GraphicsObject gObject){
        gObject.addObserver(this);
        gObjects.add(gObject);
        //java.awt.Rectangle objBounds = gObject.getBounds();
        bounds = bounds.union(gObject.getBounds());
        changed();
    }

 
    public void add(GraphicsObject gObject, double x, double y){
        gObject.setPosition(x, y);
        this.add(gObject);
    }


    public void remove(GraphicsObject gObject){
        gObject.removeObserver(this);
        gObjects.remove(gObject);
        changed();
    }


    public void removeAll(){
        Iterator<GraphicsObject> it = gObjects.iterator();
        while(it.hasNext()){
            GraphicsObject obj = it.next();
            obj.removeObserver(this);
            it.remove();
        }
        changed();
    }


    public GraphicsObject getElementAt(double x, double y){
        Iterator<GraphicsObject> it = gObjects.descendingIterator();
        while(it.hasNext()){
            GraphicsObject obj = it.next();
            if (obj.testHit(x-this.x, y-this.y, subCanvas)){
                return obj;
            }
        }
        return null;
    }


    public void draw(Graphics2D gc){
        if (bounds.isEmpty()) {
            return;
        }
        imgBuffer = new BufferedImage((int)Math.ceil(bounds.getX()+bounds.getWidth()), (int)Math.ceil(bounds.getY()+bounds.getHeight()), BufferedImage.TYPE_4BYTE_ABGR);
        subCanvas = imgBuffer.createGraphics();
        enableAntialiasing();
        subCanvas.setBackground(new Color(1, 1, 1, 0));
        subCanvas.clearRect(0, 0, (int)Math.ceil(bounds.getX()+bounds.getWidth()), (int)Math.ceil(bounds.getY()+bounds.getHeight()));

        for(GraphicsObject obj: gObjects){
            obj.draw(subCanvas);
        }
        // We need to draw on the sub canvas so that getElement at works properly

        //gc.drawImage(imgBuffer, (int)x, (int)y, null);

        gc.translate(x, y);
        for(GraphicsObject obj : gObjects){
            obj.draw(gc);
        }
        gc.translate(-x, -y);

    }

    public double getX(){
        return x;
    }


    public double getY(){
        return y;
    }

    public double getWidth(){
        return bounds.getWidth();
    }

    public double getHeight(){
        return bounds.getHeight();
    }

    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
        changed();
    }

    public Point.Double getPosition(){
        return new Point2D.Double(x, y);
    }


    public void move(double dx, double dy){
        this.x += dx;
        this.y += dy;
        changed();
    }

 
    public boolean testHit(double x, double y, Graphics2D gc){
        java.awt.Rectangle test = new java.awt.Rectangle((int)Math.round(x), (int)Math.round(y), 1,1);
        for(GraphicsObject obj : gObjects) {
            if (obj.testHit(x-this.x, y-this.y, gc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other){
        if (other != null && other instanceof GraphicsGroup){
            GraphicsGroup otherShape = (GraphicsGroup) other;
            if (this.x == otherShape.x && this.y == otherShape.y && this.gObjects.equals(otherShape.gObjects)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "A graphics group at position ("+getX()+", "+getY()+") with width="+getWidth()+" and height="+getHeight();
    }


    public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle((int) Math.ceil(this.x + bounds.getX()), (int) Math.ceil(this.y + bounds.getY()), (int) Math.ceil(bounds.getWidth()), (int) Math.ceil(bounds.getHeight()));
    }

    public Iterator<GraphicsObject> iterator(){
        return gObjects.iterator();
    }


    public void graphicChanged(GraphicsObject changedObject){
        updateBounds();
        changed();
    }

    private void enableAntialiasing() {
        subCanvas.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        subCanvas.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        subCanvas.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
    }

    private void updateBounds(){
        java.awt.Rectangle newBounds = null;
        for(GraphicsObject gObject: gObjects) {
            if (newBounds != null) {
                newBounds = newBounds.union(gObject.getBounds());
            } else {
                java.awt.Rectangle objBounds = gObject.getBounds();
                if (objBounds != null) {
                    newBounds = new java.awt.Rectangle((int) objBounds.getX(), (int) objBounds.getY(), (int) objBounds.getWidth(), (int) objBounds.getHeight());
                }
            }
        }
        bounds = newBounds;
    }
}
