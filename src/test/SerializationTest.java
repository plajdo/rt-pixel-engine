package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.engine.serialization.Serializable;
import sk.bytecode.bludisko.rt.game.engine.serialization.Serializer;
import sk.bytecode.bludisko.rt.game.engine.serialization.tags.IntTag;
import sk.bytecode.bludisko.rt.game.engine.serialization.tags.ObjectTag;
import sk.bytecode.bludisko.rt.game.engine.serialization.tags.StringTag;

import java.util.Arrays;
import java.util.Random;

class SerializationTest {

    private static Random random;

    @BeforeAll
    static void setup() {
        random = new Random();

    }

    @Test
    void writeIntTag() {
        var inputx = random.nextInt(Integer.MAX_VALUE);

        var tag = new IntTag(inputx);

        var output = tag.byteData();

        System.out.println(Arrays.toString(output));

    }

    @Test
    void writeSimpleObjectTag() {
        var tag = new ObjectTag(Class.class);
        var tg1 = new IntTag(5);
        var tg2 = new IntTag(7);
        var tg3 = new IntTag(0xF);

        tag.addChildren(tg1, tg2, tg3);

        var output = tag.byteData();
        var strout = Arrays.toString(output);
        System.out.println(strout);

        assert strout.equals("[10, 8, 3, 0, 0, 0, 15, 106, 97, 118, 97, 46, 108, 97, " +
                "110, 103, 46, 67, 108, 97, 115, 115, 3, 0, 0, 0, 5, 3, 0, 0, 0, 7, 3, 0, 0, 0, 15, 0]");

    }

    @Test
    void writeComplexObjectTag() {
        var tag = new ObjectTag(Class.class);
        var tg1 = new IntTag(5);
        var tg2 = new IntTag(7);
        var tg3 = new ObjectTag(Class.class);
        var tg4 = new IntTag(0xF);
        var tg5 = new IntTag(6);

        tg3.addChildren(tg4);
        tag.addChildren(tg1, tg2, tg3, tg5);

        var output = tag.byteData();
        var strout = Arrays.toString(output);
        System.out.println(strout);

        assert strout.equals("[10, 8, 3, 0, 0, 0, 15, 106, 97, 118, 97, 46, 108, 97, 110, 103, 46, 67, " +
                "108, 97, 115, 115, 3, 0, 0, 0, 5, 3, 0, 0, 0, 7, 10, 8, 3, 0, 0, 0, 15, 106, 97, 118, " +
                "97, 46, 108, 97, 110, 103, 46, 67, 108, 97, 115, 115, 3, 0, 0, 0, 15, 0, 3, 0, 0, 0, 6, 0]");

    }

    @Test
    void writeStringTag() {
        var tag = new StringTag("čínske znaky™");

        var output = tag.byteData();
        var strout = Arrays.toString(output);
        System.out.println(strout);

        assert strout.equals("[8, 3, 0, 0, 0, 17, -60, -115, -61, -83, 110, 115, 107, 101, 32, 122, 110, 97, 107, " +
                "121, -30, -124, -94]");

    }

    @Test
    void write2DIntArray() {
        var array = new int[][] { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };

        var tag = new ObjectTag(array.getClass());
        for(int[] row : array) {
            var rowTag = new ObjectTag(row.getClass());
            for(int element : row) {
                var intTag = new IntTag(element);
                rowTag.addChildren(intTag);
            }
            tag.addChildren(rowTag);
        }

        var output = tag.byteData();
        var strout = Arrays.toString(output);
        System.out.println(strout);

        assert strout.equals("[11, 8, 3, 0, 0, 0, 3, 91, 91, 73, 11, 8, 3, 0, 0, 0, 2, 91, 73," +
                " 3, 0, 0, 0, 1, 3, 0, 0, 0, 2, 3, 0, 0, 0, 3, 0, 11, 8, 3, 0, 0, 0, 2, 91, 73," +
                " 3, 0, 0, 0, 4, 3, 0, 0, 0, 5, 3, 0, 0, 0, 6, 0, 11, 8, 3, 0, 0, 0, 2, 91, 73," +
                " 3, 0, 0, 0, 7, 3, 0, 0, 0, 8, 3, 0, 0, 0, 9, 0, 0]");

    }

    @Test
    void testSerializer() {
        var s = new Serializer();

        class SomeData {
            @Serializable boolean bool = false;
            @Serializable byte b = 100;
            @Serializable short s = 30000;
            @Serializable char ch = 'a';
            @Serializable int i = 56;
            @Serializable long l = 18556789798212L;
            @Serializable float y = -1.24f;
            @Serializable double d = 3.141592656357989;
            @Serializable String z = "Hello";
        }

        class Test {
            @Serializable SomeData data = new SomeData();
            @Serializable String[][] arr = new String[][]{{"ahoj", "h"}, {"ok", "lorem ipsum..."}};
        }

        var result = s.serialize(new Test());

        System.out.println(Arrays.toString(result.byteData()));

    }

}