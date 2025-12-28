package io.github.leisor;

public interface PlayerStrategy {
    /**
     * Decides whether to roll again based on current roll and roll number.
     *
     * @param lastRoll The current dice roll [die1, die2], or null if no roll yet
     * @param rollNumber The current roll number (0 for first roll, 1 for second, 2 for third)
     * @return true to roll again, false to stay
     */
    boolean shouldRollAgain(int[] lastRoll, int rollNumber);
}
