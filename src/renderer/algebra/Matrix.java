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
    public Matrix(int nRows, int nCols) throws IllegalArgumentException {
        this(DEFAULT_NAME, nRows, nCols);
    }

    /**
     * Creates an identity matrix of size @size and name "I"+size (e.g. I3 for a 3x3
     * identity matrix)
     * @param size the size of the identity matrix
     * @return the identity matrix of size @size
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static final Matrix createIdentity(int size) 
            throws IllegalArgumentException {
        String name = "I" + size;
        return createIdentity(name, size);
    }

    /**
     * Creates an identity matrix of the given size.
     * @param name the name of the matrix
     * @param size the size of the identity matrix
     * @return the identity matrix of size @size and name @name
     * @throws IllegalArgumentException if the matrix dimensions are invalid
     */
    public static final Matrix createIdentity(String name, int size) 
            throws IllegalArgumentException {
        Matrix id = new Matrix(name, size, size);

        for (int i = 0; i < size; i++) {
            id.values[size * i + i] = 1.0;
        }
        return id;
    }

    /**
     * Create a random matrix of size nRows x nCols.
     *
     * @param name  the name of the matrix
     * @param nRows number of rows
     * @param nCols number of columns
     * @return the nRows x nCols matrix named `name` filled with random values
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
     * Extracts a submatrix of size nRows x nCols with top left corner at
     * (offsetRow, offsetCol).
     * @param offsetRow the row offset
     * @param offsetCol the column offset
     * @param numRows the number of rows to extract
     * @param numCols the number of columns to extract
     * @return the submatrix of size nRows x nCols
     * @throws IllegalArgumentException if the submatrix the dimensions are invalid
     */
    public Matrix getSubMatrix(final int offsetRow, final int offsetCol, final int numRows, final int numCols) 
            throws IllegalArgumentException {
        if ((offsetRow < 0) || (offsetCol < 0) || (numRows < 1) || (numCols < 1)
                || (offsetRow + numRows > this.nRows)
                || (offsetCol + numCols > this.nCols)) {
            throw new IllegalArgumentException("Invalid submatrix");
        }

        final Matrix sub = new Matrix(numRows, numCols);

        final int endXDim = offsetCol + numCols - 1;
        final int endYDim = offsetRow + numRows - 1;


        for (int i = offsetRow; i < endYDim; i++) {
            for (int j = offsetCol; j < endXDim; j++) {
                sub.set(i, j, this.get(i, j));
            }
        }

        return sub;
    }

    /**
     * Get the transposed matrix.
     * @return the transposed matrix
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
     * @param m the matrix to multiply with
     * @return the resulting matrix
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
     * @param m the vector to multiply with
     * @return the resulting matrix
     * @throws SizeMismatchException if the matrix sizes do not match for multiplication
     */
    public final Vector multiply(final Vector v) throws SizeMismatchException {
        return new Vector(multiply((Matrix) v). values);
    }


    /**
     * Matrix/Matrix addition.
     * @param m the matrix to addition with
     * @return the resulting matrix
     * @throws SizeMismatchException if the matrix sizes do not match for multiplication
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
     * Matrix/Matrix subtraction : this - other.
     * @param m the matrix to subtract with
     * @return the resulting matrix
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
     * Sets the element on row @i and column @j to the given value @value.
     * @param i the row index
     * @param j the column index
     * @param value the value to set
     */
    public void set(final int i, final int j, final double value) {
        values[i * nCols + j] = value;
    }

    /**
     * Sets the i-th column of in the matrix to the given vector.
     * @param i the column index
     * @param v the vector to set
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
     * @param i the column
     * @return the vector
     * @throws IllegalArgumentException if the vector cannot be created
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
     * Sets the i-th row of in the matrix to the given vector.
     * @param i the row index
     * @param v the vector to set
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
     * Get the elements of the i-th row of the matrix as a vector.
     * @param i the row
     * @return the vector
     * @throws IllegalArgumentException if the vector cannot be created
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
     * Gets the element on row i and column j.
     * @param i the row index
     * @param j the column index
     * @return the element at row i and column j
     */
    public final double get(final int i, final int j) {
        return values[i * nCols + j];
    }

    /**
     * Sets the matrix name.
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
     * @param f the constant to multiply the Vector by
     */
    public void scale(double f) {
        for (int i = 0; i < values.length; i++) {
            values[i] *= f;
        }
    }

}
