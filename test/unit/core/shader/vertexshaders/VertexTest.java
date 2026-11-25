package core.shader.vertexshaders;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import renderer.algebra.Vector;
import renderer.core.shader.vertexshaders.Vertex;

/**
 * Unit tests for the Vertex class.
 */
public class VertexTest {

    /** The epsilon used for comparing double values. */
    private static final double EPSILON = 1e-10;
    /** The X coordinate used for testing. */
    private static final int X_COORD = 12;
    /** The Y coordinate used for testing. */
    private static final int Y_COORD = -7;
    /** The depth value used for testing. */
    private static final double DEPTH_VALUE = 0.75;
    /** The alpha value used for testing. */
    private static final double ALPHA_VALUE = 0.4;
    /** The U coordinate used for testing. */
    private static final double U_VALUE = 0.25;
    /** The V coordinate used for testing. */
    private static final double V_VALUE = 0.65;
    /** The size of a valid RGB color array. */
    private static final int COLOR_SIZE = 3;
    /** The zero value used for default scalar fields. */
    private static final double ZERO = 0.0;
    /** The one value used for full-intensity fields. */
    private static final double ONE = 1.0;
    /** The X component index in vectors and arrays. */
    private static final int X_INDEX = 0;
    /** The Y component index in vectors and arrays. */
    private static final int Y_INDEX = 1;
    /** The default X and Y coordinate. */
    private static final int DEFAULT_COORDINATE = 0;
    /** A new X coordinate used for setter tests. */
    private static final int NEW_X_COORD = 42;
    /** A new Y coordinate used for setter tests. */
    private static final int NEW_Y_COORD = -42;
    /** A negative depth used for setter tests. */
    private static final double NEGATIVE_DEPTH = -1.25;
    /** A first source vector component. */
    private static final double VECTOR_X_1 = 1.0;
    /** A second source vector component. */
    private static final double VECTOR_Y_1 = 2.0;
    /** A third source vector component. */
    private static final double VECTOR_Z_1 = 3.0;
    /** A second vector X component. */
    private static final double VECTOR_X_2 = 4.0;
    /** A second vector Y component. */
    private static final double VECTOR_Y_2 = 5.0;
    /** A second vector Z component. */
    private static final double VECTOR_Z_2 = 6.0;
    /** World-position X used in constructor tests. */
    private static final double WORLD_X = 3.0;
    /** World-position Y used in constructor tests. */
    private static final double WORLD_Y = 4.0;
    /** World-position Z used in constructor tests. */
    private static final double WORLD_Z = 5.0;
    /** First normal X used in constructor tests. */
    private static final double NORMAL_X = 1.0;
    /** First normal Y used in constructor tests. */
    private static final double NORMAL_Y = 0.0;
    /** First normal Z used in constructor tests. */
    private static final double NORMAL_Z = 0.0;
    /** Mutated source normal X. */
    private static final double MUTATED_NORMAL_X = 9.0;
    /** Mutated source world-position X. */
    private static final double MUTATED_WORLD_X = 8.0;
    /** A color red component. */
    private static final double COLOR_RED_1 = 0.1;
    /** A color green component. */
    private static final double COLOR_GREEN_1 = 0.2;
    /** A color blue component. */
    private static final double COLOR_BLUE_1 = 0.3;
    /** A valid color red component. */
    private static final double COLOR_RED_2 = 0.4;
    /** A valid color green component. */
    private static final double COLOR_GREEN_2 = 0.5;
    /** A valid color blue component. */
    private static final double COLOR_BLUE_2 = 0.6;
    /** A cloned color red component. */
    private static final double CLONE_COLOR_RED = 0.2;
    /** A cloned color green component. */
    private static final double CLONE_COLOR_GREEN = 0.4;
    /** A cloned color blue component. */
    private static final double CLONE_COLOR_BLUE = 0.6;
    /** A mutated color component. */
    private static final double MUTATED_COLOR = 1.0;
    /** A mid-intensity green component. */
    private static final double MID_INTENSITY = 0.5;
    /** A mutated green component. */
    private static final double MUTATED_GREEN = 0.9;
    /** A color component used for short invalid arrays. */
    private static final double SHORT_COLOR_GREEN = 0.2;
    /** A color component used for original valid colors. */
    private static final double ORIGINAL_COLOR_RED = 0.2;
    /** A color component used for original valid colors. */
    private static final double ORIGINAL_COLOR_GREEN = 0.4;
    /** A color component used for original valid colors. */
    private static final double ORIGINAL_COLOR_BLUE = 0.6;
    /** A negative U coordinate used for setter tests. */
    private static final double NEGATIVE_U = -0.5;
    /** A new V coordinate used for setter tests. */
    private static final double NEW_V = 1.5;
    /** A negative clone X coordinate. */
    private static final int CLONE_X = -1;
    /** A shallow clone depth. */
    private static final double CLONE_DEPTH = 0.1;
    /** A clone world-position X. */
    private static final double CLONE_WORLD_X = 9.0;
    /** A clone world-position Y. */
    private static final double CLONE_WORLD_Y = 8.0;
    /** A clone world-position Z. */
    private static final double CLONE_WORLD_Z = 7.0;
    /** A cloned alpha value. */
    private static final double CLONE_ALPHA = 1.0;
    /** A cloned U value. */
    private static final double CLONE_U = 0.9;
    /** A cloned V value. */
    private static final double CLONE_V = 0.8;
    /** The length of an empty color array. */
    private static final int EMPTY_COLOR_LENGTH = 0;

