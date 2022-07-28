package org.macalester.edu.comp221.connectfour;

import comp124graphics.*;
import comp124graphics.Rectangle;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class ConnectFourGame extends CanvasWindow implements MouseListener, MouseMotionListener {
    private static final int CANVAS_WIDTH = 900;
    private static final int CANVAS_HEIGHT = 700;
    private static final int BOARD_WIDTH = 700;
    private static final int BOARD_HEIGHT = 700;
    private static final int BOARD_X = 100;
    private static final int BOARD_Y = 0;
    private static final int ROW_OFFSET = 100;
    private static final int TEXT_X = 10;
    private static final int TEXT_Y = 100;

    public static final Color PLAYER_COLOR = Color.RED;
    public static final Color AI_COLOR = Color.BLACK;

    public static final int NUMBER_OF_ROWS = 6;
    public static final int NUMBER_OF_COLUMNS = 7;

    private boolean gameDone;

    private GraphicsText title;
    private TokenRow[] rows;
    private Rectangle board;
    private Color activeColor;
    private ArrayList<Token> playTokens;
    private MinimaxAI AI;

    public ConnectFourGame(){
        super("Connect4", CANVAS_WIDTH, CANVAS_HEIGHT);

        board = new Rectangle(BOARD_X,BOARD_Y, BOARD_WIDTH, BOARD_HEIGHT);
        board.setFilled(true);
        board.setFillColor(Color.cyan);
        add(board);

        rows = new TokenRow[7];
        AI = new MinimaxAI();

        title = new GraphicsText("CONNECT 4",TEXT_X, TEXT_Y);
        add(title);

        playTokens = new ArrayList<>();
        reset();
    }

    public void reset()
    {
        playTokens.clear();
        addRows();
        activeColor = PLAYER_COLOR;
        gameDone = false;

        title.setText("CONNECT 4");

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    private void addRows(){
        int X = BOARD_X;
        int Y = BOARD_Y;
        for (int i = 0; i < NUMBER_OF_COLUMNS; i++){
            rows[i] = new TokenRow(i);
            rows[i].setPosition(X, Y);
            this.add(rows[i]);
            X = X + ROW_OFFSET;
        }
    }

    private boolean checkWin(){
        // Vertical Check
        for (Token curToken : playTokens){
            Color winColor = curToken.getColor();
            int winCount = 0;
            for (Token tarToken: playTokens){
                if (!tarToken.equals(curToken)){
                    boolean sameCol = curToken.getRowNum() == tarToken.getRowNum();
                    boolean adjTokenInCol1 = curToken.getTokenNum() == tarToken.getTokenNum() + 1;
                    boolean adjTokenInCol2 = curToken.getTokenNum() == tarToken.getTokenNum() + 2;
                    boolean adjTokenInCol3 = curToken.getTokenNum() == tarToken.getTokenNum() + 3;
                    boolean checkCol = (sameCol && adjTokenInCol1) || (sameCol && adjTokenInCol2) || (sameCol && adjTokenInCol3);
                    if (checkCol) {
                        if (curToken.getColor().equals(tarToken.getColor())) {
                            winCount++;
                        }
                    }
                }
            }
            if (winCount == 3){
                endGame(winColor);
                return true;
            }
        }

        // Horizontal Check
        for (Token curToken : playTokens){
            Color winColor = curToken.getColor();
            int winCount = 0;
            for (Token tarToken: playTokens){
                if (!tarToken.equals(curToken)){
                    boolean sameRow = curToken.getTokenNum() == tarToken.getTokenNum();
                    boolean adjTokenInRow1 = curToken.getRowNum() == tarToken.getRowNum() + 1;
                    boolean adjTokenInRow2 = curToken.getRowNum() == tarToken.getRowNum() + 2;
                    boolean adjTokenInRow3 = curToken.getRowNum() == tarToken.getRowNum() + 3;
                    boolean checkRow = (sameRow && adjTokenInRow1) || (sameRow && adjTokenInRow2) || (sameRow && adjTokenInRow3);
                    if (checkRow) {
                        if (curToken.getColor().equals(tarToken.getColor())) {
                            winCount++;
                        }
                    }
                }
            }
            if (winCount == 3){
                endGame(winColor);
                return true;
            }
        }

        // Ascending Diagonal Check
        for (Token curToken : playTokens){
            Color winColor = curToken.getColor();
            int winCount = 0;
            for (Token tarToken: playTokens){
                if (!tarToken.equals(curToken)){
                    boolean ascDiag1 = (curToken.getTokenNum() == tarToken.getTokenNum() - 1) && (curToken.getRowNum() == tarToken.getRowNum() + 1);
                    boolean ascDiag2 = (curToken.getTokenNum() == tarToken.getTokenNum() - 2) && (curToken.getRowNum() == tarToken.getRowNum() + 2);
                    boolean ascDiag3 = (curToken.getTokenNum() == tarToken.getTokenNum() - 3) && (curToken.getRowNum() == tarToken.getRowNum() + 3);
                    boolean checkDiagAsc = ascDiag1 || ascDiag2 || ascDiag3;
                    if (checkDiagAsc) {
                        if (curToken.getColor().equals(tarToken.getColor())) {
                            winCount++;
                        }
                    }
                }
            }
            if (winCount == 3){
                endGame(winColor);
                return true;
            }
        }

        // Descending Diagonal Check
        for (Token curToken : playTokens){
            Color winColor = curToken.getColor();
            int winCount = 0;
            for (Token tarToken: playTokens){
                if (!tarToken.equals(curToken)){
                    boolean descDiag1 = (curToken.getTokenNum() == tarToken.getTokenNum() - 1) && (curToken.getRowNum() == tarToken.getRowNum() - 1);
                    boolean descDiag2 = (curToken.getTokenNum() == tarToken.getTokenNum() - 2) && (curToken.getRowNum() == tarToken.getRowNum() - 2);
                    boolean descDiag3 = (curToken.getTokenNum() == tarToken.getTokenNum() - 3) && (curToken.getRowNum() == tarToken.getRowNum() - 3);
                    boolean checkDiagDesc = descDiag1 || descDiag2 || descDiag3;
                    if (checkDiagDesc) {
                        if (curToken.getColor().equals(tarToken.getColor())) {
                            winCount++;
                        }
                    }
                }
            }
            if (winCount == 3){
                endGame(winColor);
                return true;
            }
        }
        if(isFull())
        {
            endGame(Color.YELLOW);
            return true;
        }
        return false;
    }

    private void endGame(Color winColor){
        removeMouseListener(this);
        removeMouseMotionListener(this);
        if (winColor.equals(PLAYER_COLOR)){
            title.setText("Red Wins!");
        }
        else if (winColor.equals(AI_COLOR)){
            title.setText("Black Wins!");
        }
        else {
            title.setText("It's a tie!");
        }
        gameDone = true;
        this.pause(5000);
        reset();
    }

    public boolean isFull() {
        for (TokenRow thisRow : rows) {
            if(thisRow.getNextToken() != null)
                return false;
        }
        return true;
    }

    public void mouseClicked(MouseEvent e){
        for (TokenRow thisRow: rows){
            Token thisToken = thisRow.getNextToken();
            if(thisToken == null)
                continue;
            if (thisRow.getBounds().contains(e.getX(), e.getY())){
                makeMove(thisRow, thisToken, true);
            }
        }
    }

    public void makeMove(TokenRow row, Token token, boolean isUserTriggered)
    {
        if(playTokens.contains(token) || (activeColor.equals(AI_COLOR) && isUserTriggered))
            return;
        token.setColor(activeColor);
        playTokens.add(token);
        row.incrementNextToken();
        if(checkWin())
            return;
        if (activeColor.equals(PLAYER_COLOR)){
            activeColor = AI_COLOR;
            if(!gameDone)
            {
                AIWorker runner = new AIWorker(AI, rows, this);
                Thread thread = new Thread(runner);
                thread.start();
            }
        }
        else if (activeColor.equals(AI_COLOR)){
            activeColor = PLAYER_COLOR;
        }
    }


    public void mousePressed(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseDragged(MouseEvent e){}

    public void mouseMoved(MouseEvent e) {
        if(activeColor.equals(AI_COLOR))
            return;
        for (TokenRow thisRow : rows) {
            Token thisToken = thisRow.getNextToken();
            if(thisToken == null)
                continue;
            if (thisRow.getBounds().contains(e.getX(), e.getY()) && !playTokens.contains(thisToken)) {
                thisToken.setColor(Color.GREEN);
            }
            else{
                if (!playTokens.contains(thisToken)) {
                    thisToken.setColor(Color.WHITE);
                }
            }
        }
    }

    public static void main(String[] args){
        ConnectFourGame prog = new ConnectFourGame();
    }
}
