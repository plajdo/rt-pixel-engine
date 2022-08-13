package sk.bytecode.bludisko.rt.game.input;

import java.awt.Point;

public interface MenuInputManagerDelegate {

    /**
     * Called when the user pushes down the mouse button.
     * @param point Location of the click event
     */
    void touchesBegan(Point point);

    /**
     * Called when the user stops pushing the mouse button.
     * @param point
     */
    void touchesEnded(Point point);

    /**
     * Called when the user drags the mouse outside the window.
     * @param point
     */
    void touchesCancelled(Point point);

}
