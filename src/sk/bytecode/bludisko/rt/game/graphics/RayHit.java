package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Record containing the object the Ray had hit, position of the ray
 * at the critical moment and the distance it had travelled from its origin
 * until it had hit the object.
 * @param <T> Type of the object
 */
//public record RayHit<T>(T result, Vector2 position, float distance) {}

public class RayHit<T> {

    private final T result;
    private final Vector2 position;
    private final float distance;

    public RayHit(T result, Vector2 position, float distance) {
        this.result = result;
        this.position = position;
        this.distance = distance;
    }

    public T result() {
        return result;
    }

    public Vector2 position() {
        return position;
    }

    public float distance() {
        return distance;
    }

}
