package sk.bytecode.bludisko.rt.game.serialization;

import java.nio.ByteBuffer;

public final class CharTag extends PrimitiveTag<Character> {

    public CharTag(Character data) {
        super(data);
    }

    @Override
    public byte id() {
        return 7;
    }

    @Override
    public int length() {
        return 1 + 2;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putChar(data)
                .array();
    }

}
