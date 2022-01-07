package sk.bytecode.bludisko.rt.game.window.screens;

import sk.bytecode.bludisko.rt.game.entities.Player;
import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.IGameInputManagerDelegate;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.map.Chamber1;
import sk.bytecode.bludisko.rt.game.map.World;
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
     * @see IGameInputManagerDelegate
     */
    public GameScreen() {
        this.currentWorld = new Chamber1();

        this.setupPlayer();
        this.setupInput();
    }

    private void setupPlayer() {
        this.player = new Player(this.currentWorld);
        this.camera = new Camera();

        this.player.setCamera(this.camera);
        this.currentWorld.setPlayer(this.player);
    }

    private void setupInput() {
        this.gameInput.setDelegate(this.player);
        this.gameInput.setMouseLocked(true);
    }

    // MARK: - Screen

    @Override
    public InputManager getInputManager() {
        return this.gameInput;
    }

    @Override
    public void screenDidAppear() {
        super.screenDidAppear();

        Window window = this.getWindow().get();
        if (window != null) {
            window.setCursorVisible(false);
        }
    }

    @Override
    public void screenDidChangeBounds(Rectangle bounds) {
        super.screenDidChangeBounds(bounds);

        var window = this.getWindow().get();
        if (window != null) {
            this.camera.setScreenSize(window.canvasBounds());
        }
    }

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        super.tick(dt);
        this.player.tick(dt);
        this.currentWorld.tick(dt);
    }

    @Override
    public void draw(Graphics graphics) {
        this.camera.draw(graphics);
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