    /**
     * Test that the default constructor creates a usable Vertex.
     */
    @Test
    public void defaultConstructorValidVertexDefaultValues() {
        final Vertex vertex = new Vertex();

        assertNotNull(vertex);
        assertEquals(DEFAULT_COORDINATE, vertex.getX());
        assertEquals(DEFAULT_COORDINATE, vertex.getY());
        assertEquals(ZERO, vertex.getDepth(), EPSILON);
        assertNull(vertex.getNormal());
        assertNull(vertex.getWorldPosition());
        assertArrayEquals(new double[]{ZERO, ZERO, ZERO},
                          vertex.getColor(), EPSILON);
        assertEquals(ZERO, vertex.getAlpha(), EPSILON);
        assertEquals(ZERO, vertex.getU(), EPSILON);
        assertEquals(ZERO, vertex.getV(), EPSILON);
    }

    /**
     * Test the two-argument constructor.
     */
    @Test
    public void twoArgumentConstructorValidCoordinatesInitialized() {
        final Vertex vertex = new Vertex(X_COORD, Y_COORD);

        assertEquals(X_COORD, vertex.getX());
        assertEquals(Y_COORD, vertex.getY());
        assertEquals(ZERO, vertex.getDepth(), EPSILON);
    }

    /**
     * Test the three-argument constructor.
     */
    @Test
    public void threeArgumentConstructorValidValuesInitialized() {
        final Vertex vertex = new Vertex(X_COORD, Y_COORD, DEPTH_VALUE);

        assertEquals(X_COORD, vertex.getX());
        assertEquals(Y_COORD, vertex.getY());
        assertEquals(DEPTH_VALUE, vertex.getDepth(), EPSILON);
    }

    /**
     * Test the five-argument constructor and its defensive vector copies.
     */
    @Test
    public void fiveArgumentConstructorValidVectorsCopied() {
        final Vector normal =
            new Vector(NORMAL_X, NORMAL_Y, NORMAL_Z);
        final Vector worldPosition =
            new Vector(WORLD_X, WORLD_Y, WORLD_Z);
        final Vertex vertex =
            new Vertex(X_COORD, Y_COORD, DEPTH_VALUE, normal, worldPosition);

        assertEquals(X_COORD, vertex.getX());
        assertEquals(Y_COORD, vertex.getY());
        assertEquals(DEPTH_VALUE, vertex.getDepth(), EPSILON);
        assertNotSame(normal, vertex.getNormal());
        assertNotSame(worldPosition, vertex.getWorldPosition());
        assertEquals(normal.getX(), vertex.getNormal().getX(), EPSILON);
        assertEquals(normal.getY(), vertex.getNormal().getY(), EPSILON);
        assertEquals(normal.getZ(), vertex.getNormal().getZ(), EPSILON);
        assertEquals(worldPosition.getX(),
                     vertex.getWorldPosition().getX(), EPSILON);
        assertEquals(worldPosition.getY(),
                     vertex.getWorldPosition().getY(), EPSILON);
        assertEquals(worldPosition.getZ(),
                     vertex.getWorldPosition().getZ(), EPSILON);
    }

