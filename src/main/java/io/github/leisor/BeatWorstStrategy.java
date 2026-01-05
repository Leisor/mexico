package io.github.leisor;

public class BeatWorstStrategy implements PlayerStrategy {
    @Override
    public boolean shouldRollAgain(int[] lastRoll, int rollNumber, GameContext context) {
        // Always take the first roll.
        if (rollNumber == 0 || lastRoll == null) {
            return true;
        }

        int score = Dice.countScore(lastRoll[0], lastRoll[1]);

        // México is the best possible outcome -> never roll again.
        if (score == 21) {
            return false;
        }

        // No current worst to beat yet (first acting alive player this round):
        // For now: roll exactly once and stop (until DP is implemented).
        if (context == null || context.currentWorstScore < 0) {
            return false;
        }

        int worstToBeat = context.currentWorstScore;

        // Simple rule: keep rolling until you beat W, or you run out of rolls.
        // If you already beat W, stop immediately.
        return score <= worstToBeat;
    }
}
