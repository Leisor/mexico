# Plan for EV calculator

- We need to determine what is the optimal move in each situation:
    - to roll
    - to stay

- Determining optimal move depends on
    - What is the current worst roll of dice rolled by one of the players before him
    - Player's position in the "table"
    - Player's latest roll on the same turn
    - How many rolls the player has remaining, 1 or 2

- Situations where we already know the optimal action:
    - Player's first or second roll is better than the previous worst roll made by any player before him
        - In this case player should always stay
    - Player rolls 1,2 or 2,1
        - Player has best possible roll and should always stay

- Rule of thumb for "optimal" action:
    - Always stay when player rolls 6,2 or 2,6
        - Exception: Player is the last player and 6,2 is not enough to beat the previous worst roll
    - This is not based on any actual calculation, but could be used as base case for analysis
