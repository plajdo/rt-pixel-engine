package sk.bytecode.bludisko.rt.game.serialization;

import java.nio.ByteBuffer;

public final class IntTag extends PrimitiveTag<Integer> {

    public IntTag(Integer data) {
        super(data);
    }

    @Override
    public byte id() {
        return 3;
    }

    @Override
    public int length() {
        return 1 + 4;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .putInt(data)
                .array();
    }

}
