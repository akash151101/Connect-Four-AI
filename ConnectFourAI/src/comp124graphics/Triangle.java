package comp124graphics;

import java.awt.geom.Point2D;
import java.util.Arrays;



public class Triangle extends Polygon {

    /**
     * create a triangle from the x and y locations of the three corners of the
     * triangle (a, b, and c)
     */
    public Triangle(double ax, double ay, double bx, double by, double cx, double cy) {
        this(new Point2D.Double(ax,ay), new Point2D.Double(bx,by), new Point2D.Double(cx,cy));
    }


    public Triangle(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        super(Arrays.asList(a,b,c));
    }


    @Override
    public String toString(){
        return "A triangle at position ("+getX()+", "+getY()+") with width="+getWidth()+" and height="+getHeight();
    }

}
