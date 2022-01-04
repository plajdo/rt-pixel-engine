package sk.bytecode.bludisko.rt.game.entities;

import sk.bytecode.bludisko.rt.game.math.Vector2;

public abstract class Entity {

    protected final Vector2 movementVector;
    protected float speed;

    protected final Vector2 position;
    protected float positionZ;

    protected final Vector2 direction;
    protected float pitch;

    public Entity(Vector2 position, Vector2 direction, float positionZ, float pitch, float speed) {
        this.movementVector = new Vector2(0f, 0f);

        this.speed = speed;
        this.position = position;
        this.positionZ = positionZ;
        this.direction = direction;
        this.pitch = pitch;
    }

    public void tick(float dt) {
        position.add(movementVector.cpy().scl(dt).scl(speed).rotateRad(direction.angleRad()));
    }
}
