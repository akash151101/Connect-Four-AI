package comp124graphics;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Image extends GraphicsObject{
    private BufferedImage img;
    private int x;
    private int y;
    private String filePath;


    public Image(int x, int y, String filePath){
        this.x = x;
        this.y = y;
        this.filePath = filePath;

        try {
            Path path = Paths.get(filePath);
            path = path.toAbsolutePath();
            File file = new File(path.toString());
            if (!file.exists()){
                throw new IOException(path + " does not exist");
            }
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public void draw(Graphics2D gc){
        gc.drawImage(img, x, y, null);
    }


    public double getX(){
        return x;
    }


    public double getY(){
        return y;
    }


    public double getWidth(){
        return img.getWidth();
    }

    public double getHeight(){
        return img.getHeight();
    }


    public void setPosition(int x, int y){
        this.x = x;
        this.y =y;
        changed();
    }

    public void setPosition(double x, double y){
        setPosition((int)Math.round(x), (int)Math.round(y));
    }


    public Point.Double getPosition(){
        return new Point.Double(x, y);
    }

   
    public void move(double dx, double dy){
        x += dx;
        y += dy;
        changed();
    }

  
    public boolean testHit(double x, double y, Graphics2D gc){
        if (x >= this.x && x <= this.x+img.getWidth() && y >= this.y && y <= this.y+img.getHeight()){
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object other){
        if (other != null && other instanceof Image){
            Image otherImg = (Image)other;
            if (this.img.equals(otherImg.img) && this.x==otherImg.x && this.y == otherImg.y){
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString(){
        return "An image at position ("+x+", "+y+") with file "+filePath;
    }


    public java.awt.Rectangle getBounds(){
        return new java.awt.Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}
