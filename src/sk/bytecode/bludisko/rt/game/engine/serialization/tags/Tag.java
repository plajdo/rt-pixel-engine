package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

public abstract sealed class Tag<T> permits BooleanTag, ByteTag, CharTag, DoubleTag, FloatTag, IntTag, LongTag, ObjectTag, ShortTag, StringTag, TerminatorTag {

    protected final T data;

    protected Tag(T data) {
        this.data = data;
    }

    public abstract byte id();
    public abstract int length();

    public T get() {
        return data;
    }

    public abstract byte[] byteData();

}
