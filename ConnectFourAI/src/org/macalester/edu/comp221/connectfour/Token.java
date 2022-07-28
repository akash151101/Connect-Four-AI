package org.macalester.edu.comp221.connectfour;

import comp124graphics.Ellipse;

import java.awt.*;

public class Token {
    private Color color;
    private Ellipse circle;
    private static final int TOKEN_SIZE = 70;

    private int tokenNum;
    private int rowNum;

    public Token(int tNum, int rNum){
        circle = new Ellipse(0,0, TOKEN_SIZE, TOKEN_SIZE);
        circle.setFilled(true);
        color = Color.WHITE;
        circle.setFillColor(color);
        tokenNum = tNum;
        rowNum = rNum;
    }

    public Ellipse getCircle() {
        return circle;
    }

    public void setXY(int x, int y){
        circle.move(x, y);
    }

    public void setColor(Color color){
        this.color = color;
        circle.setFillColor(color);
    }

    public Color getColor(){
        return color;
    }

    public int getTokenNum(){
        return tokenNum;
    }

    public int getRowNum(){
        return rowNum;
    }
}
