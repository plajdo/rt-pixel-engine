package sk.bytecode.bludisko.rt.game.engine.serialization;

import java.nio.ByteBuffer;

public final class FloatTag extends PrimitiveTag<Float> {

    public FloatTag(Float data) {
        super(data);
    }

    @Override
    public byte id() {
        return 5;
    }

    @Override
    public int length() {
        return 1 + 4;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putFloat(data)
                .array();
    }

}
