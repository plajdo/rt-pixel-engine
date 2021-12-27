package sk.bytecode.bludisko.rt.game.screens;

import sk.bytecode.bludisko.rt.game.engine.GameWorld;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.GameInputManagerDelegate;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.Graphics;

public final class GameScreen extends Screen implements GameInputManagerDelegate {

    private final GameWorld gameWorld = new GameWorld();
    private final InputManager gameInput = new GameInputManager();

    // MARK: - Constructor

    public GameScreen() {
        gameInput.setDelegate(this);
        gameInput.setMouseLock(true);
    }

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        super.tick(dt);

        gameWorld.tick(dt);
    }

    @Override
    public void draw(Graphics graphics) {
        gameWorld.draw(graphics);
    }

    // MARK: - Input manager

    @Override
    public InputManager getInputManager() {
        return gameInput;
    }

    @Override
    public void didUpdateDirection(Vector2 direction) {
        gameWorld.getCamera().movementVector = direction;
    }

    @Override
    public void didUpdateRotation(Vector2 rotation) {
        gameWorld.getCamera().rotate(rotation.x * 0.025f); // TODO: magic constants fuj
    }

}
