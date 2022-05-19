package sk.bytecode.bludisko.rt.game.input;

import sk.bytecode.bludisko.rt.game.util.NullSafe;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;

public class MenuInputManager extends InputManager {

    private WeakReference<MenuInputManagerDelegate> delegate;

    // MARK: - Public

    /**
     * Sets the delegate whose methods are called, when a change in input
     * is detected.
     * @param delegate Object implementing the delegate methods.
     * @see GameInputManagerDelegate
     */
    public void setDelegate(MenuInputManagerDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    // MARK: - KeyListener

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // MARK: - MouseListener

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        NullSafe.acceptWeak(delegate, delegate -> delegate.touchesBegan(e.getPoint()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        NullSafe.acceptWeak(delegate, delegate -> delegate.touchesEnded(e.getPoint()));
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {
        NullSafe.acceptWeak(delegate, delegate -> delegate.touchesCancelled(e.getPoint()));
    }

    // MARK: - MouseMotionListener

    @Override
    public void mouseDragged(MouseEvent e) {
        NullSafe.acceptWeak(delegate, delegate -> delegate.touchesCancelled(e.getPoint()));
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

}
