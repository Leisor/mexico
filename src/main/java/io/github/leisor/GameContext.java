package io.github.leisor;

public class GameContext {
    public int currentWorstScore; // -1 when none yet
    public int[] currentWorstRoll; // null when none yet
    public boolean isLastPlayer;
    public int playersAfter; // alive players remaining to act this round
}
