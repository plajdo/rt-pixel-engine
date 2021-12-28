package sk.bytecode.bludisko.rt.desktop;

import sk.bytecode.bludisko.rt.game.graphics.texture.TextureManager;
import sk.bytecode.bludisko.rt.game.window.Window;
import sk.bytecode.bludisko.rt.game.window.screens.GameScreen;

public class DesktopLauncher {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        TextureManager.loadAll();
        System.out.println("Loading took " + (System.currentTimeMillis() - start) + " ms");

        Window window = new Window(new GameScreen());
        window.start();
    }

}
