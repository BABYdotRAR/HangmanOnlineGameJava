package online;

import java.net.*;
import java.io.*;

public class PlayerSocket
{
    public static void main(String[] args)
    {
        try
        {
            String serverMsg, response;
            boolean active = true;
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in,"ISO-8859-1"));
            int port = 1234;
            InetAddress host = InetAddress.getByName("localhost");
            Socket socket = new Socket(host, port);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"ISO-8859-1"));
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"ISO-8859-1"));

            serverMsg = serverReader.readLine();
            System.out.println(serverMsg);
            response = inputReader.readLine();
            writer.println(response);
            writer.flush();

            while (active)
            {
                active = Boolean.parseBoolean(serverReader.readLine());

                if(!active)
                {
                    serverMsg = serverReader.readLine();
                    System.out.println(serverMsg);
                    inputReader.close();
                    serverReader.close();
                    writer.close();
                    socket.close();
                    System.exit(0);
                }

                serverMsg = serverReader.readLine();
                System.out.println(serverMsg);
                response = inputReader.readLine();
                writer.println(response);
                writer.flush();
            }

            inputReader.close();
            serverReader.close();
            writer.close();
            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
