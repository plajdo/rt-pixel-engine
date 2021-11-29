package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;

public final class LongTag extends Tag<Long> {

    public LongTag(Long data) {
        super(data);
    }

    @Override
    public byte id() {
        return 4;
    }

    @Override
    public int length() {
        return 8 + 1;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putLong(data)
                .array();
    }
}
