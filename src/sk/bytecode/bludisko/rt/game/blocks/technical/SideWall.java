package sk.bytecode.bludisko.rt.game.blocks.technical;

import org.junit.platform.engine.TestExecutionResult;
import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.MathUtils;

public class SideWall extends Block {

    private final Texture texture = new Texture((x, y) -> 0xFF5F3F00);
    private final Side side;

    public SideWall(Side side) {
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
        var position = ray.getPosition();
        var positionInBlock = MathUtils.decimalPart(position);

        if(this.side == Side.NORTH && positionInBlock.x == 0 && position.x % 2 == 0) {
            return RayAction.ADD;

        } else if(this.side == Side.EAST && positionInBlock.y == 0 && position.y % 2 == 0) {
            return RayAction.ADD;

        }

        return RayAction.SKIP;
    }

}
