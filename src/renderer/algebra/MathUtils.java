package renderer.algebra;

/**
 * The MathUtils class provides utility methods for mathematical operations.
 */
public final class  MathUtils {

    /**
     * The max value on 8 bits.
     */
    public static final int MAX8INT = 255;

    private MathUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Clamps a value to a given range.
     *
     * @param <T>   the type of the value
     * @param value the value to clamp
     * @param min   the minimum value
     * @param max   the maximum value
     * @return the clamped value (min if value {@literal <} min,
     *         max if value {@literal >} max, value otherwise)
     */
    public static <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        if (value.compareTo(min) < 0) {
            return min;
        } else if (value.compareTo(max) > 0) {
            return max;
        } else {
            return value;
        }
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
     * Checks if a value is in a given range.
     *
     * @param <E>   the type of the value
     * @param value the value to check
     * @param min   the minimum value (inclusive)
     * @param max   the maximum value (inclusive)
     * @return true if value is in the range [min, max], false otherwise
     */
    public static <E extends Number> boolean isInRange(E value, E min, E max) {
        if (min.doubleValue() > max.doubleValue()) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        if (isIntegerType(min) && isIntegerType(max) && isIntegerType(value)) {
            final long minVal = min.longValue();
            final long maxVal = max.longValue();
            final long val = value.longValue();
            return minVal <= val && val <= maxVal;
        }
        if (isFloatingType(min) && isFloatingType(max) && isFloatingType(value)) {
            final double eps = 1e-6;
            final double minVal = min.doubleValue();
            final double maxVal = max.doubleValue();
            final double val = value.doubleValue();
            return minVal - eps <= val && val <= maxVal + eps;
        }
        throw new IllegalArgumentException("Mixed types are not supported.");
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
     *
     * @param value the value to check
     * @param min   the minimum value (inclusive)
     * @param max   the maximum value (inclusive)
     * @return true if value is in the range [min, max], false otherwise
     */
    public static boolean isInRange(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        final double eps = 1e-6;
        return min - eps <= value && value <= max + eps;
    }

    /**
     * Checks if a float value is in a given range.
     *
     * @param value the value to check
     * @param min   the minimum value (inclusive)
     * @param max   the maximum value (inclusive)
     * @return true if value is in the range [min, max], false otherwise
     */
    public static boolean isInRange(float value, float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be less than or equal to max");
        }
        final float eps = 1e-6f;
        return min - eps <= value && value <= max + eps;
    }

    /**
     * Checks if the type of a value is an integer type (int, short, long etc).
     *
     * @param num the value to check
     * @return true if the value is an integer type, false otherwise
     */
    private static boolean isIntegerType(Number num) {
        return num instanceof Integer
                || num instanceof Long
                || num instanceof Short
                || num instanceof Byte;
    }

    /**
     * Checks if the type of a value is a floating point type (float, double).
     *
     * @param num the value to check
     * @return true if the value is a floating point type, false otherwise
     */
    private static boolean isFloatingType(Number num) {
        return num instanceof Double || num instanceof Float;
    }

}
