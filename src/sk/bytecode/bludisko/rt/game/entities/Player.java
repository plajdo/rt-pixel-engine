package sk.bytecode.bludisko.rt.game.entities;

import sk.bytecode.bludisko.rt.game.math.Vector2;

public class Player extends Entity {

    public Player(Vector2 position, Vector2 direction, float positionZ, float pitch) {
        super(position, direction, positionZ, pitch, 1.5f);
    }

    @Override
    public void tick(float dt) {
        super.tick(dt);
    }

}
