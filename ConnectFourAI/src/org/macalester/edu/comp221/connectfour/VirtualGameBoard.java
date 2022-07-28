package org.macalester.edu.comp221.connectfour;

public class VirtualGameBoard {
    private int[][] board;
    public static final int AI_TOKEN = 2;
    public static final int PLAYER_TOKEN = 1;
    public static final int EMPTY_TOKEN = 0;

    public VirtualGameBoard(int[][] board)
    {
        this.board = board;
    }

    public VirtualGameBoard(TokenRow[] tokenRows)
    {
        board = new int[ConnectFourGame.NUMBER_OF_COLUMNS][ConnectFourGame.NUMBER_OF_ROWS];
        for(int i = 0; i < tokenRows.length; i++)
        {
            board[i] = tokenRows[i].toArray();
        }
    }

    public boolean isFull()
    {
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                if(board[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean place(int col, boolean isMax)
    {
        if (board[col][0] == EMPTY_TOKEN && col >= 0 && col < ConnectFourGame.NUMBER_OF_COLUMNS) {
            for (int y = ConnectFourGame.NUMBER_OF_ROWS - 1; y >= 0; y--) {
                if (this.board[col][y] == EMPTY_TOKEN) {
                    this.board[col][y] = isMax ? AI_TOKEN : PLAYER_TOKEN;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public int calcScore()
    {
        int vertical_points=0, horizontal_points=0, descDiagonal_points=0, ascDiagonal_points=0, total_points=0;

        for (int row = 0; row < ConnectFourGame.NUMBER_OF_ROWS - 3; row++) {
            for (int column = 0; column < ConnectFourGame.NUMBER_OF_COLUMNS; column++) {
                int tempScore = calcScorePosition(row, column, 1, 0);
                vertical_points += tempScore;
                if(tempScore >= MinimaxAI.MAX_WINNING_SCORE || tempScore <= MinimaxAI.MIN_WINNING_SCORE)
                    return tempScore;
            }
        }

        for (int row = 0; row < ConnectFourGame.NUMBER_OF_ROWS ; row++) {
            for (int column = 0; column < ConnectFourGame.NUMBER_OF_COLUMNS - 3; column++) {
                int tempScore = calcScorePosition(row, column, 0, 1);
                horizontal_points += tempScore;
                if(tempScore >= MinimaxAI.MAX_WINNING_SCORE || tempScore <= MinimaxAI.MIN_WINNING_SCORE)
                    return tempScore;
            }
        }

        for (int row = 0; row < ConnectFourGame.NUMBER_OF_ROWS - 3 ; row++) {
            for (int column = 0; column < ConnectFourGame.NUMBER_OF_COLUMNS - 3; column++) {
                int tempScore = calcScorePosition(row, column, 1, 1);
                descDiagonal_points += tempScore;
                if(tempScore >= MinimaxAI.MAX_WINNING_SCORE || tempScore <= MinimaxAI.MIN_WINNING_SCORE)
                    return tempScore;
            }
        }

        for (int row = 3; row < ConnectFourGame.NUMBER_OF_ROWS  ; row++) {
            for (int column = 0; column < ConnectFourGame.NUMBER_OF_COLUMNS - 4; column++) {
                int tempScore = calcScorePosition(row, column, -1, 1);
                ascDiagonal_points += tempScore;
                if(tempScore >= MinimaxAI.MAX_WINNING_SCORE || tempScore <= MinimaxAI.MIN_WINNING_SCORE)
                    return tempScore;
            }
        }

        total_points = vertical_points + horizontal_points + descDiagonal_points + ascDiagonal_points;
        return total_points;
    }

    private int calcScorePosition(int row, int column, int increment_row, int increment_col)
    {
        int ai_points = 0, player_points = 0;

        for (int i = 0; i < 4; i++) //connect "4"
        {
            if(board[column][row] == 2)
            {
                ai_points++;
            }
            else if (board[column][row] == 1)
            {
                player_points++;
            }

            row += increment_row;
            column += increment_col;
        }

        if(player_points == 4)
            return MinimaxAI.MIN_WINNING_SCORE;
        else if(ai_points == 4)
            return MinimaxAI.MAX_WINNING_SCORE;
        else
            return ai_points;
    }

    public VirtualGameBoard clone()
    {
        int [][] clonedBoard = new int[board.length][];
        for(int i = 0; i < board.length; i++)
            clonedBoard[i] = board[i].clone();
        return new VirtualGameBoard(clonedBoard);
    }
}
