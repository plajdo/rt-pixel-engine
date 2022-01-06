package sk.bytecode.bludisko.rt.game.entities;

import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.graphics.DistanceRay;
import sk.bytecode.bludisko.rt.game.graphics.Ray;
import sk.bytecode.bludisko.rt.game.input.GameInputManagerDelegate;
import sk.bytecode.bludisko.rt.game.map.GameMap;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sun.misc.Unsafe;

public class Player extends Entity implements GameInputManagerDelegate {

    private Camera camera;
    private Vector2 movementVector;

    private float walkingSpeed;

    // MARK: - Constructor

    public Player(GameMap map, Vector2 position, Vector2 direction, float positionZ, float pitch) {
        super(map, position, direction, positionZ, pitch);
        this.movementVector = new Vector2(0f, 0f);
        this.walkingSpeed = 1.5f;
    }

    // MARK: - Public

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        move(dt);
        camera.bind(this);
    }

    // MARK: - Input

    @Override
    public void didUpdateMovementDirection(Vector2 direction) {
        this.movementVector = direction;
    }

    @Override
    public void didUpdateRotation(Vector2 rotation) {
        this.rotate(rotation.x * MathUtils.degreesToRadians, rotation.y * 0.1f);
    }

    // MARK: - Private

    private boolean canMove(Vector2 direction, float distance) {
        var movementRay = new DistanceRay(this.map.walkable(), this.position.cpy(), direction.cpy().nor());
        var hit = movementRay.cast(distance);

        return hit < 0;
    }

    private void move(float dt) {
        var distance = movementVector.cpy()
                .scl(walkingSpeed)
                .scl(dt)
                .rotateRad(direction.angleRad());

        if(canMove(distance, distance.len())) {
            position.add(distance);
        }
    }

    private void rotate(float angleDeg, float pitch) {
        direction.rotateDeg(angleDeg);

        this.pitch += pitch;
        if(this.pitch < -200) {
            this.pitch = -200;
        }
        if(this.pitch > 200) {
            this.pitch = 200;
        }
    }

}
