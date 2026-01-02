package io.github.leisor;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class Game {
    private int numPlayers;
    private int numAIPlayers;
    private int numLives;
    private int currentWorstScore;
    private int[] currentWorstRoll;

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
                    System.out.println();
                    playerTurn(players[turnOfPlayer], terminal);
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

    private void playerTurn(Player player, Terminal terminal) throws Exception {
        System.out.println("Player " + player.getPlayerNumber() + "\'s turn.");
        if (currentWorstRoll != null) {
            System.out.println("Current worst roll to beat: " + formatRoll(currentWorstRoll));
        }
        for (int rollNumber = 0; rollNumber < 3; rollNumber++) {
            boolean wantsToRoll = player.decideToRoll(rollNumber, terminal);
            if (wantsToRoll) {
                int[] roll = Dice.roll();
                player.setLastRoll(roll);
                int score = Dice.countScore(roll[0], roll[1]);
                if (currentWorstScore == -1 || score < currentWorstScore) {
                    currentWorstScore = score;
                    currentWorstRoll = roll;
                }
                System.out.println("Rolled: " + roll[0] + " and " + roll[1]);

                if (roll[0] == 1 && roll[1] == 2 || roll[0] == 2 && roll[1] == 1) {
                    System.out.println();
                    System.out.println("MÉXICO!");
                }
            } else {
                break;
            }
            System.out.println();
        }
    }

    private String formatRoll(int[] dice) {
        int die1 = dice[0];
        int die2 = dice[1];

        // México exception: always show as 1,2
        if ((die1 == 1 && die2 == 2) || (die1 == 2 && die2 == 1)) {
            return "1,2";
        }

        // Otherwise: higher die first
        int higher = Math.max(die1, die2);
        int lower = Math.min(die1, die2);
        return higher + "," + lower;
    }

}
