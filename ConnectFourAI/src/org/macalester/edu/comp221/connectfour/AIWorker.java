package org.macalester.edu.comp221.connectfour;

public class AIWorker implements Runnable {
    private MinimaxAI ai;
    private TokenRow[] rows;
    private ConnectFourGame game;

    public AIWorker(MinimaxAI ai, TokenRow[] rows, ConnectFourGame game)
    {
        this.ai = ai;
        this.rows = rows;
        this.game = game;
    }

    @Override
    public void run() {
        ai.makeDecision(rows, game);
    }
}
