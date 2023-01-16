package non_blocking_sockets;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class NonBlockingClient
{
    public static void main(String[] args)
    {
        NonBlockingClient client = new NonBlockingClient();
        client.setupClient();
        client.runClient();
    }

    Selector selector;
    SocketChannel sc;
    InetSocketAddress address;
    BufferedReader br;
    int playerKey;

    boolean firstTime = true;
    byte playerState;

    void setupClient()
    {
        try
        {
            address = new InetSocketAddress(InetAddress.getByName("localhost"), NonBlockingServer.PORT);
            selector = Selector.open();
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            br = new BufferedReader(new InputStreamReader(System.in,"ISO-8859-1"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void runClient()
    {
        try
        {
            sc.connect(address);
            sc.register(selector, SelectionKey.OP_CONNECT);
            boolean closed = false;

            while(!closed)
            {
                selector.select();
                Iterator<SelectionKey>it =selector.selectedKeys().iterator();
                while(it.hasNext())
                {
                    SelectionKey k = (SelectionKey)it.next();
                    it.remove();
                    if(k.isConnectable()){
                        SocketChannel ch = (SocketChannel)k.channel();
                        if(ch.isConnectionPending()){
                            try
                            {
                                ch.finishConnect();
                                System.out.println("Conexion establecida..");
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        ch.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                        continue;
                    }
                    if(k.isWritable())
                    {
                        SocketChannel channel = (SocketChannel)k.channel();

                        if(firstTime)
                        {
                            firstTime = false;
                            System.out.println("ingrese un ID para empezar:");
                            playerKey = Integer.parseInt(br.readLine());
                            byte state = (byte) 0x02;
                            ByteBuffer bb = ByteBuffer.allocate(1024);
                            byte[] data = {(byte)playerKey, state};
                            bb.clear();
                            bb = ByteBuffer.wrap(data);
                            channel.write(bb);

                            k.interestOps(SelectionKey.OP_READ);
                            continue;
                        }
                        byte keyByte, stateByte;
                        ByteBuffer response;
                        keyByte = (byte) playerKey;

                        if(playerState == (byte) 0x00)
                        {
                            int op = Integer.parseInt(br.readLine());
                            byte opByte = (byte) op;
                            stateByte = (byte) 0;

                            response = ByteBuffer.allocate(1024);
                            response.clear();
                            byte[] data = {keyByte, stateByte, (byte) op};
                            response = ByteBuffer.wrap(data);
                            channel.write(response);
                        }
                        else if(playerState == (byte) 0x01)
                        {
                            String input = br.readLine();
                            byte[] inputBytes = input.getBytes();
                            stateByte = (byte) 1;

                            response = ByteBuffer.allocate(1024);
                            response.clear();
                            byte[] data = new byte[inputBytes.length + 2];
                            data[0] = keyByte;
                            data[1] = stateByte;
                            System.arraycopy(inputBytes, 0, data, 2, inputBytes.length);

                            response = ByteBuffer.wrap(data);
                            channel.write(response);
                        }
                        else
                        {
                            System.out.println("Juego acabado");
                            channel.close();
                            sc.close();
                            closed = true;
                            continue;
                        }
                        k.interestOps(SelectionKey.OP_READ);
                    }
                    else if(k.isReadable())
                    {
                        SocketChannel channel = (SocketChannel)k.channel();
                        ByteBuffer bb = ByteBuffer.allocate(1024);
                        bb.clear();
                        channel.read(bb);
                        bb.flip();

                        byte[] rawData = new byte[bb.remaining()];
                        bb.get(rawData);
                        playerState = rawData[0];
                        byte[] msgData;
                        int dataStartPos = 1;
                        String textToDisplay;

                        if(playerState == (byte) 0x00)
                        {
                            playerKey = rawData[1];
                            dataStartPos = 2;
                        }

                        msgData = new byte[rawData.length - dataStartPos];
                        System.arraycopy(rawData, dataStartPos, msgData, 0, rawData.length - 2);
                        textToDisplay = new String(msgData);

                        System.out.println(textToDisplay);

                        k.interestOps(SelectionKey.OP_WRITE);
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
