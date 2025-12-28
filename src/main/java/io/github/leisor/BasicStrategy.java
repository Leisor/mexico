package io.github.leisor;

public class BasicStrategy implements PlayerStrategy {
    @Override
    public boolean shouldRollAgain(int[] lastRoll, int rollNumber) {
        if (rollNumber == 0 || lastRoll == null) {
            return true;
        }

        int score = Dice.countScore(lastRoll[0], lastRoll[1]);

        if (score == 1000) {
            return false;
        } else if (score >= 100) {
            return false;
        } else if (score >= 62) {
            return false;
        } else {
            return true;
        }
    }


}
