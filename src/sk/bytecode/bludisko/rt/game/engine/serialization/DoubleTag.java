package sk.bytecode.bludisko.rt.game.engine.serialization;

import java.nio.ByteBuffer;

public final class DoubleTag extends PrimitiveTag<Double> {

    public DoubleTag(Double data) {
        super(data);
    }

    @Override
    public byte id() {
        return 6;
    }

    @Override
    public int length() {
        return 1 + 8;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putDouble(data)
                .array();
    }

}
