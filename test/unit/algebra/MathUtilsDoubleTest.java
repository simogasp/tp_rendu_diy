package algebra;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import renderer.algebra.MathUtils;

/**
 * Unit tests for MathUtils class with double type.
 */
public class MathUtilsDoubleTest extends BaseMathUtils<Double> {

    /** Minimum double value for testing. */
    private static final double MIN_VALUE = 1.0;
    /** Maximum double value for testing. */
    private static final double MAX_VALUE = 10.0;
    /** Mid-range double value for testing. */
    private static final double MID_VALUE = 5.0;
    /** Double value below the minimum for testing. */
    private static final double BELOW_MIN_VALUE = 0.0;
    /** Double value above the maximum for testing. */
    private static final double ABOVE_MAX_VALUE = 11.0;
    /** Epsilon for comparing double values. */
    private static final double EPSILON = 0.001;
    /**
     * Value near minimum for epsilon testing.
     * This value is DOUBLE_EPSILON/10 outside the lower bound to verify that
     * the isInRange method accepts values within its epsilon tolerance.
     */
    private static final double NEAR_MIN_VALUE =
        MIN_VALUE - MathUtils.DOUBLE_EPSILON / 10;
    /**
     * Value near maximum for epsilon testing.
     * This value is DOUBLE_EPSILON/10 outside the upper bound to verify that
     * the isInRange method accepts values within its epsilon tolerance.
     */
    private static final double NEAR_MAX_VALUE =
        MAX_VALUE + MathUtils.DOUBLE_EPSILON / 10;

    @Override
    protected Double getMinValue() {
        return MIN_VALUE;
    }

    @Override
    protected Double getMaxValue() {
        return MAX_VALUE;
    }

    @Override
    protected Double getMidValue() {
        return MID_VALUE;
    }

    @Override
    protected Double getBelowMinValue() {
        return BELOW_MIN_VALUE;
    }

    @Override
    protected Double getAboveMaxValue() {
        return ABOVE_MAX_VALUE;
    }

    @Override
    protected ClampFunction<Double> getClampFunction() {
        return MathUtils::clamp;
    }

    @Override
    protected RangeCheckFunction<Double> getRangeCheckFunction() {
        return MathUtils::isInRange;
    }

    @Override
    protected ValueComparator<Double> getComparator() {
        return (expected, actual) -> org.junit.Assert.assertEquals(
            expected, actual, EPSILON);
    }

    @Override
    protected String getTypeName() {
        return "double";
    }

    /**
     * Test checking if a double value near minimum is in range with epsilon tolerance.
     */
    @Test
    public void testIsInRangeValueNearMinReturnsTrue() {
        boolean result = MathUtils.isInRange(NEAR_MIN_VALUE, MIN_VALUE, MAX_VALUE);
        assertTrue(result);
    }

    /**
     * Test checking if a double value near maximum is in range with epsilon tolerance.
     */
    @Test
    public void testIsInRangeValueNearMaxReturnsTrue() {
        boolean result = MathUtils.isInRange(NEAR_MAX_VALUE, MIN_VALUE, MAX_VALUE);
        assertTrue(result);
    }

    // ==================== NaN Tests ====================

    /**
     * Test clamping with NaN as minimum throws exception.
     */
    @Test
    public void testClampNaNMinThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.clamp(MID_VALUE, Double.NaN, MAX_VALUE));
    }

    /**
     * Test clamping with NaN as maximum throws exception.
     */
    @Test
    public void testClampNaNMaxThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.clamp(MID_VALUE, MIN_VALUE, Double.NaN));
    }

    /**
     * Test clamping with NaN as value returns NaN.
     */
    @Test
    public void testClampNaNValueReturnsNaN() {
        double result = MathUtils.clamp(Double.NaN, MIN_VALUE, MAX_VALUE);
        assertTrue(Double.isNaN(result));
    }

    /**
     * Test isInRange with NaN as value returns false.
     */
    @Test
    public void testIsInRangeNaNValueReturnsFalse() {
        boolean result = MathUtils.isInRange(Double.NaN, MIN_VALUE, MAX_VALUE);
        assertFalse(result);
    }

    // ==================== Infinity Tests ====================

    /**
     * Test clamping positive infinity returns max.
     */
    @Test
    public void testClampPositiveInfinityReturnsMax() {
        double result = MathUtils.clamp(
            Double.POSITIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        org.junit.Assert.assertEquals(MAX_VALUE, result, EPSILON);
    }

    /**
     * Test clamping negative infinity returns min.
     */
    @Test
    public void testClampNegativeInfinityReturnsMin() {
        double result = MathUtils.clamp(
            Double.NEGATIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        org.junit.Assert.assertEquals(MIN_VALUE, result, EPSILON);
    }

    /**
     * Test isInRange with positive infinity returns false.
     */
    @Test
    public void testIsInRangePositiveInfinityReturnsFalse() {
        boolean result = MathUtils.isInRange(
            Double.POSITIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        assertFalse(result);
    }

    /**
     * Test isInRange with negative infinity returns false.
     */
    @Test
    public void testIsInRangeNegativeInfinityReturnsFalse() {
        boolean result = MathUtils.isInRange(
            Double.NEGATIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        assertFalse(result);
    }
}
