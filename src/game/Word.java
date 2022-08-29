package game;

import java.util.*;

public class Word
{
    private String w;
    private int wordStatus[];
    private int currentCharsEnabled;
    private final double PERCENTAGE_OF_CHARS_ENABLED = 0.17;
    private HashMap<Character, ArrayList<Integer>> wordTable = new HashMap<Character, ArrayList<Integer>>();

    public Word(String w)
    {
        this.w = w;
        initWord();
    }

    private void initWord()
    {
        w = w.toLowerCase();
        initWordStatus();
        initWordTable();
    }

    private void initWordStatus()
    {
        wordStatus = new int[w.length()];
        currentCharsEnabled = 0;

        for (int i = 0; i < w.length(); i++)
            wordStatus[i] = 0;

        if(w.length() > 5)
        {
            int numberOfCharsEnabled = (int)(w.length() * PERCENTAGE_OF_CHARS_ENABLED);
            currentCharsEnabled += numberOfCharsEnabled;
            Random random = new Random();
            ArrayList<Integer> charsEnabled = new ArrayList<>();

            for (int i = 0; i < numberOfCharsEnabled; i++)
            {
                int randomIndex = random.nextInt(w.length());

                while(charsEnabled.contains(randomIndex))
                    randomIndex = random.nextInt(w.length());

                charsEnabled.add(randomIndex);
            }

            for (int i = 0; i < numberOfCharsEnabled; i++)
                wordStatus[charsEnabled.get(i)] = 1;
        }
    }

    private void initWordTable()
    {
        fillTableEntry(0);
    }

    private void fillTableEntry(int currentIndex)
    {
        if(currentIndex >= w.length())
            return;

        char c = w.charAt(currentIndex);

        if(wordTable.containsKey(c))
        {
            fillTableEntry(currentIndex + 1);
            return;
        }

        ArrayList<Integer> concurrences = new ArrayList<Integer>();

        for(int i = currentIndex; i < w.length(); i++)
        {
            if(w.charAt(i) == c)
                concurrences.add(i);
        }

        wordTable.put(c, concurrences);
        fillTableEntry(currentIndex + 1);
    }

    private void updateWordStatus(char c)
    {
        ArrayList<Integer> concurrences = wordTable.get(c);

        for(int i = 0; i < concurrences.size(); i++)
        {
            if(wordStatus[concurrences.get(i)] != 1)
            {
                currentCharsEnabled++;
                wordStatus[concurrences.get(i)] = 1;
            }
        }
    }

    public int findChar(char c)
    {
        if(wordCompleted())
            return 0;

        if(wordTable.containsKey(c))
        {
            updateWordStatus(c);
            return 0;
        }
        else
            return -1;
    }

    public boolean wordCompleted()
    {
        return currentCharsEnabled == w.length();
    }

    public int[] getWordStatus ()
    {
        return wordStatus;
    }

    public String getWord()
    {
        return w;
    }
}
