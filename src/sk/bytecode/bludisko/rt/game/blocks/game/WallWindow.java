package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.*;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class WallWindow extends Block {

    private final boolean mirrored;
    private final Vector2 coordinates;

    public WallWindow(Vector2 coordinates, boolean mirrored) {
        this.coordinates = coordinates;
        this.mirrored = mirrored;
    }

    @Override
    public Texture getTexture(Side side) {
        if(side == Side.NORTH) {
            if(mirrored) {
                return TextureManager.getTexture(11);
            } else {
                return TextureManager.getTexture(10);
            }
        }
        return TextureManager.getTexture(2);
    }

    @Override
    public float getHeight() {
        return 4f;
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
