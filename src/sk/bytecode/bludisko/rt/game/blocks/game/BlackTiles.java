package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.*;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class BlackTiles extends Block {

    private final Vector2 coordinates;

    public BlackTiles(Vector2 coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Texture getTexture(Side side) {
        return TextureManager.getTexture(3);
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
        return RayAction.STOP;
    }

}
