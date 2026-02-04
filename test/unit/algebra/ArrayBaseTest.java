package algebra;

import renderer.algebra.ArrayBase;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the ArrayBase class.
 * Since ArrayBase is abstract, we use a concrete TestArrayBase implementation.
 */
public class ArrayBaseTest {

    // Constants
    /**
     * The epsilon for double comparison.
     */
    private static final double EPSILON = 0.001;

    /**
     * The name of the test array.
     */
    private static final String ARRAY_NAME = "testArray";

    /**
     * The default test size.
     */
    private static final int DEFAULT_SIZE = 5;

    /**
     * The small test size.
     */
    private static final int SMALL_SIZE = 1;

    /**
     * The large test size.
     */
    private static final int LARGE_SIZE = 100;

    /**
     * The zero size for testing invalid constructor.
     */
    private static final int ZERO_SIZE = 0;

    /**
     * The negative size for testing invalid constructor.
     */
    private static final int NEGATIVE_SIZE = -1;

    /**
     * Scaling factor for tests.
     */
    private static final double SCALE_FACTOR = 2.5;

    /**
     * Zero scaling factor.
     */
    private static final double ZERO_SCALE = 0.0;

    /**
     * Negative scaling factor.
     */
    private static final double NEGATIVE_SCALE = -3.0;

    /**
     * Test value for array initialization.
     */
    private static final double TEST_VALUE = 1.5;

    /**
     * Another test value for array initialization.
     */
    private static final double TEST_VALUE_2 = 2.5;

    /**
     * Concrete implementation of ArrayBase for testing purposes.
     * Uses Vector as the underlying implementation.
     */
    private static class TestArrayBase extends ArrayBase {

        /**
         * Constructor with name and size.
         *
         * @param name the name
         * @param size the size
         * @throws IllegalArgumentException if size is not positive
         */
        TestArrayBase(final String name, final int size)
                throws IllegalArgumentException {
            super(name, size);
        }

        /**
         * Constructor with size only.
         *
         * @param size the size
         * @throws IllegalArgumentException if size is not positive
         */
        TestArrayBase(final int size)
                throws IllegalArgumentException {
            super(size);
        }

        /**
         * Public wrapper for scaleValues to enable testing.
         *
         * @param factor the scaling factor
         * @param dest the destination array
         */
        void testScaleValues(final double factor, final double[] dest) {
            scaleValues(factor, dest);
        }

        /**
         * Public wrapper for addValues to enable testing.
         *
         * @param other the other array
         * @param dest the destination array
         */
        void testAddValues(final double[] other, final double[] dest) {
            addValues(other, dest);
        }

        /**
         * Public wrapper for subtractValues to enable testing.
         *
         * @param other the other array
         * @param dest the destination array
         */
        void testSubtractValues(final double[] other, final double[] dest) {
            subtractValues(other, dest);
        }

        /**
         * Public wrapper for copyValues to enable testing.
         *
         * @param source the source array
         */
        void testCopyValues(final double[] source) {
            copyValues(source);
        }

        /**
         * Public wrapper for multiplyValues to enable testing.
         *
         * @param other the other array
         * @param dest the destination array
         */
        void testMultiplyValues(final double[] other, final double[] dest) {
            multiplyValues(other, dest);
        }

        /**
         * Public wrapper for sumValues to enable testing.
         *
         * @return the sum of all elements
         */
        double testSumValues() {
            return sumValues();
        }

        /**
         * Gets the internal values array for testing.
         *
         * @return a copy of the values array
         */
        double[] getValuesCopy() {
            final double[] copy = new double[size()];
            System.arraycopy(getValues(), 0, copy, 0, size());
            return copy;
        }

        /**
         * Sets a value in the internal array for testing.
         *
         * @param index the index
         * @param value the value
         */
        void setValue(final int index, final double value) {
            getValues()[index] = value;
        }
    }

    /**
     * Test array base object.
     */
    private TestArrayBase arrayBase;

