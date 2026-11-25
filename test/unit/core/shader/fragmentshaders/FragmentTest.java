package core.shader.fragmentshaders;

import java.awt.Color;

import org.junit.Test;

import renderer.algebra.MathUtils;
import renderer.algebra.Vector;
import renderer.core.shader.fragmentshaders.Fragment;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for fragment attributes, validation and copying.
 */
public class FragmentTest {

    /** Tolerance for comparing floating-point values. */
    private static final double EPSILON = 1e-10;
    /** Lower color and alpha boundary. */
    private static final double ZERO = 0.0;
    /** Upper color and alpha boundary. */
    private static final double ONE = 1.0;
    /** Midpoint of the normalized color range. */
    private static final double HALF = 0.5;
    /** Value below the normalized range. */
    private static final double BELOW_RANGE = -0.1;
    /** Value above the normalized range. */
    private static final double ABOVE_RANGE = 1.1;
    /** Red color component. */
    private static final double RED = 0.2;
    /** Green color component. */
    private static final double GREEN = 0.4;
    /** Blue color component. */
    private static final double BLUE = 0.6;
    /** Example depth. */
    private static final double DEPTH = -2.5;
    /** Example alpha. */
    private static final double ALPHA = 0.75;
    /** Example texture U coordinate. */
    private static final double U = -0.25;
    /** Example texture V coordinate. */
    private static final double V = 1.5;
    /** Example vector X component. */
    private static final double VECTOR_X = -3.0;
    /** Example vector Y component. */
    private static final double VECTOR_Y = 4.0;
    /** Example vector Z component. */
    private static final double VECTOR_Z = 5.0;
    /** Value used to mutate copies. */
    private static final double REPLACEMENT = 7.0;
    /** Initial pixel X coordinate. */
    private static final int X = -12;
    /** Initial pixel Y coordinate. */
    private static final int Y = 24;
    /** Replacement pixel X coordinate. */
    private static final int NEW_X = 42;
    /** Replacement pixel Y coordinate. */
    private static final int NEW_Y = -13;
    /** Number of fragment attributes. */
    private static final int ATTRIBUTE_COUNT = 13;
    /** Number of RGB components. */
    private static final int RGB_SIZE = 3;
    /** Invalid short vector or array size. */
    private static final int SHORT_SIZE = 2;
    /** Invalid long vector or array size. */
    private static final int LONG_SIZE = 4;
    /** First component index. */
    private static final int FIRST = 0;
    /** Second component index. */
    private static final int SECOND = 1;
    /** Third component index. */
    private static final int LAST_COMPONENT = 2;
    /** Largest color byte. */
    private static final int BYTE_MAX = 255;
    /** Truncated midpoint color byte. */
    private static final int BYTE_MIDDLE = 127;
    /** Value below the color byte range. */
    private static final int BYTE_BELOW = -1;
    /** Value above the color byte range. */
    private static final int BYTE_ABOVE = 256;
    /** Red component represented as a byte. */
    private static final int RED_BYTE = 51;
    /** Green component represented as a byte. */
    private static final int GREEN_BYTE = 102;
    /** Blue component represented as a byte. */
    private static final int BLUE_BYTE = 153;
    /** Number of zero-padded attributes. */
    private static final int PADDING = 2;

    private static Fragment newFragment() {
        return new Fragment(X, Y);
    }

