package algebra;

import renderer.algebra.MathUtils;

/**
 * Unit tests for MathUtils class with int type.
 */
public class MathUtilsIntTest extends BaseMathUtils<Integer> {

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

    @Override
    protected Integer getMinValue() {
        return MIN_VALUE;
    }

    @Override
    protected Integer getMaxValue() {
        return MAX_VALUE;
    }

    @Override
    protected Integer getMidValue() {
        return MID_VALUE;
    }

    @Override
    protected Integer getBelowMinValue() {
        return BELOW_MIN_VALUE;
    }

    @Override
    protected Integer getAboveMaxValue() {
        return ABOVE_MAX_VALUE;
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
        return (expected, actual) -> org.junit.Assert.assertEquals(
            expected.intValue(), actual.intValue());
    }

    @Override
    protected String getTypeName() {
        return "int";
    }
}
