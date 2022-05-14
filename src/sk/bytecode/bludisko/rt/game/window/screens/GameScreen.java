package sk.bytecode.bludisko.rt.game.window.screens;

import sk.bytecode.bludisko.rt.game.entities.Player;
import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.items.PortalGun;
import sk.bytecode.bludisko.rt.game.map.Chamber1;
import sk.bytecode.bludisko.rt.game.map.World;
import sk.bytecode.bludisko.rt.game.util.NullSafe;
import sk.bytecode.bludisko.rt.game.window.Window;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Implemented main game screen.
 * Contains Camera for drawing and game World for ticking
 * and updating the game logic.
 * @see Camera
 * @see World
 */
public final class GameScreen extends Screen {

    private final InputManager gameInput = new GameInputManager();
    private World currentWorld;

    private Player player;
    private Camera camera;

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

        player.setCamera(camera);
        currentWorld.setPlayer(player);

        player.equip(new PortalGun(player));
    }

    private void setupInput() {
        gameInput.setDelegate(this.player);
        gameInput.setMouseLocked(true);
    }

    // MARK: - Screen

    @Override
    public InputManager getInputManager() {
        return gameInput;
    }

    @Override
    public void screenDidAppear() {
        super.screenDidAppear();

        NullSafe.acceptWeak(window, window -> window.setCursorVisible(false));
    }

    @Override
    public void screenDidChangeBounds(Rectangle bounds) {
        super.screenDidChangeBounds(bounds);

        NullSafe.acceptWeak(window, window -> {
            camera.setScreenSize(window.canvasBounds());
            player.setItemOverlayScreenSizeInformation(window.canvasBounds());
        });
    }

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        super.tick(dt);
        player.tick(dt);
        currentWorld.tick(dt);
    }

    @Override
    public void draw(Graphics graphics) {
        camera.draw(graphics);
        player.drawItemOverlay(graphics);
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
