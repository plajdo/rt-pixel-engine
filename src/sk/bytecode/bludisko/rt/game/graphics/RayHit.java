package sk.bytecode.bludisko.rt.game.graphics;

import sk.bytecode.bludisko.rt.game.math.Vector2;

public record RayHit<T>(T result, Vector2 position, float distance) {}
