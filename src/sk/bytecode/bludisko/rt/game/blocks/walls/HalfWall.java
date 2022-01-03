package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class HalfWall extends Block {

    private final Vector2 shape = new Vector2(1f, 0.5f);
    private final Texture texture = new Texture((x, y) -> MathUtils.INT_MSB_MASK |
            (256 * (y * 128 / 64 + x * 128 / 64) + 65536 * (y * 128 / 64 + x * 128 / 64)));

    @Override
    public Texture getTexture(float side) {
        return texture;
    }

    @Override
    public float getHeight() {
        return 2;
    }

    public float hitDistance(Ray ray) {
        Ray innerRay = new Ray(ray.getPosition(), ray.getDirection(), shape);
        innerRay.step();

        var position = innerRay.getPosition();
        var tilePosition = MathUtils.decimalPart(position);
        if(tilePosition.y != 0.5f || tilePosition.x > 1f || tilePosition.x < 0f) {
            return -1;
        }
        return ray.getPosition().dst(position);
    }

}
