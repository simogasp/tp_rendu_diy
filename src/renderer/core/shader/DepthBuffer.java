package renderer.core.shader;

import renderer.algebra.MathUtils;
import renderer.algebra.Matrix;

/**
 * The DepthBuffer class implements a DepthBuffer and its pass test.
 */
public class DepthBuffer {
    /**
     * The buffer of depth values.
     */
    private Matrix buffer;

    private double epsilon = 0.1;

    /**
     * Constructs a DepthBuffer of size width x height.
     * The buffer is initially cleared.
     *
     * @param width  the width of the buffer
     * @param height the height of the buffer
     */
    public DepthBuffer(int width, int height) {
        buffer = new Matrix(height, width);
        clear();
    }

    /**
     * Clears the buffer to infinite depth for all fragments.
     */
    public void clear() {
        buffer.setAll(Double.POSITIVE_INFINITY);
    }

    /**
     * Checks if the fragment coordinates are within the buffer bounds.
     *
     * @param f the fragment to check
     * @return true if coordinates are valid, false otherwise
     */
    private boolean isWithinBounds(Fragment f) {
        return MathUtils.isInRange(f.getX(), 0, width() - 1)
            && MathUtils.isInRange(f.getY(), 0, height() - 1);
    }

    /**
     * Test if a fragment passes the DepthBuffer test, i.e. is the fragment the
     * closest at its position.
     *
     * @param f the fragment to test
     * @return true if the fragment passes the test, false otherwise
     */
    public boolean testFragment(Fragment f) {
        if (isWithinBounds(f)) {
            return (buffer.get(f.getY(), f.getX()) >= f.getDepth()); //++ // TODO
            //++ return true;
        } else {
            return false;
        }
    }

    /**
     * Writes the fragment depth to the buffer.
     *
     * @param f the fragment to write
     */
    public void writeFragment(Fragment f) {
        if (isWithinBounds(f)) {
            buffer.set(f.getY(), f.getX(), f.getDepth()); //++ // TODO
        }
    }

    /**
     * Resize the buffer if it's needed.
     *
     * @param nWidth  the new width
     * @param nHeight the new height
     */
    public void resize(int nWidth, int nHeight) {
        if (width() != nWidth || height() != nHeight) {
            buffer = new Matrix(nHeight, nWidth);
        }
        clear();
    }

    /**
     * Get the width of the buffer.
     * @return the width of the buffer
     */
    public int width() {
        return buffer.getNCols();
    }

    /**
     * Get the height of the buffer.
     * @return the height of the buffer
     */
    public int height() {
        return buffer.getNRows();
    }

}
