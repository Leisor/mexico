## Working implementation of the México dice game

# To run the project, use:
mvn exec:java -Dexec.mainClass="io.github.leisor.App"

# Game rules:

1. Players roll two dice
2. On each turn after first roll the player can choose to either stay or roll again
3. Maximum number of rolls is 3. Last roll is the roll that counts.
4. At the ench of each round, each player's rolls are compared
5. Player holding the worst roll loses a life
6. If player has no lives left, the player is eliminated
7. If only one player remains, that player is the winner
8. If more than one player remain, a new round starts

- Ranking of rolls, from best to worst:
1. 1,2 and 2,1 = México!
2. Pairs, from 6,6 to 1,1
3. Highs, from 6,x to 3,1
4. Kickers from highest to lowest, e.g. 6,5 beats 6,2 and 5,3 beats 5,1

# ToDo:
- previous best roll display
- mehiko-action

# Recent changes
- MÉXICO-message
