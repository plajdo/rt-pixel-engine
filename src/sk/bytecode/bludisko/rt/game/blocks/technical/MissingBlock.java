package sk.bytecode.bludisko.rt.game.blocks.technical;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.MathUtils;

public class MissingBlock extends Block {

    private final Texture eastTexture = new Texture((x, y) -> MathUtils.INT_MSB_MASK | ((x ^ y) * 4) + ((x ^ y) * 262144));
    private final Texture northTexture = new Texture((x, y) -> MathUtils.INT_MSB_MASK | 256 * ((x * 256 / 64) ^ (y * 256 / 64)));

    @Override
    public Texture getTexture(float side) {
        if(side > 0.5f) {
            return northTexture;
        }
        return eastTexture;
    }

    @Override
    public float getHeight() {
        return 2f;
    }

    @Override
    public Ray.Result hitDistance(Ray ray) {
        return new Ray.Result(RayAction.ADD, 0);
    }

}
