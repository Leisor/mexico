package io.github.leisor;

import java.util.HashMap;
import java.util.Map;

/**
 * Probability utilities for México dice game using ordinal scores (1..21).
 *
 * This version focuses on "probability of being the worst at end of round"
 * under the simple policy:
 * - roll until you beat current worst score W or you run out of rolls
 * - if you beat W, stop immediately (safe vs previous players)
 */
public class EVCalculator {
    private static final int MIN_SCORE = 1;
    private static final int MAX_SCORE = 21;
    private static final int MAX_ROLLS_PER_TURN = 3;

    private static final Map<Integer, Double> PROBABILITY_CACHE = new HashMap<>();
    private static final Map<Integer, Double> BEATING_PROBABILITY_CACHE = new HashMap<>();

    static {
        initializeProbabilities();
        initializeBeatingProbabilities();
    }

    public static double probabilityOfScore(int score) {
        return PROBABILITY_CACHE.getOrDefault(score, 0.0);
    }

    /**
     * P(one roll produces score > targetScore)
     */
    public static double probabilityOfBeating(int targetScore) {
        return BEATING_PROBABILITY_CACHE.getOrDefault(targetScore, 0.0);
    }

    /**
     * If you roll up to {@code rolls} independent times and succeed when score > target,
     * P(at least one success) = 1 - (1 - p)^rolls.
     */
    public static double probabilityBeatWithinRolls(int targetScore, int rolls) {
        if (rolls <= 0) return 0.0;
        double p = probabilityOfBeating(targetScore);
        return 1.0 - Math.pow(1.0 - p, rolls);
    }

    /**
     * Probability that a player who is trying to beat {@code currentWorstScore} ends their turn
     * with finalScore > currentWorstScore under the "roll-until-beat-or-out" rule.
     *
     * With max 3 rolls, this is exactly P(beat within 3 rolls).
     */
    public static double probabilityFinishAboveWorst(int currentWorstScore) {
        return probabilityBeatWithinRolls(currentWorstScore, MAX_ROLLS_PER_TURN);
    }

    /**
     * Probability YOU lose if you STAY now with {@code currentScore}, given:
     * - previous worst among players before you is {@code previousWorstScore}
     * - there are {@code playersAfter} alive players remaining after you in turn order
     *
     * Tie-breaking: if you tie the worst, you become the worst (later player loses ties).
     */
    public static double probabilityLoseIfStay(int currentScore, int previousWorstScore, int playersAfter) {
        requireScore(currentScore);
        if (playersAfter < 0) throw new IllegalArgumentException("playersAfter must be >= 0");

        // If you are strictly better than the previous worst, you cannot be the worst by staying.
        if (previousWorstScore >= MIN_SCORE && currentScore > previousWorstScore) {
            return 0.0;
        }

        // Otherwise, by staying you become the current worst (or tie-worst and thus worst).
        // You lose iff all later players finish strictly above your score.
        double pLaterFinishesAbove = probabilityFinishAboveWorst(currentScore);
        return Math.pow(pLaterFinishesAbove, playersAfter);
    }

    /**
     * Probability YOU lose if you choose to ROLL now, with {@code rollsRemaining} rolls left
     * (including the roll you are about to take), assuming you follow the simple rule:
     * keep rolling until you beat {@code previousWorstScore} or you run out of rolls.
     *
     * NOTE: This method requires {@code previousWorstScore} to be known (>= 1).
     */
    public static double probabilityLoseIfRollNow(int previousWorstScore, int rollsRemaining, int playersAfter) {
        requireScore(previousWorstScore);
        if (rollsRemaining <= 0) throw new IllegalArgumentException("rollsRemaining must be >= 1");
        if (playersAfter < 0) throw new IllegalArgumentException("playersAfter must be >= 0");

        // If we fail to beat W on this roll and still have more rolls, the exact failed score
        // doesn't matter because the rule forces us to continue anyway.
        final double loseIfFailAndContinue =
                (rollsRemaining > 1) ? probabilityLoseIfRollNow(previousWorstScore, rollsRemaining - 1, playersAfter) : 0.0;

        double total = 0.0;
        for (Map.Entry<Integer, Double> e : PROBABILITY_CACHE.entrySet()) {
            int score = e.getKey();
            double p = e.getValue();

            double loseProb;
            if (score > previousWorstScore) {
                // Beat W => stop immediately => cannot be the worst vs previous players.
                loseProb = 0.0;
            } else if (rollsRemaining > 1) {
                loseProb = loseIfFailAndContinue;
            } else {
                // Last roll, fail to beat W => you finish at 'score' (<= W),
                // and you lose iff all later players finish above 'score'.
                double pLaterFinishesAbove = probabilityFinishAboveWorst(score);
                loseProb = Math.pow(pLaterFinishesAbove, playersAfter);
            }

            total += p * loseProb;
        }
        return total;
    }

    /**
     * Decision metric (positive => rolling is better) for "roll vs stay" under the simple model.
     *
     * Requires lastRoll != null and previousWorstScore >= 1.
     */
    public static double calculateRollEV(int[] lastRoll, int previousWorstScore, int rollsRemaining, int playersAfter) {
        if (lastRoll == null) throw new IllegalArgumentException("lastRoll must not be null");
        int currentScore = Dice.countScore(lastRoll[0], lastRoll[1]);
        requireScore(currentScore);

        double loseStay = probabilityLoseIfStay(currentScore, previousWorstScore, playersAfter);
        double loseRoll = probabilityLoseIfRollNow(previousWorstScore, rollsRemaining, playersAfter);

        // Positive means rolling reduces your chance of losing the round.
        return loseStay - loseRoll;
    }

    /**
     * Back-compat: prefer calculateRollEV(lastRoll, previousWorstScore, rollsRemaining, playersAfter).
     */
    @Deprecated
    public static double calculateRollEV(int[] lastRoll, int targetScore, int rollsRemaining, boolean isLastPlayer) {
        return calculateRollEV(lastRoll, targetScore, rollsRemaining, isLastPlayer ? 0 : 1);
    }

    public static Map<Integer, Double> getAllProbabilities() {
        return new HashMap<>(PROBABILITY_CACHE);
    }

    private static void initializeBeatingProbabilities() {
        for (int targetScore : PROBABILITY_CACHE.keySet()) {
            double probability = PROBABILITY_CACHE.entrySet().stream()
                    .filter(entry -> entry.getKey() > targetScore)
                    .mapToDouble(Map.Entry::getValue)
                    .sum();
            BEATING_PROBABILITY_CACHE.put(targetScore, probability);
        }
    }

    private static void initializeProbabilities() {
        Map<Integer, Integer> scoreCount = new HashMap<>();

        for (int die1 = 1; die1 <= 6; die1++) {
            for (int die2 = 1; die2 <= 6; die2++) {
                int score = Dice.countScore(die1, die2);
                scoreCount.put(score, scoreCount.getOrDefault(score, 0) + 1);
            }
        }

        scoreCount.forEach((score, count) -> PROBABILITY_CACHE.put(score, count / 36.0));
    }

    private static void requireScore(int score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("score must be in [" + MIN_SCORE + ", " + MAX_SCORE + "], got: " + score);
        }
    }
}
