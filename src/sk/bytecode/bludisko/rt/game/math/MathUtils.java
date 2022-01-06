package sk.bytecode.bludisko.rt.game.math;

import static java.lang.Math.PI;

public final class MathUtils {

    public static final float radiansToDegrees = (float) (180f / PI);
    public static final float degreesToRadians = (float) (PI / 180f);

    public static final float FLOAT_ROUNDING_ERROR = 0.000001f;
    public static final int INT_MSB_MASK = 0xFF000000;
    
    public static boolean isZero (float value) {
        return Math.abs(value) <= FLOAT_ROUNDING_ERROR;
    }

    public static boolean isZero (float value, float tolerance) {
        return Math.abs(value) <= tolerance;
    }

    /**
     * Decimal part of a vector.
     * Calculated as the distance to the nearest mathematical integer
     * closest to negative infinity, separately for both X and Y elements.
     * @param v Vector to calculate the part of.
     * @return New vector containing decimal coordinates, calculated by the rules above.
     */
    public static Vector2 decimalPart(Vector2 v) {
        return new Vector2(
                (float) Math.abs(Math.floor(v.x) - v.x),
                (float) Math.abs(Math.floor(v.y) - v.y)
        );
    }

    /**
     * 2 = halves
     * 3 = thirds
     * ...
     * TODO: write documentation
     * @param v
     * @param fraction
     * @return
     */
    public static Vector2 fractionRound(Vector2 v, float fraction) {
        return new Vector2(
                Math.abs(MathUtils.fractionRound(v.x, fraction) - v.x),
                Math.abs(MathUtils.fractionRound(v.y, fraction) - v.y)
        );
    }

    public static float fractionRound(float number, float fraction) {
        return Math.round(number * fraction) / fraction;
    }

    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static int clamp(int value, int min, int max) {
        return value > max ? max : Math.max(value, min);
    }

}
