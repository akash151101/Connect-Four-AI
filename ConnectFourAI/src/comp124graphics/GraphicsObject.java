package comp124graphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class GraphicsObject {

    private List<GraphicsObserver> observers = new ArrayList<GraphicsObserver>();


    public abstract void draw(Graphics2D gc);

 
    public abstract void move(double dx, double dy);


    public abstract void setPosition(double x, double y);


    public abstract Point.Double getPosition();


    public abstract boolean testHit(double x, double y, Graphics2D gc);


    public abstract java.awt.Rectangle getBounds();

    // ------ Observers ------

    public void addObserver(GraphicsObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GraphicsObserver observer) {
        observers.remove(observer);
    }

    protected void changed() {
        for(GraphicsObserver observer : observers) {
            observer.graphicChanged(this);
        }
    }
}
