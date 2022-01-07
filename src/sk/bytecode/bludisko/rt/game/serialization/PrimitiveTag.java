package sk.bytecode.bludisko.rt.game.serialization;

public abstract class PrimitiveTag<T> extends Tag<T> {

    protected PrimitiveTag(T data) {
        super(data);
    }

    public static Tag<? extends Number> fromNumber(Number n) {
        if(n instanceof Byte) {
            Byte b = (Byte) n;
            return new ByteTag(b);
        } else if(n instanceof Short) {
            Short s = (Short) n;
            return new ShortTag(s);
        } else if(n instanceof Integer) {
            Integer i = (Integer) n;
            return new IntTag(i);
        } else if(n instanceof Long) {
            Long l = (Long) n;
            return new LongTag(l);
        } else if(n instanceof Float) {
            Float f = (Float) n;
            return new FloatTag(f);
        } else if(n instanceof Double) {
            Double d = (Double) n;
            return new DoubleTag(d);
        } else {
            throw new IllegalStateException("BigInteger/BigDecimal not supported");
        }
    }

}
