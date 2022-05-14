package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.blocks.group.PortalPlaceable;
import sk.bytecode.bludisko.rt.game.graphics.*;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class Portal extends Block {

    public enum Color {
        BLUE,
        ORANGE
    }

    private final Vector2 coordinates;
    private final Side side;
    private final Portal.Color color;
    private Portal otherPortal;

    private Block originalWall;

    /**
     * Constructs a new portal.
     * @param side Side of the portal entrance/exit
     * @param color {@link Portal.Color Color} of the portal frame (aesthetic only)
     * @param coordinates Coordinates of this portal
     */
    public Portal(Side side, Portal.Color color, Vector2 coordinates) {
        this.coordinates = coordinates;
        this.color = color;
        this.side = side;
    }

    @Override
    public Texture getTexture(Side side) {
        if(side == this.side) {
            if(this.color == Color.ORANGE) {
                return TextureManager.getTexture(5);
            } else {
                return TextureManager.getTexture(4);
            }
        }
        return TextureManager.getTexture(2);
    }

    @Override
    public float getHeight() {
        return 4f;
    }

    @Override
    public Vector2 getCoordinates() {
        return coordinates;
    }

    @Override
    public RayAction hitAction(Ray ray) {
        var rayPosition = ray.getPosition();
        var hitSide = getSide(rayPosition);
        if(hitSide == side && otherPortal != null) {
            // var thisExit = getExitRotation();
            // var otherExit = otherPortal.getExitRotation();
            var exitRotation = getEntryRotation() - otherPortal.getExitRotation();

            rayPosition.set(MathUtils.decimalPart(rayPosition));
            rayPosition.rotateRad(exitRotation);
            rayPosition.add(otherPortal.getCoordinates());
            rayPosition.add(otherPortal.getExitOffset());

            ray.updateDirection(ray.getDirection().cpy().rotateRad(exitRotation));

            return RayAction.TELEPORT;
        } else {
            return RayAction.STOP;
        }
    }

    private Vector2 getExitOffset() {
        return switch(side) {
            case NONE -> null;
            case NORTH -> new Vector2(-1f, 0f);
            case EAST -> new Vector2(0f, 1f);
            case SOUTH -> new Vector2(1f, 0f);
            case WEST -> new Vector2(0f, -1f);
        };
    }

    private float getEntryRotation() {
        return switch(side) {
            case NONE -> 0f;
            case NORTH -> 0f;
            case EAST -> MathUtils.PI / 2f;
            case SOUTH -> MathUtils.PI;
            case WEST -> 3 * MathUtils.PI / 2f;
        };
    }

    private float getExitRotation() {
        return switch(side) {
            case NONE -> 0f;
            case NORTH -> MathUtils.PI;
            case EAST -> 3 * MathUtils.PI / 2f;
            case SOUTH -> 0f;
            case WEST -> MathUtils.PI / 2;
        };
    }

    public Portal getOtherPortal() {
        return otherPortal;
    }

    /**
     * Link two portals together.
     * @param otherPortal Paired portal
     */
    public void setOtherPortal(Portal otherPortal) {
        this.otherPortal = otherPortal;
    }

    public Block getOriginalWall() {
        return originalWall;
    }

    public void setOriginalWall(Block originalWall) {
        if(originalWall instanceof PortalPlaceable) {
            this.originalWall = originalWall;
        } else {
            throw new IllegalArgumentException("Cannot put a portal here!");
        }
    }

    public Portal.Color getColor() {
        return color;
    }

}
