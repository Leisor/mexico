package io.github.leisor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class EVCalculatorTest {

        private static final double EPS = 1e-12;

        @Test
        void probabilityDistribution_has21Scores_andSumsTo1() {
                Map<Integer, Double> probs = EVCalculator.getAllProbabilities();
                assertEquals(21, probs.size(), "Expected exactly 21 ordinal scores (1..21)");

                double sum = probs.values().stream().mapToDouble(Double::doubleValue).sum();
                assertEquals(1.0, sum, EPS, "Probabilities should sum to 1");
        }

        @Test
        void probabilityOfBeating_10_is4Over9() {
                // From the ordinal mapping in Dice.countScore:
                // P(score > 10) = 16/36 = 4/9
                double p = EVCalculator.probabilityOfBeating(10);
                assertEquals(4.0 / 9.0, p, EPS);
        }

        @Test
        void probabilityBeatWithinRolls_10_twoRolls_matchesClosedForm() {
                // P(beat within 2) = 1 - (1 - p)^2, with p = 4/9
                double expected = 1.0 - Math.pow(1.0 - (4.0 / 9.0), 2);
                double actual = EVCalculator.probabilityBeatWithinRolls(10, 2);
                assertEquals(expected, actual, EPS);
        }

        @Test
        void probabilityFinishAboveWorst_10_matchesClosedForm() {
                // Finish above worst under "roll until you beat W or out of rolls" with max 3
                // rolls:
                // 1 - (1 - P(beat W))^3
                double pBeat = 4.0 / 9.0;
                double expected = 1.0 - Math.pow(1.0 - pBeat, 3); // 1 - (5/9)^3 = 604/729
                double actual = EVCalculator.probabilityFinishAboveWorst(10);
                assertEquals(expected, actual, EPS);
        }

        @Test
        void probabilityLoseIfStay_firstPlayerScore10_threePlayersAfter() {
                // Staying at score=10 => you lose iff all later players finish strictly above
                // 10.
                double perPlayer = 1.0 - Math.pow(5.0 / 9.0, 3); // 604/729
                double expected = Math.pow(perPlayer, 3);

                double actual = EVCalculator.probabilityLoseIfStay(
                                10,
                                /* previousWorstScore */ 10,
                                /* playersAfter */ 3);

                assertEquals(expected, actual, EPS);
                assertTrue(actual > 0.0 && actual < 1.0);
        }

        @Test
        void probabilityLoseIfStay_isZeroIfAlreadyAbovePreviousWorst() {
                double actual = EVCalculator.probabilityLoseIfStay(
                                11,
                                /* previousWorstScore */ 10,
                                /* playersAfter */ 3);
                assertEquals(0.0, actual, EPS);
        }
}
