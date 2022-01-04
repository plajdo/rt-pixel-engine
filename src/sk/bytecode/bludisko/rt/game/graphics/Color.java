package sk.bytecode.bludisko.rt.game.graphics;

public record Color(int argb) {

    public int scaled(float scale) {
        int alpha = (int) (((0xFF000000 & argb) >>> 24) * scale) << 24;
        int red = (int) (((0x00FF0000 & argb) >>> 16) * scale) << 16;
        int green = (int) (((0x0000FF00 & argb) >>> 8) * scale) << 8;
        int blue = (int) ((0x000000FF & argb) * scale);

        return alpha | red | green | blue;
    }

    public int multiplied(int color) {
        float alpha1 = ((0xFF000000 & argb) >>> 24) / 255f;
        float red1 = ((0x00FF0000 & argb) >>> 16) / 255f;
        float green1 = ((0x0000FF00 & argb) >>> 8) / 255f;
        float blue1 = (0x000000FF & argb) / 255f;

        float alpha2 = ((0xFF000000 & color) >>> 24) / 255f;
        float red2 = ((0x00FF0000 & color) >>> 16) / 255f;
        float green2 = ((0x0000FF00 & color) >>> 8) / 255f;
        float blue2 = (0x000000FF & color) / 255f;

        float alphaResult = (alpha1 * alpha2) * 255f;
        float redResult = (red1 * red2) * 255f;
        float greenResult = (green1 * green2) * 255f;
        float blueResult = (blue1 * blue2) * 255f;

        return (int) alphaResult | (int) redResult | (int) greenResult | (int) blueResult;
    }

    public int fade(int color) {
        float alpha1 = ((0xFF000000 & argb) >>> 24) / 255f;
        float red1 = ((0x00FF0000 & argb) >>> 16) * alpha1;
        float green1 = ((0x0000FF00 & argb) >>> 8) * alpha1;
        float blue1 = (0x000000FF & argb) * alpha1;

        float alpha2 = 1f - alpha1;
        float red2 = ((0x00FF0000 & color) >>> 16) * alpha2;
        float green2 = ((0x0000FF00 & color) >>> 8) * alpha2;
        float blue2 = (0x000000FF & color) * alpha2;

        int alpha = 255 << 24;
        int red = (int)(red1 + red2) << 16;
        int green = (int)(green1 + green2) << 8;
        int blue = (int)(blue1 + blue2);

        return alpha | red | green | blue;
    }

}
