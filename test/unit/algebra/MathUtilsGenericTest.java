package algebra;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import renderer.algebra.MathUtils;

/**
 * Unit tests for MathUtils class with generic Number types.
 */
public class MathUtilsGenericTest extends BaseMathUtils<Integer> {

    /** Minimum integer value for testing. */
    private static final int MIN_VALUE = 1;
    /** Maximum integer value for testing. */
    private static final int MAX_VALUE = 10;
    /** Mid-range integer value for testing. */
    private static final int MID_VALUE = 5;
    /** Integer value below the minimum for testing. */
    private static final int BELOW_MIN_VALUE = 0;
    /** Integer value above the maximum for testing. */
    private static final int ABOVE_MAX_VALUE = 11;

    /** Minimum double value for mixed type testing. */
    private static final double MIN_DOUBLE = 1.0;
    /** Maximum double value for mixed type testing. */
    private static final double MAX_DOUBLE = 10.0;

    @Override
    protected Integer getMinValue() {
        return Integer.valueOf(MIN_VALUE);
    }

    @Override
    protected Integer getMaxValue() {
        return Integer.valueOf(MAX_VALUE);
    }

    @Override
    protected Integer getMidValue() {
        return Integer.valueOf(MID_VALUE);
    }

    @Override
    protected Integer getBelowMinValue() {
        return Integer.valueOf(BELOW_MIN_VALUE);
    }

    @Override
    protected Integer getAboveMaxValue() {
        return Integer.valueOf(ABOVE_MAX_VALUE);
    }

    @Override
    protected ClampFunction<Integer> getClampFunction() {
        return MathUtils::clamp;
    }

    @Override
    protected RangeCheckFunction<Integer> getRangeCheckFunction() {
        return MathUtils::isInRange;
    }

    @Override
    protected ValueComparator<Integer> getComparator() {
        return org.junit.Assert::assertEquals;
    }

    @Override
    protected String getTypeName() {
        return "generic";
    }

    /**
     * Test checking if mixed types throws an exception.
     */
    @Test
    public void testIsInRangeMixedTypesThrowsException() {
        assertThrows(IllegalArgumentException.class,
            () -> MathUtils.isInRange(Integer.valueOf(MID_VALUE),
                Double.valueOf(MIN_DOUBLE), Double.valueOf(MAX_DOUBLE)));
    }

    /**
     * Test checking if a generic Double is within the range.
     */
    @Test
    public void testIsInRangeGenericDoubleReturnsTrue() {
        boolean result = MathUtils.isInRange(Double.valueOf(MID_VALUE),
            Double.valueOf(MIN_DOUBLE), Double.valueOf(MAX_DOUBLE));
        assertTrue(result);
    }
}
