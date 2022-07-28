package comp124graphics;

import sun.java2d.SunGraphics2D;

import java.awt.geom.AffineTransform;
import java.util.List;
import java.awt.*;
import java.awt.geom.Point2D;

import static java.lang.Math.*;

public class Polygon extends GraphicsObject implements Colorable, FillColorable {

    private java.awt.Polygon shape;
    private Paint fillColor;
    private Paint strokeColor;
    private boolean isFilled;
    private boolean isStroked;
    private BasicStroke stroke;

    private double x;
    private double y;
    private double width;
    private double height;

    /**
     * Create a polygon from a given list of points
     *
     */
    public Polygon(List<Point2D.Double> points) {
        if (points == null) {
            throw new NullPointerException("list of points must be non-null");
        } else if (points.size()<3) {
            throw new IllegalArgumentException("Not enough points to make a polygon");
        }
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];

        x = Double.POSITIVE_INFINITY;
        y = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        int i = 0;
        for(Point2D.Double point : points) {
            x = min(x, point.getX());
            y = min(y, point.getY());
            maxX = max(maxX, point.getX());
            maxY = max(maxY, point.getY());
            xPoints[i] = (int) Math.round(point.getX());
            yPoints[i] = (int) Math.round(point.getY());
            i++;
        }
        width = maxX - x;
        height = maxY - y;

        shape = new java.awt.Polygon(xPoints, yPoints, points.size());
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

    /**
     * Gets the color for the filled in polygon
     */
    @Override
    public Paint getFillColor() {
        return fillColor;
    }

    /**
     * Set the fill color to fillColor
     */
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
        return x;
    }

    public double getY(){
        return y;
    }


    public double getWidth(){
        return width;
    }

    public double getHeight(){
        return height;
    }

    /**
     * Sets the shape's position to x, y
     */
    public void setPosition(double x, double y){
        double dx = x-getX();
        double dy = y-getY();
        move(dx, dy);
    }

    public Point.Double getPosition(){
        return new Point2D.Double(getX(), getY());
    }

    /**
     * Move the shape from its current x, y position by dx and dy.
     */
    public void move(double dx, double dy){
        shape.translate((int)Math.round(dx), (int)Math.round(dy));
        x+=dx;
        y+=dy;
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
        if (other != null && other instanceof Polygon){
            Polygon otherShape = (Polygon)other;
            if (this.shape.equals(otherShape.shape)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return "A polygon with "+shape.npoints+" points at position ("+getX()+", "+getY()+") with width="+getWidth()+" and height="+getHeight();
    }

    public java.awt.Rectangle getBounds(){
        return shape.getBounds();
    }
}
