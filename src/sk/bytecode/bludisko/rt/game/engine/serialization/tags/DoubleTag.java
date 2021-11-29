package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;

public final class DoubleTag extends Tag<Double> {

    public DoubleTag(Double data) {
        super(data);
    }

    @Override
    public byte id() {
        return 6;
    }

    @Override
    public int length() {
        return 8 + 1;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putDouble(data)
                .array();
    }
}
