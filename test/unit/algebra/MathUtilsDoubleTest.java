package algebra;

import org.junit.Test;
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
    /** Value near minimum for epsilon testing. */
    private static final double NEAR_MIN_VALUE = MIN_VALUE - 1e-7;
    /** Value near maximum for epsilon testing. */
    private static final double NEAR_MAX_VALUE = MAX_VALUE + 1e-7;

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
}
