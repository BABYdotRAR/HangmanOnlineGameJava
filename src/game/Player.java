package game;

public class Player
{
    private int hP = 6;
    private String hPLevels[] = {"\n\n", " o\n\n", " o\n/\n", " o\n/|\n", " o\n/|\\\n", " o\n/|\\\n/", " o\n/|\\\n/ \\"};

    public int getHP()
    {
        return hP;
    }

    public boolean alive()
    {
        return hP > 0;
    }

    public void subtractHP()
    {
        hP--;
    }

    public String printHP()
    {
        return " |\n" + hPLevels[6 - getHP()];
    }
}
/*
 *   |
 *   o
 *  /|\
 *  / \
 */