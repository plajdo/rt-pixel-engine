package sk.bytecode.bludisko.rt.game.input;

import sk.bytecode.bludisko.rt.game.math.Vector2;

public interface InputManagerDelegate {

    void direction(int direction);
    void rotation(Vector2 rotation);

}
