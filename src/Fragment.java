
import java.awt.Color;

import algebra.Vector;
import algebra.Vector3;

/**
 * The Fragment class represents an attributed 'pixel' as generated
 * by a Rasterizer.
 * @author cdehais
 */
public class Fragment {

    /**
     * The pixel x-coordinate of the Fragment.
     */
    private int x;
    /**
     * The pixel y-coordinate of the Fragment.
     */
    private int y;
    /**
     * The number of attributes of the Fragment.
     */
    private int numAttributes;

    /** The number of attributes of the Fragment. */
    private static final int NUM_ATTRIBUTES = 9;

    /**
     * attributes placement:
     * 0: depth
     * 1-3: color
     * 4-6: normal
     * 7-8: (u,v) texture coordinates.
     */
    private double[] attributes;

    // Attribute indices
    /** the depth. */
    private static final int DEPTH = 0;
    /** the red component of the color. */
    private static final int COLOR_R = 1;
    /** the green component of the color. */
    private static final int COLOR_G = 2;
    /** the blue component of the color. */
    private static final int COLOR_B = 3;
    /** the x component of the normal. */
    private static final int NORMAL_X = 4;
    /** the y component of the normal. */
    private static final int NORMAL_Y = 5;
    /** the z component of the normal. */
    private static final int NORMAL_Z = 6;
    /** the u texture coordinate. */
    private static final int TEXTURE_U = 7;
    /** the v texture coordinate. */
    private static final int TEXTURE_V = 8;

    /**
     * Creates a Fragment at pixel coordinates (x, y).
     * @param x the x pixel coordinate of the Fragment
     * @param y the y pixel coordinate of the Fragment
     */
    public Fragment(int x, int y) { // int numAdditionalAttributes) {
        this.x = x;
        this.y = y;
        numAttributes = NUM_ATTRIBUTES;
        attributes = new double[numAttributes];
    }

    /**
     * Creates a Fragment at pixel coordinates (x, y) with the given depth.
     * @return the number of attributes of the Fragment
     */
    public int getNumAttributes() {
        return numAttributes;
    }

    /**
     * Gets a scalar attribute at index.
     * @param index the index of the attribute to get
     * @return the value of the attribute at index
     */
    public double getAttribute(int index) {
        return attributes[index];
    }

    /**
     * Gets a vector attribute at the given starting location and with the given
     * dimension.
     * @param index the starting index of the attribute
     * @param dimension the dimension of the attribute
     * @return the attribute as a double array
     */
    public double[] getAttribute(int index, int dimension) {
        double[] attr = new double[dimension];

        for (int i = 0; i < dimension; i++) {
            attr[i] = attributes[index + i];
        }

        return attr;
    }

    /**
     * Sets a scalar attribute at index.
     * @param index the index of the attribute to set
     * @param value the value to set the attribute to
     */
    public void setAttribute(int index, double value) {
        attributes[index] = value;
    }

    /**
     * Gets the x pixel coordinate of the Fragment.
     * @return the x pixel coordinate of the Fragment
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y pixel coordinate of the Fragment.
     * @return the y pixel coordinate of the Fragment
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the pixel coordinates (x, y) of the Fragment as a size 2 array.
     * @return the pixel coordinates (x, y) of the Fragment as a size 2 array
     */
    public int[] getPosition() {
        int[] position = new int[2];

        position[0] = x;
        position[1] = y;

        return position;
        // return new int[]{x, y};
    }

    /**
     * Sets the pixel coordinates (x, y) of the Fragment.
     * @param xc the x pixel coordinate of the Fragment
     * @param yc the y pixel coordinate of the Fragment
     */
    public void setPosition(int xc, int yc) {
        this.x = xc;
        this.y = yc;
    }

    /**
     * Gets the depth of the Fragment.
     * @return the depth of the Fragment
     */
    public double getDepth() {
        return attributes[0];
    }

    /**
     * Sets the depth of the Fragment.
     * @param z the depth of the Fragment
     */
    public void setDepth(double z) {
        attributes[DEPTH] = z;
    }

    /**
     * Gets the normal of the Fragment.
     * @return the normal of the Fragment
     */
    public Vector3 getNormal() {
        return new Vector3(attributes[NORMAL_X],
                            attributes[NORMAL_Y],
                            attributes[NORMAL_Z]);
    }

    /**
     * Sets the normal of the Fragment.
     * @param normal the normal of the Fragment
     */
    public void setNormal(Vector normal) {
        attributes[NORMAL_X] = normal.get(0);
        attributes[NORMAL_Y] = normal.get(1);
        attributes[NORMAL_Z] = normal.get(2);
    }

    /**
     * Sets the normal of the Fragment.
     * @param nx the x component of the normal
     * @param ny the y component of the normal
     * @param nz the z component of the normal
     */
    public void setNormal(double nx, double ny, double nz) {
        attributes[NORMAL_X] = nx;
        attributes[NORMAL_Y] = ny;
        attributes[NORMAL_Z] = nz;
    }

    /**
     * Gets the color of the Fragment.
     * @return the color of the Fragment
     */
    public Color getColor() {
        final int maxVal = 255;
        int r = (int) Math.min(maxVal, Math.max(maxVal * attributes[COLOR_R], 0));
        int g = (int) Math.min(maxVal, Math.max(maxVal * attributes[COLOR_G], 0));
        int b = (int) Math.min(maxVal, Math.max(maxVal * attributes[COLOR_B], 0));
        return new Color(r, g, b);
    }

    /**
     * Sets the color of the Fragment.
     * @param color the color of the Fragment
     */
    public void setColor(Color color) {
        attributes[COLOR_R] = color.getRed();
        attributes[COLOR_G] = color.getGreen();
        attributes[COLOR_B] = color.getBlue();
    }

    /**
     * Sets the color of the Fragment.
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     */
    public void setColor(double r, double g, double b) {
        attributes[COLOR_R] = r;
        attributes[COLOR_G] = g;
        attributes[COLOR_B] = b;
    }

    /**
     * Returns a string representation of the Fragment.
     * @return a string representation of the Fragment
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
