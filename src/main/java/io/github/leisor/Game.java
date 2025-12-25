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
        int[] livesArray = new int[numPlayers];
        int[][] rollsArray = new int[numPlayers][];
        for (int i = 0; i < numPlayers; i++) {
            livesArray[i] = numLives;
        }
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            terminal.enterRawMode();
            for (int i = 0; i < numPlayers; i++) {
                if (livesArray[i] <= 0) {
                    continue;
                }
                System.out.println("Player " + (i + 1) + "'s turn.");

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
                        rollsArray[i] = Dice.roll();
                        System.out.println("You rolled: " + rollsArray[i][0] + " and " + rollsArray[i][1]);
                    } else if (keyStr.equals("s") && j > 0) {
                        break;
                    } else {
                        // Invalid input, retry
                        j--;
                        System.out.println("Invalid input. Try again.");
                    }
                }
            }
            terminal.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
