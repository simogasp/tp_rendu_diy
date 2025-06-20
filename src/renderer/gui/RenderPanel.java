package renderer.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import renderer.Renderer;
import renderer.algebra.MathUtils;

public class RenderPanel extends Canvas {

    /**
     * By default the size of the pixel of the render is 1 px by represented pixel.
     */
    private static final int DEFAULT_PIXEL_SIZE = 1;

    /**
     * The rendered image.
     */
    private BufferedImage renderedImage;

    /**
     * The width of the screen.
     */
    private int width;

    /**
     * The height of the screen.
     */
    private int height;

    /**
     * The size of the pixel represented.
     */
    private int pixelSize;

    /** The gui app. */
    private final GUIApp gui;

    /**
     * Creates a RenderPanel from the app, the width, the height and the pixel size.
     *
     * @param app       the app
     * @param width     the width
     * @param height    the height
     * @param pixelSize the pixel size
     */
    public RenderPanel(final GUIApp app, final int width,
            final int height, final int pixelSize) {
        // initialize the JPanel
        super();
        gui = app;

        Renderer.setScreen(this);

        // fields
        this.height = height;
        this.width = width;
        this.pixelSize = pixelSize;

        // set the size of the panel
        final int realWidth = width * pixelSize;
        final int realHeight = height * pixelSize;

        renderedImage = new BufferedImage(realWidth,
                realHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        renderedImage = new BufferedImage(realWidth,
                realHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        final Dimension dim = new Dimension(realWidth, realHeight);
        setSize(dim);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setVisible(true);
    }

    /**
     * Creates a render panel in the app, with a width and a height.
     *
     * @param app    the app
     * @param width  the width of the screen
     * @param height the height of the screen
     */
    public RenderPanel(final GUIApp app, final int width, final int height) {
        this(app, width, height, DEFAULT_PIXEL_SIZE);
    }

    /**
     * Lights the pixel (x,y) with color (r, g, b) (values clamped to [0,1])
     * on the current draw buffer.
     * Does nothing for pixels out of the screen.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param r the red component of the color
     * @param g the green component of the color
     * @param b the blue component of the color
     */
    public void setPixel(final int x, final int y, double r, double g, double b) {

        r = MathUtils.clamp(r, 0d, 1d);
        r = MathUtils.clamp(g, 0d, 1d);
        r = MathUtils.clamp(b, 0d, 1d);

        setPixel(x, y, (char) (r * 255), (char) (g * 255), (char) (b * 255));
    }

    /**
     * Lights the pixel (x,y) with color (r, g, b) (values clamped to [0, 255])
     * on the current draw buffer.
     * Does nothing for pixels out of the screen.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param r the red component of the color (clamped to [0, 255])
     * @param g the green component of the color (clamped to [0, 255])
     * @param b the blue component of the color (clamped to [0, 255])
     */
    public void setPixel(final int x, final int y,
            final char r, final char g, final char b) {

        if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
            int argb = 0xFF000000;
            argb += ((int) r) << (8 * 2);
            argb += ((int) g) << (8 * 1);
            argb += ((int) b);

            for (int i = 0; i < pixelSize; i++) {
                for (int j = 0; j < pixelSize; j++) {
                    renderedImage.setRGB(i + (x * pixelSize), j + (y * pixelSize), argb);
                }
            }
        }
    }

    /**
     * Clears the screen.
     */
    public void clear() {
        Graphics2D gd = renderedImage.createGraphics();
        gd.setColor(Color.BLACK);
        gd.fillRect(0, 0, width * pixelSize, height * pixelSize);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (renderedImage == null) {
            return;
        }
        ((Graphics2D) g).drawImage(renderedImage,
                new AffineTransform(1f, 0f, 0f, 1f, 0, 0),
                null);
    }

    /**
     * Lights the pixel (x,y) with the given color.
     * Does nothing for pixels out of the screen.
     *
     * @param x     the x coordinate of the pixel
     * @param y     the y coordinate of the pixel
     * @param color the color of the pixel
     */
    public void setPixel(int x, int y, Color color) {

        if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
            int rgb = color.getRGB();
            for (int i = 0; i < pixelSize; i++) {
                for (int j = 0; j < pixelSize; j++) {
                    renderedImage.setRGB(i + (x * pixelSize), j + (y * pixelSize), rgb);
                }
            }
        }
    }

    /**
     * Gets the width of the screen.
     *
     * @return the width
     */
    public int getScreenWidth() {
        return width;
    }

    /**
     * Gets the height of the screen.
     *
     * @return the height
     */
    public int getScreenHeight() {
        return height;
    }

    /**
     * Gets the pixels size.
     *
     * @return the pixelsize
     */
    public int getPixelSize() {
        return pixelSize;
    }

    /**
     * Updates the dimensions of the screen.
     *
     * @param screenW the new width
     * @param screenH the new height
     */
    public void updateDims(final int screenW, final int screenH) {
        width = screenW;
        height = screenH;

        // set the size of the panel
        final int realWidth = width * pixelSize;
        final int realHeight = height * pixelSize;

        renderedImage = new BufferedImage(realWidth,
                realHeight,
                BufferedImage.TYPE_3BYTE_BGR);
        renderedImage = new BufferedImage(realWidth,
                realHeight,
                BufferedImage.TYPE_3BYTE_BGR);

        final Dimension dim = getSize();
        dim.width = realWidth;
        dim.height = realHeight;

        setSize(dim);
        setPreferredSize(dim);
        setMinimumSize(dim);
        gui.updateDims();
    }
}
