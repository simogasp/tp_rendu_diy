/**
 * Basic linear algebra methods.
 * @author: cdehais
 */

package renderer.algebra;

/**
 * The Vector class represents a mathematical vector and provides methods to
 * manipulate it.
 */
public class Vector extends Matrix implements Cloneable {

    /**
     * The default name of the vector.
     */
    public static final String DEFAULT_NAME = "v";
    /**
     * The Number of column in a Vector.
     */
    public static final int NUMBER_COLUMN = 1;

    /**
     * The dimension of Z.
     */
    private static final int DIM_Z = 3;

    /**
     * Creates a random vector of the specified size.
     * Each element is filled with a random value between 0.0 (inclusive) and
     * 1.0 (exclusive).
     *
     * @param name  the name of the vector
     * @param nRows the size of the vector (must be strictly positive)
     * @return a new vector of size {@code nRows} filled with random values
     * @throws IllegalArgumentException if the vector dimension is invalid
     */
    public static Vector createRandom(final String name, final int nRows)
            throws IllegalArgumentException {
        final Vector v = new Vector(name, nRows);
        for (int i = 0; i < nRows; i++) {
            v.set(i, Math.random());
        }
        return v;
    }

    /**
     * Creates a named vector of the given size.
     *
     * @param vName  the name of the vector
     * @param length the size of the vector
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final String vName, final int length) {
        super(vName, length, NUMBER_COLUMN);
    }

    /**
     * Creates a vector of the given size.
     *
     * @param size the size of the vector
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final int size) {
        this(DEFAULT_NAME, size);
    }

    /**
     * Creates a vector from an array of values.
     *
     * @param initValues the array of values
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final double... initValues) {
        this(initValues.length);
        for (int j = 0; j < initValues.length; j++) {
            values[j] = initValues[j];
        }
    }

    /**
     * Creates a vector by copying another vector.
     *
     * @param v the vector to copy
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final Vector v) {
        this(v.size());
        for (int i = 0; i < v.size(); i++) {
            values[i] = v.values[i];
        }
    }

    /**
     * Creates a named vector from the given values.
     *
     * @param name   the name of the vector
     * @param values the values to initialize the vector with
     */
    public Vector(final String name, final double... values) {
        this(name, values.length);
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * Computes the Euclidean norm (L2 norm) of the vector.
     *
     * @return the Euclidean norm of the vector (always non-negative)
     */
    public final double norm() {
        double r = 0.0;

        for (int i = 0; i < nRows; i++) {
            r += this.values[i] * this.values[i];
        }

        return Math.sqrt(r);
    }

    /**
     * Returns a normalized vector.
     * This method does NOT modify the current vector.
     * If the norm is 0, returns a new zero vector of the same size.
     *
     * @return a new Vector that is the normalized version of this vector
     */
    public Vector normalize() {
        final double norm = norm();
        if (norm > 0) {
            return scale(1. / norm);
        } else {
            return new Vector(this.size());
        }
    }

    /**
     * Returns the homogeneous representation of the Vector.
     * This method does NOT modify the current vector.
     *
     * @return a new Vector with the same elements plus an additional 1.0 at the end
     */
    public Vector homogeneous() {
        double[] h = new double[nRows + 1];
        System.arraycopy(values, 0, h, 0, nRows);
        h[nRows] = 1.0;
        return new Vector(h);
    }

    /**
     * Computes the dot product (scalar product) between this vector and another vector.
     * Both vectors must have the same size.
     *
     * @param v the vector to compute the dot product with
     * @return the dot product (a scalar value)
     * @throws SizeMismatchException if the two vectors are not the same size
     */
    public double dot(final Vector v) {
        return transpose().multiply(v).get(0, 0);
    }

    /**
     * Returns a string representation of the vector in MATLAB-compatible format.
     * The format is: "name = [v0, v1, ..., vn]';"
     *
     * @return a MATLAB-compatible string representation of the vector
     */
    public String toString() {
        StringBuilder str = new StringBuilder(name + " = [");

        for (int i = 0; i < nRows - 1; i++) {
            str.append(values[i] + ", ");
        }

        str.append(values[nRows - 1] + "]';");

        return str.toString();
    }

    /**
     * Sets the i-th coordinate to the given value.
     * This method MODIFIES the current vector.
     *
     * @param i     the index of the coordinate to set
     * @param value the value to set the coordinate to
     */
    public void set(int i, double value) {
        values[i] = value;
    }

    /**
     * Sets all elements of the vector to 0.
     * This method MODIFIES the current vector.
     *
     * @return this vector (modified) filled with zeros
     */
    public Vector zeros() {
        for (int i = 0; i < nRows; i++) {
            values[i] = 0.0;
        }
        return this;
    }

    /**
     * Sets all elements of the vector to 1.
     * This method MODIFIES the current vector.
     *
     * @return this vector (modified) filled with ones
     */
    public Vector ones() {
        for (int i = 0; i < nRows; i++) {
            values[i] = 1.0;
        }
        return this;
    }

    /**
     * Gets the i-th coordinate of the Vector.
     *
     * @param i the index of the coordinate to get
     * @return the value of the i-th coordinate
     */
    public double get(int i) {
        return this.values[i];
    }

    /**
     * Returns the Vector size.
     *
     * @return the Vector size
     */
    public int size() {
        return nRows;
    }

    /**
     * Clamps the values of the Vector between the given minimum and maximum.
     * This method MODIFIES the current vector.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return this vector (modified) with clamped values
     */
    public Vector clamp(final double min, final double max) {
        for (int i = 0; i < nRows; i++) {
            values[i] = MathUtils.clamp(values[i], min, max);
        }
        return this;
    }

    /**
     * Vector/Vector subtraction : this - v.
     * This method does NOT modify the current vector or the operand.
     *
     * @param v the vector to subtract
     * @return a new Vector containing the result of this - v
     * @throws SizeMismatchException if the vector sizes do not match for subtraction
     */
    public Vector subtract(Vector v) {
        return new Vector(super.subtract(v).values);
    }

    /**
     * Vector scaling : f * this.
     * This method does NOT modify the current vector.
     *
     * @param f the scalar to multiply by
     * @return a new Vector containing the result of f * this
     */
    public Vector scale(double f) {
        return new Vector(super.scale(f).values);
    }

    /**
     * Vector/Vector addition : this + v.
     * This method does NOT modify the current vector or the operand.
     *
     * @param v the vector to add
     * @return a new Vector containing the result of this + v
     * @throws SizeMismatchException if the vector sizes do not match for addition
     */
    public Vector add(Vector v) {
        return new Vector(super.add(v).values);
    }

    /**
     * Returns the cross product : this × v.
     * This operation is only possible in dimension 3.
     * This method does NOT modify the current vector or the operand.
     *
     * @param v the vector to compute cross product with
     * @return a new Vector containing the result of this × v
     * @throws SizeMismatchException if the vector sizes do not match for cross product
     */
    public Vector cross(Vector v) {
        final Vector res = new Vector(3);
        res.set(0, this.get(1) * v.get(2) - this.get(2) * v.get(1));
        res.set(1, this.get(2) * v.get(0) - this.get(0) * v.get(2));
        res.set(2, this.get(0) * v.get(1) - this.get(1) * v.get(0));
        return res;
    }

    /**
     * Set the values of the vector.
     * This method MODIFIES the current vector.
     *
     * @param values the values (can be a double[] or doubles values separated by a comma)
     * @throws RuntimeException if the number of values does not match the vector size
     */
    public void set(double... values) {
        if (values.length != this.values.length) {
            throw new IllegalArgumentException("the number of values given has not "
                    + "the same number as the size of the vector.");
        }
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * Gets the X component (the first element) of the vector.
     *
     * @return the value at index 0
     * @throws IllegalArgumentException if the vector size is less than 1
     */
    public double getX() {
        if (size() < 1) {
            throw new IllegalArgumentException("There is no X component the size has"
                    + " to be more than 1 but here the size is " + size() + ".");
        }
        return values[0];
    }

    /**
     * Gets the Y component (the second element) of the vector.
     *
     * @return the value at index 1
     * @throws IllegalArgumentException if the vector size is less than 2
     */
    public double getY() {
        if (size() < 2) {
            throw new IllegalArgumentException("There is no Y component the size has"
                    + " to be more than 2 but here the size is " + size() + ".");
        }
        return values[1];
    }

    /**
     * Gets the Z component (the third element) of the vector.
     *
     * @return the value at index 2
     * @throws IllegalArgumentException if the vector size is less than 3
     */
    public double getZ() {
        if (size() < DIM_Z) {
            throw new IllegalArgumentException("There is no Z component the size has "
                    + "to be more than 3 but here the size is " + size() + ".");
        }
        return values[2];
    }

    /**
     * Creates and returns a copy of this vector.
     *
     * @return a new Vector that is a copy of this vector
     */
    @Override
    protected Vector clone() {
        return new Vector(this);
    }

    /**
     * Return a subvector from start with numberOfComponent components.
     * This method does NOT modify the current vector.
     *
     * @param start             the start index
     * @param numberOfComponent the number of components
     * @return a new Vector containing the extracted subvector
     */
    public Vector getSubVector(final int start, final int numberOfComponent) {
        final Matrix subVector = super.getSubMatrix(start, 0, numberOfComponent, 1);
        return new Vector(subVector.values);
    }

}
