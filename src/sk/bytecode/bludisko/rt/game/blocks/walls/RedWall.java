package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.MathUtils;

public class RedWall extends Block {

    //private final Texture texture = new Texture((x, y) -> MathUtils.INT_MSB_MASK | 65536 * 192 * ((x % 16) & (y % 16)));
    private final Texture texture = new Texture((x, y) -> 0xFF3F0000);

    @Override
    public Texture getTexture(float side) {
        return texture;
    }

    @Override
    public float getHeight() {
        return 5f;
    }

    @Override
    public Ray.Result hitDistance(Ray ray) {
        return new Ray.Result(RayAction.STOP, 0);
    }
}
