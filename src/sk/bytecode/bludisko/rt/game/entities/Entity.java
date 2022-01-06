package sk.bytecode.bludisko.rt.game.entities;

import sk.bytecode.bludisko.rt.game.map.GameMap;
import sk.bytecode.bludisko.rt.game.map.World;
import sk.bytecode.bludisko.rt.game.math.Vector2;

public abstract class Entity {
    protected World world;
    protected Vector2 position;
    protected float positionZ;

    protected Vector2 direction;
    protected float pitch;

    public Entity(World world, Vector2 position, Vector2 direction, float positionZ, float pitch) {
        this.world = world;
        this.position = position;
        this.positionZ = positionZ;
        this.direction = direction;
        this.pitch = pitch;
    }

    public abstract void tick(float dt);

    public World getWorlod() {
        return world;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getPositionZ() {
        return positionZ;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public float getPitch() {
        return pitch;
    }

}
