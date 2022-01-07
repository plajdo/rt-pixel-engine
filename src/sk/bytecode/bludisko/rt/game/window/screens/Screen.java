package sk.bytecode.bludisko.rt.game.window.screens;

import sk.bytecode.bludisko.rt.game.window.Window;
import sk.bytecode.bludisko.rt.game.input.InputManager;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;

/**
 * Empty screen with basic logic implemented. Contains default controls
 * of an InputManager.
 */
public abstract class Screen {

    private WeakReference<Window> window;

    // MARK: - Game loop

    /**
     * Game loop - logic.
     * Called every frame before rendering. Should contain
     * logic that's used to update information for renderer to draw
     * a new frame.
     * @param dt Time passed since the last frame render
     */
    public void tick(float dt) {
        this.getInputManager().tick(dt);
    }

    /**
     * Game loop - graphics.
     * Called every frame after updating drawing information.
     * Graphics object should not be finalized or closed inside draw() methods,
     * in case another object wants to draw more information into Canvas.
     * Finalizing and showing the image is handled automatically by Window.
     * @param graphics Java AWT graphics to draw into. Will be displayed on the Window's Canvas.
     * @see Window
     * @see Screen#tick(float)
     */
    public abstract void draw(Graphics graphics);

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
        this.setWindow(new WeakReference<>(window));
    }

    /**
     * Called after this Screen has been displayed and is
     * about to tick and draw the first frame.
     * @see Screen#draw(Graphics)
     */
    public void screenDidAppear() { }

    /**
     * Called every time the Window is resized.
     * @param bounds Updated Window bounds.
     * @see Window#canvasBounds()
     */
    public void screenDidChangeBounds(Rectangle bounds) {
        this.getInputManager().updateWindowDimensions(bounds);
    }

    protected WeakReference<Window> getWindow() {
        return this.window;
    }

    protected void setWindow(WeakReference<Window> window) {
        this.window = window;
    }
}
