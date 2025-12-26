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
        } else if (die1 == 1 && die2 == 2 || die1 == 2 && die2 == 1) {
            return 1000;
        } else {
            return Math.max(die1, die2) * 10 + Math.min(die1, die2);
        }

    }

    public static int determineStartingPlayer(int numPlayers) {
        java.util.Random random = new java.util.Random();
        int highestRoll = -1;
        List<Integer> highrollers = new ArrayList<>();
        List<Integer> playersLeft = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            int roll = random.nextInt(6) + 1;
            System.out.println("Player " + (i + 1) + " rolled a " + roll);
            if (roll > highestRoll) {
                highestRoll = roll;
                highrollers.clear();
                highrollers.add(i);
            } else if (roll == highestRoll) {
                highrollers.add(i);
            }
        }
        if (highrollers.size() <= 1) {
            return highrollers.get(0);
        } else {
            while (true) {
                playersLeft.clear();
                playersLeft.addAll(highrollers);
                highrollers.clear();
                highestRoll = -1;
                System.out.println();
                System.out.println("Tie detected among " + playersLeft.size() + " players. Re-rolling");
                for (int i = 0; i < playersLeft.size(); i++) {
                    int roll = random.nextInt(6) + 1;
                    System.out.println("Player " + (playersLeft.get(i)+1) + " rolled a " + roll);
                    if (roll > highestRoll) {
                        highestRoll = roll;
                        highrollers.clear();
                        highrollers.add(playersLeft.get(i));
                    } else if (roll == highestRoll) {
                        highrollers.add(playersLeft.get(i));
                    }
                }
                if (highrollers.size() <= 1) {
                    return highrollers.get(0);
                }
            }
        }
    }
}
