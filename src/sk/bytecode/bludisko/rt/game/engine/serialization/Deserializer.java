package sk.bytecode.bludisko.rt.game.engine.serialization;

import sk.bytecode.bludisko.rt.game.engine.serialization.tags.*;

import java.io.NotSerializableException;

public class Deserializer {

    private byte[] data;
    private int offset;

    public Deserializer(byte[] data) {
        this.data = data;
        this.offset = 0;
    }

    public Tag<?> deserialize() throws NotSerializableException {
        int tagIdentifier = data[offset];
        Tag<?> newTag = switch (tagIdentifier) {
            case 0 -> new TerminatorTag();
            case 1 -> this.deserializeByte();
            case 2 -> this.deserializeShort();
            case 3 -> this.deserializeInt();
            case 4 -> this.deserializeLong();
            case 5 -> this.deserializeFloat();
            case 6 -> this.deserializeDouble();
            case 7 -> this.deserializeChar();
            case 8 -> this.deserializeString();
            case 9 -> this.deserializeBoolean();
            case 10, 11 -> this.deserializeObject();
            default -> throw new NotSerializableException("Invalid identifier: " + tagIdentifier);
        };
        this.addOffsetForPrimitiveTypes(newTag, tagIdentifier);

        return newTag;
    }

    private void addOffsetForPrimitiveTypes(Tag<?> newTag, int identifier) {
        if(identifier < 10 && identifier != 8) {
            offset += newTag.length();
        }
    }

    private Tag<?> deserializeByte() throws NotSerializableException {
        byte b = (byte) deserializeBits(this.data, 1, this.offset + 1);
        return new ByteTag(b);
    }

    private Tag<?> deserializeShort() throws NotSerializableException {
        short s = (short) deserializeBits(this.data, 2, this.offset + 1);
        return new ShortTag(s);
    }

    private Tag<?> deserializeInt() throws NotSerializableException {
        int i = (int) deserializeBits(this.data, 4, this.offset + 1);
        return new IntTag(i);
    }

    private Tag<?> deserializeLong() throws NotSerializableException {
        long l = deserializeBits(this.data, 8, this.offset + 1);
        return new LongTag(l);
    }

    private Tag<?> deserializeFloat() throws NotSerializableException {
        int i = (int) deserializeBits(this.data, 4, this.offset + 1);
        float f = Float.intBitsToFloat(i);
        return new FloatTag(f);
    }

    private Tag<?> deserializeDouble() throws NotSerializableException {
        long l = deserializeBits(this.data, 8, this.offset + 1);
        double d = Double.longBitsToDouble(l);
        return new DoubleTag(d);
    }

    private Tag<?> deserializeChar() throws NotSerializableException {
        char c = (char) deserializeBits(this.data, 2, this.offset + 1);
        return new CharTag(c);
    }

    private Tag<?> deserializeString() throws NotSerializableException {
        this.offset += 1; // String marker - 8

        IntTag lengthTag = (IntTag) deserializeInt();
        this.offset += lengthTag.length();

        int length = lengthTag.get();
        byte[] stringBytes = new byte[length];
        System.arraycopy(this.data, offset, stringBytes, 0, length);
        this.offset += length;

        return new StringTag(new String(stringBytes));
    }

    private Tag<?> deserializeBoolean() throws NotSerializableException {
        boolean b = deserializeBits(this.data, 1, this.offset + 1) > 0;
        return new BooleanTag(b);
    }

    private Tag<?> deserializeObject() throws NotSerializableException {
        this.offset += 1; // Object marker - 10/11

        final String className = getObjectClassName();
        final ObjectTag objectTag = createEmptyObjectTagFromClassName(className);

        while(!endOfObject()) {
            var innerTag = this.deserialize();
            objectTag.addChildren(innerTag);
        }
        this.offset += 1; // Terminator - 0

        return objectTag;
    }

    private boolean endOfObject() {
        return this.data[offset] == 0; // 0 = Terminator ID
    }

    private String getObjectClassName() throws NotSerializableException {
        StringTag classNameTag = (StringTag) this.deserialize();
        return classNameTag.get();
    }

    private ObjectTag createEmptyObjectTagFromClassName(String className) throws NotSerializableException {
        try {
            return new ObjectTag(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new NotSerializableException("Class not found: " + className);
        }
    }

    private long deserializeBits(byte[] bytes, int byteCount, int offset) throws NotSerializableException {
        if(byteCount < 0 || byteCount > 8) throw new NotSerializableException("Invalid byte length");

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
