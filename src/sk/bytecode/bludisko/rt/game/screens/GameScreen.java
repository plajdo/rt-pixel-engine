package sk.bytecode.bludisko.rt.game.screens;

import sk.bytecode.bludisko.rt.game.engine.GameWorld;
import sk.bytecode.bludisko.rt.game.input.GameInputManager;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.math.Vector2;

import java.awt.*;

public final class GameScreen extends Screen {

    private final Robot robot;

    private final GameWorld gameWorld = new GameWorld();
    private final InputManager gameInput = new GameInputManager();

    public GameScreen() {
        try {
            this.robot = new Robot();
        } catch(AWTException e) {
            throw new RuntimeException("fuck"); // TODO: no
        }

        gameInput.setDelegate(this);
    }

    private void lockMouse() {
        var window = this.window.get();

        assert robot != null;
        assert window != null;

        var dimensions = window.dimensions();
        robot.mouseMove(dimensions.width / 2, dimensions.height / 2);
    }

    @Override
    public void tick(float dt) {
        lockMouse();
        gameWorld.tick(dt);
    }

    @Override
    public void draw(Graphics graphics) {
        gameWorld.draw(graphics);
    }

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
