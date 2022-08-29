package game;

import java.util.*;

public class GameManager
{
    private Difficulty difficulty;
    private HashMap<Difficulty, Word[]> wordsPool;
    private Word currentWord;

    public GameManager()
    {
        wordsPool = new HashMap<>();
        fillPool();
    }

    public void StartGame(int d)
    {
        setDifficulty(d);
        setCurrentWord();
    }

    public int findChar(char c)
    {
        return currentWord.findChar(c);
    }

    public boolean wordCompleted()
    {
        return currentWord.wordCompleted();
    }

    public String printWord()
    {
        int status[] = currentWord.getWordStatus();
        String w = currentWord.getWord();
        String output = "";

        for (int i = 0; i < status.length; i++)
        {
            if(status[i] == 0)
                output = output.concat("_");
            else
                output = output.concat(Character.toString(w.charAt(i)));
        }

        return output;
    }

    public String getWord()
    {
        return currentWord.getWord();
    }

    public void setDifficulty(int d)
    {
        if(d == 0)
            difficulty = Difficulty.EASY;
        else if(d == 1)
            difficulty = Difficulty.MEDIUM;
        else
            difficulty = Difficulty.HARD;
    }

    private void fillPool()
    {
        wordsPool.put(Difficulty.EASY, new Word[] {new Word("carro"), new Word("sol"), new Word("hola")});
        wordsPool.put(Difficulty.MEDIUM, new Word[] {new Word("sinaloa"), new Word("extraordinario"), new Word("teotihuacan")});
        wordsPool.put(Difficulty.HARD, new Word[] {new Word("san luis potosi"), new Word("posado, inmovil, ahi y nada mas"), new Word("in pace requiescat")});
    }

    private void setCurrentWord()
    {
        Random random = new Random();
        int index = random.nextInt(wordsPool.get(difficulty).length);

        currentWord = wordsPool.get(difficulty)[index];
    }
}

