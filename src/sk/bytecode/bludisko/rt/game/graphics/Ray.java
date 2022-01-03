package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.util.function.Supplier;

public final class Ray<T extends Traceable> {

    public static record Hit<T>(T result, Vector2 position, float distance) {}

    private Vector2 position;
    private Vector2 startingPosition;
    private Vector2 direction;

    private Vector2 marginalTileDistance;
    private Vector2 tileSize;
    private Vector2 sign;

    public Ray(Vector2 position, Vector2 direction, Vector2 tileSize) {
        this(position, direction);
        this.tileSize = tileSize;
    }

    public Ray(Vector2 position, Vector2 direction) {
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

    /**
     *
     * @param steps Maximum number of steps to take before returning
     * @param traceableSupplier Function that supplies an object, which can be traced.
     * @return Hit record containing the last object that was traced, position at
     * which the object was found and distance from the rays' origin.
     * @see Traceable
     */
    public Ray.Hit<T> cast(int steps, Supplier<T> traceableSupplier) {
        T traceableObject = null;
        for(int i = 0; i < steps; i++) {
            step();

            traceableObject = traceableSupplier.get();
            var result = traceableObject.hitDistance(this);

            if(result >= 0) {
                position.add(direction.cpy().scl(result));
                break;
            }
        }

        var distance = Vector2.dst(position.x, position.y, startingPosition.x, startingPosition.y);
        return new Ray.Hit<>(traceableObject, this.position.cpy(), distance);
    }

    public void step() {
        Vector2 positionInTile;
        if(tileSize.x != 1 || tileSize.y != 1) {
            positionInTile = MathUtils.part(position, (int) (1 / Math.min(tileSize.x, tileSize.y)));
        } else {
            positionInTile = MathUtils.decimalPart(position);
        }

        Vector2 nextTileDistance;
        if(sign.x > 0 && sign.y > 0) {
            nextTileDistance = new Vector2(tileSize).sub(positionInTile); // TODO: optimalizovať ify

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
        } else {
            position.add(marginalTileDistance.y * nextTileDistance.y * sign.x, nextTileDistance.y  * sign.y);
        }
    }
/*
    public Ray.Hit cast2() {
        Vector2 tileDistance = new Vector2(
                Math.abs(direction.y / direction.x),
                Math.abs(direction.x / direction.y)
        );

        Vector2 sign = new Vector2(
                Math.copySign(1f, direction.x),
                Math.copySign(1f, direction.y)
        );

        Vector2 rawSign = new Vector2( // TODO: ku optimalizácii ifov dolu?
                Float.floatToRawIntBits(sign.x) >>> 31,
                Float.floatToRawIntBits(sign.y) >>> 31
        ).add(1f, 1f).scl(0.5f);

        Block block = BlockManager.getBlock(0);
        float distance = Float.POSITIVE_INFINITY;
        float side = 0f;

        boolean hit = false;
        int steps = 100;

        while(!hit && steps > 0) {
            Vector2 positionInTile = MathUtils.decimalPart(position);

            Vector2 nextTileDistance;
            if(sign.x > 0 && sign.y > 0) {
                nextTileDistance = new Vector2(1, 1).sub(positionInTile); // TODO: optimalizovať ify

            } else if(sign.x > 0 && sign.y < 0) {
                nextTileDistance = new Vector2(1 - positionInTile.x, 0 + positionInTile.y);

            } else if(sign.x < 0 && sign.y > 0) {
                nextTileDistance = new Vector2(0 + positionInTile.x, 1 - positionInTile.y);

            } else {
                nextTileDistance = new Vector2(0 + positionInTile.x, 0 + positionInTile.y);
            }

            if(nextTileDistance.x == 0) {
                nextTileDistance.x = 1;
            }
            if(nextTileDistance.y == 0) {
                nextTileDistance.y = 1;
            }

            Vector2 nextStepDistance = nextTileDistance.cpy()
                    .scl(
                            Math.abs((float) (1D / Math.sin((Math.PI / 2) - direction.angleRad()))),
                            Math.abs((float) (1D / Math.sin((Math.PI / 2) - ((Math.PI / 2) - direction.angleRad()))))
                    );

            if(nextStepDistance.x < nextStepDistance.y) {
                position.add(nextTileDistance.x * sign.x, tileDistance.x * nextTileDistance.x * sign.y);
                side = 0f; // TODO: remove side handling from Ray
            } else {
                position.add(tileDistance.y * nextTileDistance.y * sign.x, nextTileDistance.y  * sign.y);
                side = 1f;
            }

            block = map.getBlockAt(position);
            float hitDistance = block.rayHitDistance(this);

            if(hitDistance >= 0) {
                hit = true;
                position.add(direction.cpy().scl(hitDistance));
                distance = new Vector2(this.position.cpy().sub(startingPosition)).len();
            }
            steps--;
        }
        return new Ray.Hit(block, distance, side);
    }

    public float cast() {
        int currentTileX = (int) this.position.x;
        int currentTileY = (int) this.position.y;

        int tileStepX;
        int tileStepY;

        Vector2 tileLength = new Vector2();
        Vector2 distance = new Vector2();

        // tileLength.x = Math.abs(1 / direction.x);
        // tileLength.y = Math.abs(1 / direction.y);

        tileLength.x = (float) Math.sqrt(1 + (direction.y * direction.y) / (direction.x * direction.x));
        tileLength.y = (float) Math.sqrt(1 + (direction.x * direction.x) / (direction.y * direction.y));

        // First step

        if(direction.x < 0) {
            tileStepX = -1;
            distance.x = (position.x - currentTileX) * tileLength.x;
        } else {
            tileStepX = 1;
            distance.x = (currentTileX + 1 - position.x) * tileLength.x;
        }
        if(direction.y < 0) {
            tileStepY = -1;
            distance.y = (position.y - currentTileY) * tileLength.y;
        } else {
            tileStepY = 1;
            distance.y = (currentTileY + 1 - position.y) * tileLength.y;
        }

        // Next hit

        boolean hit = false;
        int side = 0;

        while(!hit && distance.len2() < Config.Display.RENDER_DISTANCE_SQ) {
            if(distance.x < distance.y) {
                distance.x += tileLength.x;
                currentTileX += tileStepX;
                side = 0;
            } else {
                distance.y += tileLength.y;
                currentTileY += tileStepY;
                side = 1;
            }
            //currentPosition.set(position.cpy().add(distance));

            //if(map.getTile(currentTileX, currentTileY) > 0) {
                //hit = true;
                //hitCoords.set(currentTileX, currentTileY);
            //}

            var block = map.getBlockAt(currentTileX, currentTileY);
            var hitDistance = block.rayHitDistance(this);

            if(hitDistance >= 0) {
                hit = true;
            }
        }
        distance.sub(tileLength);

        if(!hit) {
            return Float.POSITIVE_INFINITY;
        }
        //return distance.len();
        //if(side == 0) {
            //return distance.x;
        //}
        return distance.y;
        //return Float.POSITIVE_INFINITY;
    }*/

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getDirection() {
        return this.direction;
    }
}
