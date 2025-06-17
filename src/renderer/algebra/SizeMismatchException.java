
package renderer.algebra;

/**
 * Exception class for incorrect dimensions in arithmetic operations on Matrix
 * and Vector.
 */
public class SizeMismatchException extends RuntimeException {

    /**
     * Constructor for SizeMismatchException.
     */
    public SizeMismatchException() {
        super();
    }

    /**
     * Constructor for SizeMismatchException vectors.
     * @param v1 first vector
     * @param v2 second vector
     */
    public SizeMismatchException(Vector v1, Vector v2) {
        super(v1.getName() + "[" + v1.size()
                + "] != " + v2.getName() + "[" + v2.size() + "]");
    }

    /**
     * Constructor for SizeMismatchException when using matrices.
     * @param m1 first matrix
     * @param m2 second matrix
     */
    public SizeMismatchException(Matrix m1, Matrix m2) {
        super(m1.getName() + "[*," + m1.nCols + "] != "
            + m2.getName() + "[" + m2.nRows + ",*]");
    }

    /**
     * Constructor for SizeMismatchException when using matrices and vectors.
     * @param m matrix
     * @param v vector
     */
    public SizeMismatchException(Matrix m, Vector v) {
        super(m.getName() + "[*," + m.nCols + "] != "
            + v.getName() + "[" + v.size() + ",*]");
    }

    /**
     * Constructor for SizeMismatchException when using vectors and matrices.
     * @param msg error message
     */
    public SizeMismatchException(String msg) {
        super(msg);
    }

}
