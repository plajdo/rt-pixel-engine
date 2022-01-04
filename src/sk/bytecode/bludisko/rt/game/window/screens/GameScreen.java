package sk.bytecode.bludisko.rt.game.window.screens;

import sk.bytecode.bludisko.rt.game.graphics.Camera;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.GameInputManagerDelegate;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.map.GameMap;
import sk.bytecode.bludisko.rt.game.math.MathUtils;
import sk.bytecode.bludisko.rt.game.math.Vector2;
import sk.bytecode.bludisko.rt.game.window.Window;

import java.awt.Graphics;
import java.awt.Rectangle;

public final class GameScreen extends Screen implements GameInputManagerDelegate {

    private final InputManager gameInput = new GameInputManager();

    private Camera camera;
    private GameMap map;

    // MARK: - Constructor

    public GameScreen() {
        setupMap();
        setupCamera();
        setupInput();
    }

    private void setupMap() {
        this.map = new GameMap("testMap3", 0, 0);
    }

    // TODO: setup player
    private void setupCamera() {
        //Vector2 cameraPosition = new Vector2(21f, 13f);
        Vector2 cameraPosition = new Vector2(2f, 5f);
        Vector2 cameraDirection = new Vector2(0, 1);
        this.camera = new Camera(map.getWallMap(), cameraPosition, cameraDirection);
    }

    private void setupInput() {
        gameInput.setDelegate(this);
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

        camera.tick(dt);
    }

    @Override
    public void draw(Graphics graphics) {
        camera.draw(graphics);
    }

    // MARK: - Input manager

    @Override
    public void didUpdateDirection(Vector2 direction) {
        camera.movementVector = direction;
    }

    @Override
    public void didUpdateRotation(Vector2 rotation) {
        camera.rotate(rotation.x * MathUtils.degreesToRadians);
    }

}
