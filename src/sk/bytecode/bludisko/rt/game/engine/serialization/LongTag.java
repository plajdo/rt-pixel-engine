package sk.bytecode.bludisko.rt.game.engine.serialization;

import java.nio.ByteBuffer;

public final class LongTag extends PrimitiveTag<Long> {

    public LongTag(Long data) {
        super(data);
    }

    @Override
    public byte id() {
        return 4;
    }

    @Override
    public int length() {
        return 1 + 8;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putLong(data)
                .array();
    }

}
