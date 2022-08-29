package testing;

import game.Word;

public class Tests
{
    public static void main(String[] args)
    {
        Word word = new Word("oso");
        int find;
        int status[] = word.getWordStatus();
        String w = word.getWord();
        for (int i = 0; i < status.length; i++)
        {
            if(status[i] == 0)
                System.out.print("_");
            else
                System.out.print(w.charAt(i));
        }
        System.out.println("\nWord Completed: " + word.wordCompleted());
        System.out.println();

        find = word.findChar('o');
        status = word.getWordStatus();

        for (int i = 0; i < status.length; i++)
        {
            if(status[i] == 0)
                System.out.print("_");
            else
                System.out.print(w.charAt(i));
        }
        System.out.println("\nWord Completed: " + word.wordCompleted());
        System.out.println();

        find = word.findChar('s');
        status = word.getWordStatus();

        for (int i = 0; i < status.length; i++)
        {
            if(status[i] == 0)
                System.out.print("_");
            else
                System.out.print(w.charAt(i));
        }
        System.out.println("\nWord Completed: " + word.wordCompleted());
        System.out.println();

        find = word.findChar('o');
        status = word.getWordStatus();

        for (int i = 0; i < status.length; i++)
        {
            if(status[i] == 0)
                System.out.print("_");
            else
                System.out.print(w.charAt(i));
        }
        System.out.println("\nWord Completed: " + word.wordCompleted());
        System.out.println();
    }
}
