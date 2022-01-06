package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.technical.SideWall;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class GlassPane extends SideWall {

    private final float height;

    public GlassPane(Side side, Vector2 coordinates, float height) {
        super(side, coordinates);
        this.height = height;
    }

    @Override
    public Texture getTexture(Side side) {
        return switch((int) height) {
            default -> TextureManager.getTexture(7);
            case 4 -> TextureManager.getTexture(8);
        };
    }

    @Override
    public float getHeight() {
        return height;
    }
}
