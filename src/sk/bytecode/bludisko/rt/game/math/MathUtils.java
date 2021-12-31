package sk.bytecode.bludisko.rt.game.math;

public final class MathUtils {

    public static final float PI = (float) Math.PI;

    public static final float radiansToDegrees = 180f / PI;
    public static final float degreesToRadians = PI / 180f;

    public static final float FLOAT_ROUNDING_ERROR = 0.000001f; // 32 bits
    
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

}
