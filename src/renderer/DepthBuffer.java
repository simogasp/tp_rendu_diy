package renderer;

import renderer.algebra.Matrix;

/**
 * The DepthBuffer class implements a DepthBuffer and its pass test.
 */
public class DepthBuffer {
    /**
     * The buffer of depth values.
     */
    private Matrix buffer;

    /**
     * The width the buffer.
     */
    private int width;
    /**
     * The height the buffer.
     */
    private int height;

    /**
     * Constructs a DepthBuffer of size width x height.
     * The buffer is initially cleared.
     * @param width the width of the buffer
     * @param height the height of the buffer
     */
    public DepthBuffer(int width, int height) {
        try {
            buffer = new Matrix(width, height);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.width = width;
        this.height = height;
        clear();
    }

    /**
     * Clears the buffer to infinite depth for all fragments.
     */
    public void clear() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                buffer.set(i, j, Double.POSITIVE_INFINITY);
            }
        }

    }

    /**
     * Test if a fragment passes the DepthBuffer test, i.e. is the fragment the
     * closest at its position.
     * @param fragment the fragment to test
     * @return true if the fragment passes the test, false otherwise
     */
    public boolean testFragment(Fragment fragment) {
        if ((fragment.getX() >= 0) && (fragment.getX() < width) && (fragment.getY() >= 0)
                && (fragment.getY() < height)) {
            return (buffer.get(fragment.getY(), fragment.getX()) > fragment.getAttribute(0)); //++ // TODO
            //++ return false;
        } else {
            return false;
        }
    }

    /**
     * Writes the fragment depth to the buffer.
     * @param fragment the fragment to write
     */
    public void writeFragment(Fragment fragment) {
        if ((fragment.getX() >= 0) && (fragment.getX() < width) && (fragment.getY() >= 0)
                && (fragment.getY() < height)) {
            buffer.set(fragment.getY(), fragment.getX(), fragment.getAttribute(0)); //++ // TODO
        }
    }

}
