package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class WhiteTiles extends Block {

    //private final Texture texture = new Texture((x, y) -> MathUtils.INT_MSB_MASK | 65536 * 192 * ((x % 16) & (y % 16)));
    //private final Texture texture = new Texture((x, y) -> 0xFF3F0000);
    private final Texture texture = new Texture("wallTile");

    private final Vector2 coordinates;

    public WhiteTiles(Vector2 coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public Texture getTexture(float side) {
        return TextureManager.getTexture(0);
    }

    @Override
    public float getHeight() {
        return 12f;
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