    /**
     * Test that a five-argument constructor copy is independent of source vectors.
     */
    @Test
    public void fiveArgumentConstructorSourceVectorsChangedCopyUnchanged() {
        final Vector normal =
            new Vector(NORMAL_X, NORMAL_Y, NORMAL_Z);
        final Vector worldPosition =
            new Vector(WORLD_X, WORLD_Y, WORLD_Z);
        final Vertex vertex =
            new Vertex(X_COORD, Y_COORD, DEPTH_VALUE, normal, worldPosition);

        normal.set(X_INDEX, MUTATED_NORMAL_X);
        worldPosition.set(X_INDEX, MUTATED_WORLD_X);

        assertEquals(NORMAL_X, vertex.getNormal().getX(), EPSILON);
        assertEquals(WORLD_X, vertex.getWorldPosition().getX(), EPSILON);
    }

    /**
     * Test the seven-argument constructor.
     */
    @Test
    public void sevenArgumentConstructorValidValuesAndColorInitialized() {
        final Vector normal =
            new Vector(NORMAL_Y, NORMAL_X, NORMAL_Y);
        final Vector worldPosition =
            new Vector(VECTOR_X_1, VECTOR_Y_1, VECTOR_Z_1);
        final double[] color =
            {COLOR_RED_1, COLOR_GREEN_1, COLOR_BLUE_1};
        final Vertex vertex = new Vertex(X_COORD, Y_COORD, DEPTH_VALUE,
                                         normal, worldPosition,
                                         color, ALPHA_VALUE);

        assertArrayEquals(color, vertex.getColor(), EPSILON);
        assertNotSame(color, vertex.getColor());
        assertEquals(ALPHA_VALUE, vertex.getAlpha(), EPSILON);
    }

    /**
     * Test that a constructor-created color copy is independent of the source array.
     */
    @Test
    public void sevenArgumentConstructorSourceColorChangedCopyUnchanged() {
        final double[] color =
            {COLOR_RED_1, COLOR_GREEN_1, COLOR_BLUE_1};
        final Vertex vertex = new Vertex(X_COORD, Y_COORD, DEPTH_VALUE,
                                         new Vector(NORMAL_Y, NORMAL_X, NORMAL_Y),
                                         new Vector(VECTOR_X_1, VECTOR_Y_1, VECTOR_Z_1),
                                         color, ALPHA_VALUE);

        color[X_INDEX] = MUTATED_COLOR;

        assertEquals(COLOR_RED_1, vertex.getColor()[X_INDEX], EPSILON);
    }

    /**
     * Test the nine-argument constructor.
     */
    @Test
    public void nineArgumentConstructorValidUVValuesInitialized() {
        final Vertex vertex = new Vertex(X_COORD, Y_COORD, DEPTH_VALUE,
                                         new Vector(NORMAL_Y, NORMAL_Y, NORMAL_X),
                                         new Vector(VECTOR_X_1, VECTOR_Y_1, VECTOR_Z_1),
                                         new double[]{COLOR_RED_2, COLOR_GREEN_2,
                                                      COLOR_BLUE_2},
                                         ALPHA_VALUE, U_VALUE, V_VALUE);

        assertEquals(U_VALUE, vertex.getU(), EPSILON);
        assertEquals(V_VALUE, vertex.getV(), EPSILON);
    }

    /**
     * Test setting and getting X and Y coordinates.
     */
    @Test
    public void setAndGetCoordinatesValidValuesUpdated() {
        final Vertex vertex = new Vertex();
        final int newX = NEW_X_COORD;
        final int newY = NEW_Y_COORD;

        vertex.setX(newX);
        vertex.setY(newY);

        assertEquals(newX, vertex.getX());
        assertEquals(newY, vertex.getY());
    }

    /**
     * Test setting and getting depth.
     */
    @Test
    public void setAndGetDepthValidValueUpdated() {
        final Vertex vertex = new Vertex();
        final double newDepth = NEGATIVE_DEPTH;

        vertex.setDepth(newDepth);

        assertEquals(newDepth, vertex.getDepth(), EPSILON);
    }

    /**
     * Test setting and getting normal and world position vectors.
     */
    @Test
    public void setAndGetVectorsValidReferencesReturned() {
        final Vertex vertex = new Vertex();
        final Vector normal =
            new Vector(VECTOR_X_1, VECTOR_Y_1, VECTOR_Z_1);
        final Vector worldPosition =
            new Vector(VECTOR_X_2, VECTOR_Y_2, VECTOR_Z_2);

        vertex.setNormal(normal);
        vertex.setWorldPosition(worldPosition);

        assertSame(normal, vertex.getNormal());
        assertSame(worldPosition, vertex.getWorldPosition());
    }

