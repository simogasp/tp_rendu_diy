/**
 * @author: cdehais
 */

package algebra;

/**
 * The Vector3 class represents a 3D vector and provides methods to manipulate it.
 */
public class Vector3 extends Vector {
    /**
     * The size of the vector.
     */
    private static final int VECTOR_SIZE = 3;

    /**
     * Creates a new 3D vector with coordinates (x, y, z).
     * @param x the x coordinate of the vector
     * @param y the y coordinate of the vector
     * @param z the z coordinate of the vector
     */
    public Vector3(double x, double y, double z) {
        super();
        try {
            allocValues(VECTOR_SIZE);
        } catch (java.lang.InstantiationException e) {
            // unreached
        }
        this.values[0] = x;
        this.values[1] = y;
        this.values[2] = z;
    }

    /**
     * Creates a new 3D vector with coordinates (0, 0, 0).
     */
    public Vector3() {
        this(0.0, 0.0, 0.0);
    }

    /**
     * Creates a new named 3D vector with coordinates (0, 0, 0).
     * @param name the name of the vector
     */
    public Vector3(String name) {
        this(0.0, 0.0, 0.0);
        this.setName(name);
    }

    /**
     * Creates a new named 3D vector with coordinates (x, y, z).
     * @param name the name of the vector
     * @param x the x coordinate of the vector
     * @param y the y coordinate of the vector
     * @param z the z coordinate of the vector
     * @fixme: this constructor is weird, why size 4?
     */
    public Vector3(String name, double x, double y, double z) {
        super();
        try {
            allocValues(4);
        } catch (java.lang.InstantiationException e) {
            // unreached
        }

        this.values[0] = x;
        this.values[1] = y;
        this.values[2] = z;
    }

    /**
     * Copy constructor from a Vector of size 3 or 4.
     * For a vector of size 4, divide the 3 first coordinates by the fourth.
     * @param v the vector to copy
     * @throws InstantiationException if the size of the vector is not 3 or 4
     */
    public Vector3(Vector v) throws InstantiationException {
        this();
        if ((v.size != VECTOR_SIZE) && (v.size != VECTOR_SIZE + 1)) {
            throw new InstantiationException(
                "Can only build 3D vector from vector of size 3 or 4");
        }

        if (v.size == VECTOR_SIZE) {
            set(v.get(0), v.get(1), v.get(2));
        } else {
            double w = v.get(3);
            set(v.get(0) / w, v.get(1) / w, v.get(2) / w);
        }
    }

    // /**
    //  * Makes the x, y, and z coordinates of the Vector3 cartesian, by dividing them.
    //  * by the homogeneous coordinate w.
    //  * @throws java.lang.ArithmeticException if w is 0
    //  */
    // public void makeCartesian() throws java.lang.ArithmeticException {
    //     this.values[0] /= this.values[3];
    //     this.values[1] /= this.values[3];
    //     this.values[2] /= this.values[3];
    //     this.values[3] = 1.0;
    // }

    /**
     * Sets the x, y, and z coordinates of the Vector3 to the given values.
     * @param x the x coordinate of the vector
     * @param y the y coordinate of the vector
     * @param z the z coordinate of the vector
     */
    public void set(double x, double y, double z) {
        this.values[0] = x;
        this.values[1] = y;
        this.values[2] = z;
    }

    /**
     * Computes the cross product between the Vector3 and the given vector.
     * @param v the vector to compute the cross product with
     * @return the cross product of the two vectors
     */
    public Vector3 cross(Vector3 v) {
        double rx = this.getY() * v.getZ() - this.getZ() * v.getY();
        double ry = this.getZ() * v.getX() - this.getX() * v.getZ();
        double rz = this.getX() * v.getY() - this.getY() * v.getX();

        return new Vector3(rx, ry, rz);
    }

    /**
     * Computes the dot product between the Vector3 and the given vector.
     * @param v the vector to compute the dot product with
     * @return the dot product of the two vectors
     */
    public double dot(Vector3 v) {
        return (values[0] * v.values[0]
                + values[1] * v.values[1]
                + values[2] * v.values[2]);
    }

    /**
     * Computes the norm of the Vector3.
     * @return the norm of the Vector3
     */
    @Override
    public double norm() {
        final double r = (Math.pow(values[0], 2)
                        + Math.pow(values[1], 2)
                        + Math.pow(values[2], 2));
        return Math.sqrt(r);
    }

    /**
     * Gets the x coordinates of the Vector3.
     * @return the x coordinates of the Vector3
     */
    public double getX() {
        return this.values[0];
    }

    /**
     * Gets the w coordinates of the Vector3.
     * @return the y coordinates of the Vector3
     */
    public double getY() {
        return this.values[1];
    }

    /**
     * Gets the z coordinates of the Vector3.
     * @return the z coordinates of the Vector3
     */
    public double getZ() {
        return this.values[2];
    }
}
