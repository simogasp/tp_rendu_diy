package algebra;

import renderer.algebra.MathUtils;

/**
 * Unit tests for MathUtils class with long type.
 */
public class MathUtilsLongTest extends BaseMathUtils<Long> {

    /** Minimum long value for testing. */
    private static final long MIN_VALUE = 1L;
    /** Maximum long value for testing. */
    private static final long MAX_VALUE = 10L;
    /** Mid-range long value for testing. */
    private static final long MID_VALUE = 5L;
    /** Long value below the minimum for testing. */
    private static final long BELOW_MIN_VALUE = 0L;
    /** Long value above the maximum for testing. */
    private static final long ABOVE_MAX_VALUE = 11L;

    @Override
    protected Long getMinValue() {
        return MIN_VALUE;
    }

    @Override
    protected Long getMaxValue() {
        return MAX_VALUE;
    }

    @Override
    protected Long getMidValue() {
        return MID_VALUE;
    }

    @Override
    protected Long getBelowMinValue() {
        return BELOW_MIN_VALUE;
    }

    @Override
    protected Long getAboveMaxValue() {
        return ABOVE_MAX_VALUE;
    }

    @Override
    protected ClampFunction<Long> getClampFunction() {
        return MathUtils::clamp;
    }

    @Override
    protected RangeCheckFunction<Long> getRangeCheckFunction() {
        return MathUtils::isInRange;
    }

    @Override
    protected ValueComparator<Long> getComparator() {
        return (expected, actual) -> org.junit.Assert.assertEquals(
            expected.longValue(), actual.longValue());
    }

    @Override
    protected String getTypeName() {
        return "long";
    }
}
