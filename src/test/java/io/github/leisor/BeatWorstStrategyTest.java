package io.github.leisor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BeatWorstStrategyTest {

    @Test
    void firstRoll_isAlwaysTaken() {
        BeatWorstStrategy s = new BeatWorstStrategy();
        assertTrue(s.shouldRollAgain(null, 0, null));
    }

    @Test
    void mexico_isAlwaysStay_afterFirstRoll() {
        BeatWorstStrategy s = new BeatWorstStrategy();
        int[] mexico = new int[] { 1, 2 }; // score == 21
        assertFalse(s.shouldRollAgain(mexico, 1, new GameContext()));
    }

    @Test
    void noWorstYet_stopAfterOneRoll() {
        BeatWorstStrategy s = new BeatWorstStrategy();
        GameContext ctx = new GameContext();
        ctx.currentWorstScore = -1;

        int[] roll = new int[] { 6, 4 }; // any non-mexico
        assertFalse(s.shouldRollAgain(roll, 1, ctx));
    }

    @Test
    void withWorst_rollAgainUntilBeatWorst() {
        BeatWorstStrategy s = new BeatWorstStrategy();
        GameContext ctx = new GameContext();

        int[] equalToWorst = new int[] { 6, 1 };
        ctx.currentWorstScore = Dice.countScore(equalToWorst[0], equalToWorst[1]);

        assertTrue(s.shouldRollAgain(equalToWorst, 1, ctx));
    }

    @Test
    void withWorst_stopOnceYouBeatWorst() {
        BeatWorstStrategy s = new BeatWorstStrategy();
        GameContext ctx = new GameContext();

        int[] worst = new int[] { 6, 1 };
        int[] better = new int[] { 6, 4 };

        ctx.currentWorstScore = Dice.countScore(worst[0], worst[1]);

        assertFalse(s.shouldRollAgain(better, 1, ctx));
    }
}
