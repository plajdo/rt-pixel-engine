package sk.bytecode.bludisko.rt.game.serialization;

import java.io.NotSerializableException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

final class Deserializer<T> {

    private final byte[] data;
    private int offset;

    Deserializer(byte[] data) {
        this.data = data;
        this.offset = 0;
    }

    @SuppressWarnings("unchecked")
    Tag<T> deserialize() throws NotSerializableException {
        int tagIdentifier = data[offset];
        Tag<?> newTag;
        if(tagIdentifier == -1) {
            newTag = new EmptyTag();
        } else if(tagIdentifier == 0) {
            newTag = new TerminatorTag();
        } else if(tagIdentifier == 1) {
            newTag = this.deserializeByte();
        } else if(tagIdentifier == 2) {
            newTag = this.deserializeShort();
        } else if(tagIdentifier == 3) {
            newTag = this.deserializeInt();
        } else if(tagIdentifier == 4){
            newTag = this.deserializeLong();
        } else if(tagIdentifier == 5) {
            newTag = this.deserializeFloat();
        } else if(tagIdentifier == 6) {
            newTag = this.deserializeDouble();
        } else if(tagIdentifier == 7) {
            newTag = this.deserializeChar();
        } else if(tagIdentifier == 8) {
            newTag = this.deserializeBoolean();
        } else if(tagIdentifier == 9) {
            newTag = this.deserializeString();
        } else if(tagIdentifier == 10) {
            newTag = this.deserializeObject();
        } else if(tagIdentifier == 11) {
            newTag = this.deserializeArray();
        } else {
            throw new NotSerializableException("Invalid identifier: " + tagIdentifier);
        }
        this.addOffsetForPrimitiveTypes(newTag, tagIdentifier);

        return (Tag<T>) newTag;
    }

    private void addOffsetForPrimitiveTypes(Tag<?> newTag, int identifier) {
        if(identifier < 9) {
            offset += newTag.length();
        }
    }

    private Tag<Byte> deserializeByte() throws NotSerializableException {
        byte b = (byte) deserializeBits(this.data, 1, this.offset + 1);
        return new ByteTag(b);
    }

    private Tag<Short> deserializeShort() throws NotSerializableException {
        short s = (short) deserializeBits(this.data, 2, this.offset + 1);
        return new ShortTag(s);
    }

    private Tag<Integer> deserializeInt() throws NotSerializableException {
        int i = (int) deserializeBits(this.data, 4, this.offset + 1);
        return new IntTag(i);
    }

    private Tag<Long> deserializeLong() throws NotSerializableException {
        long l = deserializeBits(this.data, 8, this.offset + 1);
        return new LongTag(l);
    }

    private Tag<Float> deserializeFloat() throws NotSerializableException {
        int i = (int) deserializeBits(this.data, 4, this.offset + 1);
        float f = Float.intBitsToFloat(i);
        return new FloatTag(f);
    }

    private Tag<Double> deserializeDouble() throws NotSerializableException {
        long l = deserializeBits(this.data, 8, this.offset + 1);
        double d = Double.longBitsToDouble(l);
        return new DoubleTag(d);
    }

    private Tag<Character> deserializeChar() throws NotSerializableException {
        char c = (char) deserializeBits(this.data, 2, this.offset + 1);
        return new CharTag(c);
    }

    private Tag<Boolean> deserializeBoolean() throws NotSerializableException {
        boolean b = deserializeBits(this.data, 1, this.offset + 1) > 0;
        return new BooleanTag(b);
    }

    private Tag<String> deserializeString() throws NotSerializableException {
        this.offset += 1; // String marker - 8

        IntTag lengthTag = (IntTag) deserializeInt();
        this.offset += lengthTag.length();

        int length = lengthTag.getContent();
        byte[] stringBytes = new byte[length];
        System.arraycopy(this.data, offset, stringBytes, 0, length);
        this.offset += length;

        return new StringTag(new String(stringBytes));
    }

