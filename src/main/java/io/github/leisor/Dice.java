package io.github.leisor;

public class Dice {
    public static int[] roll() {
        java.util.Random random = new java.util.Random();
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        return new int[]{die1, die2};
    }
}
