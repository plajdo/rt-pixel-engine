package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.texture.Texture;

public abstract class Block {

    public abstract Texture getTexture();
    public abstract float getHeight();

    /**
     * Checks if the block could be hit by a ray passing given direction.
     * @return Distance at which the block was hit by a ray
     */
    public abstract float rayHitDistance(Ray ray);

}
