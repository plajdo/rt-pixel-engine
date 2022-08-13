package sk.bytecode.bludisko.rt.game.window.screens;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.entities.Player;
import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.graphics.Overlay;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.items.PortalGun;
import sk.bytecode.bludisko.rt.game.map.Chamber1;
import sk.bytecode.bludisko.rt.game.map.World;
import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Main game screen. Is responsible for managing all rendering and ticking
 * of the game world. Provides concrete implementation of InputManager.
 * @see Camera
 * @see Player
 * @see Overlay
 * @see World
 * @see InputManager
 */
public final class GameScreen extends Screen {

    private final GameInputManager gameInputManager = new GameInputManager();
    private World currentWorld;

    private Player player;
    private Camera camera;
    private Overlay overlay;

    // MARK: - Constructor

    /**
     * Default constructor.
     * Constructs the GameScreen and sets up the Player and input.
     * @see Player
     * @see sk.bytecode.bludisko.rt.game.input.GameInputManagerDelegate
     */
    public GameScreen() {
        this.currentWorld = new Chamber1();

        setupPlayer();
        setupInput();
    }

    private void setupPlayer() {
        player = new Player(currentWorld);
        camera = new Camera();
        overlay = new Overlay();

        player.setCamera(camera);

        overlay.connectPlayer(player);
        currentWorld.setPlayer(player);

        player.equip(new PortalGun(player));
    }

    private void setupInput() {
        gameInputManager.setDelegate(this.player);
        gameInputManager.setMouseLocked(true);
    }

    // MARK: - Override

    @Override
    public InputManager getInputManager() {
        return gameInputManager;
    }

    @Override
    public void screenDidAppear() {
        super.screenDidAppear();

        NullSafe.acceptWeak(window, window -> window.setCursorVisible(false));
    }

    @Override
    public void screenDidChangeBounds(@NotNull Rectangle bounds) {
        super.screenDidChangeBounds(bounds);

        camera.setScreenSize(bounds);
        overlay.screenDidChangeBounds(bounds);
    }

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        super.tick(dt);

        player.tick(dt);
        overlay.tick(dt);
        currentWorld.tick(dt);
    }

    @Override
    public void draw(@NotNull Graphics graphics) {
        camera.draw(graphics);
        overlay.draw(graphics);
    }

    // MARK: - Public

    /**
     * Sets the world that the Player is currently in and moves the player
     * to that world's default spawn location.
     * @param world New world to set
     */
    public void setWorld(World world) {
        throw new UnsupportedOperationException();
    }

}
