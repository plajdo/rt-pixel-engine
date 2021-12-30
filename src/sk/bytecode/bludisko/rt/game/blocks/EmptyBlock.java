package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.texture.Texture;

public class EmptyBlock extends Block {

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public float getHeight() {
        return 0;
    }

    @Override
    public float rayHitDistance(Ray ray) {
        return -1;
    }

}
