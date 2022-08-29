package online;

import game.GameManager;
import game.Player;

import java.net.*;
import java.io.*;

public class GameServer
{
    public static void main(String[] args)
    {
        try
        {
            int port = 1234;
            ServerSocket server = new ServerSocket(port);
            server.setReuseAddress(true);

            while(true)
            {
                Socket playerSocket = server.accept();
                GameManager manager = new GameManager();
                Player player = new Player();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream(),"ISO-8859-1"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream(),"ISO-8859-1"));

                writer.println("Bienvenido al juego del ahorcado, por favor, seleccione una dificultad (Escriba el número correspondiente):\t\t0.Fácil\t1.Medio\t2.Difícil");
                writer.flush();

                int difficulty = Integer.parseInt(reader.readLine());
                manager.StartGame(difficulty);

                while (player.alive())
                {
                    if(manager.wordCompleted())
                    {
                        writer.println(false);
                        writer.flush();
                        writer.println(manager.printWord() + "\tVidas: " + player.getHP() + "\t¡Felicidades, has ganado!");
                        writer.flush();
                        break;
                    }

                    writer.println(true);
                    writer.println(manager.printWord() + "\tVidas: " + player.getHP() + "\tInserte un caractér:");
                    writer.flush();

                    char c = reader.readLine().charAt(0);

                    if(manager.findChar(c) != 0)
                        player.subtractHP();
                }

                if(!player.alive())
                {
                    writer.println(false);
                    writer.flush();
                    writer.println(manager.printWord() + "\tVidas: " + player.getHP() + "\tHas perdido, la palabra era: " + manager.getWord());
                    writer.flush();
                }

                reader.close();
                writer.close();
                playerSocket.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
