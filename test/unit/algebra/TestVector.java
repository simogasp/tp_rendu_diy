package unit.algebra;

import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;


public class TestVector {

    // Constants
    /**
     * The epsilon for double comparison.
     */
    private static final double EPSILON = 0.001;
    /**
     * The name of the test vector.
     */
    private static final String VECTOR_NAME = "testVector";

    /**
     * Test the creation of a Vector.
     * @throws InstantiationException
     */
    @Test
    public void testVectorCreation() throws InstantiationException {
        final int vectorSize = 5;
        Vector v = new Vector(VECTOR_NAME, vectorSize);
        assertNotNull(v);
        assertEquals(VECTOR_NAME, v.getName());
        assertEquals(vectorSize, v.size());
    }

    /**
     * Test the creation of a Vector from an array of values.
     * @throws InstantiationException
     */
    @Test
    public void testDefaultName() throws InstantiationException {
        final int vectorSize = 5;
        Vector v = new Vector(vectorSize);
        assertEquals(Vector.DEFAULT_NAME, v.getName());
        assertEquals(vectorSize, v.size());
    }

    /**
     * Test the creation of a Vector from an array of values.
     * @throws InstantiationException
     */
    @Test
    public void testInvalidSize() {
        try {
            new Vector("invalidVector", 0);
            fail("Expected an InstantiationException to be thrown");
        } catch (InstantiationException e) {
            assertEquals("Vector size must be strictly positive", e.getMessage());
        }
    }

    /**
     * Test the creation of a Vector from an array of values.
     * @throws InstantiationException
     */
    @Test
    public void testSetAndGetValues() throws InstantiationException {
        final int vectorSize = 5;
        Vector v = new Vector(VECTOR_NAME, vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, i + 1.0);
        }
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(i + 1.0, v.get(i), EPSILON);
        }
    }

    /**
     * Test the scaling of a Vector.
     * @throws InstantiationException
     */
    @Test
    public void testScale() throws InstantiationException {
        final int vectorSize = 6;
        final double scale = 2.0;
        Vector v = new Vector(VECTOR_NAME, vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, i + 1.0);
        }
        v.scale(scale);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals((i + 1.0) * scale, v.get(i), EPSILON);
        }
    }

    /**
     * Test the dot product of two Vectors.
     * @throws InstantiationException
     */
    @Test
    public void testDot() throws InstantiationException {
        final int vectorSize = 4;
        final double expected = 20.0;
        Vector v1 = new Vector("v1", vectorSize);
        Vector v2 = new Vector("v2", vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v1.set(i, i + 1.0);
            v2.set(i, (double) vectorSize - i);
        }
        try {
            assertEquals(expected, v1.dot(v2), EPSILON);
        } catch (SizeMismatchException e) {
            e.printStackTrace();
            fail("Unexpected SizeMismatchException exception");
        }
    }

    /**
     * Test the addition of two Vectors.
     * @throws InstantiationException
     */
    @Test
    public void testAdd() throws InstantiationException {
        final int vectorSize = 4;
        final double expectedSum = 5;
        Vector v1 = new Vector("v1", vectorSize);
        Vector v2 = new Vector("v2", vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v1.set(i, i + 1.0);
            v2.set(i, (double) vectorSize - i);
        }
        try {
            v1.add(v2);
            for (int i = 0; i < vectorSize; i++) {
                assertEquals(expectedSum, v1.get(i), EPSILON);
            }
        } catch (SizeMismatchException e) {
            e.printStackTrace();
            fail("Unexpected SizeMismatchException exception");
        }
    }

    /**
     * Test the subtraction of two Vectors.
     * @throws InstantiationException
     */
    @Test
    public void testSubtract() throws InstantiationException {
        final int vectorSize = 4;
        final double expectedRes = -vectorSize;
        Vector v1 = new Vector("v1", vectorSize);
        Vector v2 = new Vector("v2", vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v1.set(i, i + 1.0);
            v2.set(i, vectorSize + 1.0 + i);
        }
        try {
            v1.subtract(v2);
            for (int i = 0; i < vectorSize; i++) {
                assertEquals(expectedRes, v1.get(i), EPSILON);
            }
        } catch (SizeMismatchException e) {
            fail("Unexpected SizeMismatchException exception");
            e.printStackTrace();
        }

        try {
            Vector v3 = new Vector("testVector3", 2);
            v1.subtract(v3);
            fail("Expected a SizeMismatchException to be thrown");
        } catch (SizeMismatchException e) {

        }

    }

    /**
     * Test the normalization of a Vector.
     * @throws InstantiationException
     */
    @Test
    public void testZeros() throws InstantiationException {
        final int vectorSize = 6;
        Vector v = new Vector(VECTOR_NAME, vectorSize);
        v.zeros();
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(0.0, v.get(i), EPSILON);
        }
    }

    /**
     * Test the normalization of a Vector.
     * @throws InstantiationException
     */
    @Test
    public void testOnes() throws InstantiationException {
        final int vectorSize = 6;
        Vector v = new Vector(VECTOR_NAME, vectorSize);
        v.ones();
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(1.0, v.get(i), EPSILON);
        }
    }
}
