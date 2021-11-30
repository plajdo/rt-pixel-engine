package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

import sk.bytecode.bludisko.rt.game.engine.serialization.Serializable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public final class ObjectTag extends Tag<Class<?>> {

    private final ArrayList<Tag<?>> subtags;

    public ObjectTag(Class<?> data) {
        super(data);

        this.subtags = new ArrayList<>();

        var classNameTag = new StringTag(data.getName());
        var annotatedFieldsTag = new ObjectTag(String[].class);

        Arrays.stream(data.getDeclaredFields())
                .filter((field) -> field.getAnnotation(Serializable.class) != null)
                .map(Field::getName)
                .map(StringTag::new)
                .forEach(annotatedFieldsTag::addChildren);

        this.addChildren(classNameTag, annotatedFieldsTag);
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

    public Object getObject() {
        try {
            Object instance = this.getInstance(data);

            this.injectFields(data, instance);

            return instance;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * Create an instance for a given class without calling any constructor.
     * @param forClass Class to create the instance for
     * @return Instance of the class, safe to force-cast to type of the class
     * @throws ReflectiveOperationException If using non-standard JVMs without sun.* package
     */
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

    private void injectFields(Class<?> forClass, Object instance) {
        for (Field f : forClass.getDeclaredFields()) {


        }

    }

}
