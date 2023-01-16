package non_blocking_sockets;

import game.GameManager;
import game.Player;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class NonBlockingServer
{
    public static void main(String[] args)
    {
        NonBlockingServer server = new NonBlockingServer();
        server.setupServer();
        server.runServer();
    }

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    public static final int PORT = 1234;

    void setupServer()
    {
        try
        {
            InetAddress host = InetAddress.getByName("localhost");
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.bind(new InetSocketAddress(host, PORT));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    void runServer()
    {
        try
        {
            SelectionKey key;
            Integer playerKey = 0;
            HashMap<Integer, GameManager> managers = new HashMap<>();
            HashMap<Integer, Player> players = new HashMap<>();

            while (true)
            {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext())
                {
                    key = (SelectionKey) iterator.next();
                    iterator.remove();
                    if(key.isAcceptable())
                    {
                        SocketChannel sc = serverSocketChannel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connection Accepted: " + sc.getLocalAddress() + "n");
                        continue;
                    }
                    if (key.isReadable())
                    {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer bb = ByteBuffer.allocate(1024);
                        bb.clear();
                        int temp = sc.read(bb);
                        bb.flip();

                        byte playerState;
                        String msg;
                        byte[] msgBytes;
                        ByteBuffer response;

                        byte[] rawData = new byte[bb.remaining()];
                        byte[] msgData = new byte[bb.remaining() - 2];
                        bb.get(rawData);
                        int pKey = rawData[0];
                        int state = rawData[1];
                        System.out.println("respondiendo al jugador con ID: " + pKey);

                        if(state == 2)
                        {
                            managers.put(pKey, new GameManager());
                            players.put(pKey, new Player());
                            msg = "Bienvenido al juego del ahorcado, por favor, seleccione una dificultad (Escriba el número correspondiente):\t\t0.Fácil\t1.Medio\t2.Difícil";
                            msgBytes = msg.getBytes();
                            byte keyByte = (byte)pKey;
                            playerState = (byte) 0x00;
                            int resLength = msgBytes.length + 2;

                            response = ByteBuffer.allocate(1024);
                            response.clear();
                            byte[] data = new byte[resLength];
                            data[0] = playerState;
                            data[1] = keyByte;
                            System.arraycopy(msgBytes, 0, data, 2, msgBytes.length);
                            response = ByteBuffer.wrap(data);

                            sc.write(response);
                            continue;
                        }
                        System.arraycopy(rawData, 2, msgData, 0, msgData.length);

                        GameManager manager = managers.get(pKey);
                        Player player = players.get(pKey);

                        if(state == 0)
                        {
                            int difficulty = msgData[0];
                            manager.StartGame(difficulty);

                            playerState = (byte) 0x01;
                            msg = manager.printWord() + "\tVidas: " + player.getHP() + "\tInserte un caractér:";
                            msgBytes = msg.getBytes();

                            response = ByteBuffer.allocate(1024);
                            response.clear();
                            byte[] data = new byte[msgBytes.length + 1];
                            data[0] = playerState;
                            System.arraycopy(msgBytes, 0, data, 1, msgBytes.length);
                            response = ByteBuffer.wrap(data);
                            sc.write(response);
                            continue;
                        }

                        String character = new String(msgData);
                        if(manager.findChar(character.charAt(0)) != 0)
                            player.subtractHP();

                        if(manager.wordCompleted())
                        {
                            playerState = (byte) 0x02;
                            msg = manager.printWord() + "\tVidas: " + player.getHP() + "\t¡Felicidades, has ganado!";
                        }
                        else if (!player.alive())
                        {
                            playerState = (byte) 0x03;
                            msg = manager.printWord() + "\tVidas: " + player.getHP() + "\tHas perdido, la palabra era: " + manager.getWord();
                        }
                        else
                        {
                            playerState = (byte) 0x01;
                            msg = manager.printWord() + "\tVidas: " + player.getHP() + "\tInserte un caractér:";
                        }

                        msgBytes = msg.getBytes();
                        response = ByteBuffer.allocate(1024);
                        response.clear();
                        byte[] data = new byte[msgBytes.length + 1];
                        data[0] = playerState;
                        System.arraycopy(msgBytes, 0, data, 1, msgBytes.length);
                        response = ByteBuffer.wrap(data);
                        sc.write(response);

                        if(manager.wordCompleted() || !player.alive())
                            sc.close();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
