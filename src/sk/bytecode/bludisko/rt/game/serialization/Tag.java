package sk.bytecode.bludisko.rt.game.serialization;

import java.io.NotSerializableException;

/**
 * <p>Class representing the main Facade of the Tag serialization API.
 * Tag is sealed to allow easier iterating over its children.</p>
 *
 * <p>Tag serialization is a type of serialization, where an object is
 * decomposed into smaller parts, called sub-tags. Each sub-tag
 * can then be decomposed further into coresponding byte arrays.</p>
 *
 * <p>Serialization of an object happens on a per-field basis, where
 * every field that's marked with @{@link Serializable} annotation
 * is recursively represented as another tag, that can be
 * split and serialized into coresponding tags further. When
 * a {@link Tag#byteData()} method is called on any tag, that tag
 * and all its subtags are serialized into a byte array, representing
 * the tags, and thus, indirectly, the original object.</p>
 *
 * <p>Deserialization happens on a similar principle, where a parent Tag
 * object is created from a byte array. A byte array is passed into
 * a {@link Tag#fromBytes(byte[], Class)} method together with a Class
 * reference to cast the result to. The byte array is then iterated
 * over and each Tag that is found in the array is created. When the
 * {@link Tag#getContent()} method is invoked, each tag is cast to
 * its original type and the object is constructed from inwards out.</p>
 *
 * <p>A Tag can be serialized indirectly from the Tag class or
 * instantiated directly, as, for example, an {@link ArrayTag}, if
 * the caller knows all specifics needed to instantiate that
 * Tag directly.</p>
 *
 * @param <T> Type of the outermost serialized object
 */
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

    /**
     * Creates a Tag representing the object.
     * @param o Object to serialize to a Tag
     * @param <T> Type of the object
     * @return A tag
     */
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

    /**
     * Deserializes a byte array into a Tag.
     * @param bytes Byte array containing the binary representation of the tag
     * @param type Class to type the resulting Tag with
     * @param <T> Type of the casting class
     * @return Tag from a deserialized array
     * @throws NotSerializableException If the input is invalid
     */
    public static <T> Tag<T> fromBytes(byte[] bytes, Class<T> type) throws NotSerializableException {
        var deserializer = new Deserializer<T>(bytes);
        return deserializer.deserialize();
    }

}
