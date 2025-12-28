package io.github.leisor;

public class App {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            System.out.println("Welcome to game of México [meh-hee-koh]!");
            System.out.println();
            System.out.println("Enter number of players");
            int numPlayers = readNumberOfPlayers(scanner);
            System.out.println("Enter number of AI players (0 to " + (numPlayers - 1) + ")");
            int numAIPlayers = readNumberOfAIPlayers(scanner, numPlayers);
            System.out.println("Enter number of lives");
            int numLives = readNumberOfLives(scanner);
            System.out.println();

            Game game = new Game(numPlayers, numAIPlayers, numLives);
            game.start();

            System.out.println("Game over. Do you want to play again? (y/n)");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("y")) {
                System.out.println("Thank you for playing! Goodbye.");
                break;
            }
        }
        scanner.close();
    }

    private static int readNumberOfPlayers(java.util.Scanner scanner) {
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

    private static int readNumberOfAIPlayers(java.util.Scanner scanner, int totalPlayers) {
        int numAI = -1;
        while (numAI < 0 || numAI > totalPlayers) {
            System.out.print("> ");
            String line = scanner.nextLine();
            try {
                numAI = Integer.parseInt(line.trim());
                if (numAI < 0 || numAI > totalPlayers) {
                    System.out.println("Please enter a number between 0 and " + totalPlayers);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, try again.");
            }
        }
        return numAI;
    }

    private static int readNumberOfLives(java.util.Scanner scanner) {
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
}
