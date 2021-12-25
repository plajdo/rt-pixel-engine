package sk.bytecode.bludisko.rt.game.screens;

import sk.bytecode.bludisko.rt.game.engine.GameWorld;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.*;

public final class GameScreen extends Screen {

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
    public void direction(int direction) {
        System.out.println(direction);
        gameWorld.getCamera().movementVector = switch(direction) {
            case 0b0001 -> new Vector2(0, -1);
            case 0b0010 -> new Vector2(-1, 0);
            case 0b0100 -> new Vector2(0, 1);
            case 0b1000 -> new Vector2(1, 0);
            default -> Vector2.Zero.cpy();
        };
    }

    @Override
    public void rotation(Vector2 rotation) {
        gameWorld.getCamera().rotate(rotation.x * 0.025f); // TODO: magic constants fuj
    }

}
