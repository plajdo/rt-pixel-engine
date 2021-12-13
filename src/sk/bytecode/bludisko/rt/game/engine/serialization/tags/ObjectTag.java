package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import org.jetbrains.annotations.Nullable;
import sk.bytecode.bludisko.rt.game.engine.serialization.Serializable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class ObjectTag extends Tag<Class<?>> {

    private static final int SUBTAG_FIELDS = 1;
    private static final int SUBTAG_DATA_START = 2;

    private final ArrayList<Tag<?>> subtags;

    // MARK: - Constructor

    public ObjectTag(Class<?> data) {
        super(data);

        this.subtags = new ArrayList<>();
        var classNameTag = new StringTag(data.getName());
        this.addChildren(classNameTag);

        if(this.isObject()) {
            var fieldTags = getFieldTags(data);
            this.addChildren(fieldTags);
        }
    }

    @SuppressWarnings("unused")
    private ObjectTag(Class<?> data, @Nullable ObjectTag fieldsToSerialize) {
        super(data);

        this.subtags = new ArrayList<>();
        var classNameTag = getClassNameTag();
        this.addChildren(classNameTag);

        if(fieldsToSerialize != null) {
            this.addChildren(fieldsToSerialize);
        }
    }

    private StringTag getClassNameTag() {
        return new StringTag(data.getName());
    }

    private ObjectTag getFieldTags(Class<?> data) {
        var fields = data.getDeclaredFields();
        var annotatedFieldsTag = new ObjectTag(String[].class);
        Arrays.stream(fields)
                .filter((field) -> field.getAnnotation(Serializable.class) != null)
                .map(Field::getName)
                .map(StringTag::new)
                .forEach(annotatedFieldsTag::addChildren);

        return annotatedFieldsTag;
    }

    // MARK: - Override

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

    // MARK: - Public

    public boolean isObject() {
        return !isArray();
    }

    public boolean isArray() {
        return this.id() == 11;
    }

    public void addChildren(Tag<?>... children) {
        this.subtags.addAll(Arrays.asList(children));
    }

    @Nullable
    public Object getObject() {
        if(this.isArray()) return null;
        try {
            Object instance = getInstance(data);
            String[] fields = getFieldTags();

            this.injectFields(data, fields, instance);

            return instance;
        } catch(ReflectiveOperationException e) {
            return null;
        }
    }

    @Nullable
    public Object[] getArray() {
        if(!this.isArray()) return null;

        // Array Class tag
        ArrayList<Object> list = new ArrayList<>();
        boolean first = true;
        for(Tag<?> subtag : subtags) {
            if(first) {
                first = false;
                continue;
            }
            Object o = subtag.get();
            list.add(o);
        }
        //return list.toArray();

        return subtags.stream()
                .skip(1) // Array Class tag
                .map(Tag::get)
                .toArray();
    }

    // MARK: - Private

    private String[] getFieldTags() {
        var fieldTags = (ObjectTag) subtags.get(SUBTAG_FIELDS);
        var fieldCount = fieldTags.subtags.size() - 1; // -1 for Class name

        var fieldArray = new String[fieldCount];
        System.arraycopy(fieldTags.getArray(), 0, fieldArray, 0, fieldCount);

        return fieldArray;
    }

    private Object getInstance(Class<?> forClass) throws ReflectiveOperationException {
        Constructor<Object> fakeConstructor = Object.class.getConstructor((Class<?>[]) null);
        Class<?> reflectionFactoryClass = Class.forName("sun.reflect.ReflectionFactory");
        Object reflectionFactoryInstance = reflectionFactoryClass.getDeclaredMethod("getReflectionFactory").invoke(null);
        Method serializationConstructorCreatorMethod = reflectionFactoryClass.getDeclaredMethod("newConstructorForSerialization", Class.class, Constructor.class);

        @SuppressWarnings("unchecked")
        Constructor<Object> serializationConstructor = (Constructor<Object>) serializationConstructorCreatorMethod.invoke(reflectionFactoryInstance, forClass, fakeConstructor);
        serializationConstructor.setAccessible(true);

        Object classInstance = serializationConstructor.newInstance((Object[]) null);
        return forClass.cast(classInstance);
    }

    private void injectFields(Class<?> forClass, String[] fieldNames, Object instance) throws ReflectiveOperationException {
        for(int i = 0; i < fieldNames.length; i++) {
            String fieldName = fieldNames[i];
            Field field = forClass.getDeclaredField(fieldName);
            field.setAccessible(true);

            var subtag = subtags.get(SUBTAG_DATA_START + i);
            var subtagObject = switch(subtag) {
                case ObjectTag o && o.isObject() -> o.getObject();
                case ObjectTag o && o.isArray() -> o.getArray();
                /*case ObjectTag o && o.isArray() -> {
                    var array = o.getArray();
                    var arrayClass = o.get();
                    var typedArray = arrayClass.cast(Array.newInstance(arrayClass.componentType(), array.length));

                    for(int j = 0; j < array.length; j++) {
                        Object element = array[j];
                        ((Object[]) typedArray)[j] = element;
                    }

                    yield typedArray;
                }*/
                case Tag t -> t.get();
            };

            field.set(instance, subtagObject);
        }
    }

}
