package comp124graphics;

import sun.java2d.SunGraphics2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;


public class Line extends GraphicsObject implements Colorable {

    private Line2D.Double shape;
    private Paint strokeColor;
    private BasicStroke stroke;

    public Line(double x1, double y1, double x2, double y2){
        shape = new Line2D.Double(x1, y1, x2, y2);
        strokeColor = Color.black;
        stroke = new BasicStroke(1.0f);
    }

 
    public void draw(Graphics2D gc){
        Paint originalColor = gc.getPaint();
        gc.setStroke(stroke);
        gc.setPaint(strokeColor);
        gc.draw(shape);
        gc.setPaint(originalColor); // set the color back to the original
    }

    /**
     * Gets the stroke color used to draw the shape outline
     */
    @Override
    public Paint getStrokeColor() {
        return strokeColor;
    }


    @Override
    public void setStrokeColor(Paint strokeColor) {
        this.strokeColor = strokeColor;
        changed();
    }


    public float getStrokeWidth(){
        return stroke.getLineWidth();
    }


    public void setStrokeWidth(float width){
        stroke = new BasicStroke(width);
        changed();
    }


    public double getX1(){
        return shape.getX1();
    }


    public double getY1(){
        return shape.getY1();
    }


    public double getX2(){
        return shape.getX2();
    }


    public double getY2(){
        return shape.getY2();
    }

    public void setStartPosition(double x, double y){
        shape.setLine(x, y, shape.getX2(), shape.getY2());
        changed();
    }

    public void setEndPosition(double x, double y){
        shape.setLine(shape.getX1(), shape.getY1(), x, y);
        changed();
    }

    public void setPosition(double x, double y){
        shape.setLine(x, y, (x-shape.getX1())+shape.getX2(), (y - shape.getY1())+shape.getY2());
        changed();
    }

    public Point.Double getPosition(){
        return new Point.Double(shape.getX1(), shape.getY1());
    }

    public void move(double dx, double dy){
        shape.setLine(shape.getX1() + dx, shape.getY1() + dy, shape.getX2()+dx, shape.getY2()+dy);
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
        if (gc.hit(test, shape, false || gc.hit(test, shape, true))){
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object other){
        if (other != null && other instanceof Line){
            Line otherShape = (Line)other;
            if (this.shape.equals(otherShape.shape)){
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString(){
        return "A line at position ("+getX1()+", "+getY1()+") and ("+getX2()+", "+getY2()+")";
    }


    public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle((int)getX1(), (int)getY1(), (int)Math.abs(getX2()-getX1()), (int)Math.abs(getY1()-getY2()));
    }
}
