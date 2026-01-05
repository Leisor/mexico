package io.github.leisor;

import org.jline.terminal.Terminal;

public class AIPlayer extends Player {
    private PlayerStrategy strategy;

    public AIPlayer(int playerNumber, int lives) {
        super(playerNumber, lives);
        this.strategy = new BeatWorstStrategy();
    }

    public void setStrategy(PlayerStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean decideToRoll(int rollNumber, Terminal terminal, GameContext context) throws Exception {
        Thread.sleep(200);

        boolean decision = strategy.shouldRollAgain(lastRoll, rollNumber, context);

        if (decision) {
            if (rollNumber == 0) {
                System.out.println("AI decides to roll.");
            } else {
                System.out.println("AI decides to roll again.");
            }
        } else {
            System.out.println("AI decides to stay.");
        }

        return decision;
    }

    @Override
    public String getPlayerType() {
        return "AI";
    }
}
