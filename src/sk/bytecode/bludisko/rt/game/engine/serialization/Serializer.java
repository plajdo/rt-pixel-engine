package sk.bytecode.bludisko.rt.game.engine.serialization;

import sk.bytecode.bludisko.rt.game.engine.serialization.tags.*;

import java.io.NotSerializableException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class Serializer {

    public Tag serialize(Object obj) throws NotSerializableException {
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

    private Tag serializePrimitiveTypes(Object obj) {
        if (obj instanceof Number) {
            if(obj instanceof Integer) {
                return new IntTag((Integer) obj);

            } else if (obj instanceof Float) {
                return new FloatTag((Float) obj);

            } else if (obj instanceof Long) {
                return new LongTag((Long) obj);

            } else if (obj instanceof Double) {
                return new DoubleTag((Double) obj);

            } else if (obj instanceof Byte) {
                return new ByteTag((Byte) obj);

            } else if (obj instanceof Short) {
                return new ShortTag((Short) obj);
            }

        } else if(obj instanceof String) {
            return new StringTag((String) obj);

        } else if (obj instanceof Boolean) {
            return new BooleanTag((Boolean) obj);

        } else if (obj instanceof Character) {
            return new CharTag((Character) obj);
        }

        return null;
    }

    private Tag serializeObjectTypes(Object obj) {
        var wrapperTag = new ObjectTag(obj.getClass());

        forAnnotatedFieldsIn(obj.getClass(), (annotation, field) -> {
            try {
                Object fieldValue = field.get(obj);
                wrapperTag.addChildren(this.serialize(fieldValue));

            } catch(IllegalAccessException e) {
                System.err.println("Skipping serialization of " + obj + " due to exception:");
                System.err.println(e.getLocalizedMessage());

            } catch(NotSerializableException e) {
                System.err.println("Cannot serialize " + obj + ".");
                System.err.println(e.getLocalizedMessage());

            }

        });

        return wrapperTag;

    }

    private boolean isArray(Object obj) {
        return obj.getClass().isArray();
    }

    private Tag serializeArray(Object obj) throws NotSerializableException {
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
