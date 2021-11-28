package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public final class ObjectTag extends Tag<Class<?>> {

    private final ArrayList<Tag<?>> subtags;

    public ObjectTag(Class<?> data) {
        super(data);

        this.subtags = new ArrayList<>();

    }

    public void addChildren(Tag<?>... children) {
        subtags.addAll(Arrays.asList(children));
    }

    @Override
    protected byte id() {
        return 10;
    }

    @Override
    protected int length() {
        return 2;
    }

    @Override
    public byte[] byteData() {
        final ArrayList<byte[]> tags = new ArrayList<>(subtags.size());

        subtags.forEach((tag) -> {
            tags.add(tag.byteData());
        });

        int size = tags.stream().mapToInt(b -> b.length).sum();
        size = size + 2;

        var mergedTags = ByteBuffer.allocate(size);

        mergedTags.put(this.id());
        tags.forEach(mergedTags::put);
        mergedTags.put(new TerminatorTag().byteData());

        return mergedTags.array();
    }

}
