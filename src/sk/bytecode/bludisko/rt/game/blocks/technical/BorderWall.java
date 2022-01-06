package sk.bytecode.bludisko.rt.game.blocks.technical;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.*;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class BorderWall extends Block {

    private final Vector2 coordinates;

    public BorderWall(Vector2 coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Texture getTexture(Side side) {
        return TextureManager.getEmpty();
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public boolean hasPriority() {
        return false;
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
