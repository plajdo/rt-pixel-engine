package sk.bytecode.bludisko.rt.game.graphics;

/**
 * Color record with handy methods for color blending.
 */
public record Color(int argb) {

    /**
     * Multiplies the color by a given scalar.
     * @param scale Multiplier
     * @return New Color with a modified value
     */
    public Color scaled(float scale) {
        int alpha = (int) (((0xFF000000 & argb) >>> 24) * scale) << 24;
        int red = (int) (((0x00FF0000 & argb) >>> 16) * scale) << 16;
        int green = (int) (((0x0000FF00 & argb) >>> 8) * scale) << 8;
        int blue = (int) ((0x000000FF & argb) * scale);

        return new Color(alpha | red | green | blue);
    }

    /**
     * Blends current color with a different color
     * based on their alpha values.
     * @param color Other color to blend with
     * @return New blended Color
     */
    public Color fade(int color) {
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

        return new Color(alpha | red | green | blue);
    }

}