    /**
     * Test that normal and world position setters accept null.
     */
    @Test
    public void setVectorsNullValuesNullReturned() {
        final Vertex vertex = new Vertex();

        vertex.setNormal(null);
        vertex.setWorldPosition(null);

        assertNull(vertex.getNormal());
        assertNull(vertex.getWorldPosition());
    }

    /**
     * Test setting and getting a valid RGB color.
     */
    @Test
    public void setColorValidLengthArrayStored() {
        final Vertex vertex = new Vertex();
        final double[] color = {ZERO, MID_INTENSITY, ONE};

        vertex.setColor(color);

        assertSame(color, vertex.getColor());
        assertArrayEquals(color, vertex.getColor(), EPSILON);
    }

    /**
     * Test that setColor does not copy the provided array.
     */
    @Test
    public void setColorValidArrayMutatedExternalArrayChangesVertex() {
        final Vertex vertex = new Vertex();
        final double[] color =
            {COLOR_RED_1, COLOR_GREEN_1, COLOR_BLUE_1};
        final double green = MUTATED_GREEN;

        vertex.setColor(color);
        color[Y_INDEX] = green;

        assertEquals(green, vertex.getColor()[Y_INDEX], EPSILON);
    }

    /**
     * Test setColor with arrays shorter than RGB size.
     */
    @Test
    public void setColorShortArrayIllegalArgumentExceptionThrown() {
        final Vertex vertex = new Vertex();
        final double[] shortColor =
            {COLOR_RED_1, SHORT_COLOR_GREEN};

        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class,
                         () -> vertex.setColor(shortColor));

