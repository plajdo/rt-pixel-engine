package sk.bytecode.bludisko.rt.game.graphics;

import org.jetbrains.annotations.NotNull;
import sk.bytecode.bludisko.rt.game.window.Window;
import sk.bytecode.bludisko.rt.game.window.screens.Screen;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface Actor extends Tickable {

    /**
     * Game loop - graphics.
     * Called every frame after updating drawing information.
     * Graphics object should not be finalized or closed inside draw() methods,
     * in case another object wants to draw more information into Canvas.
     * Finalizing and showing the image is handled automatically by Window.
     * @param graphics Java AWT graphics to draw into. Will be displayed on the Window's Canvas
     * @see Window
     * @see Screen#tick(float)
     */
    void draw(@NotNull Graphics graphics);

    /**
     * Called every time the drawing quality is updated or the Window is resized.
     * @param bounds Updated bounds rectangle
     * @see Window#canvasBounds()
     */
    void screenDidChangeBounds(@NotNull Rectangle bounds);

}
