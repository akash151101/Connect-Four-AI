package comp124graphics;

import sun.java2d.SunGraphics2D;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;


public class Arc extends GraphicsObject implements Colorable{

    private Arc2D.Double shape;
    private Paint strokeColor;
    private BasicStroke stroke;

    private double x; // upper left x position
    private double y; // upper left y position
    private double width;
    private double height;
    private double startAngle;
    private double sweepAngle;
    private int type;

    public Arc(double x, double y, double width, double height, double startAngle, double sweepAngle) {
        shape = new Arc2D.Double(x, y, width, height, startAngle, sweepAngle, Arc2D.OPEN);
        strokeColor = Color.black;
        stroke = new BasicStroke(1.0f);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
        this.type = Arc2D.OPEN;
    }

    @Override
    public void draw(Graphics2D gc){
        //Color originalColor = gc.getColor();
        gc.setStroke(stroke);
        //gc.setColor(strokeColor);
        gc.setPaint(strokeColor);
        gc.draw(shape);
        //gc.setColor(originalColor); // set the color back to the original
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


    public float getStrokeWidth(){
        return stroke.getLineWidth();
    }


    public void setStrokeWidth(float width){
        stroke = new BasicStroke(width);
        changed();
    }

    @Override
    public void setPosition(double x, double y) {
        shape.setArc(x, y, width, height, startAngle, sweepAngle, type);
        this.x = x;
        this.y = y;
        changed();
    }


    @Override
    public void move(double dx, double dy) {
        shape.setArc(x + dx, y + dy, width, height, startAngle, sweepAngle, type);
        this.x = x + dx;
        this.y = y + dy;
        changed();
    }

    @Override
    public Point.Double getPosition() {
        return new Point.Double(shape.getX(), shape.getY());
    }

    @Override
    public boolean testHit(double x, double y, Graphics2D gc) {
        int devScale = (int) ((SunGraphics2D)gc).getSurfaceData().getDefaultScaleX();
        AffineTransform transform = new AffineTransform();
        transform.setToScale(devScale, devScale);
        Point.Double point = new Point2D.Double(x, y);
        Point.Double transformedPoint = new Point2D.Double(x, y);
        transform.transform(point, transformedPoint);
        java.awt.Rectangle test = new java.awt.Rectangle((int)Math.round(transformedPoint.getX()),
                (int)Math.round(transformedPoint.getY()), 1*devScale,1*devScale);
        if (gc.hit(test, shape, false || gc.hit(test, shape, true))){
            return true;
        }
        return false;

    }

    @Override
    public Rectangle getBounds() {
       return shape.getBounds();
    }
}
