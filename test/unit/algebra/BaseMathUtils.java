package algebra;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Abstract base class for MathUtils tests.
 * Provides all test logic, concrete subclasses provide type-specific data.
 *
 * @param <T> the type being tested
 */
public abstract class BaseMathUtils<T> {

    /**
     * Functional interface for clamp operations.
     *
     * @param <V> the type of the value to clamp
     */
    @FunctionalInterface
    protected interface ClampFunction<V> {
        /**
         * Clamps a value to a given range.
         *
         * @param value the value to clamp
         * @param min   the minimum value
         * @param max   the maximum value
         * @return the clamped value
         */
        V clamp(V value, V min, V max);
    }

    /**
     * Functional interface for range check operations.
     *
     * @param <V> the type of the value to check
     */
    @FunctionalInterface
    protected interface RangeCheckFunction<V> {
        /**
         * Checks if a value is in a given range.
         *
         * @param value the value to check
         * @param min   the minimum value
         * @param max   the maximum value
         * @return true if value is in range, false otherwise
         */
        boolean isInRange(V value, V min, V max);
    }

    /**
     * Functional interface for comparing values with tolerance.
     *
     * @param <V> the type of the values to compare
     */
    @FunctionalInterface
    protected interface ValueComparator<V> {
        /**
         * Compares two values for equality.
         *
         * @param expected the expected value
         * @param actual   the actual value
         */
        void assertEquals(V expected, V actual);
    }

    // Abstract methods to be implemented by concrete test classes

    /**
     * Gets the minimum value for testing.
     *
     * @return the minimum value
     */
    protected abstract T getMinValue();

    /**
     * Gets the maximum value for testing.
     *
     * @return the maximum value
     */
    protected abstract T getMaxValue();

    /**
     * Gets the mid-range value for testing.
     *
     * @return the mid-range value
     */
    protected abstract T getMidValue();

    /**
     * Gets a value below the minimum for testing.
     *
     * @return the value below minimum
     */
    protected abstract T getBelowMinValue();

    /**
     * Gets a value above the maximum for testing.
     *
     * @return the value above maximum
     */
    protected abstract T getAboveMaxValue();

    /**
     * Gets the clamp function for the specific type.
     *
     * @return the clamp function
     */
    protected abstract ClampFunction<T> getClampFunction();

    /**
     * Gets the range check function for the specific type.
     *
     * @return the range check function
     */
    protected abstract RangeCheckFunction<T> getRangeCheckFunction();

    /**
     * Gets the value comparator for the specific type.
     *
     * @return the value comparator
     */
    protected abstract ValueComparator<T> getComparator();

    /**
     * Gets the type name for error messages.
     *
     * @return the type name
     */
    protected abstract String getTypeName();

    // ==================== Clamp Tests ====================

    /**
     * Test clamping a value within the range.
     */
    @Test
    public void testClampValueWithinRangeReturnsValue() {
        T result = getClampFunction().clamp(getMidValue(), getMinValue(), getMaxValue());
        getComparator().assertEquals(getMidValue(), result);
    }

    /**
     * Test clamping a value below the minimum.
     */
    @Test
    public void testClampValueBelowMinReturnsMin() {
        T result = getClampFunction().clamp(
            getBelowMinValue(), getMinValue(), getMaxValue());
        getComparator().assertEquals(getMinValue(), result);
    }

    /**
     * Test clamping a value above the maximum.
     */
    @Test
    public void testClampValueAboveMaxReturnsMax() {
        T result = getClampFunction().clamp(
            getAboveMaxValue(), getMinValue(), getMaxValue());
        getComparator().assertEquals(getMaxValue(), result);
    }

    /**
     * Test clamping a value at the minimum boundary.
     */
    @Test
    public void testClampValueAtMinReturnsMin() {
        T result = getClampFunction().clamp(getMinValue(), getMinValue(), getMaxValue());
        getComparator().assertEquals(getMinValue(), result);
    }

    /**
     * Test clamping a value at the maximum boundary.
     */
    @Test
    public void testClampValueAtMaxReturnsMax() {
        T result = getClampFunction().clamp(getMaxValue(), getMinValue(), getMaxValue());
        getComparator().assertEquals(getMaxValue(), result);
    }

    /**
     * Test clamping with invalid range throws exception.
     */
    @Test
    public void testClampMinGreaterThanMaxThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> getClampFunction().clamp(getMidValue(), getMaxValue(), getMinValue()));
    }

    /**
     * Test clamping with zero interval returns the constant.
     */
    @Test
    public void testClampZeroIntervalReturnsConstant() {
        T result = getClampFunction().clamp(
            getAboveMaxValue(), getMaxValue(), getMaxValue());
        getComparator().assertEquals(getMaxValue(), result);
    }

    // ==================== IsInRange Tests ====================

    /**
     * Test checking if a value is within the range.
     */
    @Test
    public void testIsInRangeValueWithinRangeReturnsTrue() {
        boolean result = getRangeCheckFunction().isInRange(
            getMidValue(), getMinValue(), getMaxValue());
        assertTrue(result);
    }

    /**
     * Test checking if a value is below the minimum.
     */
    @Test
    public void testIsInRangeValueBelowMinReturnsFalse() {
        boolean result = getRangeCheckFunction().isInRange(
            getBelowMinValue(), getMinValue(), getMaxValue());
        assertFalse(result);
    }

    /**
     * Test checking if a value is above the maximum.
     */
    @Test
    public void testIsInRangeValueAboveMaxReturnsFalse() {
        boolean result = getRangeCheckFunction().isInRange(
            getAboveMaxValue(), getMinValue(), getMaxValue());
        assertFalse(result);
    }

    /**
     * Test checking if a value at minimum boundary is in range.
     */
    @Test
    public void testIsInRangeValueAtMinReturnsTrue() {
        boolean result = getRangeCheckFunction().isInRange(
            getMinValue(), getMinValue(), getMaxValue());
        assertTrue(result);
    }

    /**
     * Test checking if a value at maximum boundary is in range.
     */
    @Test
    public void testIsInRangeValueAtMaxReturnsTrue() {
        boolean result = getRangeCheckFunction().isInRange(
            getMaxValue(), getMinValue(), getMaxValue());
        assertTrue(result);
    }

    /**
     * Test checking range with invalid bounds throws exception.
     */
    @Test
    public void testIsInRangeMinGreaterThanMaxThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> getRangeCheckFunction().isInRange(
                getMidValue(), getMaxValue(), getMinValue()));
    }
}
