package sk.bytecode.bludisko.rt.game.math;

/**
 * Collection of handy Math constants and shortcuts.
 */
public final class MathUtils {

    public static final float PI = (float) Math.PI;

    public static final float radiansToDegrees = 180f / PI;
    public static final float degreesToRadians = PI / 180f;

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
     * Decimal part of a vector, rounded down to the nearest fraction.
     * 2 - round to halves, 3 - round to thirds, 0.5 - round to twos, ...
     * @param v Vector to round
     * @param fraction Fraction to be rounded to
     * @return New Vector2 with X and Y parameters rounded
     */
    public static Vector2 fractionRound(Vector2 v, float fraction) {
        return new Vector2(
                Math.abs(MathUtils.fractionRound(v.x, fraction) - v.x),
                Math.abs(MathUtils.fractionRound(v.y, fraction) - v.y)
        );
    }

    /**
     * Rounds a number down to the nearest fraction
     * @param number Number to round
     * @param fraction Fraction denominator
     * @return Rounded number
     * @see MathUtils#fractionRound(Vector2, float)
     */
    public static float fractionRound(float number, float fraction) {
        return Math.round(number * fraction) / fraction;
    }

    /**
     * Returns the greatest common divisor (GCD) of two numbers A and B.
     * @param a Number A
     * @param b Number B
     * @return GCD(A, B)
     */
    public static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * Clamps a value between two values, rounding them to min/max if they exceed
     * the interval, or returning the same value if it's inside the interval.
     * @param value Value to clamp
     * @param min Lower bound of the interval
     * @param max Upper bound of the interval
     * @return Clamped number
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamps a value between two values, rounding them to min/max if they exceed
     * the interval, or returning the same value if it's inside the interval.
     * @param value Value to clamp
     * @param min Lower bound of the interval
     * @param max Upper bound of the interval
     * @return Clamped number
     */
    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Re-maps a number from one range to another.
     * @param x The number to map
     * @param fromLowest Smallest value of the input range
     * @param fromHighest Highest value of the input range
     * @param toLowest Smallest value of the output range
     * @param toHighest Highest value of the output range
     * @return Value scaled from one range to another one
     */
    public static float map(float x, float fromLowest, float fromHighest, float toLowest, float toHighest) {
        return (x - fromLowest) * (toHighest - toLowest) / (fromHighest - fromLowest) + toLowest;
    }

}
