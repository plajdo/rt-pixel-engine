package sk.bytecode.bludisko.rt.game.engine.serialization;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.engine.serialization.tags.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public final class Serializer {

    public Tag<?> serialize(@NotNull Object obj) {
        var result = serializePrimitiveTypes(obj);
        if(result != null) {
            return result;
        }

        if(isArray(obj)) result = serializeArray(obj);
        if(result != null) {
            return result;
        }

        result = serializeObjectTypes(obj);

        return result;
    }

    private Tag<?> serializePrimitiveTypes(Object obj) {
        return switch(obj) {
            case Integer i -> new IntTag(i);
            case Float f -> new FloatTag(f);
            case Long l -> new LongTag(l);
            case Double d -> new DoubleTag(d);
            case Byte b -> new ByteTag(b);
            case Short s -> new ShortTag(s);
            case String s -> new StringTag(s);
            case Boolean b -> new BooleanTag(b);
            case Character c -> new CharTag(c);
            default -> null;
        };
        /*
        if (obj instanceof Number) {
            if(obj instanceof Integer i) {
                return new IntTag(i);

            } else if (obj instanceof Float f) {
                return new FloatTag(f);

            } else if (obj instanceof Long l) {
                return new LongTag(l);

            } else if (obj instanceof Double d) {
                return new DoubleTag(d);

            } else if (obj instanceof Byte b) {
                return new ByteTag(b);

            } else if (obj instanceof Short s) {
                return new ShortTag(s);
            }

        } else if(obj instanceof String s) {
            return new StringTag(s);

        } else if (obj instanceof Boolean b) {
            return new BooleanTag(b);

        } else if (obj instanceof Character c) {
            return new CharTag(c);
        }

        return null;*/
    }

    private Tag<?> serializeObjectTypes(Object obj) {
        var wrapperTag = new ObjectTag(obj.getClass());

        forAnnotatedFieldsIn(obj.getClass(), (annotation, field) -> {
            try {
                Object fieldValue = field.get(obj);
                wrapperTag.addChildren(this.serialize(fieldValue));

            } catch(IllegalAccessException e) {
                System.err.println("Skipping serialization of " + obj + " due to exception:");
                System.err.println(e.getLocalizedMessage());
            }
        });

        return wrapperTag;
    }

    private boolean isArray(Object obj) {
        return obj.getClass().isArray();
    }

    private Tag<?> serializeArray(Object obj) {
        int length = Array.getLength(obj);
        var wrapperTag = new ObjectTag(obj.getClass());

        for(int i = 0; i < length; i++) {
            Object element = Array.get(obj, i);
            wrapperTag.addChildren(this.serialize(element));
        }

        return wrapperTag;
    }

    private void forAnnotatedFieldsIn(Class<?> serializableClass, BiConsumer<Serializable, Field> completion) {
        final Field[] fields = serializableClass.getDeclaredFields();

        for(Field field : fields) {
            field.setAccessible(true);
            Serializable annotation = field.getAnnotation(Serializable.class);
            if(annotation != null) {
                completion.accept(annotation, field);
            }
        }
    }

}
