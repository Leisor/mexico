package io.github.leisor;

public class BasicStrategy implements PlayerStrategy {
    @Override
    public boolean shouldRollAgain(int[] lastRoll, int rollNumber, GameContext context) {
        if (rollNumber == 0 || lastRoll == null) {
            return true;
        }

        int score = Dice.countScore(lastRoll[0], lastRoll[1]);

        // México
        if (score == 21) {
            return false;
        // Pairs
        } else if (score >= 15) {
            return false;
        // 6,2 is enough to stay
        } else if (score >= 11) {
            return false;
        } else {
            return true;
        }
    }
}
