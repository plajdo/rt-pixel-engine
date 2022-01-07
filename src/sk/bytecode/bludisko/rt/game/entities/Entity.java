package sk.bytecode.bludisko.rt.game.entities;

import sk.bytecode.bludisko.rt.game.map.World;
import sk.bytecode.bludisko.rt.game.math.Vector2;

/**
 * Class representing an entity in the world. Contains its position.
 */
public abstract class Entity {

    private World world;
    private Vector2 position;
    private float positionZ;

    private Vector2 direction;
    private float pitch;

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
        return this.world;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public float getPositionZ() {
        return this.positionZ;
    }

    public Vector2 getDirection() {
        return this.direction;
    }

    public float getPitch() {
        return this.pitch;
    }

    protected void setWorld(World world) {
        this.world = world;
    }

    protected void setPosition(Vector2 position) {
        this.position = position;
    }

    protected void setPositionZ(float positionZ) {
        this.positionZ = positionZ;
    }

    protected void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    protected void setPitch(float pitch) {
        this.pitch = pitch;
    }

}
