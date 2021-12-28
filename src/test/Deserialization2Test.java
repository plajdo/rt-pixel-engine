package test;

import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.serialization.Serializable;
import sk.bytecode.bludisko.rt.game.serialization.Tag;

import java.io.NotSerializableException;
import java.util.Arrays;
import java.util.Objects;

public class Deserialization2Test {

    @Test
    void deserializePrimitives() throws NotSerializableException {
        byte[] input = new byte[] { 1, 127 };
        assert Tag.fromBytes(input, Byte.TYPE).getContent() == 127;

        input = new byte[] { 2, 0, 114 };
        assert Tag.fromBytes(input, Short.TYPE).getContent() == 114;

        input = new byte[] { 3, 0, 0, -1, -1 };
        assert Tag.fromBytes(input, Integer.TYPE).getContent() == 65535;

        input = new byte[] { 4, 0, 0, 0, 0, 0, 0, 0, 5 };
        assert Tag.fromBytes(input, Long.TYPE).getContent() == 5L;

        input = new byte[] { 8, 0 };
        assert !(Tag.fromBytes(input, Boolean.TYPE).getContent());

        input = new byte[] { 9, 3, 0, 0, 0, 2, 97, 104 };
        assert Objects.equals(Tag.fromBytes(input, String.class).getContent(), "ah");
    }

    @Test
    void deserializeArray() throws NotSerializableException {
        byte[] input = new byte[] { 11, 9, 3, 0, 0, 0, 17, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 73, 110, 116,
                101, 103, 101, 114, 3, 0, 0, 0, 17, 3, 0, 0, 0, 36, 3, 0, 0, -39, 113, 3, 0, 0, 0, 123, 3, -1, -1, -1,
                -17, 3, 0, 0, 0, 54, 3, -1, -1, -1, -36, 3, 0, 0, 0, 89, 3, 0, 0, 0, 72, 0 };

        Integer[] x = Tag.fromBytes(input, Integer[].class).getContent();

    }

    @Test
    void deserializeObject() throws NotSerializableException {
        byte[] input = new byte[] { 10, 9, 3, 0, 0, 0, 39, 116, 101, 115, 116, 46, 83, 101, 114, 105, 97, 108, 105,
                122, 97, 116, 105, 111, 110, 50, 84, 101, 115, 116, 36, 84, 101, 115, 116, 80, 108, 97, 99, 101, 104,
                111, 108, 100, 101, 114, 11, 9, 3, 0, 0, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116,
                114, 105, 110, 103, 9, 3, 0, 0, 0, 1, 120, 9, 3, 0, 0, 0, 1, 102, 9, 3, 0, 0, 0, 1, 108, 9, 3, 0, 0,
                0, 1, 100, 9, 3, 0, 0, 0, 1, 115, 9, 3, 0, 0, 0, 3, 97, 114, 114, 0, 3, 0, 0, 0, 7, 5, 64, -53, 51, 51,
                4, 0, 43, -36, 84, 98, -111, -12, -79, 6, 64, 74, -77, 51, 51, 51, 51, 51, 9, 3, 0, 0, 0, 17, 66, 111,
                104, 101, 109, 105, 97, 110, 32, 114, 104, 97, 112, 115, 111, 100, 121, 11, 9, 3, 0, 0, 0, 33, 116,
                101, 115, 116, 46, 83, 101, 114, 105, 97, 108, 105, 122, 97, 116, 105, 111, 110, 50, 84, 101, 115, 116,
                36, 78, 117, 108, 108, 65, 114, 114, 97, 121, 10, 9, 3, 0, 0, 0, 33, 116, 101, 115, 116, 46, 83, 101,
                114, 105, 97, 108, 105, 122, 97, 116, 105, 111, 110, 50, 84, 101, 115, 116, 36, 78, 117, 108, 108, 65,
                114, 114, 97, 121, 11, 9, 3, 0, 0, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 83, 116, 114
                , 105, 110, 103, 9, 3, 0, 0, 0, 1, 98, 9, 3, 0, 0, 0, 1, 99, 9, 3, 0, 0, 0, 4, 98, 97, 114, 114, 9, 3,
                0, 0, 0, 4, 115, 97, 114, 114, 0, 8, 0, 7, 0, 104, 11, 9, 3, 0, 0, 0, 14, 106, 97, 118, 97, 46, 108,
                97, 110, 103, 46, 66, 121, 116, 101, 1, 21, 1, 3, 1, 127, 1, 92, 0, 0, 10, 9, 3, 0, 0, 0, 33, 116, 101,
                115, 116, 46, 83, 101, 114, 105, 97, 108, 105, 122, 97, 116, 105, 111, 110, 50, 84, 101, 115, 116, 36,
                78, 117, 108, 108, 65, 114, 114, 97, 121, 11, 9, 3, 0, 0, 0, 16, 106, 97, 118, 97, 46, 108, 97, 110,
                103, 46, 83, 116, 114, 105, 110, 103, 9, 3, 0, 0, 0, 1, 98, 9, 3, 0, 0, 0, 1, 99, 9, 3, 0, 0, 0, 4, 98,
                97, 114, 114, 9, 3, 0, 0, 0, 4, 115, 97, 114, 114, 0, 8, 0, 7, 0, 104, 11, 9, 3, 0, 0, 0, 14, 106, 97,
                118, 97, 46, 108, 97, 110, 103, 46, 66, 121, 116, 101, 1, 21, 1, 3, 1, 127, 1, 92, 0, 0, 0, 0 };

        var obj = Tag.fromBytes(input, Serialization2Test.TestPlaceholder.class);
        var content = obj.getContent();

        assert content.d == 53.4;
    }

    static class TestObject {
        @Serializable int x = 8;
        @Serializable Object o = null;
        @Serializable float f = 4.5f;
    }

    @Test
    void deserializeNullField() throws NotSerializableException {
        var input = Tag.fromObject(new TestObject()).byteData();
        System.out.println(Arrays.toString(input));

        var obj = Tag.fromBytes(input, TestObject.class);
        var content = obj.getContent();

        assert content.x == 8;
        assert content.f == 4.5f;
        assert content.o == null;
    }

}
