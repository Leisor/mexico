package io.github.leisor;

import org.jline.terminal.Terminal;

public abstract class Player {
    protected int playerNumber;
    protected int lives;
    protected int[] lastRoll;
    public Player(int playerNumber, int lives) {
        this.playerNumber = playerNumber;
        this.lives = lives;
        this.lastRoll = null;
    }

    public abstract boolean decideToRoll(int rollNumber, Terminal terminal) throws Exception;

    public abstract String getPlayerType();

    public int getPlayerNumber() {
        return playerNumber;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    public boolean isAlive() {
        return lives > 0;
    }

    public void setLastRoll(int[] roll) {
        this.lastRoll = roll;
    }

    public int[] getLastRoll() {
        return lastRoll;
    }




}
