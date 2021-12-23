package sk.bytecode.bludisko.rt.game.screens;

import sk.bytecode.bludisko.rt.game.engine.Window;
import sk.bytecode.bludisko.rt.game.input.InputManager;
import sk.bytecode.bludisko.rt.game.input.InputManagerDelegate;

import java.awt.Graphics;
import java.lang.ref.WeakReference;

public abstract class Screen implements InputManagerDelegate {

    protected WeakReference<Window> window;

    public abstract void tick(float dt);
    public abstract void draw(Graphics graphics);

    public abstract InputManager getInputManager();

    public void screenWillAppear(Window window) {
        this.window = new WeakReference<>(window);
    }
    public void screenDidAppear() {}

    // TODO: didChangeResolution
    // TODO: updatovat inputmanager z gamescreenu podla zmeny rozlisenia nech tam nie je referencia na screen/window

}
