package unit.algebra;

import algebra.SizeMismatchException;
import algebra.Vector;
import algebra.Vector3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;


public class TestVector3 {

    /**
     * The epsilon for double comparison.
     */
    private static final double EPSILON = 0.001;

    /**
     * Test the creation of a Vector3.
     */
    @Test
    public void testVector3Creation() {
        final double x = 1.0;
        final double y = 2.0;
        final double z = 3.0;
        Vector3 v = new Vector3(x, y, z);
        assertNotNull(v);
        assertEquals(x, v.getX(), EPSILON);
        assertEquals(y, v.getY(), EPSILON);
        assertEquals(z, v.getZ(), EPSILON);
        assertEquals(Vector.DEFAULT_NAME, v.getName());
    }

    /**
     * Test the creation of a Vector3 with a name.
     */
    @Test
    public void testCrossProduct() {
        Vector3 v1 = new Vector3(1.0, 0.0, 0.0);
        Vector3 v2 = new Vector3(0.0, 1.0, 0.0);
        Vector3 result = v1.cross(v2);
        assertEquals(0.0, result.getX(), EPSILON);
        assertEquals(0.0, result.getY(), EPSILON);
        assertEquals(1.0, result.getZ(), EPSILON);
    }

    /**
     * Test the dot product of two vectors.
     */
    @Test
    public void testDotProduct() {
        final double x1 = 1.0;
        final double y1 = 2.0;
        final double z1 = 3.0;
        final double x2 = 4.0;
        final double y2 = 5.0;
        final double z2 = 6.0;
        final double expected = x1 * x2 + y1 * y2 + z1 * z2;
        Vector3 v1 = new Vector3(x1, y1, z1);
        Vector3 v2 = new Vector3(x2, y2, z2);
        double result = v1.dot(v2);
        assertEquals(expected, result, EPSILON);
    }

    /**
     * Test the norm of a vector.
     */
    @Test
    public void testNorm() {
        final double expected = 3.0;
        Vector3 v = new Vector3(1.0, 2.0, 2.0);
        double result = v.norm();
        assertEquals(expected, result, EPSILON);
    }

    /**
     * Test the normalization of a vector.
     */
    @Test
    public void testAdd() {
        final double x1 = 1.0;
        final double y1 = 2.0;
        final double z1 = 3.0;
        Vector3 v1 = new Vector3(x1, y1, z1);
        final double x2 = 4.0;
        final double y2 = 5.0;
        final double z2 = 6.0;
        Vector3 v2 = new Vector3(x2, y2, z2);
        try {
            v1.add(v2);
            assertEquals(x1 + x2, v1.getX(), EPSILON);
            assertEquals(y1 + y2, v1.getY(), EPSILON);
            assertEquals(z1 + z2, v1.getZ(), EPSILON);
        } catch (SizeMismatchException e) {
            e.printStackTrace();
            fail("Unexpected SizeMismatchException exception");
        }

    }

    /**
     * Test the subtraction of two vectors.
     */
    @Test
    public void testSubtract() {
        final double expected = 3.0;
        final double x1 = 4.0;
        final double y1 = 5.0;
        final double z1 = 6.0;
        final double x2 = 1.0;
        final double y2 = 2.0;
        final double z2 = 3.0;
        Vector3 v1 = new Vector3(x1, y1, z1);
        Vector3 v2 = new Vector3(x2, y2, z2);
        try {
            v1.subtract(v2);
            assertEquals(expected, v1.getX(), EPSILON);
            assertEquals(expected, v1.getY(), EPSILON);
            assertEquals(expected, v1.getZ(), EPSILON);
        } catch (SizeMismatchException e) {
            e.printStackTrace();
            fail("Unexpected SizeMismatchException exception");
        }

    }

    /**
     * Test the scale of a vector.
     */
    @Test
    public void testScale() {
        final double x = 1.0;
        final double y = 2.0;
        final double z = 3.0;
        final double scale = 2.0;
        Vector3 v = new Vector3(x, y, z);
        v.scale(scale);
        assertEquals(scale * x, v.getX(), EPSILON);
        assertEquals(scale * y, v.getY(), EPSILON);
        assertEquals(scale * z, v.getZ(), EPSILON);
    }

    /**
     * Test the normalization of a vector.
     */
    @Test
    public void testNormalize() {
        final double x = 1.0;
        final double y = 2.0;
        final double z = 2.0;
        final double norm = Math.sqrt(x * x + y * y + z * z);
        Vector3 v = new Vector3(x, y, z);
        v.normalize();
        assertEquals(x / norm, v.getX(), EPSILON);
        assertEquals(y / norm, v.getY(), EPSILON);
        assertEquals(z / norm, v.getZ(), EPSILON);
    }
}
