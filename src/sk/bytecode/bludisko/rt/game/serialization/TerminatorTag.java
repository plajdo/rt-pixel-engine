package sk.bytecode.bludisko.rt.game.serialization;

public final class TerminatorTag extends Tag<Void> {

    public TerminatorTag() {
        super(null);
    }

    @Override
    public byte id() {
        return 0;
    }

    @Override
    public int length() {
        return 1;
    }

    @Override
    public byte[] byteData() {
        return new byte[] { 0 };
    }

}
