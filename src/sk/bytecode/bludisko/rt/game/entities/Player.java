package sk.bytecode.bludisko.rt.game.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.graphics.DistanceRay;
import sk.bytecode.bludisko.rt.game.input.GameInputManagerDelegate;
import sk.bytecode.bludisko.rt.game.items.Item;
import sk.bytecode.bludisko.rt.game.map.Map;
import sk.bytecode.bludisko.rt.game.map.World;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * A Player object representing the real player. Handles input and moves the camera accordingly.
 */
public class Player extends Entity implements GameInputManagerDelegate {

    private static final float WALKING_SPEED = 1.75f;
    private static final float RUNNING_SPEED = 2.5f;

    private Map worldWallMap;
    private WeakReference<Camera> camera;

    private Vector2 movementVector;
    private Item heldItem;

    private float movementSpeed = 1.75f;

    // MARK: - Constructor

    /**
     * Creates a new Player inside a given World.
     * World information is used to determine movement and initial coordinates.
     * @param world World to get information from
     */
    public Player(World world) {
        super(
                world,
                world.getMap().getSpawnLocation(),
                world.getMap().getSpawnDirection(),
                50f,
                0f
        );

        this.movementVector = new Vector2(0f, 0f);
        this.worldWallMap = world.getMap().walls();
    }

    // MARK: - Public

    /**
     * Sets a new world for the Player to use.
     * @param world New world
     */
    public void setWorld(World world) {
        this.world = world;
        this.worldWallMap = world.getMap().walls();
    }

    /**
     * Gives this Player a camera to control.
     * @param camera Camera to control
     */
    public void setCamera(Camera camera) {
        this.camera = new WeakReference<>(camera);
    }

    /**
     * Equips an item
     * @param item Item to equip
     */
    public void equip(@Nullable Item item) {
        this.heldItem = item;
    }

    /**
     * @return Currently held item or null
     */
    public Item getHeldItem() {
        return heldItem;
    }

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        move(dt);
        NullSafe.acceptWeak(camera, camera -> camera.bind(this));
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

    @Override
    public void didUpdateSprintingStatus(boolean isSprinting) {
        this.movementSpeed = isSprinting ? Player.RUNNING_SPEED : Player.WALKING_SPEED;
    }

    @Override
    public void didToggleMouseButton(boolean rmb) {
        NullSafe.accept(heldItem, rmb ? Item::useSecondary : Item::use);
    }

    // MARK: - Private

    private void move(float dt) {
        var movementVector = this.movementVector.cpy()
                .scl(movementSpeed)
                .scl(dt)
                .rotateRad(direction.angleRad());

        var movementRay = new DistanceRay(this.worldWallMap, this.position.cpy(), movementVector.cpy().nor());
        var nextHitDistance = movementRay.cast(movementVector.len());

        if(Float.isNaN(nextHitDistance)) {
            position.set(movementRay.getPosition());
            direction.set(movementRay.getDirection());
        } else if(nextHitDistance == -1) {
            position.add(movementVector);
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
