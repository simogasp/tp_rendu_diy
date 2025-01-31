/*
 * @author: cdehais  
 */

package algebra;

/**
 * Matrix class
 */
public class Matrix {

    /**
     * The name of the matrix
     */
    public String name = "M";
    
    /**
     * The matrix values
     */
    protected double values[];

    /**
     * The number of rows
     */
    private int nRows;

    /**
     * The number of columns
     */
    private int nCols;

    /**
     * Default constructor
     */
    protected Matrix() {
    }

    /**
     * Creates a named Matrix
     * @param name the name of the matrix
     */
    protected Matrix(String name) {
        this.name = name;
    }

    /**
     * Creates a named Matrix of size nRows x nCols.
     * @param name the name of the matrix
     * @param nRows number of rows
     * @param nCols number of columns
     * @throws InstantiationException if the matrix dimensions are invalid
     */
    public Matrix(String name, int nRows, int nCols) throws java.lang.InstantiationException {
        this(nRows, nCols);
        this.name = name;
    }

    /**
     * Creates a Matrix of size nRows x nCols.
     * @param nRows number of rows
     * @param nCols number of columns
     * @throws InstantiationException if the matrix dimensions are invalid
     */
    public Matrix(int nRows, int nCols) throws java.lang.InstantiationException {
        allocValues(nRows, nCols);
    }

    /**
     * Creates an identity matrix of size @size and name "I"+size (e.g. I3 for a 3x3
     * identity matrix)
     * 
     * @param size the size of the identity matrix
     * @return the identity matrix of size @size
     * @throws InstantiationException if the matrix dimensions are invalid
     */
    public static Matrix createIdentity(int size) throws java.lang.InstantiationException {
        String name = "I" + size;
        return createIdentity(name, size);
    }

    /**
     * Creates an identity matrix of size @size
     * 
     * @param name the name of the matrix
     * @param size the size of the identity matrix
     * @return the identity matrix of size @size and name @name
     * @throws InstantiationException if the matrix dimensions are invalid
     */
    public static Matrix createIdentity(String name, int size) throws java.lang.InstantiationException {
        Matrix id = new Matrix(name, size, size);

        for (int i = 0; i < size; i++) {
            id.values[size * i + i] = 1.0;
        }
        return id;
    }

