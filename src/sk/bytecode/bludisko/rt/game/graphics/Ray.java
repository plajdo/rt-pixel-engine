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

    private Vector2 position;
    private Vector2 startingPosition;
    private Vector2 direction;
    private float distance;

    private Vector2 marginalTileDistance;
    private Vector2 tileSize;
    private Vector2 sign;

    // MARK: - Constructor

    /**
     * Constructs a new ray with a given tile-size.
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

        this.updateDirection(direction);
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
        if (this.tileSize.x != 1 || this.tileSize.y != 1) {
            positionInTile = MathUtils.fractionRound(this.position, MathUtils.gcd((int)(1f / this.tileSize.x), (int)(1f / this.tileSize.y)));
        } else {
            positionInTile = MathUtils.decimalPart(this.position);
        }

        Vector2 nextTileDistance;
        if (this.sign.x > 0 && this.sign.y > 0) {
            nextTileDistance = new Vector2(this.tileSize).sub(positionInTile);

        } else if (this.sign.x > 0 && this.sign.y < 0) {
            nextTileDistance = new Vector2(this.tileSize.x - positionInTile.x, 0 + positionInTile.y);

        } else if (this.sign.x < 0 && this.sign.y > 0) {
            nextTileDistance = new Vector2(0 + positionInTile.x, this.tileSize.y - positionInTile.y);

        } else {
            nextTileDistance = new Vector2(0 + positionInTile.x, 0 + positionInTile.y);
        }

        if (nextTileDistance.x == 0) {
            nextTileDistance.x = this.tileSize.x;
        }
        if (nextTileDistance.y == 0) {
            nextTileDistance.y = this.tileSize.y;
        }

        Vector2 nextStepDistance = nextTileDistance.cpy()
                .scl(
                        Math.abs((float)(1D / Math.sin((Math.PI / 2) - this.direction.angleRad()))),
                        Math.abs((float)(1D / Math.sin((Math.PI / 2) - ((Math.PI / 2) - this.direction.angleRad()))))
                );

        if (nextStepDistance.x < nextStepDistance.y) {
            this.position.add(nextTileDistance.x * this.sign.x, this.marginalTileDistance.x * nextTileDistance.x * this.sign.y);
            this.distance += nextStepDistance.x;
        } else {
            this.position.add(this.marginalTileDistance.y * nextTileDistance.y * this.sign.x, nextTileDistance.y  * this.sign.y);
            this.distance += nextStepDistance.y;
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
        return this.position;
    }

    public Vector2 getDirection() {
        return this.direction;
    }

    /**
     * @return Distance the ray had travelled from its origin.
     */
    public float getDistance() {
        return this.distance;
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

    protected void setPosition(Vector2 position) {
        this.position = position;
    }

    protected Vector2 getStartingPosition() {
        return this.startingPosition;
    }

    protected void setStartingPosition(Vector2 startingPosition) {
        this.startingPosition = startingPosition;
    }

    protected void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    protected void setDistance(float distance) {
        this.distance = distance;
    }

    protected Vector2 getMarginalTileDistance() {
        return this.marginalTileDistance;
    }

    protected void setMarginalTileDistance(Vector2 marginalTileDistance) {
        this.marginalTileDistance = marginalTileDistance;
    }

    protected Vector2 getTileSize() {
        return this.tileSize;
    }

    protected Vector2 getSign() {
        return this.sign;
    }

    protected void setSign(Vector2 sign) {
        this.sign = sign;
    }

}
