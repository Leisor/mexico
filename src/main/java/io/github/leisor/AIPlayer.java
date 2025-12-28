package io.github.leisor;

import org.jline.terminal.Terminal;

public class AIPlayer extends Player {
    private PlayerStrategy strategy;


    public AIPlayer(int playerNumber, int lives) {
        super(playerNumber, lives);
        this.strategy = new BasicStrategy();
    }

    public void setStrategy(PlayerStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean decideToRoll(int rollNumber, Terminal terminal) throws Exception {
        // Small delay to simulate thinking
        Thread.sleep(500);

        boolean decision = strategy.shouldRollAgain(currentRoll, rollNumber);

        if (decision) {
            System.out.println("AI decides to roll again.");
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
