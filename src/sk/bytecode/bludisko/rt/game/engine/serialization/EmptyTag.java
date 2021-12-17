package sk.bytecode.bludisko.rt.game.engine.serialization;

public final class EmptyTag extends Tag<Void> {

    public EmptyTag() {
        super(null);
    }

    @Override
    public byte id() {
        return -1;
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public byte[] byteData() {
        return new byte[] { -1 };
    }

}
