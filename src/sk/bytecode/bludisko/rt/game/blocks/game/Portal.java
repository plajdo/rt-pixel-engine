package sk.bytecode.bludisko.rt.game.blocks.game;

import sk.bytecode.bludisko.rt.game.blocks.Block;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.graphics.RayAction;
import sk.bytecode.bludisko.rt.game.graphics.Side;
import sk.bytecode.bludisko.rt.game.graphics.Texture;
import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class Portal extends Block {

    /**
     * Color of the portal. Also serves as a portal pair identifier.
     */
    public enum Color {
        BLUE,
        ORANGE
    }

    // MARK: - Attributes

    private final Vector2 coordinates;
    private final Side side;
    private final Portal.Color color;

    private Portal otherPortal;
    private Block originalWall;

    // MARK: - Constructor

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

    // MARK: - Override

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
        if(hitSide == side && otherPortal != null && insidePortalFrame(rayPosition)) {
            var coordinates = otherPortal.getCoordinates();
            var rotation = getRotation(side) - getPiComplementaryRotation(otherPortal.side);
            var offset = getOffset(side, otherPortal.side);

            rayPosition.set(MathUtils.decimalPart(rayPosition));
            rayPosition.rotateRad(rotation);
            rayPosition.add(coordinates);
            rayPosition.add(offset);

            ray.updateDirection(ray.getDirection().cpy().rotateRad(rotation));

            return RayAction.TELEPORT;
        } else {
            return RayAction.STOP;
        }
    }

    // MARK: - Static

    /**
     * When moving a ray from one portal to another and when the portals are on
     * different sides, the ray will end up misaligned. Adding this offset
     * corrects the difference. Since the ray will then also end up inside a wall
     * (on a coordinate exactly divisible by an integer, a block edge), the
     * smallest possible float-expressible value will be added to the result
     * to fix Z-fighting.
     * @param entrySide Side of a block the entrance portal is on
     * @param exitSide Side of a block the exit portal is on
     * @return [x, y] offset to add
     * @see Side
     * @see Portal#getRotation(Side)
     */
    private static Vector2 getOffset(Side entrySide, Side exitSide) {
        return (switch(entrySide) {
            case NONE -> new Vector2();
            case NORTH, EAST -> switch(exitSide) {
                case NONE -> new Vector2();
                case NORTH -> new Vector2(0f, 1f);
                case EAST -> new Vector2(1f, 1f);
                case SOUTH -> new Vector2(1f, 0f);
                case WEST -> new Vector2(0f, 0f);
            };
            case SOUTH, WEST -> switch(exitSide) {
                case NONE -> new Vector2();
                case NORTH -> new Vector2(0f, 0f);
                case EAST -> new Vector2(0f, 1f);
                case SOUTH -> new Vector2(1f, 1f);
                case WEST -> new Vector2(1f, 0f);
            };
        }).add(switch(exitSide) {
            case NONE -> new Vector2();
            case NORTH -> new Vector2(-MathUtils.FLOAT_ROUNDING_ERROR, 0f);
            case EAST -> new Vector2(0f, MathUtils.FLOAT_ROUNDING_ERROR);
            case SOUTH -> new Vector2(MathUtils.FLOAT_ROUNDING_ERROR, 0f);
            case WEST -> new Vector2(0f, -MathUtils.FLOAT_ROUNDING_ERROR);
        });
    }

    /**
     * Converts {@link Side} to rotation. North is at 0°, rotation increases in
     * a clockwise direction.
     * @param side Side of the block the portal is on
     * @return Rotation of the side in radians
     */
    private static float getRotation(Side side) {
        return switch(side) {
            case NONE -> 0f;
            case NORTH -> 0f;
            case EAST -> MathUtils.PI / 2f;
            case SOUTH -> MathUtils.PI;
            case WEST -> 3 * MathUtils.PI / 2f;
        };
    }

    /**
     * Degrees of rotation left to the nearest n*PI value where n*PI &ne; 0.<br><br>
     * When used in combination as <br>
     * {@link Portal#getRotation(Side)} - getPiComplementaryRotation(Side),
     * result is the angle required to rotate from facing entrance side to exit side.
     *
     * @param side Side of the block the portal is on
     * @return Rotation in radians
     *
     * @see Side
     * @see Portal#getRotation(Side)
     */
    private static float getPiComplementaryRotation(Side side) {
        return switch(side) {
            case NONE -> 0f;
            case NORTH -> MathUtils.PI;
            case EAST -> 3 * MathUtils.PI / 2f;
            case SOUTH -> 0f;
            case WEST -> MathUtils.PI / 2;
        };
    }

    private static boolean insidePortalFrame(Vector2 location) {
        final float frameSize = 0.2f;

        var decimalPart = MathUtils.decimalPart(location);
        if(decimalPart.x == 0) {
            return decimalPart.y > frameSize && decimalPart.y < 1f - frameSize;
        } else {
            return decimalPart.x > frameSize && decimalPart.x < 1f - frameSize;
        }
    }

    // MARK: - Public

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
        this.originalWall = originalWall;
    }

    public Portal.Color getColor() {
        return color;
    }

}
