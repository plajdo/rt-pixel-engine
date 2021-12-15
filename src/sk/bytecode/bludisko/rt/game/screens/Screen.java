package sk.bytecode.bludisko.rt.game.screens;

import sk.bytecode.bludisko.rt.game.engine.Window;

import java.awt.Graphics;
import java.lang.ref.WeakReference;

public abstract class Screen {

    protected WeakReference<Window> window;

    public abstract void tick(float dt);
    public abstract void draw(Graphics graphics);

    public void screenWillAppear(Window window) {
        this.window = new WeakReference<>(window);
    }
    public void screenDidAppear() {}

}
