package sk.bytecode.bludisko.rt.game.util;

import java.awt.event.KeyEvent;

public class Config {

    public static class Keybinds {

        public static final int UP = KeyEvent.VK_W;
        public static final int LEFT = KeyEvent.VK_A;
        public static final int DOWN = KeyEvent.VK_S;
        public static final int RIGHT = KeyEvent.VK_D;

        public static final int LOCK_MOUSE = KeyEvent.VK_ESCAPE;
        public static final int SPRINT = KeyEvent.VK_CONTROL;

    }

    public static class Display {

        private static float drawingQuality = 1f;
        private static int renderDistance = 100;

        public static void setDrawingQuality(float newDrawingQuality) {
            Display.drawingQuality = newDrawingQuality;
        }

        public static float getDrawingQuality() {
            return drawingQuality;
        }

        public static int getRenderDistance() {
            return renderDistance;
        }

    }

}