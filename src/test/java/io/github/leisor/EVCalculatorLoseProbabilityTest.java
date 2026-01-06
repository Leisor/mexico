package io.github.leisor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EVCalculatorLoseProbabilityTest {

    private static final double EPS = 1e-12;

    @Test
    void loseIfStay_lastPlayer_isCertainLoss_whenNotAboveWorst() {
        // If you are last (playersAfter = 0) and you are not strictly above the current
        // worst,
        // staying makes you the worst with certainty.
        // (Tie-breaking: tying the worst is still losing when you are later.)
        int playersAfter = 0;

        // worst to beat is 12, you have 5 => you will be the worst if you stay
        double lose = EVCalculator.probabilityLoseIfStay(
                /* currentScore */ 5,
                /* previousWorstScore */ 12,
                playersAfter);

        assertEquals(1.0, lose, EPS);
    }

    @Test
    void loseIfStay_lastPlayer_isZero_whenAboveWorst() {
        int playersAfter = 0;

        double lose = EVCalculator.probabilityLoseIfStay(
                /* currentScore */ 14,
                /* previousWorstScore */ 12,
                playersAfter);

        assertEquals(0.0, lose, EPS);
    }

    @Test
    void loseIfRollNow_lastPlayer_equalsFailToBeatWorstWithinRemainingRolls() {
        // If you are last (playersAfter = 0), you lose iff you fail to beat the worst
        // within your remaining rolls.
        // So: P(lose | roll now) = (1 - P(beat worst in one roll))^(rollsRemaining)
        int worst = 12;
        int rollsRemaining = 3;
        int playersAfter = 0;

        double pBeatOne = EVCalculator.probabilityOfBeating(worst);
        double expectedLose = Math.pow(1.0 - pBeatOne, rollsRemaining);

        double actualLose = EVCalculator.probabilityLoseIfRollNow(worst, rollsRemaining, playersAfter);

        assertEquals(expectedLose, actualLose, EPS);
    }

    @Test
    void loseIfRollNow_lastPlayer_oneRollRemaining_equalsFailToBeatWorstOnce() {
        int worst = 12;
        int rollsRemaining = 1;
        int playersAfter = 0;

        double pBeatOne = EVCalculator.probabilityOfBeating(worst);
        double expectedLose = 1.0 - pBeatOne;

        double actualLose = EVCalculator.probabilityLoseIfRollNow(worst, rollsRemaining, playersAfter);

        assertEquals(expectedLose, actualLose, EPS);
    }
}