        assertEquals("Color array must have length " + COLOR_SIZE
                     + " provided one has length " + shortColor.length,
                     exception.getMessage());
    }

    /**
     * Test setColor with arrays longer than RGB size.
     */
    @Test
    public void setColorLongArrayIllegalArgumentExceptionThrown() {
        final Vertex vertex = new Vertex();
        final double[] longColor =
            {COLOR_RED_1, COLOR_GREEN_1, COLOR_BLUE_1, COLOR_RED_2};

        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class,
                         () -> vertex.setColor(longColor));

        assertEquals("Color array must have length " + COLOR_SIZE
                     + " provided one has length " + longColor.length,
                     exception.getMessage());
    }

    /**
     * Test setColor with an empty array.
     */
    @Test
    public void setColorEmptyArrayIllegalArgumentExceptionThrown() {
        final Vertex vertex = new Vertex();

        assertThrows(IllegalArgumentException.class,
                     () -> vertex.setColor(new double[EMPTY_COLOR_LENGTH]));
    }

    /**
     * Test setColor with null.
     */
    @Test
    public void setColorNullArrayNullPointerExceptionThrown() {
        final Vertex vertex = new Vertex();

        assertThrows(NullPointerException.class,
                     () -> vertex.setColor(null));
    }

    /**
     * Test that an invalid setColor call leaves the existing color unchanged.
     */
    @Test
    public void setColorInvalidArrayExistingColorUnchanged() {
        final Vertex vertex = new Vertex();
        final double[] originalColor =
            {ORIGINAL_COLOR_RED, ORIGINAL_COLOR_GREEN, ORIGINAL_COLOR_BLUE};
        vertex.setColor(originalColor);

        assertThrows(IllegalArgumentException.class,
                     () -> vertex.setColor(
                         new double[]{COLOR_RED_1, SHORT_COLOR_GREEN}));

        assertSame(originalColor, vertex.getColor());
    }

    /**
     * Test setting and getting alpha.
     */
    @Test
    public void setAndGetAlphaValidValueUpdated() {
        final Vertex vertex = new Vertex();
        final double newAlpha = ZERO;

        vertex.setAlpha(newAlpha);

        assertEquals(newAlpha, vertex.getAlpha(), EPSILON);
    }

    /**
     * Test setting and getting U and V coordinates.
     */
    @Test
    public void setAndGetUVValidValuesUpdated() {
        final Vertex vertex = new Vertex();
        final double newU = NEGATIVE_U;
        final double newV = NEW_V;

        vertex.setU(newU);
        vertex.setV(newV);

        assertEquals(newU, vertex.getU(), EPSILON);
        assertEquals(newV, vertex.getV(), EPSILON);
    }

    /**
     * Test that clone creates an independent copy of all Vertex data.
     */
    @Test
    public void cloneValidVertexIndependentCopyReturned() {
        final Vertex vertex = new Vertex(X_COORD, Y_COORD, DEPTH_VALUE,
                                         new Vector(NORMAL_X, NORMAL_Y, NORMAL_Z),
                                         new Vector(VECTOR_X_1, VECTOR_Y_1, VECTOR_Z_1),
                                         new double[]{CLONE_COLOR_RED,
                                                      CLONE_COLOR_GREEN,
                                                      CLONE_COLOR_BLUE},
                                         ALPHA_VALUE, U_VALUE, V_VALUE);

        final Vertex clone = vertex.clone();

        assertNotSame(vertex, clone);
        assertEquals(vertex.getX(), clone.getX());
        assertEquals(vertex.getY(), clone.getY());
        assertEquals(vertex.getDepth(), clone.getDepth(), EPSILON);
        assertNotSame(vertex.getNormal(), clone.getNormal());
        assertNotSame(vertex.getWorldPosition(), clone.getWorldPosition());
        assertEquals(vertex.getNormal().getX(),
                     clone.getNormal().getX(), EPSILON);
        assertEquals(vertex.getNormal().getY(),
                     clone.getNormal().getY(), EPSILON);
        assertEquals(vertex.getNormal().getZ(),
                     clone.getNormal().getZ(), EPSILON);
        assertEquals(vertex.getWorldPosition().getX(),
                     clone.getWorldPosition().getX(), EPSILON);
        assertEquals(vertex.getWorldPosition().getY(),
                     clone.getWorldPosition().getY(), EPSILON);
        assertEquals(vertex.getWorldPosition().getZ(),
                     clone.getWorldPosition().getZ(), EPSILON);
        assertNotSame(vertex.getColor(), clone.getColor());
        assertArrayEquals(vertex.getColor(), clone.getColor(), EPSILON);
        assertEquals(vertex.getAlpha(), clone.getAlpha(), EPSILON);
        assertEquals(vertex.getU(), clone.getU(), EPSILON);
        assertEquals(vertex.getV(), clone.getV(), EPSILON);
    }

    /**
     * Test that mutations to a clone do not affect the original Vertex.
     */
    @Test
    public void cloneValidCloneMutatedOriginalUnchanged() {
        final double[] color =
            {CLONE_COLOR_RED, CLONE_COLOR_GREEN, CLONE_COLOR_BLUE};
        final Vertex vertex = new Vertex(X_COORD, Y_COORD, DEPTH_VALUE,
                                         new Vector(NORMAL_X, NORMAL_Y, NORMAL_Z),
                                         new Vector(VECTOR_X_1, VECTOR_Y_1, VECTOR_Z_1),
                                         color, ALPHA_VALUE, U_VALUE, V_VALUE);
        final Vertex clone = vertex.clone();

        clone.setX(CLONE_X);
        clone.setDepth(CLONE_DEPTH);
        clone.setNormal(new Vector(NORMAL_Y, NORMAL_X, NORMAL_Y));
        clone.setWorldPosition(
            new Vector(CLONE_WORLD_X, CLONE_WORLD_Y, CLONE_WORLD_Z));
        clone.getColor()[X_INDEX] = MUTATED_COLOR;
        clone.setAlpha(CLONE_ALPHA);
        clone.setU(CLONE_U);
        clone.setV(CLONE_V);

        assertEquals(X_COORD, vertex.getX());
        assertEquals(DEPTH_VALUE, vertex.getDepth(), EPSILON);
        assertEquals(NORMAL_X, vertex.getNormal().getX(), EPSILON);
        assertEquals(VECTOR_X_1, vertex.getWorldPosition().getX(), EPSILON);
        assertEquals(CLONE_COLOR_RED, vertex.getColor()[X_INDEX], EPSILON);
        assertEquals(ALPHA_VALUE, vertex.getAlpha(), EPSILON);
        assertEquals(U_VALUE, vertex.getU(), EPSILON);
        assertEquals(V_VALUE, vertex.getV(), EPSILON);
    }

    /**
     * Test cloning a default Vertex.
     */
    @Test
    public void cloneDefaultVertexIndependentCopyReturned() {
        final Vertex vertex = new Vertex();
        final Vertex clone = vertex.clone();

        assertNotSame(vertex, clone);
        assertNotSame(vertex.getColor(), clone.getColor());
        assertArrayEquals(vertex.getColor(), clone.getColor(), EPSILON);
    }
}
