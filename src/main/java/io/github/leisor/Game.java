package io.github.leisor;

public class Game {
    private int numPlayers;
    private int numLives;

    public Game(int numPlayers, int numLives) {
        this.numPlayers = numPlayers;
        this.numLives = numLives;
    }
    public void start() {

    System.out.println("Starting game with " + numPlayers + " players and " + numLives + " lives.");

    }
}
