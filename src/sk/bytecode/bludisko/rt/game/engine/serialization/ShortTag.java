package sk.bytecode.bludisko.rt.game.engine.serialization;

import java.nio.ByteBuffer;

public final class ShortTag extends PrimitiveTag<Short> {

    public ShortTag(Short data) {
        super(data);
    }

    @Override
    public byte id() {
        return 2;
    }

    @Override
    public int length() {
        return 1 + 2;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putShort(this.data)
                .array();
    }

}
