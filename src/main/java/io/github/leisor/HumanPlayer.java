package io.github.leisor;

import org.jline.terminal.Terminal;

public class HumanPlayer extends Player {
    public HumanPlayer(int playerNumber, int lives) {
        super(playerNumber, lives);
    }

    @Override
    public boolean decideToRoll(int rollNumber, Terminal terminal, GameContext context) throws Exception {
        if (rollNumber == 0) {
            System.out.println("Press R to roll the dice.");
        } else {
            System.out.println("Press R to roll again or S to stay.");
        }

        while (true) {
            int input = terminal.reader().read();
            char key = (char) input;
            String keyStr = String.valueOf(key).toLowerCase();

            if (keyStr.equals("r")) {
                return true;
            } else if (keyStr.equals("s") && rollNumber > 0) {
                return false;
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    @Override
    public String getPlayerType() {
        return "Human";
    }
}
