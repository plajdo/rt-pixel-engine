package sk.bytecode.bludisko.rt.game.blocks.technical;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Modification of a Block that only renders one side
 * of the cube.
 */
public class SideWall extends Block {

    private final Vector2 coordinates;
    private final Side side;

    public SideWall(Side side, Vector2 coordinates) {
        this.coordinates = coordinates;
        this.side = side;
    }

    @Override
    public Texture getTexture(Side side) {
        return TextureManager.getGenerated(6);
    }

    @Override
    public float getHeight() {
        return 2f;
    }

    @Override
    public Vector2 getCoordinates() {
        return this.coordinates;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        var position = ray.getPosition();
        var positionInBlock = MathUtils.decimalPart(position);

        if (this.side == Side.NORTH && positionInBlock.x == 0 && position.x % 2 == this.getCoordinates().x % 2) {
            return RayAction.ADD;

        } else if (this.side == Side.SOUTH && positionInBlock.x == 0 && position.x % 2 != this.getCoordinates().x % 2) {
            return RayAction.ADD;

        } else if (this.side == Side.WEST && positionInBlock.y == 0 && position.y % 2 == this.getCoordinates().y % 2) {
            return RayAction.ADD;

        } else if (this.side == Side.EAST && positionInBlock.y == 0 && position.y % 2 != this.getCoordinates().y % 2) {
            return RayAction.ADD;
        }

        return RayAction.SKIP;
    }

}
