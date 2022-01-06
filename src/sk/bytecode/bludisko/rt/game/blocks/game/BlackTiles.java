package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.*;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class BlackTiles extends Block {

    private final Vector2 coordinates;
    private final float height;

    public BlackTiles(Vector2 coordinates, float height) {
        this.coordinates = coordinates;
        this.height = height;
    }

    @Override
    public Texture getTexture(Side side) {
        return switch((int) height) {
            default -> TextureManager.getTexture(1);
            case 4 -> TextureManager.getTexture(3);
        };
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public Vector2 getCoordinates() {
        return coordinates;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        return RayAction.STOP;
    }

}
