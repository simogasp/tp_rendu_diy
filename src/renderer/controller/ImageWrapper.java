package renderer.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;

import renderer.core.shader.Fragment;
import renderer.core.mesh.Scene;

public class ImageWrapper extends BufferedImage {

    /**
     * Default width of a screen (For test).
     */
    private static final int DEFAULT_WIDTH = 100;

    /**
     * Default height of a screen (For test).
     */
    private static final int DEFAULT_HEIGHT = 100;

    /**
     * Background color of the image is Zambezi.
     */
    private static final int BACKGROUND_COLOR = 0x5C5C5C;

    /**
     * Creates a default ImageWrapper for test with the default width and height.
     */
    public ImageWrapper() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Creates a Image wrapper with the image configuration.
     *
     * @param scene the scene of the image
     */
    public ImageWrapper(Scene scene) {
        this(scene.getScreenW(), scene.getScreenH());
    }

    /**
     * Creates a Image wrapper with the given width and height.
     * @param width the width of the image
     * @param height the height of the image
     */
    public ImageWrapper(int width, int height) {
        this(width, height, BACKGROUND_COLOR);
    }

    /**
     * Creates a Image wrapper with the given width and height.
     * @param width the width of the image
     * @param height the height of the image
     * @param backgroundColor the background color of the image
     */
    public ImageWrapper(int width, int height, int backgroundColor) {
        super(width, height, BufferedImage.TYPE_3BYTE_BGR);
        init(backgroundColor);
    }

    /**
     * Places a pixel of rgb color in the (x, y) pixel.
     *
     * @param x   the abscissa of the pixel
     * @param y   the ordinate of the pixel
     * @param rgb the color of the pixel
     */
    public void setPixel(final int x, final int y, final Color rgb) {
        if (isClipped(new Fragment(x, y))) {
            return;
        }
        this.setRGB(x, y, rgb.getRGB());
    }

    /**
     * Test whether the fragment falls onto the screen.
     *
     * @param fragment the fragment to test
     * @return true if the fragment is clipped, false otherwise
     */
    public boolean isClipped(Fragment fragment) {
        return ((fragment.getX() < 0) || (fragment.getX() >= super.getWidth())
                || (fragment.getY() < 0) || (fragment.getY() >= super.getHeight()));
    }

    /**
     * Initializes the image with the background color.
     * @param rgb the background color
     */
    private void init(int rgb) {
        for (int x = 0; x < getWidth(); ++x) {
            for (int y = 0; y < getHeight(); ++y) {
                // init with the background color
                this.setRGB(x, y, rgb);
            }
        }
    }
}
