while (true)
{
    System.Console.WriteLine("Welcome to Game of Mexico");
    System.Console.WriteLine();

    while (true)
    {

        int[] playerDice = new int[] { };

        for (int i = 1; i < 4; i++)
        {
            System.Console.WriteLine("[Enter] Roll dice");
            System.Console.WriteLine("[Space] Stay");
            var keyInfo = System.Console.ReadKey(intercept: true);
            if (keyInfo.Key == ConsoleKey.Enter)
            {
                System.Console.WriteLine();
                System.Console.WriteLine("Roll #" + i);
                System.Console.WriteLine("Rolling...");
                System.Console.WriteLine();

                playerDice = RollDice();
                System.Console.WriteLine("Got:");
                System.Console.WriteLine(String.Join(", ", playerDice));
                System.Console.WriteLine();

            }
            else if (keyInfo.Key == ConsoleKey.Spacebar)
            {
                break;
            }
        }

        System.Console.WriteLine("CPU's turn");
        System.Console.WriteLine();

        int[] CPUDice = new int[] { };

        for (int i = 1; i < 4; i++)
        {
            System.Console.WriteLine();
            System.Console.WriteLine("Roll #" + i);
            System.Console.WriteLine("Rolling...");
            System.Console.WriteLine();

            CPUDice = RollDice();
            System.Console.WriteLine("Got:");
            System.Console.WriteLine(String.Join(", ", CPUDice));
            System.Console.WriteLine();

            if (!CPUContinue(CPUDice, i)) break;

        }


    }
}

static int[] RollDice()
{
    return [2,1];
}

static Boolean CPUContinue(int[] dice, int roll)
{
    switch ((dice[0], dice[1]))
    {
        case(2, 1):
            System.Console.WriteLine("CPU: Mehiko! I'm staying");
            System.Console.WriteLine();
            return false;

        case var (a, b) when a == b:
            System.Console.WriteLine("CPU: Pair! Nice! I'm staying");
            System.Console.WriteLine();
            return false;

        case var (a,b) when a == 6:
            System.Console.WriteLine("That's good enough I guess.. I'm staying");
            System.Console.WriteLine();
            return false;


        case var (a,b) when a < 6 && a != b:
            System.Console.WriteLine("Yeah that's not good enough.. Reroll!");
            System.Console.WriteLine();
            return true;

        default:
            return true;
    }

}
