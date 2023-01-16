package testing;

import java.nio.ByteBuffer;

public class TestBytes
{
    public static void main(String[] args) {
        Integer test = 0;
        byte b = test.byteValue();

        System.out.println((int)b);

        byte[] bytearray = new byte[] { (byte) 0x65, (byte)0x10, (byte)0xf3, (byte)0x29};
        byte b1 = (byte) 0xFF;
        ByteBuffer bb = ByteBuffer.allocate(bytearray.length + 1);
        bb.put(b1).put(bytearray);

        for (byte b2: bb.array()) {
            System.out.format("0x%x ", b2);
        }
        Boolean a = false;
    }
}
