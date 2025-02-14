/**
 * Basic linear algebra methods
 * @author: cdehais
 */

package renderer.algebra;

/**
 * The Vector class represents a mathematical vector and provides methods to
 * manipulate it.
 */
public class Vector implements Cloneable {

    /**
     * The default name of the vector.
     */
    public static final String DEFAULT_NAME = "v";

    /**
     * The size of the vector.
     */
    protected int size;
    /**
     * The values of the vector.
     */
    protected double values[];
    /**
     * The name of the vector.
     */
    private String name = DEFAULT_NAME;

    /**
     * Default constructor.
     */
    protected Vector() {
    }

    /**
     * Creates a named vector of the given size.
     * @param name the name of the vector
     * @param size the size of the vector
     * @throws java.lang.InstantiationException if the size is not strictly positive
     */
    public Vector(String name, int size) throws java.lang.InstantiationException {
        this(size);
        this.name = name;
    }

    /**
     * Creates a vector of the given size.
     * @param size the size of the vector
     * @throws java.lang.InstantiationException if the size is not strictly positive
     */
    public Vector(int size) throws java.lang.InstantiationException {
        allocValues(size);
    }

    /**
     * Creates a vector from an array of values.
     * @param values the array of values
     * @throws java.lang.InstantiationException if the size is not strictly positive
     */
    public Vector(double[] values) throws java.lang.InstantiationException {
        allocValues(values.length);
        System.arraycopy(values, 0, this.values, 0, size);
    }

    /**
     * Compute the norm of the vector.
     * @return the norm of the vector
     */
    public double norm() {
        double r = 0.0;

        for (int i = 0; i < this.size; i++) {
            r += this.values[i] * this.values[i];
        }

        return Math.sqrt(r);
    }

    /**
     * Makes the Vector unitary.
     * If the norm is 0, does nothing.
     * Otherwise, divides each element by the norm.
     */
    public void normalize() {
        double norm = norm();

        for (int i = 0; i < size; i++) {
            values[i] /= norm;
        }
    }

    /**
     * Multiplies the Vector by the given constant.
     * @param f the constant to multiply the Vector by
     */
    public void scale(double f) {
        for (int i = 0; i < size; i++) {
            values[i] *= f;
        }
    }

    /**
     * Computes the vector dot product between the Vector and another Vector.
     * Both must be the same size.
     * @param v the Vector to compute the dot product with
     * @return the dot product of the two Vectors
     * @throws SizeMismatchException if the two Vectors are not the same size
     */
    public double dot(Vector v) throws SizeMismatchException {
        if (size != v.size) {
            throw new SizeMismatchException(this, v);
        }

        double d = 0.0;

        for (int i = 0; i < size; i++) {
            d += this.values[i] * v.values[i];
        }

        return d;
    }

    /**
     * Adds the given Vector to the Vector.
     * @param v the Vector to add
     * @throws SizeMismatchException if the two Vectors are not the same size
     */
    public void add(Vector v) throws SizeMismatchException {
        if (size != v.size) {
            throw new SizeMismatchException(this, v);
        }

        for (int i = 0; i < size; i++) {
            values[i] += v.values[i];
        }
    }

    /**
     * Subtracts the given Vector to the Vector.
     * @param v the Vector to subtract
     * @throws SizeMismatchException if the two Vectors are not the same size
     */
    public void subtract(Vector v) throws SizeMismatchException {
        if (size != v.size) {
            throw new SizeMismatchException(this, v);
        }

        for (int i = 0; i < size; i++) {
            values[i] -= v.values[i];
        }
    }

    /**
     * Returns a string representation of the Vector.
     * Using Matlab compatible output for easy debugging.
     * @return the string representation of the Vector
     */
    public String toString() {
        StringBuilder str = new StringBuilder(name + " = [");

        for (int i = 0; i < size - 1; i++) {
            str.append(values[i] + ", ");
        }

        str.append(values[size - 1] + "]';");

        return str.toString();
    }

    /**
     * Sets the name of the Vector.
     * @param name the name of the Vector
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the Vector's name.
     * @return the Vector's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the @i-th coordinate to the given value @value.
     * @param i the index of the coordinate to set
     * @param value the value to set the coordinate to
     */
    public void set(int i, double value) {
        this.values[i] = value;
    }

    /**
     * Sets the values of the vector to the values contained in the given array.
     * @param valuesList the array of values to set the vector to
     * @throws SizeMismatchException if the size of the array is different from
     * the size of the vector
     */
    public void set(double[] valuesList) throws SizeMismatchException {
        if (valuesList.length != this.size) {
            throw new SizeMismatchException("Bad size");
        }
        this.values = valuesList;
    }

    /**
     * Sets all elements of the vector to 0.
     */
    public void zeros() {
        for (int i = 0; i < size; i++) {
            values[i] = 0.0;
        }
    }

    /**
     * Sets all elements of the vector to 1.
     */
    public void ones() {
        for (int i = 0; i < size; i++) {
            values[i] = 1.0;
        }
    }

    /**
     * Gets the i-th coordinate of the Vector.
     * @param i the index of the coordinate to get
     * @return the value of the i-th coordinate
     */
    public double get(int i) {
        return this.values[i];
    }

    /**
     * Returns the Vector size.
     * @return the Vector size
     */
    public int size() {
        return this.size;
    }

    /**
     * Allocates the values array of the given size.
     * @param length the size of the values array
     * @throws java.lang.InstantiationException if the size is not strictly greater than 0
     */
    protected void allocValues(int length) throws java.lang.InstantiationException {
        if (length < 1) {
            throw new java.lang.InstantiationException(
                "Vector size must be strictly positive");
        }
        this.values = new double[length];
        this.size = length;
    }
}
