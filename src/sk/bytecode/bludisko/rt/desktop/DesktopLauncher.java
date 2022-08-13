package sk.bytecode.bludisko.rt.desktop;

import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.window.Window;
import sk.bytecode.bludisko.rt.game.window.screens.MenuScreen;

public class DesktopLauncher {

    /**
     * Loads and generates all textures and starts the game.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        TextureManager.loadAll();

        Window window = new Window(new MenuScreen());
        window.start();
    }

}
