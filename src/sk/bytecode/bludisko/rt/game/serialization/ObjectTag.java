package sk.bytecode.bludisko.rt.game.serialization;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public final class ObjectTag<T> extends Tag<T> {

    private final StringTag typeTag;
    private final ArrayTag<String> fieldsTag;
    private final byte[] bytes;

    // MARK: - Constructor

    ObjectTag(T data, StringTag typeTag, ArrayTag<String> fieldsTag, byte[] bytes) {
        super(data);

        this.typeTag = typeTag;
        this.fieldsTag = fieldsTag;
        this.bytes = bytes;
    }

    public ObjectTag(T data) {
        super(data);

        this.typeTag = new StringTag(data.getClass().getName());
        this.fieldsTag = getFieldsTag(data.getClass());
        this.bytes = getBytes();
    }

    // MARK: - Override

    @Override
    public byte id() {
        return 10;
    }

    @Override
    public int length() {
        return bytes.length;
    }

    @Override
    public byte[] byteData() {
        return bytes;
    }

    // MARK: - Private

    private ArrayTag<String> getFieldsTag(Class<?> forClass) {
        var fields = forClass.getDeclaredFields();
        var fieldNames = Arrays.stream(fields)
                .filter(field -> field.getAnnotation(Serializable.class) != null)
                .map(Field::getName)
                .toArray(String[]::new);
        return new ArrayTag<>(fieldNames);
    }

    private Object getFieldContent(Object fromObject, String fieldName) {
        try {
            Field field = fromObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(fromObject);
        } catch(ReflectiveOperationException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return null;
    }

    private byte[] getBytes() {
        final ArrayList<byte[]> subtagBytes = new ArrayList<>(fieldsTag.getContent().length);
        for(String fieldName : fieldsTag) {
            Object content = getFieldContent(data, fieldName);
            subtagBytes.add(Tag.fromObject(content).byteData());
        }

        var finalSize = subtagBytes.stream()
                .mapToInt(e -> e.length)
                .sum()
                + typeTag.length()
                + fieldsTag.length()
                + 2; // Header & terminator tags

        var finalBytes = ByteBuffer.allocate(finalSize);
        finalBytes.put(this.id());
        finalBytes.put(this.typeTag.byteData());
        finalBytes.put(this.fieldsTag.byteData());
        subtagBytes.forEach(finalBytes::put);
        finalBytes.put(new TerminatorTag().byteData());

        return finalBytes.array();
    }

}
