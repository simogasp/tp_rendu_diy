package algebra;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import renderer.algebra.MathUtils;

/**
 * Unit tests for MathUtils class with float type.
 */
public class MathUtilsFloatTest extends BaseMathUtils<Float> {

    /** Minimum float value for testing. */
    private static final float MIN_VALUE = 1.0f;
    /** Maximum float value for testing. */
    private static final float MAX_VALUE = 10.0f;
    /** Mid-range float value for testing. */
    private static final float MID_VALUE = 5.0f;
    /** Float value below the minimum for testing. */
    private static final float BELOW_MIN_VALUE = 0.0f;
    /** Float value above the maximum for testing. */
    private static final float ABOVE_MAX_VALUE = 11.0f;
    /** Epsilon for comparing float values. */
    private static final float EPSILON = 0.001f;
    /**
     * Value near minimum for epsilon testing.
     * This value is FLOAT_EPSILON/10 outside the lower bound to verify that
     * the isInRange method accepts values within its epsilon tolerance.
     */
    private static final float NEAR_MIN_VALUE = MIN_VALUE - MathUtils.FLOAT_EPSILON / 10;
    /**
     * Value near maximum for epsilon testing.
     * This value is FLOAT_EPSILON/10 outside the upper bound to verify that
     * the isInRange method accepts values within its epsilon tolerance.
     */
    private static final float NEAR_MAX_VALUE = MAX_VALUE + MathUtils.FLOAT_EPSILON / 10;

    @Override
    protected Float getMinValue() {
        return MIN_VALUE;
    }

    @Override
    protected Float getMaxValue() {
        return MAX_VALUE;
    }

    @Override
    protected Float getMidValue() {
        return MID_VALUE;
    }

    @Override
    protected Float getBelowMinValue() {
        return BELOW_MIN_VALUE;
    }

    @Override
    protected Float getAboveMaxValue() {
        return ABOVE_MAX_VALUE;
    }

    @Override
    protected ClampFunction<Float> getClampFunction() {
        return MathUtils::clamp;
    }

    @Override
    protected RangeCheckFunction<Float> getRangeCheckFunction() {
        return MathUtils::isInRange;
    }

    @Override
    protected ValueComparator<Float> getComparator() {
        return (expected, actual) -> org.junit.Assert.assertEquals(
            expected, actual, EPSILON);
    }

    @Override
    protected String getTypeName() {
        return "float";
    }

    /**
     * Test checking if a float value near minimum is in range with epsilon tolerance.
     */
    @Test
    public void testIsInRangeValueNearMinReturnsTrue() {
        boolean result = MathUtils.isInRange(NEAR_MIN_VALUE, MIN_VALUE, MAX_VALUE);
        assertTrue(result);
    }

    /**
     * Test checking if a float value near maximum is in range with epsilon tolerance.
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
            () -> MathUtils.clamp(MID_VALUE, Float.NaN, MAX_VALUE));
    }

    /**
     * Test clamping with NaN as maximum throws exception.
     */
    @Test
    public void testClampNaNMaxThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.clamp(MID_VALUE, MIN_VALUE, Float.NaN));
    }

    /**
     * Test clamping with NaN as value returns NaN.
     */
    @Test
    public void testClampNaNValueReturnsNaN() {
        float result = MathUtils.clamp(Float.NaN, MIN_VALUE, MAX_VALUE);
        assertTrue(Float.isNaN(result));
    }

    /**
     * Test isInRange with NaN as value returns false.
     */
    @Test
    public void testIsInRangeNaNValueReturnsFalse() {
        boolean result = MathUtils.isInRange(Float.NaN, MIN_VALUE, MAX_VALUE);
        assertFalse(result);
    }

    // ==================== Infinity Tests ====================

    /**
     * Test clamping positive infinity returns max.
     */
    @Test
    public void testClampPositiveInfinityReturnsMax() {
        float result = MathUtils.clamp(
            Float.POSITIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        org.junit.Assert.assertEquals(MAX_VALUE, result, EPSILON);
    }

    /**
     * Test clamping negative infinity returns min.
     */
    @Test
    public void testClampNegativeInfinityReturnsMin() {
        float result = MathUtils.clamp(
            Float.NEGATIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        org.junit.Assert.assertEquals(MIN_VALUE, result, EPSILON);
    }

    /**
     * Test isInRange with positive infinity returns false.
     */
    @Test
    public void testIsInRangePositiveInfinityReturnsFalse() {
        boolean result = MathUtils.isInRange(
            Float.POSITIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        assertFalse(result);
    }

    /**
     * Test isInRange with negative infinity returns false.
     */
    @Test
    public void testIsInRangeNegativeInfinityReturnsFalse() {
        boolean result = MathUtils.isInRange(
            Float.NEGATIVE_INFINITY, MIN_VALUE, MAX_VALUE);
        assertFalse(result);
    }
}
