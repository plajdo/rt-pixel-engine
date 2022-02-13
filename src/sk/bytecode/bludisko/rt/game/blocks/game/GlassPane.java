package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.technical.SideWall;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class GlassPane extends SideWall {

    public GlassPane(Side side, Vector2 coordinates) {
        super(side, coordinates);
    }

    @Override
    public Texture getTexture(Side side) {
        return TextureManager.getTexture(8);
    }

    @Override
    public float getHeight() {
        return 4f;
    }
}