    /**
     * Sets up test fixtures before each test.
     */
    @Before
    public void setUp() {
        arrayBase = new TestArrayBase(ARRAY_NAME, DEFAULT_SIZE);
    }

    /**
     * Test constructor with name and size.
     */
    @Test
    public void testConstructorWithName() {
        final TestArrayBase arr = new TestArrayBase(ARRAY_NAME, DEFAULT_SIZE);
        assertNotNull(arr);
        assertEquals(ARRAY_NAME, arr.getName());
        assertEquals(DEFAULT_SIZE, arr.size());
    }

    /**
     * Test constructor with size only.
     */
    @Test
    public void testConstructorWithoutName() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        assertNotNull(arr);
        assertEquals("", arr.getName());
        assertEquals(DEFAULT_SIZE, arr.size());
    }

    /**
     * Test constructor with minimum valid size.
     */
    @Test
    public void testConstructorWithMinimumSize() {
        final TestArrayBase arr = new TestArrayBase(ARRAY_NAME, SMALL_SIZE);
        assertNotNull(arr);
        assertEquals(SMALL_SIZE, arr.size());
    }

    /**
     * Test constructor with large size.
     */
    @Test
    public void testConstructorWithLargeSize() {
        final TestArrayBase arr = new TestArrayBase(ARRAY_NAME, LARGE_SIZE);
        assertNotNull(arr);
        assertEquals(LARGE_SIZE, arr.size());
    }

    /**
     * Test constructor with zero size should throw exception.
     */
    @Test
    public void testConstructorWithZeroSize() {
        try {
            new TestArrayBase(ARRAY_NAME, ZERO_SIZE);
            fail("Should throw IllegalArgumentException for zero size");
        } catch (IllegalArgumentException e) {
            assertEquals("Size must be strictly positive", e.getMessage());
        }
    }

    /**
     * Test constructor with negative size should throw exception.
     */
    @Test
    public void testConstructorWithNegativeSize() {
        try {
            new TestArrayBase(ARRAY_NAME, NEGATIVE_SIZE);
            fail("Should throw IllegalArgumentException for negative size");
        } catch (IllegalArgumentException e) {
            assertEquals("Size must be strictly positive", e.getMessage());
        }
    }

    /**
     * Test size method returns correct value.
     */
    @Test
    public void testSize() {
        assertEquals(DEFAULT_SIZE, arrayBase.size());
    }

    /**
     * Test getName returns the correct name.
     */
    @Test
    public void testGetName() {
        assertEquals(ARRAY_NAME, arrayBase.getName());
    }

    /**
     * Test setName updates the name correctly.
     */
    @Test
    public void testSetName() {
        final String newName = "newName";
        arrayBase.setName(newName);
        assertEquals(newName, arrayBase.getName());
    }

    /**
     * Test setName with empty string.
     */
    @Test
    public void testSetNameEmpty() {
        arrayBase.setName("");
        assertEquals("", arrayBase.getName());
    }

    /**
     * Test setName with null.
     */
    @Test
    public void testSetNameNull() {
        arrayBase.setName(null);
        assertEquals(null, arrayBase.getName());
    }

    /**
     * Test scaleValues with positive factor.
     */
    @Test
    public void testScaleValuesPositiveFactor() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];

        // Initialize array with known values
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, i + 1.0);
        }

        arr.testScaleValues(SCALE_FACTOR, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals((i + 1.0) * SCALE_FACTOR, dest[i], EPSILON);
        }
    }

    /**
     * Test scaleValues with zero factor.
     */
    @Test
    public void testScaleValuesZeroFactor() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testScaleValues(ZERO_SCALE, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test scaleValues with negative factor.
     */
    @Test
    public void testScaleValuesNegativeFactor() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testScaleValues(NEGATIVE_SCALE, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 * NEGATIVE_SCALE, dest[i], EPSILON);
        }
    }

    /**
     * Test scaleValues does not modify source array.
     */
    @Test
    public void testScaleValuesDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] original = arr.getValuesCopy();
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testScaleValues(SCALE_FACTOR, dest);

        assertArrayEquals(original, arr.getValuesCopy(), EPSILON);
    }

    /**
     * Test scaleValues with mismatched destination size.
     */
    @Test
    public void testScaleValuesWrongDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testScaleValues(SCALE_FACTOR, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("Destination array size must match source size",
                        e.getMessage());
        }
    }

    /**
     * Test scaleValues with smaller destination size.
     */
    @Test
    public void testScaleValuesSmallerDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int smallerSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[smallerSize];

        try {
            arr.testScaleValues(SCALE_FACTOR, dest);
            fail("Should throw IllegalArgumentException for smaller dest size");
        } catch (IllegalArgumentException e) {
            assertEquals("Destination array size must match source size",
                        e.getMessage());
        }
    }

    /**
     * Test addValues with two arrays.
     */
    @Test
    public void testAddValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE_2;
        }

        arr.testAddValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 + TEST_VALUE_2, dest[i], EPSILON);
        }
    }

    /**
     * Test addValues does not modify source arrays.
     */
    @Test
    public void testAddValuesDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] otherCopy = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE;
            otherCopy[i] = TEST_VALUE;
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testAddValues(other, dest);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
        assertArrayEquals(otherCopy, other, EPSILON);
    }

    /**
     * Test addValues with mismatched other array size.
     */
    @Test
    public void testAddValuesWrongOtherSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] other = new double[wrongSize];
        final double[] dest = new double[DEFAULT_SIZE];

        try {
            arr.testAddValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test addValues with mismatched destination size.
     */
    @Test
    public void testAddValuesWrongDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final int wrongSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testAddValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test addValues with all arrays wrong size.
     */
    @Test
    public void testAddValuesAllWrongSizes() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize1 = DEFAULT_SIZE + 1;
        final int wrongSize2 = DEFAULT_SIZE + 2;
        final double[] other = new double[wrongSize1];
        final double[] dest = new double[wrongSize2];

        try {
            arr.testAddValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched sizes");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test addValues with zero values.
     */
    @Test
    public void testAddValuesWithZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testAddValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test subtractValues with two arrays.
     */
    @Test
    public void testSubtractValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE;
        }

        arr.testSubtractValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 - TEST_VALUE, dest[i], EPSILON);
        }
    }

    /**
     * Test subtractValues does not modify source arrays.
     */
    @Test
    public void testSubtractValuesDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] otherCopy = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = TEST_VALUE_2;
            otherCopy[i] = TEST_VALUE_2;
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testSubtractValues(other, dest);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
        assertArrayEquals(otherCopy, other, EPSILON);
    }

    /**
     * Test subtractValues with mismatched other array size.
     */
    @Test
    public void testSubtractValuesWrongOtherSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] other = new double[wrongSize];
        final double[] dest = new double[DEFAULT_SIZE];

        try {
            arr.testSubtractValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test subtractValues with mismatched destination size.
     */
    @Test
    public void testSubtractValuesWrongDestSize() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final int wrongSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testSubtractValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test subtractValues with all arrays wrong size.
     */
    @Test
    public void testSubtractValuesAllWrongSizes() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize1 = DEFAULT_SIZE + 1;
        final int wrongSize2 = DEFAULT_SIZE + 2;
        final double[] other = new double[wrongSize1];
        final double[] dest = new double[wrongSize2];

        try {
            arr.testSubtractValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched sizes");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test subtractValues with zero values.
     */
    @Test
    public void testSubtractValuesWithZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        arr.testSubtractValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test subtractValues resulting in negative values.
     */
    @Test
    public void testSubtractValuesNegativeResult() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double largeValue = 10.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            other[i] = largeValue;
        }

        arr.testSubtractValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 - largeValue, dest[i], EPSILON);
        }
    }

    /**
     * Test that size is immutable after construction.
     */
    @Test
    public void testSizeIsImmutable() {
        final int size1 = arrayBase.size();
        final int size2 = arrayBase.size();
        assertEquals(size1, size2);
    }

    /**
     * Test operations on single element array.
     */
    @Test
    public void testSingleElementArray() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        final double[] other = new double[SMALL_SIZE];
        final double[] dest = new double[SMALL_SIZE];

        other[0] = TEST_VALUE;
        arr.testAddValues(other, dest);

        assertEquals(TEST_VALUE, dest[0], EPSILON);
    }

    /**
     * Test operations preserve precision.
     */
    @Test
    public void testPrecisionPreservation() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] dest = new double[DEFAULT_SIZE];
        final double preciseValue = 1.234567890123456;

        arr.testScaleValues(preciseValue, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0 * preciseValue, dest[i], EPSILON);
        }
    }

    /**
     * Test copyValues copies all elements correctly.
     */
    @Test
    public void testCopyValuesValidSourceCopiesAllElements() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = i + TEST_VALUE;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues does not modify source array.
     */
    @Test
    public void testCopyValuesValidSourceDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final double[] sourceCopy = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = i + TEST_VALUE_2;
            sourceCopy[i] = i + TEST_VALUE_2;
        }

        arr.testCopyValues(source);

        assertArrayEquals(sourceCopy, source, EPSILON);
    }

    /**
     * Test copyValues with source size larger than destination.
     */
    @Test
    public void testCopyValuesLargerSourceSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int largerSize = DEFAULT_SIZE + 1;
        final double[] source = new double[largerSize];

        try {
            arr.testCopyValues(source);
            fail("Should throw IllegalArgumentException for larger source size");
        } catch (IllegalArgumentException e) {
            assertEquals("Source array size must match destination size",
                        e.getMessage());
        }
    }

    /**
     * Test copyValues with source size smaller than destination.
     */
    @Test
    public void testCopyValuesSmallerSourceSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int smallerSize = DEFAULT_SIZE - 1;
        final double[] source = new double[smallerSize];

        try {
            arr.testCopyValues(source);
            fail("Should throw IllegalArgumentException for smaller source size");
        } catch (IllegalArgumentException e) {
            assertEquals("Source array size must match destination size",
                        e.getMessage());
        }
    }

    /**
     * Test copyValues with zero values.
     */
    @Test
    public void testCopyValuesZeroValuesCopiesZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, result[i], EPSILON);
        }
    }

    /**
     * Test copyValues with negative values.
     */
    @Test
    public void testCopyValuesNegativeValuesCopiesNegatives() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final double negativeValue = -5.5;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = negativeValue - i;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues with mixed positive and negative values.
     */
    @Test
    public void testCopyValuesMixedValuesCopiesAllValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = (i % 2 == 0) ? i + TEST_VALUE : -(i + TEST_VALUE);
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues replaces existing values.
     */
    @Test
    public void testCopyValuesExistingValuesReplacesWithNewValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] initialSource = new double[DEFAULT_SIZE];
        final double[] newSource = new double[DEFAULT_SIZE];
        final double initialValue = 1.0;
        final double newValue = 10.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            initialSource[i] = initialValue;
            newSource[i] = newValue;
        }

        arr.testCopyValues(initialSource);
        arr.testCopyValues(newSource);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(newSource, result, EPSILON);
    }

    /**
     * Test copyValues with single element array.
     */
    @Test
    public void testCopyValuesSingleElementCopiesSingleValue() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        final double[] source = new double[SMALL_SIZE];
        source[0] = TEST_VALUE;

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertEquals(TEST_VALUE, result[0], EPSILON);
    }

    /**
     * Test copyValues with large array.
     */
    @Test
    public void testCopyValuesLargeArrayCopiesAllElements() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        final double[] source = new double[LARGE_SIZE];

        for (int i = 0; i < LARGE_SIZE; i++) {
            source[i] = i * TEST_VALUE;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test copyValues preserves precision.
     */
    @Test
    public void testCopyValuesPreciseValuesPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] source = new double[DEFAULT_SIZE];
        final double preciseValue = 1.234567890123456;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            source[i] = preciseValue + i * 0.1;
        }

        arr.testCopyValues(source);

        final double[] result = arr.getValuesCopy();
        assertArrayEquals(source, result, EPSILON);
    }

    /**
     * Test multiplyValues with positive values.
     */
    @Test
    public void testMultiplyValuesPositiveValuesMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, i + 1.0);
            other[i] = SCALE_FACTOR;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals((i + 1.0) * SCALE_FACTOR, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues does not modify source arrays.
     */
    @Test
    public void testMultiplyValuesValidArraysDoesNotModifySource() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] otherCopy = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, TEST_VALUE);
            other[i] = TEST_VALUE_2;
            otherCopy[i] = TEST_VALUE_2;
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testMultiplyValues(other, dest);

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
        assertArrayEquals(otherCopy, other, EPSILON);
    }

    /**
     * Test multiplyValues with zero values.
     */
    @Test
    public void testMultiplyValuesZeroValuesProducesZeros() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, TEST_VALUE);
            other[i] = ZERO_SCALE;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(0.0, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with negative values.
     */
    @Test
    public void testMultiplyValuesNegativeValuesMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double negativeValue = -2.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, i + 1.0);
            other[i] = negativeValue;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals((i + 1.0) * negativeValue, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with mixed positive and negative values.
     */
    @Test
    public void testMultiplyValuesMixedValuesMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, i + 1.0);
            other[i] = (i % 2 == 0) ? SCALE_FACTOR : NEGATIVE_SCALE;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double expected = (i + 1.0)
                * ((i % 2 == 0) ? SCALE_FACTOR : NEGATIVE_SCALE);
            assertEquals(expected, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with identity (ones).
     */
    @Test
    public void testMultiplyValuesIdentityMultiplicationPreservesValues() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double identityValue = 1.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, i + TEST_VALUE);
            other[i] = identityValue;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(i + TEST_VALUE, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with mismatched other array size.
     */
    @Test
    public void testMultiplyValuesWrongOtherSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize = DEFAULT_SIZE + 1;
        final double[] other = new double[wrongSize];
        final double[] dest = new double[DEFAULT_SIZE];

        try {
            arr.testMultiplyValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test multiplyValues with mismatched destination size.
     */
    @Test
    public void testMultiplyValuesWrongDestSizeThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final int wrongSize = DEFAULT_SIZE - 1;
        final double[] dest = new double[wrongSize];

        try {
            arr.testMultiplyValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched size");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test multiplyValues with all arrays wrong size.
     */
    @Test
    public void testMultiplyValuesAllWrongSizesThrowsException() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final int wrongSize1 = DEFAULT_SIZE + 1;
        final int wrongSize2 = DEFAULT_SIZE + 2;
        final double[] other = new double[wrongSize1];
        final double[] dest = new double[wrongSize2];

        try {
            arr.testMultiplyValues(other, dest);
            fail("Should throw IllegalArgumentException for mismatched sizes");
        } catch (IllegalArgumentException e) {
            assertEquals("All arrays must have the same size", e.getMessage());
        }
    }

    /**
     * Test multiplyValues with single element array.
     */
    @Test
    public void testMultiplyValuesSingleElementMultipliesCorrectly() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        final double[] other = new double[SMALL_SIZE];
        final double[] dest = new double[SMALL_SIZE];

        arr.setValue(0, TEST_VALUE);
        other[0] = SCALE_FACTOR;

        arr.testMultiplyValues(other, dest);

        assertEquals(TEST_VALUE * SCALE_FACTOR, dest[0], EPSILON);
    }

    /**
     * Test multiplyValues with large array.
     */
    @Test
    public void testMultiplyValuesLargeArrayMultipliesAllElements() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        final double[] other = new double[LARGE_SIZE];
        final double[] dest = new double[LARGE_SIZE];
        final double multiplier = 3.0;

        for (int i = 0; i < LARGE_SIZE; i++) {
            arr.setValue(i, i * TEST_VALUE);
            other[i] = multiplier;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < LARGE_SIZE; i++) {
            assertEquals(i * TEST_VALUE * multiplier, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues preserves precision.
     */
    @Test
    public void testMultiplyValuesPreciseValuesPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];
        final double preciseValue1 = 1.234567890123456;
        final double preciseValue2 = 2.345678901234567;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, preciseValue1);
            other[i] = preciseValue2;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            assertEquals(preciseValue1 * preciseValue2, dest[i], EPSILON);
        }
    }

    /**
     * Test multiplyValues with varying values in both arrays.
     */
    @Test
    public void testMultiplyValuesVaryingValuesMultipliesElementWise() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double[] other = new double[DEFAULT_SIZE];
        final double[] dest = new double[DEFAULT_SIZE];

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, (i + 1) * TEST_VALUE);
            other[i] = (i + 2) * TEST_VALUE_2;
        }

        arr.testMultiplyValues(other, dest);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double expected = (i + 1) * TEST_VALUE
                * (i + 2) * TEST_VALUE_2;
            assertEquals(expected, dest[i], EPSILON);
        }
    }

    /**
     * Test sumValues with positive values.
     */
    @Test
    public void testSumValuesPositiveValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = i + TEST_VALUE;
            arr.setValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues with zero values.
     */
    @Test
    public void testSumValuesZeroValuesReturnsZero() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        final double result = arr.testSumValues();

        assertEquals(0.0, result, EPSILON);
    }

    /**
     * Test sumValues with negative values.
     */
    @Test
    public void testSumValuesNegativeValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;
        final double negativeValue = -3.5;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = negativeValue - i;
            arr.setValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues with mixed positive and negative values.
     */
    @Test
    public void testSumValuesMixedValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = (i % 2 == 0) ? (i + TEST_VALUE) : -(i + TEST_VALUE);
            arr.setValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues does not modify the array.
     */
    @Test
    public void testSumValuesDoesNotModifyArray() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, i + TEST_VALUE);
        }

        final double[] originalValues = arr.getValuesCopy();
        arr.testSumValues();

        assertArrayEquals(originalValues, arr.getValuesCopy(), EPSILON);
    }

    /**
     * Test sumValues with single element.
     */
    @Test
    public void testSumValuesSingleElementReturnsValue() {
        final TestArrayBase arr = new TestArrayBase(SMALL_SIZE);
        arr.setValue(0, TEST_VALUE);

        final double result = arr.testSumValues();

        assertEquals(TEST_VALUE, result, EPSILON);
    }

    /**
     * Test sumValues with large array.
     */
    @Test
    public void testSumValuesLargeArrayReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(LARGE_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < LARGE_SIZE; i++) {
            final double value = i * TEST_VALUE;
            arr.setValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues preserves precision.
     */
    @Test
    public void testSumValuesPreciseValuesPreservesPrecision() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        final double preciseValue = 1.234567890123456;
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            arr.setValue(i, preciseValue);
            expectedSum += preciseValue;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }

    /**
     * Test sumValues with values that sum to zero.
     */
    @Test
    public void testSumValuesOppositeValuesReturnZero() {
        final int evenSize = 4;
        final TestArrayBase arr = new TestArrayBase(evenSize);

        for (int i = 0; i < evenSize / 2; i++) {
            arr.setValue(i, TEST_VALUE);
        }
        for (int i = evenSize / 2; i < evenSize; i++) {
            arr.setValue(i, -TEST_VALUE);
        }

        final double result = arr.testSumValues();

        assertEquals(0.0, result, EPSILON);
    }

    /**
     * Test sumValues with varying values.
     */
    @Test
    public void testSumValuesVaryingValuesReturnsCorrectSum() {
        final TestArrayBase arr = new TestArrayBase(DEFAULT_SIZE);
        double expectedSum = 0.0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            final double value = (i + 1) * (i + 2) * 0.5;
            arr.setValue(i, value);
            expectedSum += value;
        }

        final double result = arr.testSumValues();

        assertEquals(expectedSum, result, EPSILON);
    }
}
