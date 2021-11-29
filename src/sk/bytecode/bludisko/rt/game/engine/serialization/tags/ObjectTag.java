package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public final class ObjectTag extends Tag<Class<?>> {

    private final ArrayList<Tag<?>> subtags;

    public ObjectTag(Class<?> data) {
        super(data);

        this.subtags = new ArrayList<>();

        var classNameTag = new StringTag(data.getName());
        this.addChildren(classNameTag);

    }

    public void addChildren(Tag<?>... children) {
        this.subtags.addAll(Arrays.asList(children));
    }

    @Override
    public byte id() {
        if(this.data.isArray()) return 11;
        return 10;
    }

    @Override
    public int length() {
        return 2; // Header & terminator tags
    }

    @Override
    public byte[] byteData() {
        final ArrayList<byte[]> tagBytes = new ArrayList<>(subtags.size());

        this.subtags.forEach((tag) -> tagBytes.add(tag.byteData()));

        int size = tagBytes.stream().mapToInt(b -> b.length).sum();
        size = size + this.length();

        var mergedTags = ByteBuffer.allocate(size);

        mergedTags.put(this.id());
        tagBytes.forEach(mergedTags::put);
        mergedTags.put(new TerminatorTag().byteData());

        return mergedTags.array();
    }

}
