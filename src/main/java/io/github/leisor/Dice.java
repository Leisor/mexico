package io.github.leisor;

import java.util.ArrayList;
import java.util.List;

public class Dice {
    public static int[] roll() {
        java.util.Random random = new java.util.Random();
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        return new int[] { die1, die2 };
    }

    public static int evaluateRound(int[][] rollsArray, int numPlayers, int startingPlayer) {
        System.out.println("Evaluating rolls...");
        int[] scores = new int[numPlayers];
        int lowestScore = Integer.MAX_VALUE;
        int playerWithLowestScore = -1;
        for (int i = 0; i < numPlayers; i++) {
            int playerIndex = (startingPlayer + i) % numPlayers;
            if (rollsArray[playerIndex] == null) {
                continue; // Player did not roll
            }
            int die1 = rollsArray[playerIndex][0];
            int die2 = rollsArray[playerIndex][1];
            scores[playerIndex] = CountScore(die1, die2);
            if (scores[playerIndex] <= lowestScore) {
                lowestScore = scores[playerIndex];
                playerWithLowestScore = playerIndex;
            }
        }
        System.out.println("Player " + (playerWithLowestScore + 1) + " had the worst roll of "
                + rollsArray[playerWithLowestScore][0] + " and " + rollsArray[playerWithLowestScore][1]
                + " and loses a life.");
        System.out.println();
        return playerWithLowestScore;
    }

    private static int CountScore(int die1, int die2) {
        if (die1 == die2) {
            return die1 * 100;
        } else if (die1 == 1 && die2 == 2 || die2 == 2 && die1 == 1) {
            return 1000;
        } else {
            return Math.max(die1, die2) * 10 + Math.min(die1, die2);
        }

    }

    public static int determineStartingPlayer(int numPlayers) {
        java.util.Random random = new java.util.Random();
        int[] rolls = new int[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            int roll = random.nextInt(6) + 1;
            System.out.println("Player " + (i + 1) + " rolled a " + roll);
            rolls[i] = roll;
        }
        List<Integer> allPlayers = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            allPlayers.add(i);
        }
        return findStartingPlayerRecursive(rolls, allPlayers);
    }

    private static int findStartingPlayerRecursive(int[] rolls, List<Integer> currentPlayers) {
        java.util.Random random = new java.util.Random();
        int highestRoll = -1;
        List<Integer> tiedPlayers = new ArrayList<>();
        for (int i = 0; i < currentPlayers.size(); i++) {
            int rollValue = rolls[i]; // i indexes into rolls correctly for both initial and recursive calls
            if (rollValue > highestRoll) {
                highestRoll = rollValue;
                tiedPlayers.clear();
                tiedPlayers.add(currentPlayers.get(i));
            } else if (rollValue == highestRoll) {
                tiedPlayers.add(currentPlayers.get(i));
            }
        }
        if (tiedPlayers.size() == 1) {
            return tiedPlayers.get(0);
        } else {
            System.out.println("Tie detected. Re-rolling among " + tiedPlayers.size() + " players.");
            int[] newRolls = new int[tiedPlayers.size()];
            for (int i = 0; i < tiedPlayers.size(); i++) {
                newRolls[i] = random.nextInt(6) + 1;
                System.out.println("Player " + (tiedPlayers.get(i) + 1) + " re-rolled a " + newRolls[i]);
            }
            return findStartingPlayerRecursive(newRolls, tiedPlayers);
        }
    }
}
