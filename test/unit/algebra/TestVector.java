package algebra;

import renderer.algebra.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
     */
    @Test
    public void testVectorCreation() {
        final int vectorSize = 5;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        assertNotNull(v);
        assertEquals(VECTOR_NAME, v.getName());
        assertEquals(vectorSize, v.size());
    }

    /**
     * Test the creation of a Vector from an array of values.
     */
    @Test
    public void testDefaultName() {
        final int vectorSize = 5;
        final Vector v = new Vector(vectorSize);
        assertEquals(Vector.DEFAULT_NAME, v.getName());
        assertEquals(vectorSize, v.size());
    }

    /**
     * Test the creation of a Vector from an array of values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSize() {
        new Vector("invalidVector", 0);
        fail("Expected an IllegalArgumentException to be thrown");
    }

    /**
     * Test the creation of a Vector from an array of values.
     */
    @Test
    public void testSetAndGetValues() {
        final int vectorSize = 5;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, i + 1.0);
        }
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(i + 1.0, v.get(i), EPSILON);
        }
    }

    /**
     * Test the scaling of a Vector.
     */
    @Test
    public void testScale() {
        final int vectorSize = 6;
        final double scale = 2.0;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, i + 1.0);
        }
        final Vector nv = v.scale(scale);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals((i + 1.0) * scale, nv.get(i), EPSILON);
        }
    }

    /**
     * Test the dot product of two Vectors.
     */
    @Test
    public void testDot() {
        final int vectorSize = 4;
        final double expected = 20.0;
        final Vector v1 = new Vector("v1", vectorSize);
        final Vector v2 = new Vector("v2", vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            v1.set(i, i + 1.0);
            v2.set(i, (double) vectorSize - i);
        }

        assertEquals(expected, v1.dot(v2), EPSILON);

    }


    /**
     * Test the getter of the norm.
     */
    @Test
    public void testNorm() {
        final int vectorSize = 3;
        final Vector v = new Vector(vectorSize);

        // square root of the sum of integer squares from 1 to vectorSize

        final double expectedValue = Math.sqrt(vectorSize * (vectorSize + 1) * (2 * vectorSize + 1) / 6);

        double value = 1;
        for (int i = 0; i < vectorSize; i++) {
            v.set(i, value++);
        }
        
        assertEquals(expectedValue, v.norm(), EPSILON);

    }

    /**
     * Test the normalization of a vector
     */
    @Test
    public void testNormalization() {
        final int vectorSize = 23;
        final Vector v = new Vector(vectorSize);

        final double expectedValue = 1 / Math.sqrt(vectorSize);
        final double value = 1;

        for (int i = 0; i < vectorSize; i++) {
            v.set(i, value);
        }

        final Vector nv = v.normalize();
        
        // v and nv are the same object.
        assertTrue("v == nv should be true", v == nv);

        for (int i = 0; i < vectorSize; i++) {
            assertEquals("error for " + i + "-th component", expectedValue, v.get(i), EPSILON);
        }

    }

    /**
     * Test the set zeros on a vector.
     */
    @Test
    public void testZeros() {
        final int vectorSize = 6;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        final Vector nv = v.zeros();
        
        // v and nv are the same object.
        assertTrue(v == nv);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(0.0, v.get(i), EPSILON);
        }
    }

    /**
     * Test the set ones of a Vector.
     */
    @Test
    public void testOnes() {
        final int vectorSize = 6;
        final Vector v = new Vector(VECTOR_NAME, vectorSize);
        final Vector nv = v.ones();
        
        // v and nv are the same object.
        assertTrue(v == nv);
        for (int i = 0; i < vectorSize; i++) {
            assertEquals(1.0, v.get(i), EPSILON);
        }
    }
}
