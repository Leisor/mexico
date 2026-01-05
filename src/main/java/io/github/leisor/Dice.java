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
                continue;
            }
            int die1 = rollsArray[playerIndex][0];
            int die2 = rollsArray[playerIndex][1];
            scores[playerIndex] = countScore(die1, die2);
            if (scores[playerIndex] <= lowestScore) {
                lowestScore = scores[playerIndex];
                playerWithLowestScore = playerIndex;
            }
        }
        System.out.println("Player " + (playerWithLowestScore + 1) + " had the worst roll of "
                + formatRoll(rollsArray[playerWithLowestScore])
                + " and loses a life.");
        System.out.println();
        return playerWithLowestScore;
    }

    /**
     * Calculate ordinal score based on ranking (1 = worst, 21 = best).
     * Used for ranking and EV calculations where rank differences matter.
     */
    public static int countScore(int die1, int die2) {
        // Normalize to ensure low <= high
        int low = Math.min(die1, die2);
        int high = Math.max(die1, die2);

        // México (1,2) - best roll
        if (low == 1 && high == 2) {
            return 21;
        }

        // Pairs (highest to lowest): 6,6=20, 5,5=19, ..., 1,1=15
        if (low == high) {
            return 14 + low;
        }

        // Non-pairs in descending order
        if (high == 6 && low == 5) return 14;
        if (high == 6 && low == 4) return 13;
        if (high == 6 && low == 3) return 12;
        if (high == 6 && low == 2) return 11;
        if (high == 6 && low == 1) return 10;
        if (high == 5 && low == 4) return 9;
        if (high == 5 && low == 3) return 8;
        if (high == 5 && low == 2) return 7;
        if (high == 5 && low == 1) return 6;
        if (high == 4 && low == 3) return 5;
        if (high == 4 && low == 2) return 4;
        if (high == 4 && low == 1) return 3;
        if (high == 3 && low == 2) return 2;
        if (high == 3 && low == 1) return 1;

        // Should never reach here
        return 0;
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

    public static String formatRoll(int[] dice) {
        int die1 = dice[0];
        int die2 = dice[1];

        if ((die1 == 1 && die2 == 2) || (die1 == 2 && die2 == 1)) {
            return "1,2";
        }

        int higher = Math.max(die1, die2);
        int lower = Math.min(die1, die2);
        return higher + "," + lower;
    }
}
