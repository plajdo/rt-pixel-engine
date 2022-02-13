package sk.bytecode.bludisko.rt.game.serialization;

import java.nio.ByteBuffer;

public final class BooleanTag extends PrimitiveTag<Boolean> {

    public BooleanTag(Boolean data) {
        super(data);
    }

    @Override
    public byte id() {
        return 8;
    }

    @Override
    public int length() {
        return 1 + 1;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .put(this.data ? (byte) 1 : (byte) 0)
                .array();
    }

}
