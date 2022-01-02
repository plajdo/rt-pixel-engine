package sk.bytecode.bludisko.rt.game.graphics;

public record Color(int argb) {

    public int scaled(float scale) {
        int alpha = (int) (((0xFF000000 & argb) >>> 24) * scale) << 24;
        int red = (int) (((0x00FF0000 & argb) >>> 16) * scale) << 16;
        int green = (int) (((0x0000FF00 & argb) >>> 8) * scale) << 8;
        int blue = (int) ((0x000000FF & argb) * scale);

        return alpha | red | green | blue;
    }

}
