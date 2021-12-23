package sk.bytecode.bludisko.rt.game.input;

import javax.swing.event.MouseInputListener;
import java.awt.event.KeyListener;

public abstract class InputManager implements KeyListener, MouseInputListener {

    protected InputManagerDelegate delegate; // TODO: weak reference!

    public void setDelegate(InputManagerDelegate delegate) {
        this.delegate = delegate;
    }

}
