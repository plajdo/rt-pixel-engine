package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;

public class FloatTag extends Tag<Float> {

    public FloatTag(Float data) {
        super(data);
    }

    @Override
    protected byte id() {
        return 5;
    }

    @Override
    protected int length() {
        return 4 + 1;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putFloat(data)
                .array();
    }
}
