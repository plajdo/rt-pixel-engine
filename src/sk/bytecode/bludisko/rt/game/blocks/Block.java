package sk.bytecode.bludisko.rt.game.blocks;

import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public abstract class Block {

    public abstract Texture getTexture(Side side);
    public abstract float getHeight();

    public abstract Vector2 getCoordinates();
    public abstract RayAction hitAction(Ray ray);

    public Side getHitSide(Vector2 hitPosition) {
        if(hitPosition.x % 1 == 0) {
            // North-South
            if(hitPosition.x % 2 == this.getCoordinates().x % 2) {
                return Side.NORTH;
            } else {
                return Side.SOUTH;
            }

        }
        if(hitPosition.y % 1 == 0) {
            // East-West
            if(hitPosition.y % 2 == this.getCoordinates().y % 2) {
                return Side.WEST;
            } else {
                return Side.EAST;
            }
        }

        return Side.NONE;
    }

    @Override
    public String toString() {
        return super.toString() + " " + getCoordinates();
    }

}