    private static double[] attributes(Fragment fragment) {
        final double[] result = new double[ATTRIBUTE_COUNT];
        final double[] color = fragment.getColorRaw();
        final double[] textureCoordinates = fragment.getTextureCoordinates();
        final Vector normal = fragment.getNormal();
        final Vector worldPosition = fragment.getWorldPosition();

        result[Fragment.DEPTH] = fragment.getDepth();
        result[Fragment.COLOR_R] = color[FIRST];
        result[Fragment.COLOR_G] = color[SECOND];
        result[Fragment.COLOR_B] = color[LAST_COMPONENT];
        result[Fragment.COLOR_ALPHA] = fragment.getAlpha();
        result[Fragment.NORMAL_X] = normal.get(FIRST);
        result[Fragment.NORMAL_Y] = normal.get(SECOND);
        result[Fragment.NORMAL_Z] = normal.get(LAST_COMPONENT);
        result[Fragment.TEXTURE_U] = textureCoordinates[FIRST];
        result[Fragment.TEXTURE_V] = textureCoordinates[SECOND];
        result[Fragment.WORLD_X] = worldPosition.get(FIRST);
        result[Fragment.WORLD_Y] = worldPosition.get(SECOND);
        result[Fragment.WORLD_Z] = worldPosition.get(LAST_COMPONENT);
        return result;
    }

    private static double[] vectorValues(Vector vector) {
        return new double[]{vector.get(FIRST), vector.get(SECOND),
            vector.get(LAST_COMPONENT)};
    }

    /** Checks initial coordinates and zero-valued attributes. */
    @Test
    public void constructorValidCoordinatesInitializesZeroAttributes() {
        Fragment fragment = newFragment();

        assertEquals(X, fragment.getX());
        assertEquals(Y, fragment.getY());
        assertEquals(ATTRIBUTE_COUNT, fragment.getNumAttributes());
        assertArrayEquals(new double[ATTRIBUTE_COUNT], attributes(fragment), EPSILON);
        assertEquals(ZERO, fragment.getDepth(), EPSILON);
        assertEquals(Color.BLACK, fragment.getColor());
        assertArrayEquals(new double[RGB_SIZE],
                vectorValues(fragment.getNormal()), EPSILON);
        assertArrayEquals(new double[RGB_SIZE],
                vectorValues(fragment.getWorldPosition()), EPSILON);
    }

    /** Checks that the returned position is a copy. */
    @Test
    public void getPositionReturnedArrayMutatedFragmentUnchanged() {
        Fragment fragment = newFragment();
        int[] position = fragment.getPosition();

        assertArrayEquals(new int[]{X, Y}, position);
        position[FIRST] = NEW_X;
        assertEquals(X, fragment.getX());
        assertEquals(Y, fragment.getY());
    }

