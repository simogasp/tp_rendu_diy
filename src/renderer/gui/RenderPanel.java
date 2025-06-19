package renderer.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import renderer.Renderer;
import renderer.algebra.MathUtils;

public class RenderPanel extends JPanel {

    private static final int DEFAULT_PIXEL_SIZE = 1;

    private BufferedImage renderedImage;

    private int width;

    private int height;

    private int pixelSize;

    public RenderPanel(final int width, final int height, final int pixelSize) {
        // initialize the JPanel
        super();

        Renderer.setScreen(this);

        // fields
        this.height = height;
        this.width = width;
        this.pixelSize = pixelSize;

        // set the size of the panel
        final int realWidth = width * pixelSize;
        final int realHeight = height * pixelSize;

        renderedImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_3BYTE_BGR);
        renderedImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_3BYTE_BGR);

        JLabel image = new JLabel(new ImageIcon(renderedImage));
        add(image);
        setSize(realWidth, realHeight);
    }

    

    public RenderPanel(final int width, final int height) {
        this(width, height, DEFAULT_PIXEL_SIZE);
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
    public void setPixel(final int x, final int y, final char r, final char g, final char b) {

        if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
            int argb = 0xFF000000;
            argb += ((int) r) << (8 * 2);
            argb += ((int) g) << (8 * 1);
            argb += ((int) b);

            for (int i = 0; i < pixelSize; i++) {
                for (int j = 0; j < pixelSize; j++) {
                    System.out.println("HERE");
                    renderedImage.setRGB(i + (x * pixelSize), j + (y * pixelSize), argb);
                }
            }
        }
    }

    public void clearBuffer() {
        Graphics2D gd = renderedImage.createGraphics();
        gd.setColor(Color.BLACK);
        gd.fillRect(0, 0, width * pixelSize, height * pixelSize);
        repaint();
    }

    public void swapBuffers() {
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (renderedImage == null) {
            return;
        }
        ((Graphics2D) g).drawImage(renderedImage, new AffineTransform(1f, 0f, 0f, 1f, 0, 0), null);
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
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the screen.
     * 
     * @return the height
     */
    public int getHeight() {
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

    public void updateDims(int screenW, int screenH) {
        width = screenW;
        height = screenH;


        // set the size of the panel
        final int realWidth = width * pixelSize;
        final int realHeight = height * pixelSize;

        
        renderedImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_3BYTE_BGR);
        renderedImage = new BufferedImage(realWidth, realHeight, BufferedImage.TYPE_3BYTE_BGR);

        JLabel image = new JLabel(new ImageIcon(renderedImage));
        add(image);
        setSize(width, height);
    }

}
