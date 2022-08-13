package sk.bytecode.bludisko.rt.game.window.screens;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.graphics.Actor;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.window.Window;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;

/**
 * Empty screen with basic logic implemented. Contains default controls
 * of an InputManager.
 */
public abstract class Screen implements Actor {

    protected WeakReference<Window> window;

    // MARK: - Game loop

    @Override
    public void tick(float dt) {
        this.getInputManager().tick(dt);
    }

    // MARK: - Public

    /**
     * Gets the Input Manager from an implementation.
     * @return Modified input manager for this Screen.
     */
    public abstract InputManager getInputManager();

    /**
     * Called before this Screen is displayed on a Window.
     * @param window Window that the Screen will be displayed on.
     */
    public void screenWillAppear(Window window) {
        this.window = new WeakReference<>(window);
    }

    /**
     * Called after this Screen has been displayed and is
     * about to tick and draw the first frame.
     * @see Screen#draw(Graphics)
     */
    public void screenDidAppear() {}

    /**
     * Called every time the Window is resized.
     * @param bounds Updated Window bounds.
     * @see Window#canvasBounds()
     */
    @Override
    public void screenDidChangeBounds(@NotNull Rectangle bounds) {
        this.getInputManager().screenDidChangeBounds(bounds);
    }

}
