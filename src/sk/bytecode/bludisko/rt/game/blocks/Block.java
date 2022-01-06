package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public abstract class Block {

    public abstract Texture getTexture(float side);
    public abstract float getHeight();
    public abstract boolean hasPriority();
    public abstract Vector2 getCoordinates();
    public abstract RayAction hitAction(Ray ray);

    @Override
    public String toString() {
        return super.toString() + " " + getCoordinates();
    }

}
