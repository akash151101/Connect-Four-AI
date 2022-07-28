package org.macalester.edu.comp221.connectfour;

import comp124graphics.GraphicsGroup;
import comp124graphics.Rectangle;

import java.awt.*;

public class TokenRow extends GraphicsGroup{

    private static final int BASE_TOKEN_X = 15;
    private static final int BASE_TOKEN_Y = 50;
    private static final int TOKEN_OFFSET = 110;

    private Token nextToken;
    private int rowNum;
    private int nextTokenNum;
    private Token[] tokenRow;

    public TokenRow(int num){
        tokenRow = new Token[ConnectFourGame.NUMBER_OF_ROWS ];
        Rectangle rowRec = new Rectangle(0,0, 100, 700);
        rowRec.setFilled(true);
        rowRec.setStrokeColor(Color.BLACK);
        rowRec.setFillColor(Color.cyan);
        add(rowRec);
        nextTokenNum = ConnectFourGame.NUMBER_OF_ROWS - 1;
        rowNum = num;
        int X = BASE_TOKEN_X;
        int Y = BASE_TOKEN_Y;
        for (int i = 0; i<ConnectFourGame.NUMBER_OF_ROWS; i++){
            tokenRow[i] = new Token(i, num);
            tokenRow[i].setXY(X,Y);
            add(tokenRow[i].getCircle());
            Y = Y + TOKEN_OFFSET;
        }
        nextToken = tokenRow[nextTokenNum];
    }

    public boolean incrementNextToken(){
        if(nextTokenNum <= 0)
        {
            nextToken = null;
            return false;
        }
        nextTokenNum -= 1;
        nextToken = tokenRow[nextTokenNum];
        return true;
    }

    public Token getTokenAtRow(int row)
    {
        return tokenRow[row];
    }

    public Token getNextToken() {
        return nextToken;
    }

    public int[] toArray()
    {
        int[] result = new int[tokenRow.length];
        for (int i = 0; i < tokenRow.length; i++)
        {
            int num = VirtualGameBoard.EMPTY_TOKEN;
            if (tokenRow[i].getColor().equals(ConnectFourGame.PLAYER_COLOR))
            {
                num = VirtualGameBoard.PLAYER_TOKEN;
            }
            else if (tokenRow[i].getColor().equals(ConnectFourGame.AI_COLOR))
            {
                num = VirtualGameBoard.AI_TOKEN;
            }
            result[i] = num;
        }
        return result;
    }
}
