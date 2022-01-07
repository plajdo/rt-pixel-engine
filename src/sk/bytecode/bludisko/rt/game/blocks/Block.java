package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * A common template for all Blocks implemented in game.
 * A Block defines its {@link Texture}, height, coordinates at which
 * it's located and a {@link RayAction} it messages to a ray hitting it,
 * with optionally custom behaviour further modifying the ray.
 */
public abstract class Block {

    /**
     * Get texture for a given {@link Side} of this block.
     * Depending on implementation this parameter may or may not
     * have an effect on the final texture.
     * @param side Block side
     * @return Texture for a given side
     */
    public abstract Texture getTexture(Side side);

    /**
     * Get height of this block. Perfect cube in the world coordinates
     * has dimensions 1x2x1 (w * h * d).
     * @return Height of this block
     */
    public abstract float getHeight();

    /**
     * Get coordinates of this block. This must be handled by children
     * implementations separately, some blocks can differ in their coordinates
     * during the lifecycle (eg. a block is moved).
     * @return Coordinates of the block
     */
    public abstract Vector2 getCoordinates();

    /**
     * Perform an action that's executed when a ray hits this block. Action may,
     * based on the implementation, for example, change the course of the ray,
     * or modify it in any way. This method always returns a signal for the action
     * the ray should take after executing this action. Signalling possibilities
     * are defined in {@link RayAction}.
     * @param ray The ray that collided with this block
     * @return Signal determining next action of the ray
     */
    public abstract RayAction hitAction(Ray ray);

    /**
     * Returns a side of this Block at a coordinate. If the coordinate is not
     * on the side of the block, returns {@link Side#NONE}.
     * @param position Coordinate to check
     * @return Side of the block or {@link Side#NONE}
     */
    public Side getSide(Vector2 position) {
        if (position.x % 1 == 0) {
            // North-South
            if (position.x % 2 == this.getCoordinates().x % 2) {
                return Side.NORTH;
            } else {
                return Side.SOUTH;
            }

        }
        if (position.y % 1 == 0) {
            // East-West
            if (position.y % 2 == this.getCoordinates().y % 2) {
                return Side.WEST;
            } else {
                return Side.EAST;
            }
        }

        return Side.NONE;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.getCoordinates();
    }

}
