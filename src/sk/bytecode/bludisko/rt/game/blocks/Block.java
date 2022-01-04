package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Texture;

public abstract class Block {

    public abstract Texture getTexture(float side);
    public abstract float getHeight();
    public abstract RayAction hitAction(Ray ray);

}
