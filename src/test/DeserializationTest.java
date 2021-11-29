package test;

import org.junit.jupiter.api.Test;
import sk.bytecode.bludisko.rt.game.engine.serialization.Deserializer;
import sk.bytecode.bludisko.rt.game.engine.serialization.Serializable;
import sk.bytecode.bludisko.rt.game.engine.serialization.Serializer;
import sk.bytecode.bludisko.rt.game.engine.serialization.tags.*;

import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class DeserializationTest {

    @Test
    void deserializeInt() throws NotSerializableException {
        var data = 123456;

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize();

        assert ((IntTag) tag).get() == data;

    }

    @Test
    void deserializeLong() throws NotSerializableException {
        var data = 1234567898765L;

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize();

        assert ((LongTag) tag).get() == data;

    }

    @Test
    void deserializeBoolean() throws NotSerializableException {
        var data = true;

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize();

        assert ((BooleanTag) tag).get() == data;

    }

    @Test
    void deserializeFloat() throws NotSerializableException {
        var data = 153.68f;

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize();

        assert ((FloatTag) tag).get() == data;

    }

    @Test
    void deserializeObject() throws NotSerializableException {
        class AnotherTestObject {
            @Serializable String text1 = "Lorem ipsum...";
            @Serializable double d = 56d;
            @Serializable String text2 = "...dolor sit amet...";
        }

        class TestObject {
            @Serializable int x = 15;
            @Serializable AnotherTestObject anotherTestObject = new AnotherTestObject();
            @Serializable float z = 3.14156f;
            @Serializable String text = "Hello";
        }

        var data = new TestObject();

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize().byteData();

        // System.out.println(Arrays.toString(tag));
        // System.out.println(new String(tag));
        assert Arrays.equals(bytes, tag);
    }

    @Test
    void deserializeArray() throws NotSerializableException {
        var data = new int[] { 1, 2, 3 };

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize().byteData();

        // System.out.println(Arrays.toString(tag));
        // System.out.println(new String(tag));

        assert Arrays.equals(bytes, tag);
    }

    @Test
    void deserialize2DArray() throws NotSerializableException {
        var data = new int[][] {{1, 2, 3}, {4}, {5, 6, 7}};

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize().byteData();

        // System.out.println(Arrays.toString(tag));
        // System.out.println(new String(tag));

        assert Arrays.equals(bytes, tag);
    }

    @Test
    void deserializeComplexObject() throws NotSerializableException {
        class AnotherTestObject {
            @Serializable String text1 = "Lorem ipsum...";
            @Serializable double d = 56d;
            @Serializable String text2 = "...dolor sit amet...";
            @Serializable long[][] x = new long[3][3];
        }

        class TestObject {
            @Serializable int x = 15;
            @Serializable AnotherTestObject anotherTestObject = new AnotherTestObject();
            @Serializable float z = 3.14156f;
            @Serializable char c = 'f';
            @Serializable short d = 32568;
            @Serializable boolean[] boolarr = new boolean[]{ false, false, true, false };
            @Serializable String text = "Hello";
        }

        var data = new TestObject();

        var s = new Serializer();
        var bytes = s.serialize(data).byteData();

        var d = new Deserializer(bytes);
        var tag = d.deserialize().byteData();

        // System.out.println(Arrays.toString(tag));
        // System.out.println(new String(tag));

        assert Arrays.equals(bytes, tag);
    }

    class TestObject {
        int x = 4;
        float h = 5.3f;
    }
    @Test
    void classCreationTest() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //or simply: throws ReflectiveOperationException

        var classToInstantiate = Class.forName("test.DeserializationTest$TestObject");

        var fakeConstructor = Object.class.getConstructor((Class<?>[]) null);
        var reflectionFactoryClass = Class.forName("sun.reflect.ReflectionFactory");
        var reflectionFactory = reflectionFactoryClass.getDeclaredMethod("getReflectionFactory").invoke(null);

        var constructorCreator = reflectionFactoryClass.getDeclaredMethod("newConstructorForSerialization", Class.class, Constructor.class);

        @SuppressWarnings("unchecked")
        var constructor = (Constructor<Object>) constructorCreator.invoke(reflectionFactory, classToInstantiate, fakeConstructor);
        constructor.setAccessible(true);

        var instance = constructor.newInstance((Object[]) null);

        var castedInstance = classToInstantiate.cast(instance);

        System.out.println(castedInstance);

    }

}
