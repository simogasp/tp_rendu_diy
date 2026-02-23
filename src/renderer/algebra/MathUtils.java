package renderer.algebra;

/**
 * The MathUtils class provides utility methods for mathematical operations.
 */
public final class  MathUtils {

    /**
     * The max value on 8 bits.
     */
    public static final int MAX8INT = 255;

    /**
     * Epsilon tolerance for double precision floating-point comparisons.
     * This value accounts for floating-point arithmetic precision errors
     * in range checking operations.
     */
    public static final double DOUBLE_EPSILON = 1e-6;

    /**
     * Epsilon tolerance for single precision floating-point comparisons.
     * This value accounts for floating-point arithmetic precision errors
     * in range checking operations. While float has lower precision than double,
     * the same epsilon value is used for consistency in the application.
     */
    public static final float FLOAT_EPSILON = 1e-6f;

    private MathUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Clamps an int value to a given range.
     *
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamped value (min if value {@literal <} min,
     *         max if value {@literal >} max, value otherwise)
     */
    public static int clamp(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Clamps a long value to a given range.
     *
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamped value (min if value {@literal <} min,
     *         max if value {@literal >} max, value otherwise)
     */
    public static long clamp(long value, long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Clamps a double value to a given range.
     *
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamped value (min if value {@literal <} min,
     *         max if value {@literal >} max, value otherwise)
     */
    public static double clamp(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Clamps a float value to a given range.
     *
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamped value (min if value {@literal <} min,
     *         max if value {@literal >} max, value otherwise)
     */
    public static float clamp(float value, float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Checks if an int value is in a given range.
     *
     * @param value the value to check
     * @param min   the minimum value (inclusive)
     * @param max   the maximum value (inclusive)
     * @return true if value is in the range [min, max], false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        return min <= value && value <= max;
    }

    /**
     * Checks if a long value is in a given range.
     *
     * @param value the value to check
     * @param min   the minimum value (inclusive)
     * @param max   the maximum value (inclusive)
     * @return true if value is in the range [min, max], false otherwise
     */
    public static boolean isInRange(long value, long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        return min <= value && value <= max;
    }

    /**
     * Checks if a double value is in a given range.
     * Uses an epsilon tolerance to account for floating-point
     * precision issues, effectively checking if the value is in the range
     * [min - DOUBLE_EPSILON, max + DOUBLE_EPSILON].
     *
     * @param value the value to check
     * @param min   the minimum value (inclusive with epsilon tolerance)
     * @param max   the maximum value (inclusive with epsilon tolerance)
     * @return true if value is in the range
     *         [min - DOUBLE_EPSILON, max + DOUBLE_EPSILON], false otherwise
     */
    public static boolean isInRange(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        return min - DOUBLE_EPSILON <= value && value <= max + DOUBLE_EPSILON;
    }

    /**
     * Checks if a float value is in a given range.
     * Uses an epsilon tolerance to account for floating-point
     * precision issues, effectively checking if the value is in the range
     * [min - FLOAT_EPSILON, max + FLOAT_EPSILON].
     *
     * @param value the value to check
     * @param min   the minimum value (inclusive with epsilon tolerance)
     * @param max   the maximum value (inclusive with epsilon tolerance)
     * @return true if value is in the range
     *         [min - FLOAT_EPSILON, max + FLOAT_EPSILON], false otherwise
     */
    public static boolean isInRange(float value, float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        return min - FLOAT_EPSILON <= value && value <= max + FLOAT_EPSILON;
    }

}
