package sk.bytecode.bludisko.rt.game.blocks.technical;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.*;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Default block with a famous "missing" (XOR) texture.
 * Fallback when another block is not found.
 */
public class MissingBlock extends Block {

    private final Vector2 coordinates;

    public MissingBlock(Vector2 coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Texture getTexture(Side side) {
        if(side == Side.EAST || side == Side.WEST) {
            return TextureManager.getGenerated(3);
        }
        return TextureManager.getGenerated(4);
    }

    @Override
    public float getHeight() {
        return 2f;
    }

    @Override
    public Vector2 getCoordinates() {
        return coordinates;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        return RayAction.ADD;
    }

}
