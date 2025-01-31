
import java.awt.*;
import algebra.*;

/**
 * The Fragment class represents an attributed 'pixel' as generated
 * by a Rasterizer.
 * 
 * @author cdehais
 */

public class Fragment {
    private int x;
    private int y;
    private int numAttributes;

    /*
     * attributes placement:
     * 0: depth
     * 1-3: color
     * 4-6: normal
     * 7-8: (u,v) texture coordinates
     */
    double[] attributes;

    /**
     * Creates a Fragment at pixel coordinates (x, y)
     * @param x the x pixel coordinate of the Fragment
     * @param y the y pixel coordinate of the Fragment
     */
    public Fragment(int x, int y) { // int numAdditionalAttributes) {
        this.x = x;
        this.y = y;
        numAttributes = 9;
        attributes = new double[numAttributes];
    }

    /**
     * Creates a Fragment at pixel coordinates (x, y) with the given depth
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
     * Gets the x pixel coordinate of the Fragment
     * @return the x pixel coordinate of the Fragment
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y pixel coordinate of the Fragment
     * @return the y pixel coordinate of the Fragment
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the pixel coordinates (x, y) of the Fragment as a size 2 array
     * @return the pixel coordinates (x, y) of the Fragment as a size 2 array
     */
    public int[] getPosition() {
        int[] position = new int[2];

        position[0] = x;
        position[1] = y;

        return position;
    }

    /**
     * Sets the pixel coordinates (x, y) of the Fragment
     * @param x the x pixel coordinate of the Fragment
     * @param y the y pixel coordinate of the Fragment
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the depth of the Fragment
     * @return the depth of the Fragment
     */
    public double getDepth() {
        return attributes[0];
    }

    /**
     * Sets the depth of the Fragment
     * @param z the depth of the Fragment
     */
    public void setDepth(double z) {
        attributes[0] = z;
    }

    /**
     * Gets the normal of the Fragment
     * @return the normal of the Fragment
     */
    public Vector3 getNormal() {
        return new Vector3(attributes[0], attributes[1], attributes[2]);
    }

    /**
     * Sets the normal of the Fragment
     * @param normal the normal of the Fragment
     */
    public void setNormal(Vector normal) {
        attributes[4] = normal.get(0);
        attributes[5] = normal.get(1);
        attributes[6] = normal.get(2);
    }

    /**
     * Sets the normal of the Fragment
     * @param nx the x component of the normal
     * @param ny the y component of the normal
     * @param nz the z component of the normal
     */
    public void setNormal(double nx, double ny, double nz) {
        attributes[4] = nx;
        attributes[5] = ny;
        attributes[6] = nz;
    }

    /**
     * Gets the color of the Fragment
     * @return the color of the Fragment
     */
    public Color getColor() {
        int r = (int) Math.min(255, Math.max(255 * attributes[1], 0));
        int g = (int) Math.min(255, Math.max(255 * attributes[2], 0));
        int b = (int) Math.min(255, Math.max(255 * attributes[3], 0));
        return new Color(r, g, b);
    }

    /**
     * Sets the color of the Fragment
     * @param color the color of the Fragment
     */
    public void setColor(Color color) {
        attributes[1] = color.getRed();
        attributes[2] = color.getGreen();
        attributes[3] = color.getBlue();
    }

    /**
     * Sets the color of the Fragment
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     */
    public void setColor(double r, double g, double b) {
        attributes[1] = r;
        attributes[2] = g;
        attributes[3] = b;
    }

    /**
     * Returns a string representation of the Fragment
     * @return a string representation of the Fragment
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
