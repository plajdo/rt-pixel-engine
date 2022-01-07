package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class Board extends Block {

    private final Vector2 coordinates;
    private final int number;

    public Board(Vector2 coordinates, int number) {
        this.coordinates = coordinates;
        this.number = number;
    }

    @Override
    public Texture getTexture(Side side) {
        if (this.number == 0) {
            return TextureManager.getTexture(6);
        }
        return TextureManager.getTexture(2);
    }

    @Override
    public float getHeight() {
        return 4;
    }

    @Override
    public Vector2 getCoordinates() {
        return this.coordinates;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        return RayAction.STOP;
    }
}
