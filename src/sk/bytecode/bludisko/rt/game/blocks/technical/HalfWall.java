package sk.bytecode.bludisko.rt.game.blocks.technical;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class HalfWall extends Block {

    //private final Texture texture = new Texture((x, y) -> 0x3F000000 |
    //        (256 * (y * 128 / 64 + x * 128 / 64) + 65536 * (y * 128 / 64 + x * 128 / 64)));

    private final Texture texture = new Texture((x, y) -> 0x3FBCD2F5);

    @Override
    public Texture getTexture(float side) {
        return texture;
    }

    @Override
    public float getHeight() {
        return 2f;
    }

    public RayAction hitAction(Ray ray) {
        var angle = ray.getDirection().angleRad();
        var distance = Math.abs((float)(0.5d / Math.sin(angle)));

        ray.setTileSize(new Vector2(0.5f, 0.5f));
        var position = ray.getPosition();

        if((position.y % 1) - 0.5f < MathUtils.FLOAT_ROUNDING_ERROR && (position.y % 1) - 0.5f >= 0) {
            ray.setTileSize(new Vector2(1f, 1f));
            return RayAction.ADD;
        }

        return RayAction.SKIP;
    }

}