    /** Checks that changing coordinates preserves attributes. */
    @Test
    public void setPositionValidCoordinatesUpdatesOnlyPosition() {
        Fragment fragment = newFragment();
        fragment.setDepth(DEPTH);
        double[] before = attributes(fragment);

        fragment.setPosition(NEW_X, NEW_Y);

        assertArrayEquals(new int[]{NEW_X, NEW_Y}, fragment.getPosition());
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks that setting depth preserves other attributes. */
    @Test
    public void setDepthValidValueUpdatesOnlyDepth() {
        Fragment fragment = newFragment();
        fragment.setDepth(DEPTH);
        double[] expected = new double[ATTRIBUTE_COUNT];
        expected[Fragment.DEPTH] = DEPTH;

        assertEquals(DEPTH, fragment.getDepth(), EPSILON);
        assertArrayEquals(expected, attributes(fragment), EPSILON);
    }

    /** Checks normal component storage. */
    @Test
    public void setNormalComponentsValidValuesUpdatesOnlyNormal() {
        Fragment fragment = newFragment();
        fragment.setNormal(VECTOR_X, VECTOR_Y, VECTOR_Z);
        double[] expected = new double[ATTRIBUTE_COUNT];
        expected[Fragment.NORMAL_X] = VECTOR_X;
        expected[Fragment.NORMAL_Y] = VECTOR_Y;
        expected[Fragment.NORMAL_Z] = VECTOR_Z;

        assertArrayEquals(expected, attributes(fragment), EPSILON);
        assertArrayEquals(new double[]{VECTOR_X, VECTOR_Y, VECTOR_Z},
                vectorValues(fragment.getNormal()), EPSILON);
    }

    /** Checks that normal vectors do not alias fragment storage. */
    @Test
    public void setNormalVectorSourceAndReturnedVectorMutatedFragmentUnchanged() {
        Fragment fragment = newFragment();
        Vector source = new Vector(VECTOR_X, VECTOR_Y, VECTOR_Z);
        fragment.setNormal(source);
        source.set(FIRST, REPLACEMENT);
        Vector result = fragment.getNormal();
        result.set(LAST_COMPONENT, REPLACEMENT);

        assertArrayEquals(new double[]{VECTOR_X, VECTOR_Y, VECTOR_Z},
                vectorValues(fragment.getNormal()), EPSILON);
    }

    /** Checks null normal handling. */
    @Test
    public void setNormalNullVectorThrowsAndLeavesFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setNormal(VECTOR_X, VECTOR_Y, VECTOR_Z);
        double[] before = attributes(fragment);

        assertThrows(NullPointerException.class, () -> fragment.setNormal(null));
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks that a short normal is rejected without modifying the fragment. */
    @Test
    public void setNormalVectorShorterThanThreeThrowsAndLeavesFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setNormal(RED, GREEN, BLUE);
        Vector shortVector = new Vector(VECTOR_X, VECTOR_Y);
        double[] before = attributes(fragment);

        assertThrows(RuntimeException.class,
                () -> fragment.setNormal(shortVector));
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks that a long normal is rejected without modifying the fragment. */
    @Test
    public void setNormalVectorLongerThanThreeThrowsAndLeavesFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setNormal(RED, GREEN, BLUE);
        Vector longVector = new Vector(VECTOR_X, VECTOR_Y, VECTOR_Z, REPLACEMENT);
        double[] before = attributes(fragment);

        assertThrows(RuntimeException.class, () -> fragment.setNormal(longVector));
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks world-position component storage. */
    @Test
    public void setWorldPositionComponentsValidValuesUpdatesOnlyWorldPosition() {
        Fragment fragment = newFragment();
        fragment.setWorldPosition(VECTOR_X, VECTOR_Y, VECTOR_Z);
        double[] expected = new double[ATTRIBUTE_COUNT];
        expected[Fragment.WORLD_X] = VECTOR_X;
        expected[Fragment.WORLD_Y] = VECTOR_Y;
        expected[Fragment.WORLD_Z] = VECTOR_Z;

        assertArrayEquals(expected, attributes(fragment), EPSILON);
        assertArrayEquals(new double[]{VECTOR_X, VECTOR_Y, VECTOR_Z},
                vectorValues(fragment.getWorldPosition()), EPSILON);
    }

    /** Checks that world-position vectors do not alias fragment storage. */
    @Test
    public void setWorldPositionVectorSourceAndReturnedVectorMutatedFragmentUnchanged() {
        Fragment fragment = newFragment();
        Vector source = new Vector(VECTOR_X, VECTOR_Y, VECTOR_Z);
        fragment.setWorldPosition(source);
        source.set(FIRST, REPLACEMENT);
        Vector result = fragment.getWorldPosition();
        result.set(LAST_COMPONENT, REPLACEMENT);

        assertArrayEquals(new double[]{VECTOR_X, VECTOR_Y, VECTOR_Z},
                vectorValues(fragment.getWorldPosition()), EPSILON);
    }

    /** Checks invalid world-position vectors preserve state. */
    @Test
    public void setWorldPositionInvalidVectorsThrowAndLeaveFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setWorldPosition(VECTOR_X, VECTOR_Y, VECTOR_Z);
        double[] before = attributes(fragment);

        assertThrows(NullPointerException.class, () -> fragment.setWorldPosition(null));
        assertThrows(RuntimeException.class,
                () -> fragment.setWorldPosition(new Vector(SHORT_SIZE)));
        assertThrows(RuntimeException.class,
                () -> fragment.setWorldPosition(new Vector(LONG_SIZE)));
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks conversion and truncation of normalized color values. */
    @Test
    public void colorToIntValidValuesReturnsTruncatedByteValue() {
        assertEquals(FIRST, Fragment.colorToInt(ZERO));
        assertEquals(BYTE_MAX, Fragment.colorToInt(ONE));
        assertEquals(BYTE_MIDDLE, Fragment.colorToInt(HALF));
        assertEquals(RED_BYTE, Fragment.colorToInt(RED));
    }

    /** Checks tolerated boundary deviations are clamped. */
    @Test
    public void colorToIntWithinToleranceClampsToByteRange() {
        assertEquals(FIRST, Fragment.colorToInt(-MathUtils.DOUBLE_EPSILON));
        assertEquals(BYTE_MAX, Fragment.colorToInt(ONE + MathUtils.DOUBLE_EPSILON));
    }

    /** Checks rejection of invalid normalized color values. */
    @Test
    public void colorToIntInvalidValuesThrows() {
        for (double value : new double[]{BELOW_RANGE, ABOVE_RANGE, Double.NaN,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class,
                    () -> Fragment.colorToInt(value));
        }
    }

    /** Checks normalization of byte color values. */
    @Test
    public void colorToFloatValidValuesReturnsNormalizedValue() {
        assertEquals(ZERO, Fragment.colorToFloat(FIRST), EPSILON);
        assertEquals(ONE, Fragment.colorToFloat(BYTE_MAX), EPSILON);
        assertEquals(RED, Fragment.colorToFloat(RED_BYTE), EPSILON);
    }

    /** Checks rejection of values outside the byte range. */
    @Test
    public void colorToFloatOutsideByteRangeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> Fragment.colorToFloat(BYTE_BELOW));
        assertThrows(IllegalArgumentException.class,
                () -> Fragment.colorToFloat(BYTE_ABOVE));
    }

    /** Checks RGB component storage and conversion. */
    @Test
    public void setColorComponentsValidValuesUpdatesOnlyColor() {
        Fragment fragment = newFragment();
        fragment.setColor(RED, GREEN, BLUE);
        double[] expected = new double[ATTRIBUTE_COUNT];
        expected[Fragment.COLOR_R] = RED;
        expected[Fragment.COLOR_G] = GREEN;
        expected[Fragment.COLOR_B] = BLUE;

        assertArrayEquals(expected, attributes(fragment), EPSILON);
        assertEquals(new Color(RED_BYTE, GREEN_BYTE, BLUE_BYTE), fragment.getColor());
    }

    /** Checks that raw RGB values are returned in a new array. */
    @Test
    public void getColorRawValidColorReturnsIndependentRgbValues() {
        Fragment fragment = newFragment();
        fragment.setColor(RED, GREEN, BLUE);

        double[] rawColor = fragment.getColorRaw();

        assertArrayEquals(new double[]{RED, GREEN, BLUE}, rawColor, EPSILON);
        assertEquals(RGB_SIZE, rawColor.length);
        assertNotSame(rawColor, fragment.getColorRaw());

        rawColor[FIRST] = ONE;
        assertArrayEquals(new double[]{RED, GREEN, BLUE},
                fragment.getColorRaw(), EPSILON);
    }

    /** Checks inclusive RGB boundaries. */
    @Test
    public void setColorComponentsBoundaryValuesAccepted() {
        Fragment fragment = newFragment();
        fragment.setColor(ZERO, ONE, ZERO);

        assertEquals(new Color(FIRST, BYTE_MAX, FIRST), fragment.getColor());
    }

    /** Checks that the shared tolerance also applies to RGB setters. */
    @Test
    public void setColorComponentsWithinToleranceAcceptedAndClampedOnRead() {
        Fragment fragment = newFragment();
        double below = -MathUtils.DOUBLE_EPSILON;
        double above = ONE + MathUtils.DOUBLE_EPSILON;

        fragment.setColor(below, above, HALF);

        assertArrayEquals(new double[]{below, above, HALF},
                fragment.getColorRaw(), EPSILON);
        assertEquals(new Color(FIRST, BYTE_MAX, BYTE_MIDDLE), fragment.getColor());
    }

    /** Checks invalid values for each RGB component preserve state. */
    @Test
    public void setColorComponentsInvalidValuesThrowAndLeaveFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setColor(RED, GREEN, BLUE);
        double[] before = attributes(fragment);
        double[] invalidValues = {BELOW_RANGE, ABOVE_RANGE, Double.NaN,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};

        for (double value : invalidValues) {
            assertThrows(IllegalArgumentException.class,
                    () -> fragment.setColor(value, GREEN, BLUE));
            assertThrows(IllegalArgumentException.class,
                    () -> fragment.setColor(RED, value, BLUE));
            assertThrows(IllegalArgumentException.class,
                    () -> fragment.setColor(RED, GREEN, value));
            assertArrayEquals(before, attributes(fragment), EPSILON);
        }
    }

    /** Checks that RGB array components are copied. */
    @Test
    public void setColorArrayValidValuesCopiesComponents() {
        Fragment fragment = newFragment();
        double[] source = {RED, GREEN, BLUE};
        fragment.setColor(source);
        source[FIRST] = ONE;

        assertArrayEquals(new double[]{RED, GREEN, BLUE},
                fragment.getColorRaw(), EPSILON);
    }

    /** Checks invalid RGB arrays preserve state. */
    @Test
    public void setColorArrayInvalidValuesThrowAndLeaveFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setColor(RED, GREEN, BLUE);
        double[] before = attributes(fragment);

        assertThrows(NullPointerException.class,
                () -> fragment.setColor((double[]) null));
        assertThrows(IllegalArgumentException.class,
                () -> fragment.setColor(new double[FIRST]));
        assertThrows(IllegalArgumentException.class,
                () -> fragment.setColor(new double[SHORT_SIZE]));
        assertThrows(IllegalArgumentException.class,
                () -> fragment.setColor(new double[LONG_SIZE]));
        assertThrows(IllegalArgumentException.class,
                () -> fragment.setColor(new double[]{RED, ABOVE_RANGE, BLUE}));
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks Color conversion leaves fragment alpha unchanged. */
    @Test
    public void setColorColorValidValueStoresRgbWithoutChangingAlpha() {
        Fragment fragment = newFragment();
        fragment.setAlpha(ALPHA);
        Color color = new Color(RED_BYTE, GREEN_BYTE, BLUE_BYTE, BYTE_MIDDLE);
        fragment.setColor(color);

        assertEquals(new Color(RED_BYTE, GREEN_BYTE, BLUE_BYTE), fragment.getColor());
        assertEquals(ALPHA, fragment.getAlpha(), EPSILON);
    }

    /** Checks null Color handling preserves state. */
    @Test
    public void setColorColorNullThrowsAndLeavesFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setColor(RED, GREEN, BLUE);
        double[] before = attributes(fragment);

        assertThrows(NullPointerException.class, () -> fragment.setColor((Color) null));
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks alpha values and boundaries. */
    @Test
    public void setAlphaValidValuesUpdatesOnlyAlpha() {
        Fragment fragment = newFragment();
        for (double alpha : new double[]{ZERO, ALPHA, ONE}) {
            fragment.setAlpha(alpha);
            double[] expected = new double[ATTRIBUTE_COUNT];
            expected[Fragment.COLOR_ALPHA] = alpha;
            assertEquals(alpha, fragment.getAlpha(), EPSILON);
            assertArrayEquals(expected, attributes(fragment), EPSILON);
        }
    }

    /** Checks that alpha defaults to zero. */
    @Test
    public void getAlphaUninitializedFragmentReturnsZero() {
        assertEquals(ZERO, newFragment().getAlpha(), EPSILON);
    }

    /** Checks that the shared tolerance also applies to alpha setters. */
    @Test
    public void setAlphaWithinToleranceStoresUnclampedValue() {
        Fragment fragment = newFragment();
        double below = -MathUtils.DOUBLE_EPSILON;
        double above = ONE + MathUtils.DOUBLE_EPSILON;

        fragment.setAlpha(below);
        assertEquals(below, fragment.getAlpha(), EPSILON);
        fragment.setAlpha(above);
        assertEquals(above, fragment.getAlpha(), EPSILON);
    }

    /** Checks invalid alpha values preserve state. */
    @Test
    public void setAlphaInvalidValuesThrowAndLeaveFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setAlpha(ALPHA);
        double[] before = attributes(fragment);
        for (double value : new double[]{BELOW_RANGE, ABOVE_RANGE, Double.NaN,
                Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> fragment.setAlpha(value));
            assertArrayEquals(before, attributes(fragment), EPSILON);
        }
    }

    /** Checks texture coordinates outside the unit interval. */
    @Test
    public void setTextureCoordinatesOutOfUnitRangeUpdatesOnlyTextureCoordinates() {
        Fragment fragment = newFragment();
        fragment.setTextureCoordinates(U, V);
        double[] expected = new double[ATTRIBUTE_COUNT];
        expected[Fragment.TEXTURE_U] = U;
        expected[Fragment.TEXTURE_V] = V;

        assertArrayEquals(expected, attributes(fragment), EPSILON);
        assertArrayEquals(new double[]{U, V},
                fragment.getTextureCoordinates(), EPSILON);
    }

    /** Checks that texture coordinates are returned in a defensive copy. */
    @Test
    public void getTextureCoordinatesReturnedArrayMutatedFragmentUnchanged() {
        Fragment fragment = newFragment();
        fragment.setTextureCoordinates(U, V);

        double[] coordinates = fragment.getTextureCoordinates();

        assertArrayEquals(new double[]{U, V}, coordinates, EPSILON);
        assertNotSame(coordinates, fragment.getTextureCoordinates());
        coordinates[FIRST] = REPLACEMENT;

        assertArrayEquals(new double[]{U, V},
                fragment.getTextureCoordinates(), EPSILON);
    }

    /** Checks coordinate formatting and purity. */
    @Test
    public void toStringValidCoordinatesReturnsCoordinatesWithoutMutation() {
        Fragment fragment = newFragment();
        fragment.setDepth(DEPTH);
        double[] before = attributes(fragment);

        assertEquals("(" + X + "," + Y + ")", fragment.toString());
        assertArrayEquals(new int[]{X, Y}, fragment.getPosition());
        assertArrayEquals(before, attributes(fragment), EPSILON);
    }

    /** Checks the clone copies all attributes independently in both directions. */
    @Test
    public void clonePopulatedFragmentCopiesAllStateAndIsIndependent() {
        Fragment original = newFragment();
        original.setDepth(DEPTH);
        original.setColor(RED, GREEN, BLUE);
        original.setAlpha(ALPHA);
        original.setNormal(VECTOR_X, VECTOR_Y, VECTOR_Z);
        original.setTextureCoordinates(U, V);
        original.setWorldPosition(VECTOR_X, VECTOR_Y, VECTOR_Z);
        double[] before = attributes(original);

        Fragment clone = original.clone();
        assertNotSame(original, clone);
        assertArrayEquals(original.getPosition(), clone.getPosition());
        assertEquals(original.getNumAttributes(), clone.getNumAttributes());
        assertArrayEquals(before, attributes(clone), EPSILON);
        assertArrayEquals(before, attributes(original), EPSILON);

        clone.setPosition(NEW_X, NEW_Y);
        clone.setDepth(REPLACEMENT);
        clone.setColor(ONE, ZERO, HALF);
        clone.setAlpha(ZERO);
        clone.setNormal(REPLACEMENT, ZERO, ZERO);
        clone.setTextureCoordinates(REPLACEMENT, ZERO);
        clone.setWorldPosition(REPLACEMENT, ZERO, ZERO);
        assertArrayEquals(new int[]{X, Y}, original.getPosition());
        assertArrayEquals(before, attributes(original), EPSILON);

        original.setDepth(ZERO);
        assertEquals(REPLACEMENT, clone.getDepth(), EPSILON);
    }
}
