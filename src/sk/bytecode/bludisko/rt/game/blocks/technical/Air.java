package sk.bytecode.bludisko.rt.game.blocks.technical;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.Texture;

public class Air extends Block {

    private final Texture texture = new Texture();

    @Override
    public Texture getTexture(float side) {
        return texture;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public Ray.Result hitDistance(Ray ray) {
        return new Ray.Result(RayAction.SKIP, -1);
    }

}
