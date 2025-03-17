package unit.algebra;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;


public class TestMatrix {

    /**
     * The epsilon for double comparison.
     */
    private static final double EPSILON = 0.001;

    /**
     * The name for test matrices.
     */
    private static final String TEST_MATRIX_NAME = "testMatrix";

    /**
     * The name for invalid matrices.
     */
    private static final String INVALID_MATRIX_NAME = "invalidMatrix";

    /**
     * Test the creation of a Matrix.
     */
    @Test
    public void testMatrixCreation() {
        final int numRows = 3;
        final int numCols = 3;
        final int numCols2 = 2;
        Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        assertNotNull(m);
        assertEquals(TEST_MATRIX_NAME, m.getName());
        assertEquals(numRows, m.nRows());
        assertEquals(numCols, m.nCols());

        Matrix m2 = new Matrix("rectMatrix", numRows, numCols2);
        assertNotNull(m2);
        assertEquals("rectMatrix", m2.getName());
        assertEquals(numRows, m2.nRows());
        assertEquals(numCols2, m2.nCols());
    }

    /**
     * Test the creation of an invalid Matrix.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMatrixCreation() throws IllegalArgumentException {
        final int numCols = 3;
        new Matrix(INVALID_MATRIX_NAME, 0, numCols);
    }

    /**
     * Test the creation of an invalid Matrix.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNegativeSizesMatrixCreation() throws IllegalArgumentException {
        final int negativeRows = -5;
        final int negativeCols = -4;
        new Matrix(INVALID_MATRIX_NAME, negativeRows, negativeCols);
    }

    /**
     * Test the creation of an invalid Matrix with negative rows.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSingleNegativeSizesMatrixCreation1()
            throws IllegalArgumentException {
        final int negativeRows = -5;
        final int positiveCols = 4;
        new Matrix(INVALID_MATRIX_NAME, negativeRows, positiveCols);
    }

    /**
     * Test the creation of an invalid Matrix with negative cols.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSingleNegativeSizesMatrixCreation2()
            throws IllegalArgumentException {
        final int numCols = 2;
        final int negativeCols = -2;
        new Matrix(TEST_MATRIX_NAME, numCols, negativeCols);
    }

    /**
     * Test the set and get methods of a Matrix.
     * @throws IllegalArgumentException
     */
    @Test
    public void testSetAndGets() throws IllegalArgumentException {
        final int numRows = 2;
        final int numCols = 3;
        Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                m.set(i, j, value);
                assertEquals(value, m.get(i, j), EPSILON);
                value += 1.0;
            }
        }
    }

    /**
     * Test the set method with an invalid indices.
     * @throws IndexOutOfBoundsException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetInvalidValue() {
        Matrix m = new Matrix(TEST_MATRIX_NAME, 2, 2);
        m.set(2, 2, .0);
    }

    /**
     * Test the get method with an invalid indices.
     * @throws IndexOutOfBoundsException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInvalidValue() {
        Matrix m = new Matrix(TEST_MATRIX_NAME, 2, 2);
        m.get(2, 2);
    }

    /**
     * Test the transpose of a square matrix.
     */
    @Test
    public void testTransposeSquareMatrix() {
        final int numRows = 4;
        final int numCols = 4;
        Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                m.set(i, j, value++);
            }
        }
        Matrix transposed = m.transpose();
        value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                assertEquals(value++, transposed.get(j, i), EPSILON);
            }
        }
    }

    /**
     * Test the transpose of a rectangular matrix.
     */
    @Test
    public void testTransposeRectangularMatrix() {
        final int numRows = 3;
        final int numCols = 4;
        Matrix m = new Matrix(TEST_MATRIX_NAME, numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
            m.set(i, j, value++);
            }
        }
        Matrix transposed = m.transpose();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
            assertEquals(m.get(i, j), transposed.get(j, i), EPSILON);
            }
        }
    }

    /**
     * Test the multiplication of two square matrices.
     * @throws SizeMismatchException
     */
    @Test
    public void testMultiplySquareMatrices() {
        Matrix m1 = new Matrix("m1", 2, 2);
        m1.set(0, 0, 1.0);
        m1.set(0, 1, 2.0);
        m1.set(1, 0, 3.0);
        m1.set(1, 1, 4.0);

        Matrix m2 = new Matrix("m2", 2, 2);
        m2.set(0, 0, 2.0);
        m2.set(0, 1, 0.0);
        m2.set(1, 0, 1.0);
        m2.set(1, 1, 2.0);

        Matrix result = m1.multiply(m2);
        assertEquals(4.0, result.get(0, 0), EPSILON);
        assertEquals(4.0, result.get(0, 1), EPSILON);
        assertEquals(10.0, result.get(1, 0), EPSILON);
        assertEquals(8.0, result.get(1, 1), EPSILON);
    }

    /**
     * Test the multiplication of two rectangular matrices.
     * @throws SizeMismatchException
     */
    @Test
    public void testMultiplyRectangularMatrices() {
        Matrix m1 = new Matrix("m1", 2, 3);
        m1.set(0, 0, 1.0);
        m1.set(0, 1, 2.0);
        m1.set(0, 2, 3.0);
        m1.set(1, 0, 4.0);
        m1.set(1, 1, 5.0);
        m1.set(1, 2, 6.0);

        Matrix m2 = new Matrix("m2", 3, 2);
        m2.set(0, 0, 7.0);
        m2.set(0, 1, 8.0);
        m2.set(1, 0, 9.0);
        m2.set(1, 1, 10.0);
        m2.set(2, 0, 11.0);
        m2.set(2, 1, 12.0);

        Matrix result = m1.multiply(m2);
        final double[][] expectedValues = {
            {58.0, 64.0},
            {139.0, 154.0}
        };

        for (int i = 0; i < expectedValues.length; i++) {
            for (int j = 0; j < expectedValues[i].length; j++) {
                assertEquals(expectedValues[i][j], result.get(i, j), EPSILON);
            }
        }
    }

    /**
     * Test the multiplication of a matrices with wrong sizes.
     * @throws SizeMismatchException
     */
    @Test(expected = SizeMismatchException.class)
    public void testMultiplyInvalidMatrices() {
        Matrix m1 = new Matrix("m1", 2, 2);
        Matrix m2 = new Matrix("m2", 3, 3);
        m1.multiply(m2);
    }

    /**
     * Test the identity matrix.
     * @throws SizeMismatchException
     */
    @Test
    public void testIdentityMatrix() {
        final int identitySize = 3;
        Matrix m = Matrix.createIdentity("identity", identitySize);
        for (int i = 0; i < identitySize; i++) {
            for (int j = 0; j < identitySize; j++) {
                if (i == j) {
                    assertEquals(1.0, m.get(i, j), EPSILON);
                } else {
                    assertEquals(0.0, m.get(i, j), EPSILON);
                }
            }
        }
    }

    /**
     * Test setting the column values.
     */
    @Test
    public void testSetColValid() {
        final int sizeRow = 5;
        final int sizeCol = 3;
        final int colToSet = 1;
        Matrix matrix = Matrix.createRandom(TEST_MATRIX_NAME, sizeRow, sizeCol);
        Vector vector = new Vector(sizeRow);
        // Set vector values
        for (int i = 0; i < sizeRow; i++) {
            vector.set(i, i + 1.);
        }

        matrix.setCol(colToSet, vector);

        for (int i = 0; i < sizeRow; i++) {
            assertEquals(i + 1., matrix.get(i, colToSet), EPSILON);
        }
    }

    /**
     * Test setting the column values with an invalid vector size.
     * @throws IllegalArgumentException
     */
    @Test
    public void testSetColInvalidVectorSize() {
        final int sizeMat = 3;
        Matrix matrix = new Matrix(sizeMat, sizeMat);
        Vector vector = new Vector(2); // Invalid size

        assertThrows(IllegalArgumentException.class, () ->
            matrix.setCol(1, vector));
    }

    /**
     * Test setting the column values with column.
     */
    @Test
    public void testGetColValidIndex() {
        final int numRows = 3;
        final int numCols = 3;
        Matrix matrix = new Matrix(numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix.set(i, j, value++);
            }
        }

        for (int j = 0; j < numCols; j++) {
            Vector col = matrix.getCol(j);
            for (int i = 0; i < numRows; i++) {
                assertEquals((i * numCols) + (j + 1), col.get(i), EPSILON);
            }
        }
    }

    /**
     * Test getting the column values with an invalid index.
     */
    @Test
    public void testGetColInvalidIndexNegative() {
        final int numRows = 3;
        final int numCols = 3;
        Matrix matrix;
        matrix = new Matrix(numRows, numCols);
        assertThrows(IllegalArgumentException.class, () -> matrix.getCol(-1));
    }

    /**
     * Test getting the column values with an invalid index.
     */
    @Test
    public void testGetColInvalidIndexTooLarge() {
        final int squareMat = 3;
        final int invalidColNegative = -4;
        Matrix matrix = new Matrix(squareMat, squareMat);
        assertThrows(IllegalArgumentException.class, () ->
            matrix.getCol(squareMat));
        assertThrows(IllegalArgumentException.class, () ->
            matrix.getCol(invalidColNegative));
    }

    /**
     * Test setting the column values with an invalid index.
     */
    @Test
    public void testSetColInvalidColumnIndex()  {
        Matrix matrix;
        final int numRows = 3;
        final int numCols = 3;
        final int invalidColNegative = -1;
        final int invalidColPositive = 3;
        matrix = new Matrix(numRows, numCols);

        Vector vector = new Vector(numRows);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setCol(invalidColNegative, vector);
        });

        assertEquals("Invalid column index", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setCol(invalidColPositive, vector);
        });

        assertEquals("Invalid column index", exception.getMessage());
    }

    /**
     * Test setting the row values.
     */
    @Test
    public void testSetRowValid() {
        final int numRows = 3;
        final int numCols = 5;
        final int rowToSet = 2;
        Matrix matrix = Matrix.createRandom("test", numRows, numCols);
        Vector vector = new Vector(numCols);
        // set vector values
        for (int i = 0; i < numCols; i++) {
            vector.set(i, i + 1.);
        }

        matrix.setRow(rowToSet, vector);

        for (int i = 0; i < numCols; i++) {
            assertEquals(i + 1., matrix.get(rowToSet, i), EPSILON);
        }
    }

    /**
     * Test setting the row values with an invalid vector size.
     */
    @Test
    public void testSetRowInvalidSize() {
        final int numRows = 3;
        final int numCols = 3;
        Matrix matrix = new Matrix(numRows, numCols);
        Vector vector = new Vector(2); // Invalid size
        vector.set(0, 1.0);
        vector.set(1, 2.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setRow(1, vector);
        });

        assertEquals("Vector size does not match matrix size", exception.getMessage());
    }

    /**
     * Test setting the row values with an invalid index.
     */
    @Test
    public void testSetRowInvalidIndex() {
        final int numRows = 3;
        final int numCols = 3;
        final int invalidRowNegative = -1;
        final int invalidRowPositive = 3;
        Matrix matrix = new Matrix(numRows, numCols);
        Vector vector = new Vector(numCols);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setRow(invalidRowNegative, vector);
        });

        assertEquals("Invalid row index", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            matrix.setRow(invalidRowPositive, vector);
        });

        assertEquals("Invalid row index", exception.getMessage());
    }

    /**
     * Test getting the row values with an invalid index.
     */
    @Test
    public void testGetRowValidIndex() {
        final int numRows = 3;
        final int numCols = 4;
        Matrix matrix;
        matrix = new Matrix(numRows, numCols);
        double value = 1.0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix.set(i, j, value++);
            }
        }

        for (int i = 0; i < numRows; i++) {
            Vector row = matrix.getRow(i);
            assertEquals(numCols, row.size());
            for (int j = 0; j < numCols; j++) {
                assertEquals((double) (i * numCols) + (j + 1), row.get(j), EPSILON);
            }
        }
    }

    /**
     * Test getting the row values with an invalid index.
     */
    @Test
    public void testGetRowInvalidIndexNegative() {
        final int numRows = 3;
        final int numCols = 3;
        Matrix matrix;
        matrix = new Matrix(numRows, numCols);
        assertThrows(IllegalArgumentException.class, () -> matrix.getRow(-1));

    }

    /**
     * Test getting the row values with an invalid index.
     */
    @Test
    public void testGetRowInvalidIndexTooLarge() {
        final int numRows = 3;
        final int numCols = 3;
        final int invalidRowNegative = -3;
        final int invalidRowPositive = 3;
        Matrix matrix;
        matrix = new Matrix(numRows, numCols);
        assertThrows(IllegalArgumentException.class, ()
            -> matrix.getRow(invalidRowPositive));
        assertThrows(IllegalArgumentException.class, ()
            -> matrix.getRow(invalidRowNegative));
    }

}
