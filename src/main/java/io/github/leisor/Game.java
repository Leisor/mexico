package io.github.leisor;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;


public class Game {
    private int numPlayers;
    private int numLives;

    public Game(int numPlayers, int numLives) {
        this.numPlayers = numPlayers;
        this.numLives = numLives;
    }
    public void start() {

        System.out.println("Starting game with " + numPlayers + " players and " + numLives + " lives.");
        System.out.println();
        int[] livesArray = new int[numPlayers];
        int[][] rollsArray = new int[numPlayers][];
        int playersLeft = numPlayers;
        System.out.println("Let's determine the starting player by (auto)rolling one die each.");
        int startingPlayer = Dice.determineStartingPlayer(numPlayers);
        System.out.println();
        System.out.println("Player " + (startingPlayer + 1) + " starts first.");
        System.out.println();
        for (int i = 0; i < numPlayers; i++) {
            livesArray[i] = numLives;
        }
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            terminal.enterRawMode();
            int roundCounter = 1;
            while (true) {
                System.out.println("----- Round " + roundCounter + " -----");
                roundCounter++;
                for (int i = 0; i < numPlayers; i++) {
                    int turnOfPlayer = (startingPlayer + i) % numPlayers;
                    if (livesArray[turnOfPlayer] <= 0) {
                        continue;
                    }
                    System.out.println();
                    System.out.println("Player " + (turnOfPlayer + 1) + "\'s turn.");
                    for (int j = 0; j < 3; j++) {
                        if (j == 0) {
                            System.out.println("Press R to roll the dice.");
                        } else {
                            System.out.println("Press R to roll again or S to stay.");
                        }

                        // Read single character without Enter
                        int input = terminal.reader().read();
                        char key = (char) input;
                        String keyStr = String.valueOf(key).toLowerCase();

                        if (keyStr.equals("r")) {
                            rollsArray[turnOfPlayer] = Dice.roll();
                            System.out.println("You rolled: " + rollsArray[turnOfPlayer][0] + " and " + rollsArray[turnOfPlayer][1]);
                            if (rollsArray[turnOfPlayer][0] == 1 && rollsArray[turnOfPlayer][1] == 2
                                || rollsArray[turnOfPlayer][0] == 2 && rollsArray[turnOfPlayer][1] == 1) {
                                    System.out.println();
                                    System.out.println("MÉXICO!");
                            }
                        } else if (keyStr.equals("s") && j > 0) {
                            break;
                        } else {
                            // Invalid input, retry
                            j--;
                            System.out.println("Invalid input. Try again.");
                        }
                        System.out.println();
                    }
                }
                System.out.println();
                System.out.println("--- Round " + (roundCounter - 1) + " Results ---");
                System.out.println();
                int lowestScoringPlayer = Dice.evaluateRound(rollsArray, numPlayers, startingPlayer);
                livesArray[lowestScoringPlayer]--;
                if (livesArray[lowestScoringPlayer] == 0) {
                    System.out.println("Player " + (lowestScoringPlayer + 1) + " has been eliminated!");
                    System.out.println();
                    playersLeft--;
                }
                System.out.println("Lives remaining:");
                for (int i = 0; i < numPlayers; i++) {
                    System.out.println("Player " + (i + 1) + ": " + livesArray[i] + " lives");
                }
                System.out.println();
                if (playersLeft <= 1) {
                    System.out.println("Game over!");
                    System.out.println();
                    for (int i = 0; i < numPlayers; i++) {
                        if (livesArray[i] > 0) {
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
                rollsArray = new int[numPlayers][];
            }
            terminal.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
