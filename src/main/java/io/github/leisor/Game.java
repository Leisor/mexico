package io.github.leisor;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Game {
    private int numPlayers;
    private int numAIPlayers;
    private int numLives;
    private int currentWorstScore;
    private int[] currentWorstRoll;

    private static final int MAX_ROLLS_PER_TURN = 3;

    public Game(int numPlayers, int numAIPlayers, int numLives) {
        this.numPlayers = numPlayers;
        this.numAIPlayers = numAIPlayers;
        this.numLives = numLives;
    }

    public void start() {
        System.out.println("Starting game with " + numPlayers + " players and " + numLives + " lives.");
        System.out.println();
        Player[] players = new Player[numPlayers];
        initializePlayers(players);
        int playersLeft = numPlayers;
        System.out.println("Let's determine the starting player by (auto)rolling one die each.");
        int startingPlayer = Dice.determineStartingPlayer(numPlayers);
        System.out.println();
        System.out.println("Player " + (startingPlayer + 1) + " starts first.");
        System.out.println();
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            terminal.enterRawMode();
            int roundCounter = 1;
            while (true) {
                System.out.println("----- Round " + roundCounter + " -----");
                roundCounter++;
                currentWorstScore = -1;
                currentWorstRoll = null;

                for (int i = 0; i < numPlayers; i++) {
                    int turnOfPlayer = (startingPlayer + i) % numPlayers;
                    if (!players[turnOfPlayer].isAlive()) {
                        continue;
                    }

                    // Count *alive players remaining to act later in this round order*
                    int playersAfter = 0;
                    for (int k = i + 1; k < numPlayers; k++) {
                        int idx = (startingPlayer + k) % numPlayers;
                        if (players[idx].isAlive()) {
                            playersAfter++;
                        }
                    }

                    System.out.println();
                    playerTurn(players[turnOfPlayer], terminal, playersAfter);
                }

                System.out.println();
                System.out.println("--- Round " + (roundCounter - 1) + " Results ---");
                System.out.println();
                int lowestScoringPlayer = Dice.evaluateRound(
                        java.util.Arrays.stream(players)
                                .map(p -> p.getLastRoll())
                                .toArray(int[][]::new),
                        numPlayers,
                        startingPlayer);

                players[lowestScoringPlayer].loseLife();

                if (!players[lowestScoringPlayer].isAlive()) {
                    System.out.println("Player " + (lowestScoringPlayer + 1) + " has been eliminated!");
                    System.out.println();
                    playersLeft--;
                }
                System.out.println("Lives remaining:");
                for (int i = 0; i < numPlayers; i++) {
                    System.out.println("Player " + (i + 1) + ": " + players[i].getLives() + " lives");
                }
                System.out.println();
                if (playersLeft <= 1) {
                    System.out.println("Game over!");
                    System.out.println();
                    for (int i = 0; i < numPlayers; i++) {
                        if (players[i].isAlive()) {
                            System.out.println("Player " + (i + 1) + " wins!");
                            System.out.println();
                        }
                    }
                    break;
                }

                startingPlayer++;
                if (startingPlayer >= numPlayers) {
                    startingPlayer = 0;
                }
                for (int i = 0; i < numPlayers; i++) {
                    players[i].setLastRoll(null);
                }
            }
            terminal.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializePlayers(Player[] players) {
        for (int i = 0; i < numPlayers; i++) {
            if (i < numAIPlayers) {
                players[i] = new AIPlayer(i + 1, numLives);
            } else {
                players[i] = new HumanPlayer(i + 1, numLives);
            }
        }
    }

    private void printDecisionHints(Player player, int rollNumber, GameContext context) {
        int rollsRemaining = MAX_ROLLS_PER_TURN - rollNumber;

        System.out.println("[Decision hints]");
        System.out.println("  Roll number: " + (rollNumber + 1) + "/" + MAX_ROLLS_PER_TURN);
        System.out.println("  Rolls remaining: " + rollsRemaining);
        System.out.println("  Players after you (alive): " + (context != null ? context.playersAfter : "?"));

        int[] lastRoll = player.getLastRoll();
        if (lastRoll == null) {
            System.out.println("  Current roll: (none yet)");
        } else {
            int currentScore = Dice.countScore(lastRoll[0], lastRoll[1]);
            System.out.println("  Current roll: " + Dice.formatRoll(lastRoll) + " (score: " + currentScore + ")");
        }

        boolean hasWorstToBeat = context != null && context.currentWorstScore >= 0 && context.currentWorstRoll != null;
        if (!hasWorstToBeat) {
            System.out.println("  Worst to beat: (none yet)");
            System.out.println("  Probabilities: (you are setting the first score this round)");
            return;
        }

        System.out.println("  Worst to beat: " + Dice.formatRoll(context.currentWorstRoll)
                + " (score: " + context.currentWorstScore + ")");

        // If we don't have a current roll yet, we can still show "roll now"
        // probabilities.
        if (lastRoll == null) {
            double pBeatWithin = EVCalculator.probabilityBeatWithinRolls(context.currentWorstScore, rollsRemaining);
            double loseIfRoll = EVCalculator.probabilityLoseIfRollNow(
                    context.currentWorstScore,
                    rollsRemaining,
                    context.playersAfter);

            System.out
                    .println("  P(beat worst within remaining rolls): " + String.format("%.2f%%", pBeatWithin * 100.0));
            System.out.println("  P(lose life | roll): " + String.format("%.2f%%", loseIfRoll * 100.0));
            return;
        }

        int currentScore = Dice.countScore(lastRoll[0], lastRoll[1]);

        double loseIfStay = EVCalculator.probabilityLoseIfStay(
                currentScore,
                context.currentWorstScore,
                context.playersAfter);

        double loseIfRoll = EVCalculator.probabilityLoseIfRollNow(
                context.currentWorstScore,
                rollsRemaining,
                context.playersAfter);

        double delta = loseIfStay - loseIfRoll;

        System.out.println("  P(lose life | stay): " + String.format("%.2f%%", loseIfStay * 100.0));
        System.out.println("  P(lose life | roll): " + String.format("%.2f%%", loseIfRoll * 100.0));
        System.out.println("  Benefit (stay - roll): " + String.format("%.2f%%", delta * 100.0)
                + (delta > 0 ? " (rolling helps)" : (delta < 0 ? " (staying helps)" : " (indifferent)")));
    }

    private void playerTurn(Player player, Terminal terminal, int playersAfter) throws Exception {
        System.out.println("Player " + player.getPlayerNumber() + "\'s turn.");
        if (currentWorstRoll != null) {
            System.out.println("Current worst roll to beat: " + Dice.formatRoll(currentWorstRoll));
        }

        GameContext context = new GameContext();
        context.currentWorstScore = currentWorstScore;
        context.currentWorstRoll = currentWorstRoll;
        context.playersAfter = playersAfter;
        context.isLastPlayer = playersAfter == 0;

        for (int rollNumber = 0; rollNumber < MAX_ROLLS_PER_TURN; rollNumber++) {
            printDecisionHints(player, rollNumber, context);

            boolean wantsToRoll = player.decideToRoll(rollNumber, terminal, context);
            if (wantsToRoll) {
                int[] roll = Dice.roll();
                player.setLastRoll(roll);
                System.out.println("Rolled: " + roll[0] + " and " + roll[1]);

                if ((roll[0] == 1 && roll[1] == 2) || (roll[0] == 2 && roll[1] == 1)) {
                    System.out.println();
                    System.out.println("MÉXICO!");
                }
            } else {
                break;
            }
            System.out.println();
        }

        // Update the round's current worst (must match Dice.evaluateRound tie-breaking:
        // <=).
        int[] finalRoll = player.getLastRoll();
        if (finalRoll != null) {
            int score = Dice.countScore(finalRoll[0], finalRoll[1]);
            if (currentWorstScore == -1 || score <= currentWorstScore) {
                currentWorstScore = score;
                currentWorstRoll = finalRoll;
            }
        }
    }
}
