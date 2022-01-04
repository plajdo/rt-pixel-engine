package sk.bytecode.bludisko.rt.game.blocks.technical;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.MathUtils;

public class TunnelWall extends Block {

    private final Texture texture = new Texture((x, y) -> 0xFF3F5F00);
    private final Side side;

    public TunnelWall(Side side) {
        this.side = side;
    }

    @Override
    public Texture getTexture(float side) {
        return texture;
    }

    @Override
    public float getHeight() {
        return 2;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        var position = MathUtils.decimalPart(ray.getPosition());

        if(this.side == Side.EASTWEST && position.x == 0) {
            return RayAction.ADD;
        } else if(this.side == Side.NORTHSOUTH && position.y == 0) {
            return RayAction.ADD;
        }
        return RayAction.SKIP;
    }

}
