package sk.bytecode.bludisko.rt.game.window.screens;

import sk.bytecode.bludisko.rt.game.entities.Player;
import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.map.GameMap;
import sk.bytecode.bludisko.rt.game.window.Window;

import java.awt.Graphics;
import java.awt.Rectangle;

public final class GameScreen extends Screen {

    private final InputManager gameInput = new GameInputManager();

    private Player player;
    private Camera camera;
    private GameMap map;

    // MARK: - Constructor

    public GameScreen() {
        setupMap();
        setupPlayer();
        setupInput();
    }

    private void setupMap() {
        this.map = GameMap.load("testMap3");
    }

    private void setupPlayer() {
        this.player = new Player(
                this.map,
                this.map.getSpawnLocation(),
                this.map.getSpawnDirection(),
                50f,
                0f
        );
        this.camera = new Camera();
        player.setCamera(this.camera);
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

        Window window = this.window.get();
        if(window != null) {
            window.setCursorVisible(false);
        }
    }

    @Override
    public void screenDidChangeBounds(Rectangle bounds) {
        super.screenDidChangeBounds(bounds);

        var window = this.window.get();
        if(window != null) {
            camera.setScreenSize(window.canvasBounds());
        }
    }

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        super.tick(dt);
        player.tick(dt);
    }

    @Override
    public void draw(Graphics graphics) {
        camera.draw(graphics);
    }

}
