package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.BlockType;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.MathUtils;

public class HalfWall extends Block {

    private final Texture texture = new Texture((x, y) -> MathUtils.INT_MSB_MASK |
            (256 * (y * 128 / 64 + x * 128 / 64) + 65536 * (y * 128 / 64 + x * 128 / 64)));

    @Override
    public Texture getTexture(float side) {
        return texture;
    }

    @Override
    public float getHeight() {
        return 2f;
    }

    public Ray.Result hitDistance(Ray ray) {
        var positionInBlock = MathUtils.decimalPart(ray.getPosition());

        var innerRay = new Ray(positionInBlock, ray.getDirection());
        innerRay.step();

        var hitPosition = innerRay.getPosition();
        var distance = innerRay.getDistance();

        if(hitPosition.x == 0) {
            return new Ray.Result(BlockType.TRANSLUCENT, distance);
        }

        return new Ray.Result(BlockType.TRANSPARENT, -1);
    }

}
