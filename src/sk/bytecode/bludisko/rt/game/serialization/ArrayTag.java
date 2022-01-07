package sk.bytecode.bludisko.rt.game.serialization;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class ArrayTag<T> extends Tag<T[]> implements Iterable<T> {

    private final StringTag typeTag;
    private final byte[] bytes;

    // MARK: - Constructor

    ArrayTag(T[] data, StringTag typeTag, byte[] bytes) {
        super(data);

        this.typeTag = typeTag;
        this.bytes = bytes;
    }

    public ArrayTag(@NotNull T[] data) {
        super(data);

        this.typeTag = new StringTag(data.getClass().componentType().getName());
        this.bytes = this.getBytes();
    }

    // MARK: - Override

    @Override
    public byte id() {
        return 11;
    }

    @Override
    public int length() {
        return this.bytes.length;
    }

    @Override
    public byte[] byteData() {
        return this.bytes;
    }

    // MARK: - Private

    private byte[] getBytes() {
        final ArrayList<byte[]> subtagBytes = new ArrayList<>(data.length);
        for (T tag : data) {
            subtagBytes.add(Tag.fromObject(tag).byteData());
        }

        var finalSize = subtagBytes.stream()
                .mapToInt(e -> e.length)
                .sum()
                + this.typeTag.length()
                + 2; // Header & terminator tags

        var finalBytes = ByteBuffer.allocate(finalSize);
        finalBytes.put(this.id());
        finalBytes.put(this.typeTag.byteData());
        subtagBytes.forEach(finalBytes::put);
        finalBytes.put(new TerminatorTag().byteData());

        return finalBytes.array();
    }

    // MARK: - Iterable

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(data).iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Arrays.stream(data).forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Arrays.stream(data).spliterator();
    }

}
