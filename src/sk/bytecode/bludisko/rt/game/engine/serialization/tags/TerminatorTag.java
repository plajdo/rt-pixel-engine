package sk.bytecode.bludisko.rt.game.engine.serialization.tags;

public final class TerminatorTag extends Tag<Object> {

    public TerminatorTag() {
        super(null);
    }

    @Override
    protected byte id() {
        return 0;
    }

    @Override
    protected int length() {
        return 1;
    }

    @Override
    public byte[] byteData() {
        return new byte[]{ 0 };
    }

}
