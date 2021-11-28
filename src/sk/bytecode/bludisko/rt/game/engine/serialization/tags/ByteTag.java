package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;

public final class ByteTag extends Tag<Byte> {

    public ByteTag(Byte data) {
        super(data);
    }

    @Override
    protected byte id() {
        return 1;
    }

    @Override
    protected int length() {
        return 1 + 1;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .put(this.data)
                .array();
    }
}
