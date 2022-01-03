package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.Traceable;

public abstract class Block implements Traceable {

    public abstract Texture getTexture(float side);
    public abstract float getHeight();

}
