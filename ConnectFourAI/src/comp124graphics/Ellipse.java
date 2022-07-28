package comp124graphics;

import sun.java2d.SunGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;


public class Ellipse extends GraphicsObject implements Colorable, FillColorable{

    private Ellipse2D.Double shape;
    private Paint fillColor;
    private Paint strokeColor;
    private boolean isFilled;
    private boolean isStroked;
    private BasicStroke stroke;

    public Ellipse(double x, double y, double width, double height){
        shape = new Ellipse2D.Double(x, y, width, height);
        fillColor = Color.black;
        strokeColor = Color.black;
        stroke = new BasicStroke(1.0f);
        isFilled = false;
        isStroked = true;
    }


    public void draw(Graphics2D gc){
        Paint originalColor = gc.getPaint();
        if (isFilled){
            gc.setPaint(fillColor);
            gc.fill(shape);
        }
        if (isStroked) {
            gc.setStroke(stroke);
            gc.setPaint(strokeColor);
            gc.draw(shape);
        }
        gc.setPaint(originalColor); // set the color back to the original
    }


    @Override
    public Paint getFillColor() {
        return fillColor;
    }


    @Override
    public void setFillColor(Paint fillColor) {
        this.fillColor = fillColor;
        changed();
    }


    @Override
    public Paint getStrokeColor() {
        return strokeColor;
    }


    @Override
    public void setStrokeColor(Paint strokeColor) {
        this.strokeColor = strokeColor;
        changed();
    }

  
    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
        changed();
    }

 
    public boolean isStroked() {
        return isStroked;
    }


    public void setStroked(boolean stroked) {
        isStroked = stroked;
        changed();
    }


    public float getStrokeWidth(){
        return stroke.getLineWidth();
    }


    public void setStrokeWidth(float width){
        stroke = new BasicStroke(width);
        changed();
    }

   
    public double getX(){
        return shape.getX();
    }


    public double getY(){
        return shape.getY();
    }


    public double getWidth(){
        return shape.getWidth();
    }


    public double getHeight(){
        return shape.getHeight();
    }

 
    public void setPosition(double x, double y){
        shape.setFrame(x, y, shape.getWidth(), shape.getHeight());
        changed();
    }

    public Point.Double getPosition(){
        return new Point.Double(shape.getX(), shape.getY());
    }


    public void setWidthAndHeight(double width, double height){
        shape.setFrame(shape.getX(), shape.getY(), width, height);
        changed();
    }


    public void move(double dx, double dy){
        shape.setFrame(shape.getX() + dx, shape.getY() + dy, shape.getWidth(), shape.getHeight());
        changed();
    }


    public boolean testHit(double x, double y, Graphics2D gc){
        int devScale = (int) ((SunGraphics2D)gc).getSurfaceData().getDefaultScaleX();
        AffineTransform transform = new AffineTransform();
        transform.setToScale(devScale, devScale);
        Point.Double point = new Point2D.Double(x, y);
        Point.Double transformedPoint = new Point2D.Double(x, y);
        transform.transform(point, transformedPoint);
        java.awt.Rectangle test = new java.awt.Rectangle((int)Math.round(transformedPoint.getX()), (int)Math.round(transformedPoint.getY()), 1*devScale,1*devScale);
        boolean hit = false;
        if (isFilled && gc.hit(test, shape, false)){
            hit = true;
        }
        if(isStroked && gc.hit(test, shape, true)){
            hit = true;
        }
        return hit;
    }


    @Override
    public boolean equals(Object other){
        if (other != null && other instanceof Ellipse){
            Ellipse otherShape = (Ellipse) other;
            if (this.shape.equals(otherShape.shape)){
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString(){
        return "An ellipse at position ("+getX()+", "+getY()+") with width="+getWidth()+" and height="+getHeight();
    }


    public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }

}
