package io.github.leisor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DiceEvaluateRoundTest {

    @Test
    void evaluateRound_laterPlayerLosesTies() {
        // Two players tie for worst: later in evaluation order should be chosen as
        // worst.
        int numPlayers = 3;
        int startingPlayer = 0;

        // Score 1 is worst: (3,1) => ordinal 1
        int[][] rolls = new int[numPlayers][];
        rolls[0] = new int[] { 3, 1 }; // worst
        rolls[1] = new int[] { 6, 6 }; // good
        rolls[2] = new int[] { 1, 3 }; // same as (3,1) => worst tie

        int loser = Dice.evaluateRound(rolls, numPlayers, startingPlayer);

        // Since player 2 is evaluated after player 0 and ties the lowest score,
        // it should be selected as the loser.
        assertEquals(2, loser);
    }
}
