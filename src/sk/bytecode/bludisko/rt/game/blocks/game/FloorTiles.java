package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class FloorTiles extends Block {

    private final Vector2 coordinates;

    public FloorTiles(Vector2 coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Texture getTexture(Side side) {
        return TextureManager.getTexture(9);
    }

    @Override
    public float getHeight() {
        return 2;
    }

    @Override
    public Vector2 getCoordinates() {
        return this.coordinates;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        return RayAction.STOP;
    }

}
