/*
 * @author: cdehais
 */

package renderer.algebra;

/**
 * Matrix class.
 */
public class Matrix {

    /**
     * The name of the matrix.
     */
    protected String name;

    /**
     * The matrix values.
     */
    protected final double[] values;

    /**
     * The number of rows.
     */
    protected final int nRows;

    /**
     * The number of columns.
     */
    protected final int nCols;

    /**
     * The default name of the matrix.
     */
    public static final String DEFAULT_NAME = "M";

    /**
     * Creates a named Matrix of size nRows x nCols.
     * @param name the name of the matrix
     * @param numRows number of rows
     * @param numCols number of columns
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public Matrix(final String name, final int numRows, final int numCols)
            throws IllegalArgumentException {
        if (numRows < 1 || numCols < 1) {
            throw new IllegalArgumentException(
                "Both matrix dimensions must be strictly positive");
        }
        final int size = numRows * numCols;
        this.values = new double[size];
        this.nRows = numRows;
        this.nCols = numCols;
        this.name = name;
    }

    /**
     * Creates a Matrix of size nRows x nCols.
     * @param nRows number of rows
     * @param nCols number of columns
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public Matrix(final int nRows, final int nCols) throws IllegalArgumentException {
        this(DEFAULT_NAME, nRows, nCols);
    }

    /**
     * Creates an identity matrix of the given size with name "I"+size (e.g., "I3"
     * for a 3x3 identity matrix).
     *
     * @param size the size of the identity matrix (number of rows and columns)
     * @return a new identity matrix of size {@code size x size}
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static final Matrix createIdentity(final int size)
            throws IllegalArgumentException {
        String name = "I" + size;
        return createIdentity(name, size);
    }

    /**
     * Creates an identity matrix of the given size and name.
     *
     * @param name the name of the matrix
     * @param size the size of the identity matrix (number of rows and columns)
     * @return a new identity matrix of size {@code size x size} with the given name
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static final Matrix createIdentity(final String name, final int size)
            throws IllegalArgumentException {
        Matrix id = new Matrix(name, size, size);

        for (int i = 0; i < size; i++) {
            id.values[size * i + i] = 1.0;
        }
        return id;
    }

    /**
     * Creates a random matrix of size nRows x nCols.
     * Each element is filled with a random value between 0.0 (inclusive)
     * and 1.0 (exclusive).
     *
     * @param name  the name of the matrix
     * @param nRows the number of rows (must be strictly positive)
     * @param nCols the number of columns (must be strictly positive)
     * @return a new {@code nRows x nCols} matrix filled with random values
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static Matrix createRandom(final String name, final int nRows, final int nCols)
            throws IllegalArgumentException {
        Matrix m = new Matrix(name, nRows, nCols);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                m.set(i, j, Math.random());
            }
        }
        return m;
    }

    /**
     * Extracts a submatrix of size {@code numRows x numCols} with top-left corner at
     * position {@code (offsetRow, offsetCol)}.
     * This method does NOT modify the current matrix.
     *
     * @param offsetRow the starting row index (0-based)
     * @param offsetCol the starting column index (0-based)
     * @param numRows the number of rows to extract (must be at least 1)
     * @param numCols the number of columns to extract (must be at least 1)
     * @return a new Matrix containing the extracted {@code numRows x numCols} submatrix
     * @throws IllegalArgumentException if the submatrix dimensions are invalid or extend
     * beyond the matrix bounds
     */
    public Matrix getSubMatrix(final int offsetRow, final int offsetCol,
            final int numRows, final int numCols)
            throws IllegalArgumentException {
        if ((offsetRow < 0) || (offsetCol < 0) || (numRows < 1) || (numCols < 1)
                || (offsetRow + numRows > this.nRows)
                || (offsetCol + numCols > this.nCols)) {
            throw new IllegalArgumentException("Invalid submatrix");
        }

        final Matrix sub = new Matrix(numRows, numCols);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sub.set(i, j, this.get(i + offsetRow, j + offsetCol));
            }
        }

        return sub;
    }

    /**
     * Get the transposed matrix.
     * This method does NOT modify the current matrix.
     *
     * @return a new Matrix that is the transpose of this matrix
     */
    public final Matrix transpose() {
        final Matrix trans = new Matrix(this.nCols, this.nRows);
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                trans.set(c, r, this.get(r, c));
            }
        }
        return trans;
    }

    /**
     * Matrix/Matrix multiplication.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param m the matrix to multiply with
     * @return a new Matrix containing the result of this * m
     * @throws SizeMismatchException if the matrix sizes do not match for multiplication
     */
    public final Matrix multiply(final Matrix m) throws SizeMismatchException {
        if (nCols != m.nRows) {
            throw new SizeMismatchException(this, m);
        }

        final Matrix res = new Matrix(this.nRows, m.nCols);

        for (int i = 0; i < res.nRows; i++) {
            for (int j = 0; j < res.nCols; j++) {
                for (int k = 0; k < this.nCols; k++) {
                    final double value = res.get(i, j) + this.get(i, k) * m.get(k, j);
                    res.set(i, j, value);
                }
            }
        }

        return res;
    }

    /**
     * Matrix/Vector multiplication.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param v the vector to multiply with
     * @return a new Vector containing the result of this * v
     * @throws SizeMismatchException if the matrix sizes do not match for multiplication
     */
    public final Vector multiply(final Vector v) throws SizeMismatchException {
        return new Vector(multiply((Matrix) v).values);
    }


    /**
     * Matrix/Matrix addition.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param m the matrix to add with
     * @return a new Matrix containing the result of this + m
     * @throws SizeMismatchException if the matrix sizes do not match for addition
     */
    public final Matrix add(final Matrix m) throws SizeMismatchException {
        if (nCols != m.nCols || nRows != m.nRows) {
            throw new SizeMismatchException(this, m);
        }

        final Matrix res = new Matrix(this.nRows, m.nCols);

        for (int i = 0; i < res.nRows; i++) {
            for (int j = 0; j < res.nCols; j++) {
                final double value = get(i, j) + m.get(i, j);
                res.set(i, j, value);
            }
        }
        return res;
    }

    /**
     * Matrix/Matrix subtraction : this - m.
     * This method does NOT modify the current matrix or the operand.
     *
     * @param m the matrix to subtract
     * @return a new Matrix containing the result of this - m
     * @throws SizeMismatchException if the matrix sizes do not match for subtraction
     */
    public final Matrix subtract(final Matrix m) throws SizeMismatchException {
        if (nCols != m.nCols || nRows != m.nRows) {
            throw new SizeMismatchException(name + " is " + nRows + "x" + nCols
                + " and " + m.name + " is " + m.nRows + "x" + m.nCols);
        }

        final Matrix res = new Matrix(this.nRows, m.nCols);

        for (int i = 0; i < res.nRows; i++) {
            for (int j = 0; j < res.nCols; j++) {
                final double value = get(i, j) - m.get(i, j);
                res.set(i, j, value);
            }
        }
        return res;
    }


    /**
     * Sets the element on row i and column j to the given value.
     * This method MODIFIES the current matrix.
     *
     * @param i the row index
     * @param j the column index
     * @param value the value to set
     */
    public void set(final int i, final int j, final double value) {
        values[i * nCols + j] = value;
    }

    /**
     * Sets the i-th column in the matrix to the given vector.
     * This method MODIFIES the current matrix.
     *
     * @param i the column index (0-based)
     * @param v the vector to set (its size must match the number of rows)
     * @throws IllegalArgumentException if the vector size does not match the number of
     * rows or if the column index is invalid
     */
    public void setCol(final int i, final Vector v) {
        // check if the vector has the right size
        if (v.size() != nRows) {
            throw new IllegalArgumentException("Vector size does not match matrix size");
        }
        // check if the column index is valid
        if (i < 0 || i >= nCols) {
            throw new IllegalArgumentException("Invalid column index");
        }
        // set the column
        for (int j = 0; j < nRows; j++) {
            values[j * nCols + i] = v.get(j);
        }
    }

    /**
     * Get the elements of the i-th column of the matrix as a vector.
     * This method does NOT modify the current matrix.
     *
     * @param i the column index
     * @return a new Vector containing the elements of column i
     * @throws IllegalArgumentException if the column index is invalid
     */
    public final Vector getCol(final int i) {
        // check if the column index is valid
        if (i < 0 || i >= nCols) {
            throw new IllegalArgumentException("Invalid column index");
        }
        // get the column
        final Vector v = new Vector(nRows);
        for (int j = 0; j < nRows; j++) {
            v.set(j, values[j * nCols + i]);
        }
        return v;
    }

    /**
     * Sets the i-th row in the matrix to the given vector.
     * This method MODIFIES the current matrix.
     *
     * @param i the row index (0-based)
     * @param v the vector to set (its size must match the number of columns)
     * @throws IllegalArgumentException if the vector size does not match the number of
     * columns or if the row index is invalid
     */
    public void setRow(final int i, final Vector v) {
        // check if the vector has the right size
        if (v.size() != nCols) {
            throw new IllegalArgumentException("Vector size does not match matrix size");
        }
        // check if the row index is valid
        if (i < 0 || i >= nRows) {
            throw new IllegalArgumentException("Invalid row index");
        }
        // set the row
        for (int j = 0; j < nCols; j++) {
            values[i * nCols + j] = v.get(j);
        }
    }

    /**
     * Get a copy of the elements of the i-th row of the matrix as a vector.
     * This method does NOT modify the current matrix.
     *
     * @param i the row index
     * @return a new Vector containing the elements of row i
     * @throws IllegalArgumentException if the row index is invalid
     */
    public final Vector getRow(final int i) {
        // check if the row index is valid
        if (i < 0 || i >= nRows) {
            throw new IllegalArgumentException("Invalid row index");
        }
        // get the row
        final Vector v = new Vector(nCols);
        for (int j = 0; j < nCols; j++) {
            v.set(j, values[i * nCols + j]);
        }
        return v;
    }

    /**
     * Gets the element at the specified position.
     *
     * @param i the row index (0-based)
     * @param j the column index (0-based)
     * @return the element at position {@code (i, j)}
     */
    public final double get(final int i, final int j) {
        return values[i * nCols + j];
    }

    /**
     * Sets the matrix name.
     * This method MODIFIES the current matrix.
     *
     * @param name the name of the matrix
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns a Matlab compatible representation of the Matrix.
     * @return the string representation of the matrix
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(name + " = [");

        int spacing = str.length();
        for (int i = 0; i < nRows; i++) {
            if (i > 0) {
                for (int j = 0; j < spacing; j++) {
                    str.append(" ");
                }
            }
            for (int j = 0; j < nCols; j++) {
                str.append(get(i, j) + " ");
            }
            str.append(";\n");
        }

        str.append("];");

        return str.toString();
    }

    /**
     * Returns the name of the matrix.
     * @return the name of the matrix
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of rows.
     * @return the number of rows
     */
    public final int getNRows() {
        return nRows;
    }

    /**
     * Returns the number of columns.
     * @return the number of columns
     */
    public final int getNCols() {
        return nCols;
    }

    /**
     * Multiplies the Matrix by the given constant.
     * This method does NOT modify the current matrix.
     *
     * @param f the constant to multiply the matrix by
     * @return a new Matrix containing the result of this * f
     */
    public Matrix scale(final double f) {
        final Matrix res = new Matrix(nRows, nCols);
        for (int i = 0; i < values.length; i++) {
            res.values[i] = values[i] * f;
        }
        return res;
    }

}
