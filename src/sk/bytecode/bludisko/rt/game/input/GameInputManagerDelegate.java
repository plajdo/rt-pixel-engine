package sk.bytecode.bludisko.rt.game.input;

import sk.bytecode.bludisko.rt.game.math.Vector2;

public interface GameInputManagerDelegate {

    void didUpdateMovementDirection(Vector2 direction);
    void didUpdateRotation(Vector2 rotation);
    void didUpdateSprintingStatus(boolean isSprinting);

}
