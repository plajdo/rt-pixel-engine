package sk.bytecode.bludisko.rt.game.util;

import sk.bytecode.bludisko.rt.game.serialization.Serializable;

import java.awt.event.KeyEvent;

public class Config {

    public static class Keybinds {

        public static final int UP = KeyEvent.VK_W;
        public static final int LEFT = KeyEvent.VK_A;
        public static final int DOWN = KeyEvent.VK_S;
        public static final int RIGHT = KeyEvent.VK_D;

        public static final int LOCK_MOUSE = KeyEvent.VK_ESCAPE;

    }

    public static class Display {

        public static final float DRAWING_QUALITY = 0.5f;
        public static final int RENDER_DISTANCE = 100;

    }

}
