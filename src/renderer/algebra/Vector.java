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
     * Creates a random vector of size nRows x nCols.
     *
     * @param name  the name of the vector
     * @param nRows number of rows
     * @return the nRows vector named `name` filled with random values
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
     * Creates a vector by a copy of anoter vector.
     *
     * @param v the vector to copy.
     * @throws IllegalArgumentException if the size is not strictly positive
     */
    public Vector(final Vector v) {
        this(v.size());
        for (int i = 0; i < v.size(); i++) {
            values[i] = v.values[i];
        }
    }

    /**
     * Creates a vector from his name and values.
     *
     * @param name   the name of the vector
     * @param values the values
     */
    public Vector(final String name, final double... values) {
        this(name, values.length);
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * Compute the norm of the vector.
     *
     * @return the norm of the vector
     */
    public final double norm() {
        double r = 0.0;

        for (int i = 0; i < nRows; i++) {
            r += this.values[i] * this.values[i];
        }

        return Math.sqrt(r);
    }

    /**
     * Returns this Vector normalize. Be careful this method modify it.
     * If the norm is 0, returns 0 vector.
     *
     * @return the normalized vector
     */
    public Vector normalize() {
        final double norm = norm();
        if (norm > 0) {
            return scale(1. / norm());
        } else {
            return zeros();
        }
    }

    /**
     * Returns the homogeneous representation of the Vector.
     *
     * @return the same vector with an additional 1.0 at the end.
     */
    public Vector homogeneous() {
        double[] h = new double[nRows + 1];
        System.arraycopy(values, 0, h, 0, nRows);
        h[nRows] = 1.0;
        return new Vector(h);
    }

    /**
     * Computes the vector dot product between the Vector and another Vector.
     * Both must be the same size.
     *
     * @param v the Vector to compute the dot product with
     * @return the dot product of the two Vectors
     * @throws SizeMismatchException if the two Vectors are not the same size
     */
    public double dot(final Vector v) {
        return transpose().multiply(v).get(0, 0);
    }

    /**
     * Returns a string representation of the Vector.
     * Using Matlab compatible output for easy debugging.
     *
     * @return the string representation of the Vector
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
     * Sets the @i-th coordinate to the given value @value.
     *
     * @param i     the index of the coordinate to set
     * @param value the value to set the coordinate to
     */
    public void set(int i, double value) {
        values[i] = value;
    }

    /**
     * Sets all elements of the vector to 0 and return it.
     *
     * @return this vector full of 0
     */
    public Vector zeros() {
        for (int i = 0; i < nRows; i++) {
            values[i] = 0.0;
        }
        return this;
    }

    /**
     * Sets all elements of the vector to 1.
     *
     * @return this vector full of 1
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
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return this vector clamped
     */
    public Vector clamp(final double min, final double max) {
        for (int i = 0; i < nRows; i++) {
            values[i] = MathUtils.clamp(values[i], min, max);
        }
        return this;
    }

    /**
     * Vector/Vector subtraction : this - v.
     *
     * @param v the vector to subtract with
     * @return the resulting vector
     * @throws SizeMismatchException if the vector sizes do not match for
     *                               subtraction.
     */
    public Vector subtract(Vector v) {
        return new Vector(super.subtract(v).values);
    }

    /**
     * Vector scaling : fv.
     *
     * @param f the scalar
     * @return the resulting vector
     */
    public Vector scale(double f) {
        return new Vector(super.scale(f).values);
    }

    /**
     * Vector/Vector subtraction : this + v.
     *
     * @param v the vector to addition with
     * @return the resulting vector
     * @throws SizeMismatchException if the vector sizes do not match for
     *                               subtraction.
     */
    public Vector add(Vector v) {
        return new Vector(super.add(v).values);
    }

    /**
     * Returns the cross product : thix /\ v. this operation is only possible in
     * dimension 3.
     *
     * @param v the vector to cross product with
     * @return the resulting vector
     * @throws SizeMismatchException if the vector sizes do not match for cross
     *                               product.
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
     *
     * @param values the values (can be a double[] or doubles
     *               values separated by a comma).
     */
    public void set(double... values) {
        if (values.length != this.values.length) {
            throw new RuntimeException("the number of values given has not "
                    + "the same number as the size of the vector.");
        }
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * Gets the X components ie. the first values of a vector.
     *
     * @return the first values of a vector
     */
    public double getX() {
        if (size() < 1) {
            throw new RuntimeException("There is no X component the size has"
                    + " to be more than 1 but here the size is " + size() + ".");
        }
        return values[0];
    }

    /**
     * Gets the Y components ie. the second values of a vector.
     *
     * @return the second values of a vector
     */
    public double getY() {
        if (size() < 2) {
            throw new RuntimeException("There is no Y component the size has"
                    + " to be more than 2 but here the size is " + size() + ".");
        }
        return values[1];
    }

    /**
     * Gets the Z components ie. the third values of a vector.
     *
     * @return the third values of a vector
     */
    public double getZ() {
        if (size() < 3) {
            throw new RuntimeException("There is no Z component the size has "
                    + "to be more than 3 but here the size is " + size() + ".");
        }
        return values[2];
    }

    @Override
    protected Vector clone() throws CloneNotSupportedException {
        return new Vector(this);
    }

    /**
     * Return The subvector from start with numberOfComponent components.
     *
     * @param start             the start index
     * @param numberOfComponent the number of component
     * @return the subvector
     */
    public Vector getSubVector(final int start, final int numberOfComponent) {
        final Matrix subVector = super.getSubMatrix(start, 0, numberOfComponent, 1);
        return new Vector(subVector.values);
    }

}
