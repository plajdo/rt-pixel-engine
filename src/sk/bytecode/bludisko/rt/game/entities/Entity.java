package sk.bytecode.bludisko.rt.game.entities;

import sk.bytecode.bludisko.rt.game.graphics.Tickable;
import sk.bytecode.bludisko.rt.game.map.World;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Class representing an entity in the world. Contains its position.
 */
public abstract class Entity implements Tickable {

    protected World world;
    protected Vector2 position;
    protected float positionZ;

    protected Vector2 direction;
    protected float pitch;

    // MARK: - Constructor

    /**
     * New entity with given parameters.
     */
    public Entity(World world, Vector2 position, Vector2 direction, float positionZ, float pitch) {
        this.world = world;
        this.position = position;
        this.positionZ = positionZ;
        this.direction = direction;
        this.pitch = pitch;
    }

    /**
     * Notifies the entity to process its tick.
     * @param dt Time that's passed since the last frame has finished rendering.
     */
    public abstract void tick(float dt);

    // MARK: - Getters

    public World getWorld() {
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
