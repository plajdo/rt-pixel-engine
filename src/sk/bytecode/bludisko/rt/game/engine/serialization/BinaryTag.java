package sk.bytecode.bludisko.rt.game.engine.serialization;

import sk.bytecode.bludisko.rt.game.engine.serialization.tags.Tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BinaryTag {

    private final ByteArrayOutputStream outputStream;

    public BinaryTag() {
        this.outputStream = new ByteArrayOutputStream();
    }

    public ByteArrayOutputStream getOutputStream() {
        return this.outputStream;
    }

    public void write(Tag<?> tag) throws IOException {
        this.outputStream.write(tag.byteData());

    }

}
