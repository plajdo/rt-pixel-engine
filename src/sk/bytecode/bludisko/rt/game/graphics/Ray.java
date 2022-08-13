package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Ray object. Can be cast in a 2D plane with tiles of a specified size.
 * Uses a custom implementation of a digital differential analysis algorithm
 * to move to the nearest cross-point of two axes. Cross-points frequency
 * is given by the tileSize parameter in one of the constructors, or the
 * Ray moves in a 2D plane with tiles 1 by 1, stopping only on edges
 * of those tiles.
 */
public class Ray {

    protected Vector2 position;
    protected Vector2 startingPosition;
    protected Vector2 direction;
    protected float distance;

    private Vector2 marginalTileDistance;
    private Vector2 tileSize;
    private Vector2 sign;

    // MARK: - Constructor

    /**
     * Constructs a new ray with a given tile size (step size).
     * @param position Starting position
     * @param direction Starting direction
     * @param tileSize Size of tiles in a 2D plane
     * @see Ray
     */
    public Ray(Vector2 position, Vector2 direction, Vector2 tileSize) {
        this(position, direction);
        this.tileSize = tileSize;
    }

    /**
     * Constructs a new ray with the default tile size of 1 by 1.
     * @param position Starting position
     * @param direction Starting direction
     * @see Ray
     */
    public Ray(Vector2 position, Vector2 direction) {
        this.position = position.cpy();
        this.startingPosition = position.cpy();
        this.tileSize = new Vector2(1, 1);

        updateDirection(direction);
    }

    // MARK: - Tracing algorithm

    /**
     * Advances the ray to the next crossing point between X and Y axes.
     * Uses a custom implementation of DDA algorithm to calculate the
     * next step size and then adds the difference to the current position.
     * @see Ray
     */
    public void step() {
        Vector2 positionInTile;
        if(tileSize.x != 1 || tileSize.y != 1) {
            positionInTile = MathUtils.fractionRound(position, MathUtils.gcd((int) (1f / tileSize.x), (int) (1f / tileSize.y)));
        } else {
            positionInTile = MathUtils.decimalPart(position);
        }

        Vector2 nextTileDistance;
        if(sign.x > 0 && sign.y > 0) {
            nextTileDistance = new Vector2(tileSize).sub(positionInTile);

        } else if(sign.x > 0 && sign.y < 0) {
            nextTileDistance = new Vector2(tileSize.x - positionInTile.x, 0 + positionInTile.y);

        } else if(sign.x < 0 && sign.y > 0) {
            nextTileDistance = new Vector2(0 + positionInTile.x, tileSize.y - positionInTile.y);

        } else {
            nextTileDistance = new Vector2(0 + positionInTile.x, 0 + positionInTile.y);
        }

        if(nextTileDistance.x == 0) {
            nextTileDistance.x = tileSize.x;
        }
        if(nextTileDistance.y == 0) {
            nextTileDistance.y = tileSize.y;
        }

        Vector2 nextStepDistance = nextTileDistance.cpy()
                .scl(
                        Math.abs((float) (1D / Math.sin((Math.PI / 2) - direction.angleRad()))),
                        Math.abs((float) (1D / Math.sin((Math.PI / 2) - ((Math.PI / 2) - direction.angleRad()))))
                );

        if(nextStepDistance.x < nextStepDistance.y) {
            position.add(nextTileDistance.x * sign.x, marginalTileDistance.x * nextTileDistance.x * sign.y);
            distance += nextStepDistance.x;
        } else {
            position.add(marginalTileDistance.y * nextTileDistance.y * sign.x, nextTileDistance.y  * sign.y);
            distance += nextStepDistance.y;
        }
    }

    // MARK: - Public

    /**
     * Change the direction of the ray. Do not use
     * ray.getDirection().set(Vector2), it will result in skewed calculations.
     * @param direction New direction
     */
    public void updateDirection(Vector2 direction) {
        this.direction = direction.cpy();
        this.marginalTileDistance = new Vector2(
                Math.abs(direction.y / direction.x),
                Math.abs(direction.x / direction.y)
        );
        this.sign = new Vector2(
                Math.copySign(1f, direction.x),
                Math.copySign(1f, direction.y)
        );
    }

    // MARK: - Getters

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return this.direction;
    }

    /**
     * @return Distance the ray had travelled from its origin.
     */
    public float getDistance() {
        return distance;
    }

    // MARK: - Setters

    /**
     * Sets a new tile size for the ray to continue in. Can be used in
     * combination with {@link #updateDirection(Vector2)} to get shape
     * information about more advanced objects.
     * @param tileSize New tile size
     */
    public void setTileSize(Vector2 tileSize) {
        this.tileSize = tileSize;
    }
}