    /**
     * Create a random matrix of size nRows x nCols
     *
     * @param name  the name of the matrix
     * @param nRows number of rows
     * @param nCols number of columns
     * @return the nRows x nCols matrix named @name
     * @throws InstantiationException if the matrix dimensions are invalid
     */
    public static Matrix createRandom(String name, int nRows, int nCols) throws InstantiationException {
        Matrix M = new Matrix(name, nRows, nCols);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                M.set(i, j, Math.random());
            }
        }
        return M;
    }

    /**
     * Extracts a submatrix of size nRows x nCols with top left corner at
     * (offsetRow, offsetCol)
     * @param offsetRow the row offset
     * @param offsetCol the column offset
     * @param nRows the number of rows to extract
     * @param nCols the number of columns to extract
     * @return the submatrix of size nRows x nCols
     * @throws InstantiationException if the submatrix the dimensions are invalid
     */
    public Matrix getSubMatrix(int offsetRow, int offsetCol, int nRows, int nCols)
            throws InstantiationException {
        if ((offsetRow < 0) || (offsetCol < 0) || (nRows < 1) || (nCols < 1) ||
                (offsetRow + nRows > this.nRows) || (offsetCol + nCols > this.nCols)) {
            throw new InstantiationException("Invalid submatrix");
        }

        Matrix sub = new Matrix(nRows, nCols);

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                sub.set(i, j, this.get(offsetRow + i, offsetCol + j));
            }
        }

        return sub;
    }

    /**
     * Get the transposed matrix.
     * @return the transposed matrix
     */
    public Matrix transpose() {
        Matrix trans;
        try {
            trans = new Matrix(this.nCols, this.nRows);
        } catch (java.lang.InstantiationException e) {
            // unreached
            return null;
        }
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                trans.values[j * nRows + i] = this.values[i * nCols + j];
            }
        }
        return trans;
    }

    /**
     * Matrix/Matrix multiplication
     * @param M the matrix to multiply with
     * @return the resulting matrix
     * @throws SizeMismatchException if the matrix sizes do not match for multiplication
     */
    public Matrix multiply(Matrix M) throws SizeMismatchException {
        if (nCols != M.nRows) {
            throw new SizeMismatchException(this, M);
        }

        Matrix R;
        try {
            R = new Matrix(this.nRows, M.nCols);
        } catch (java.lang.InstantiationException e) {
            // unreached
            return null;
        }

        for (int i = 0; i < R.nRows; i++) {
            for (int j = 0; j < R.nCols; j++) {
                for (int k = 0; k < this.nCols; k++) {
                    R.values[i * R.nCols + j] += this.values[i * nCols + k] * M.values[k * M.nCols + j];
                }
            }
        }

        return R;
    }

    /**
     * Matrix/vector multiplication
     * @param v the vector to multiply with
     * @return the resulting vector
     * @throws SizeMismatchException if the vector size does not match the matrix column size
     */
    public Vector multiply(Vector v) throws SizeMismatchException {
        if (nCols != v.size()) {
            throw new SizeMismatchException(this, v);
        }

        Vector u = null;
        try {
            u = new Vector(nRows);
        } catch (java.lang.InstantiationException e) {
            // unreached
        }

        for (int i = 0; i < u.size(); i++) {
            double e = 0.0;
            for (int k = 0; k < this.nCols; k++) {
                e += values[i * nCols + k] * v.get(k);
            }
            u.set(i, e);
        }

        return u;
    }

    /**
     * Sets the element on row @i and column @j to the given value @value.
     * @param i the row index
     * @param j the column index
     * @param value the value to set
     */
    public void set(int i, int j, double value) {
        values[i * nCols + j] = value;
    }

    /**
     * Sets the i-th column of in the matrix to the given vector.
     * @param i the column index
     * @param v the vector to set
     */
    public void setCol(int i, Vector v) {
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
     * 
     * @param i the column
     * @return the vector
     * @throws InstantiationException if the vector cannot be created
     */
    public Vector getCol(int i) throws InstantiationException {
        // check if the column index is valid
        if (i < 0 || i >= nCols) {
            throw new IllegalArgumentException("Invalid column index");
        }
        // get the column
        Vector v = new Vector(nRows);
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
    public void setRow(int i, Vector v) {
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
     * @throws InstantiationException if the vector cannot be created
     */
    public Vector getRow(int i) throws InstantiationException {
        // check if the row index is valid
        if (i < 0 || i >= nRows) {
            throw new IllegalArgumentException("Invalid row index");
        }
        // get the row
        Vector v = new Vector(nCols);
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
    public double get(int i, int j) {
        return values[i * nCols + j];
    }

    /**
     * Sets the matrix name
     * @param name the name of the matrix
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a Matlab compatible representation of the Matrix.
     * @return the string representation of the matrix
     */
    public String toString() {
        String repr = name + " = [";
        int spacing = repr.length();
        for (int i = 0; i < nRows; i++) {
            if (i > 0) {
                for (int j = 0; j < spacing; j++) {
                    repr += " ";
                }
            }
            for (int j = 0; j < nCols; j++) {
                repr += values[nCols * i + j] + " ";
            }
            repr += ";\n";
        }

        repr += "];";

        return repr;
    }

    /**
     * Allocates the matrix values
     * @param nRows the number of rows
     * @param nCols the number of columns
     * @throws InstantiationException if the matrix dimensions are invalid
     */
    protected void allocValues(int nRows, int nCols) throws java.lang.InstantiationException {

        if (nRows < 1 || nCols < 1) {
            throw new java.lang.InstantiationException("Both matrix dimensions must be strictly positive");
        }
        int size = nRows * nCols;
        this.values = new double[size];
        this.nRows = nRows;
        this.nCols = nCols;
    }

    /**
     * Returns the name of the matrix
     * @return the name of the matrix
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of rows
     * @return the number of rows
     */
    public int nRows() {
        return nRows;
    }

    /**
     * Returns the number of columns
     * @return the number of columns
     */
    public int nCols() {
        return nCols;
    }

}
