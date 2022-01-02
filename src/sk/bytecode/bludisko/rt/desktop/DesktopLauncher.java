package sk.bytecode.bludisko.rt.desktop;

import sk.bytecode.bludisko.rt.game.window.Window;
import sk.bytecode.bludisko.rt.game.window.screens.GameScreen;

public class DesktopLauncher {

    public static void main(String[] args) {
        Window window = new Window(new GameScreen());
        window.start();
    }

}
