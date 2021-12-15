package sk.bytecode.bludisko.rt.game.screens;

import sk.bytecode.bludisko.rt.game.engine.GameWorld;

import java.awt.*;

public class GameScreen extends Screen {

    private GameWorld gameWorld = new GameWorld();

    @Override
    public void tick(float dt) {
        gameWorld.tick(dt);
    }

    @Override
    public void draw(Graphics graphics) {
        gameWorld.draw(graphics);
    }

}
