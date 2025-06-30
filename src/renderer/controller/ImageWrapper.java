package renderer.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;

import renderer.model.shader.Fragment;
import renderer.model.mesh.Scene;

public class ImageWrapper extends BufferedImage {

    /**
     * Creates a Image wrapper with the image configuration.
     *
     * @param scene the scene of the image
     */
    public ImageWrapper(Scene scene) {
        super(scene.getScreenW(), scene.getScreenH(), BufferedImage.TYPE_3BYTE_BGR);
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
}
