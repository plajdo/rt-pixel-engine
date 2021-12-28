package sk.bytecode.bludisko.rt.game.serialization;

import java.io.NotSerializableException;

public abstract sealed class Tag<T> permits EmptyTag, TerminatorTag, PrimitiveTag, StringTag, ArrayTag, ObjectTag {

    protected final T data;
    protected Tag(T data) {
        this.data = data;
    }

    public abstract byte id();
    public abstract int length();

    public abstract byte[] byteData();
    public T getContent() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public static <T> Tag<?> fromObject(T o) {
        return switch(o) {
            case null -> new EmptyTag();
            case Number n -> PrimitiveTag.fromNumber(n);
            case Character c -> new CharTag(c);
            case Boolean b -> new BooleanTag(b);
            case String s -> new StringTag(s);
            case Object obj && obj.getClass().isArray() -> new ArrayTag<>((T[]) obj);
            case T obj -> new ObjectTag<>(obj);
        };
    }

    public static <T> Tag<T> fromBytes(byte[] bytes, Class<T> type) throws NotSerializableException {
        var deserializer = new Deserializer<T>(bytes);
        return deserializer.deserialize();
    }

}
