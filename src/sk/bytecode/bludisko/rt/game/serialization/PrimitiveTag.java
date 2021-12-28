package sk.bytecode.bludisko.rt.game.serialization;

public abstract sealed class PrimitiveTag<T> extends Tag<T> permits ByteTag, ShortTag, IntTag, LongTag, FloatTag, DoubleTag, CharTag, BooleanTag {

    protected PrimitiveTag(T data) {
        super(data);
    }

    public static Tag<? extends Number> fromNumber(Number n) {
        return switch(n) {
            case Byte b -> new ByteTag(b);
            case Short s -> new ShortTag(s);
            case Integer i -> new IntTag(i);
            case Long l -> new LongTag(l);
            case Float f -> new FloatTag(f);
            case Double d -> new DoubleTag(d);
            default -> throw new IllegalStateException("BigInteger/BigDecimal not supported");
        };
    }

}
