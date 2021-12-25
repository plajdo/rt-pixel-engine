package sk.bytecode.bludisko.rt.game.screens;

import sk.bytecode.bludisko.rt.game.engine.Window;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.input.InputManagerDelegate;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;

public abstract class Screen implements InputManagerDelegate {

    protected WeakReference<Window> window;

    // MARK: - Game loop

    public void tick(float dt) {
        getInputManager().tick(dt);
    }

    public abstract void draw(Graphics graphics);

    // MARK: - Public

    public abstract InputManager getInputManager();

    public void screenWillAppear(Window window) {
        this.window = new WeakReference<>(window);
    }

    public void screenDidAppear() {}

    public void screenDidChangeResolution(Rectangle newDimensions) {
        this.getInputManager().updateWindowDimensions(newDimensions);
    }

}
