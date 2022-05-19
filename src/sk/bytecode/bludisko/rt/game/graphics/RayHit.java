package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Record containing the object the Ray had hit, position of the ray
 * at the hit moment and the distance it had travelled from its origin
 * until it had reached the hit location with the object.
 * @param <T> Type of the object
 */
public record RayHit<T>(T result, Vector2 position, float distance) {}
