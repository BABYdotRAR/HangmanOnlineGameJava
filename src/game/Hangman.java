package game;

import java.util.Scanner;

public class Hangman
{
    public static void main(String[] args)
    {
        GameManager manager = new GameManager();
        Player player = new Player();
        Scanner scanner = new Scanner(System.in);
        int difficulty = 2;

        manager.StartGame(difficulty);

        while (player.alive())
        {
            if(manager.wordCompleted())
            {
                System.out.println(manager.printWord());
                System.out.println(player.printHP());
                System.out.println("Congratulations, you've won!");
                break;
            }

            System.out.println(manager.printWord());
            System.out.println(player.printHP());
            System.out.println("Insert a character: ");
            char c = scanner.nextLine().charAt(0);

            if(manager.findChar(c) != 0)
                player.subtractHP();
        }

        if(!player.alive())
        {
            System.out.println(player.printHP());
            System.out.println("The word was: " + manager.getWord());
            System.out.println("You've lose.");
        }
    }
}
