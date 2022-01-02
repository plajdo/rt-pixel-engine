package sk.bytecode.bludisko.rt.game.math;

import org.jetbrains.annotations.Contract;

public final class MathUtils {

    public static final float PI = (float) Math.PI;

    public static final float radiansToDegrees = 180f / PI;
    public static final float degreesToRadians = PI / 180f;

    public static final float FLOAT_ROUNDING_ERROR = 0.000001f; // 32 bits
    public static final int INT_MSB_MASK = 0xFF000000;
    
    public static boolean isZero (float value) {
        return Math.abs(value) <= FLOAT_ROUNDING_ERROR;
    }

    public static boolean isZero (float value, float tolerance) {
        return Math.abs(value) <= tolerance;
    }

    public static float sin(float value) {
        return (float) Math.sin(value);
    }

    public static float roundAway(float value) {
        return (float) (Math.ceil(Math.abs(value)) * Math.signum(value));
    }

    /**
     * Decimal part of a vector.
     * Calculated as the distance to the nearest mathematical integer
     * closest to negative infinity, separately for both X and Y elements.
     * @param v Vector to calculate the part of.
     * @return New vector containing decimal coordinates, calculated by rules explained above.
     */
    public static Vector2 decimalPart(Vector2 v) {
        return new Vector2(
                (float) Math.abs(Math.floor(v.x) - v.x),
                (float) Math.abs(Math.floor(v.y) - v.y)
        );
    }

}
