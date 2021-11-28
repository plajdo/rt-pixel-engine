package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;

public final class ShortTag extends Tag<Short> {


    public ShortTag(Short data) {
        super(data);
    }

    @Override
    protected byte id() {
        return 2;
    }

    @Override
    protected int length() {
        return 2 + 1;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putShort(data)
                .array();
    }
}
