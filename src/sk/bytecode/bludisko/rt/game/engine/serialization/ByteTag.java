package sk.bytecode.bludisko.rt.game.engine.serialization;

import java.nio.ByteBuffer;

public final class ByteTag extends PrimitiveTag<Byte> {

    public ByteTag(Byte data) {
        super(data);
    }

    @Override
    public byte id() {
        return 1;
    }

    @Override
    public int length() {
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
