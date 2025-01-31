
package algebra;

/**
 * Exception class for incorrect dimensions in arithmetic operations on Matrix
 * and Vector
 */
public class SizeMismatchException extends Exception {

    /**
     * Constructor for SizeMismatchException
     */
    public SizeMismatchException() { 
        super();
    }

    /**
     * Constructor for SizeMismatchException vectors
     * @param v1 first vector
     * @param v2 second vector
     */
    public SizeMismatchException(Vector v1, Vector v2) {
        super(v1.name + "[" + v1.size() + "] != " + v2.name + "[" + v2.size() + "]");
    }

    /**
     * Constructor for SizeMismatchException when using matrices
     * @param M1 first matrix
     * @param M2 second matrix
     */
    public SizeMismatchException(Matrix M1, Matrix M2) {
        super(M1.name + "[*," + M1.nCols() + "] != " + M2.name + "[" + M2.nRows() + ",*]");
    }

    /**
     * Constructor for SizeMismatchException when using matrices and vectors
     * @param M matrix
     * @param v vector
     */
    public SizeMismatchException(Matrix M, Vector v) {
        super(M.name + "[*," + M.nCols() + "] != " + v.name + "[" + v.size() + ",*]");
    }

    /**
     * Constructor for SizeMismatchException when using vectors and matrices
     * @param msg error message
     */
    public SizeMismatchException(String msg) {
        super(msg);
    }

}
