package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringTag extends Tag<String> {

    private final byte[] stringBytes;
    private final IntTag lengthTag;

    public StringTag(String data) {
        super(data);

        this.stringBytes = data.getBytes(StandardCharsets.UTF_8);
        this.lengthTag = new IntTag(stringBytes.length);
    }

    @Override
    protected byte id() {
        return 8;
    }

    @Override
    protected int length() {
        return this.stringBytes.length + this.lengthTag.length() + 1;
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
