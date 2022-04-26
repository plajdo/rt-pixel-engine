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

import java.awt.*;

/**
 * A Player object representing the real player. Handles input and moves the camera accordingly.
 */
public class Player extends Entity implements GameInputManagerDelegate {

    private Map worldWallMap;

    private Camera camera;
    private Vector2 movementVector;

    private Item heldItem;

    private float walkingSpeed = 1.75f;

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
        this.camera = camera;
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
        camera.bind(this);
    }

    /**
     * Draws player overlay - currently held item to current graphics content.
     * @param graphics Graphics content to draw on
     */
    public void drawItemOverlay(@NotNull Graphics graphics) {
        if(heldItem != null) {
            var overlay = heldItem.getOverlay().toImage();
            graphics.drawImage(overlay, 0, 0, 320, 240, null);
        }
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
        this.walkingSpeed = isSprinting ? 2.5f : 1.75f;
    }

    // MARK: - Private

    private void move(float dt) {
        var movementVector = this.movementVector.cpy()
                .scl(walkingSpeed)
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
