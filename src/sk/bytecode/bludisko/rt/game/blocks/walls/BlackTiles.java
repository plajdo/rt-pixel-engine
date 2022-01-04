package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Texture;

public class BlackTiles extends Block {

    private final Texture texture = new Texture("darkWallTile");

    @Override
    public Texture getTexture(float side) {
        return texture;
    }

    @Override
    public float getHeight() {
        return 12f;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        return RayAction.STOP;
    }

}
