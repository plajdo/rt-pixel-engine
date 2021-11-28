package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;

public final class CharTag extends Tag<Character> {

    public CharTag(Character data) {
        super(data);
    }

    @Override
    protected byte id() {
        return 7;
    }

    @Override
    protected int length() {
        return 2 + 1;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putChar(data)
                .array();
    }
}
