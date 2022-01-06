package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public class Ray {

    public static record Hit<T>(T result, Vector2 position, float distance) {}

    protected Map map;
    protected Vector2 position;
    protected Vector2 startingPosition;
    protected Vector2 direction;
    protected float distance;

    private Vector2 marginalTileDistance;
    private Vector2 tileSize;
    private Vector2 sign;

    public Ray(Map map, Vector2 position, Vector2 direction, Vector2 tileSize) {
        this(map, position, direction);
        this.tileSize = tileSize;
    }

    public Ray(Map map, Vector2 position, Vector2 direction) {
        this.map = map;
        this.position = position.cpy();
        this.startingPosition = position.cpy();
        this.tileSize = new Vector2(1, 1);

        updateDirection(direction);
    }

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

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return this.direction;
    }

    public float getDistance() {
        return distance;
    }

    public void setTileSize(Vector2 tileSize) {
        this.tileSize = tileSize;
    }
}
