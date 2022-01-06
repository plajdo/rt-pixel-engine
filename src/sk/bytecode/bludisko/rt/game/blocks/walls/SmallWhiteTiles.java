package sk.bytecode.bludisko.rt.game.blocks.walls;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class SmallWhiteTiles extends WhiteTiles {

    public SmallWhiteTiles(Vector2 coordinates) {
        super(coordinates);
    }

    @Override
    public float getHeight() {
        return 6f;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        return RayAction.ADD;
    }

}
