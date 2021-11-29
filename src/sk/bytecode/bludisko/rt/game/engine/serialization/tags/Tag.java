package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

public abstract class Tag<T> {

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
