package org.macalester.edu.comp221.connectfour;

public class MinimaxAI {
    private int searchDepth;
    public static final int MAX_WINNING_SCORE = 999999;
    public static final int MIN_WINNING_SCORE = -999999;

    public MinimaxAI()
    {
        searchDepth = 8;
    }

    public void makeDecision(TokenRow[] tokenRows, ConnectFourGame game)
    {
        int[] decision = maxPlay(new VirtualGameBoard(tokenRows), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if(decision[0] == -1)
            return;
        Token AI_token = tokenRows[decision[0]].getNextToken();
        game.makeMove(tokenRows[decision[0]], AI_token, false);
    }

    public boolean isDone(int depth, VirtualGameBoard board, int score)
    {
        return depth >= searchDepth || board.isFull() || score >= MAX_WINNING_SCORE || score <= MIN_WINNING_SCORE;
    }

    private int[] maxPlay(VirtualGameBoard board, int depth, int alpha, int beta)
    {
        int score = board.calcScore();

        if (isDone(depth, board, score))
            return new int[]{-1, score};

        int[] max = new int[]{-1, 0};

        for (int column = 0; column < ConnectFourGame.NUMBER_OF_COLUMNS; column++) {
            VirtualGameBoard new_board = board.clone();
            if (new_board.place(column, true)) {
                int[] next = minPlay(new_board, depth + 1, alpha, beta);

                if (max[0] == -1 || next[1] > max[1]) {
                    max[0] = column;
                    max[1] = next[1];
                    alpha = next[1];
                }

                if(beta <= alpha)
                    return max;
            }
        }

        return max;
    }

    private int[] minPlay(VirtualGameBoard board, int depth, int alpha, int beta)
    {
        int score = board.calcScore();

        if (isDone(depth, board, score))
            return new int[]{-1, score};

        int[] min = new int[]{-1, 0};

        for (int column = 0; column < ConnectFourGame.NUMBER_OF_COLUMNS; column++) {
            VirtualGameBoard new_board = board.clone();
            if (new_board.place(column, false)) {
                int[] next = maxPlay(new_board, depth + 1, alpha, beta);

                if (min[0] == -1 || next[1] < min[1]) {
                    min[0] = column;
                    min[1] = next[1];
                    beta = next[1];
                }

                if(beta <= alpha)
                    return min;
            }
        }

        return min;
    }

}
