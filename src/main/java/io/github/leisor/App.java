package io.github.leisor;

public class App {
    public static void main(String[] args) {
        // System.out.println("Hello World!");

        while (true) {
            System.out.println("Welcome to game of México [meh-hee-koh]!");
            System.out.println("Enter number of players");

            System.out.println("Enter number of lives");

            System.out.println("Starting game with " + numPlayers + " players and " + numLives + " lives.");
        }


    }

    private int readNumberOfPlayers() {
        int numPlayers = -1;
        while (numPlayers <= 0) {
            System.out.print("> ");
            String line = scanner.nextLine();
            try {
                numPlayers = Integer.parseInt(line.trim());
                if (numPlayers <= 0) {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again.");
            }
        }
        return numPlayers;
    }

    private int readNumberOfLives() {
        int numLives = -1;
        while (numLives <= 0) {
            System.out.print("> ");
            String line = scanner.nextLine();
            try {
                numLives = Integer.parseInt(line.trim());
                if (numLives <= 0) {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again.");
            }
        }
        return numLives;
    }

    private java.util.Scanner scanner = new java.util.Scanner(System.in);
}