    private Tag<T[]> deserializeArray() throws NotSerializableException {
        final int initialOffset = offset;
        this.offset += 1; // Array marker - 11

        final StringTag typeTag = getTypeTag();
        final T[] emptyGenericArray = arrayForClassName(typeTag.getContent());

        final ArrayList<T> elements = new ArrayList<>();
        while(!endOfObject()) {
            T element = this.deserialize().getContent();
            elements.add(element);
        }

        this.offset += 1; // Terminator - 0

        final byte[] arrayBytes = new byte[offset - initialOffset];
        System.arraycopy(data, initialOffset, arrayBytes, 0, offset - initialOffset);

        return new ArrayTag<>(elements.toArray(emptyGenericArray), typeTag, arrayBytes);
    }

    private Tag<T> deserializeObject() throws NotSerializableException {
        final int initialOffset = offset;
        this.offset += 1; // Object marker - 10

        final StringTag typeTag = getTypeTag();
        final ArrayTag<String> fieldsTag = getFieldsTag();
        final T emptyObject = objectForClassName(typeTag.getContent());

        for(String fieldName : fieldsTag) {
            if(!endOfObject()) {
                Object fieldContent = this.deserialize().getContent();
                injectField(emptyObject, fieldName, fieldContent);
            }
        }

        this.offset += 1;

        final byte[] objectBytes = new byte[offset - initialOffset];
        System.arraycopy(data, initialOffset, objectBytes, 0, offset - initialOffset);

        return new ObjectTag<>(emptyObject, typeTag, fieldsTag, objectBytes);
    }

    private boolean endOfObject() {
        return this.data[offset] == 0; // 0 = Terminator ID
    }

    private StringTag getTypeTag() throws NotSerializableException {
        return (StringTag) this.deserialize();
    }

    private ArrayTag<String> getFieldsTag() throws NotSerializableException {
        return (ArrayTag<String>) this.deserialize();
    }

    @SuppressWarnings("unchecked")
    private T[] arrayForClassName(String className) throws NotSerializableException {
        try {
            return (T[]) Array.newInstance(Class.forName(className), 0);
        } catch(ClassNotFoundException e) {
            throw new NotSerializableException(e.getLocalizedMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private T objectForClassName(String className) throws NotSerializableException {
        try {
            Class<?> forClass = Class.forName(className);

            Constructor<Object> fakeConstructor = Object.class.getConstructor((Class<?>[]) null);
            Class<?> reflectionFactoryClass = Class.forName("sun.reflect.ReflectionFactory");
            Object reflectionFactoryInstance = reflectionFactoryClass.getDeclaredMethod("getReflectionFactory").invoke(null);
            Method serializationConstructorCreatorMethod = reflectionFactoryClass.getDeclaredMethod("newConstructorForSerialization", Class.class, Constructor.class);

            @SuppressWarnings("unchecked")
            Constructor<Object> serializationConstructor = (Constructor<Object>) serializationConstructorCreatorMethod.invoke(reflectionFactoryInstance, forClass, fakeConstructor);
            serializationConstructor.setAccessible(true);

            Object classInstance = serializationConstructor.newInstance((Object[]) null);
            return (T) forClass.cast(classInstance);

        } catch(ReflectiveOperationException e) {
            throw new NotSerializableException(e.getLocalizedMessage());
        }
    }

    private void injectField(Object instance, String fieldName, Object content) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            field.set(instance, content);

        } catch(ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private long deserializeBits(byte[] bytes, int byteCount, int offset) throws NotSerializableException {
        if(byteCount < 0 || byteCount > 8) throw new NotSerializableException("Invalid length");

        long parsedBits = 0L;
        try {
            for(int i = 0; i < byteCount; i++) {
                parsedBits |= Byte.toUnsignedLong(bytes[offset + i]) << (byteCount * 8 - ((i + 1) * 8));
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            throw new NotSerializableException("Invalid input for byte length " + byteCount);
        }

        return parsedBits;
    }

}
