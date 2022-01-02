package test;

import sk.bytecode.bludisko.rt.game.serialization.Serializable;
import sk.bytecode.bludisko.rt.game.serialization.*;

import org.junit.jupiter.api.Test;

import java.io.NotSerializableException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class Serialization2Test {

    @Test
    void serializePrimitives() {
        var f = 2.5f;
        var s = "Ahoj";
        var l = 36L;
        var b = true;
        var c = 'h';

        var ftag = new FloatTag(f);
        var stag = new StringTag(s);
        var ltag = new LongTag(l);
        var btag = new BooleanTag(b);
        var ctag = new CharTag(c);

        var x = ltag.byteData();

        assert Arrays.equals(ftag.byteData(), new byte[]{5, 64, 32, 0, 0});
        assert Arrays.equals(stag.byteData(), new byte[]{9, 3, 0, 0, 0, 4, 65, 104, 111, 106});
        assert Arrays.equals(ltag.byteData(), new byte[]{4, 0, 0, 0, 0, 0, 0, 0, 36});
        assert Arrays.equals(btag.byteData(), new byte[]{8, 1});
        assert Arrays.equals(ctag.byteData(), new byte[]{7, 0, 104});

    }

    @Test
    void serializeArray() {
        var array = new Integer[] {0, 123, 3, 4, 2, 5, 6};
        var tag = new ArrayTag<>(array);

        var stringArray = new String[] { "mama", "uuu", "didn't mean", "to make you cry" };
        var stringArrayTag = new ArrayTag<>(stringArray);

        var boolArr = new Boolean[] {};
        var boolTag = new ArrayTag<>(boolArr);

        var bytes = tag.byteData();
        System.out.println(Arrays.toString(bytes));

        var bytes2 = stringArrayTag.byteData();
        System.out.println(Arrays.toString(bytes2));

        var bytes3 = boolTag.byteData();
        System.out.println(Arrays.toString(bytes3));
    }

    @Test
    void serializeNullArray() {
        try {
            Integer[] arr = null;
            var tag = new ArrayTag<>(arr);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        Integer[] arr = null;
        var tag = Tag.fromObject(arr);
        assert tag.id() == new EmptyTag().id();
    }

    // -----------------------------
    static class NullArray {
        @Serializable boolean b = false;
        @Serializable char c = 'h';
        @Serializable Byte[] barr = {0x15, 0x03, 0x7F, 0x5C};
        @Serializable String[] sarr = null;
    }

    static class TestPlaceholder {
        @Serializable int x = 7;
        @Serializable float f = 6.35f;
        @Serializable long l = 12345678987654321L;
        @Serializable double d = 53.4d;
        @Serializable String s = "Bohemian rhapsody";
        @Serializable NullArray[] arr = { new NullArray(), null, new NullArray() };
    }

    @Test
    void serializeObject() {

        var placeholder = new TestPlaceholder();
        var placeholderTag = new ObjectTag<>(placeholder);

        var bytes = placeholderTag.byteData();
        System.out.println(Arrays.toString(bytes));

    }
    // -----------------------------

    @Test
    void arrayByteCopyTest() throws NotSerializableException {
        Integer[] x = {0, 1, 2, 3, 4, 5};
        var tag = new ArrayTag<>(x);

        var bytes = tag.byteData();
        var newTag = Tag.fromBytes(bytes, Integer[].class);

        assert Arrays.equals(tag.byteData(), newTag.byteData());
    }

}
