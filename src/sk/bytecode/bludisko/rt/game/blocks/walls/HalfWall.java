package sk.bytecode.bludisko.rt.game.blocks.walls;

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

    public Ray.Result hitDistance(Ray ray) {
        var angle = ray.getDirection().angleRad();
        var distance = Math.abs((float)(0.5d / Math.sin(angle)));

        //ray.step();


        if(distance < 1f) {
            return new Ray.Result(RayAction.ADD, distance);
        }

        return new Ray.Result(RayAction.SKIP, -1);
    }

}
