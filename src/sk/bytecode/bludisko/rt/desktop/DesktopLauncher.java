package sk.bytecode.bludisko.rt.desktop;

import sk.bytecode.bludisko.rt.game.graphics.TextureManager;
import sk.bytecode.bludisko.rt.game.window.Window;
import sk.bytecode.bludisko.rt.game.window.screens.GameScreen;

public class DesktopLauncher {

    public static void main(String[] args) {
        TextureManager.loadAll();

        Window window = new Window(new GameScreen());
        window.start();
    }

}
