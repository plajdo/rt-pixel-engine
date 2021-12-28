package sk.bytecode.bludisko.rt.game.serialization;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class StringTag extends Tag<String> {

    private final byte[] stringBytes;
    private final IntTag lengthTag;

    public StringTag(String data) {
        super(data);

        this.stringBytes = data.getBytes(StandardCharsets.UTF_8);
        this.lengthTag = new IntTag(stringBytes.length);
    }

    @Override
    public byte id() {
        return 9;
    }

    @Override
    public int length() {
        return 1 + this.lengthTag.length() + this.stringBytes.length;
    }

    @Override
    public byte[] byteData() {
        return ByteBuffer.allocate(this.length())
                .put(this.id())
                .put(this.lengthTag.byteData())
                .put(this.stringBytes)
                .array();
    }

}
