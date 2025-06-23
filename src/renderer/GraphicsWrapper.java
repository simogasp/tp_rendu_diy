package renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import renderer.algebra.MathUtils;

class ImageComponent extends Component {

  /**
   * The image displayed.
   */
  private BufferedImage renderedImage = null;

  ImageComponent(final BufferedImage init) {
    renderedImage = init;
  }

  /**
   * Update the display image by a new one.
   *
   * @param bi the new display
   * @return the old one
   */
  public BufferedImage swapImage(final BufferedImage bi) {
    final BufferedImage ret = renderedImage;
    renderedImage = bi;
    return ret;
  }

  /**
   * Display the rendered image if it exists.
   *
   * @param g the screen
   */
  public void paint(final Graphics g) {

    if (renderedImage != null) {
      ((Graphics2D) g).drawImage(renderedImage, new AffineTransform(1f, 0f, 0f, 1f, 0, 0),
          null);
    }
  }

}

/**
 * A "virtual" screen, where only "setPixel" is available
 * (It is a JFrame, and JFrame.EXIT_ON_CLOSE is set).
 *
 * @author smondet
 */
public class GraphicsWrapper {


  /**
   * The height of represented pixel.
   */
  private int height = 0;
  /**
   * The width of represented pixel.
   */
  private int width = 0;
  /**
   * The pixelSize of a represented pixel.
   */
  private int pixelSize = 0;

  /**
   * The App.
   */
  private JFrame myFrame;

  /**
   * The Image component.
   */
  private ImageComponent drawComp = null;

  /**
   * The next image to render.
   */
  private BufferedImage backBuffer = null;
  /**
   * The image displayed.
   */
  private BufferedImage frontBuffer = null;

  /**
   * Init the App : fill all field by the values.
   */
  private void init() {
    backBuffer = new BufferedImage(width * pixelSize,
      height * pixelSize,
      BufferedImage.TYPE_INT_ARGB);

    frontBuffer = new BufferedImage(width * pixelSize,
      height * pixelSize,
      BufferedImage.TYPE_3BYTE_BGR);

    /*
     * Graphics2D gd = initial.createGraphics ();
     * gd.setColor (Color.BLACK) ;
     * gd.fillRect (0,0, width * pixelSize, height * pixelSize) ;
     * gd = drawingImage.createGraphics ();
     * gd.setColor (Color.BLACK) ;
     * gd.fillRect (0,0, width * pixelSize, height * pixelSize) ;
     */

    drawComp = new ImageComponent(frontBuffer);
    drawComp.setPreferredSize(new Dimension(width * pixelSize, height * pixelSize));
    drawComp.setVisible(true);

    myFrame = new JFrame("Simple Inverse Rasterization Renderer (TSI)");
    myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    myFrame.add("Center", drawComp);
    myFrame.pack();
    myFrame.setVisible(true);
  }

  /**
   * Build a virtual screen of size width x height
   * And set its window visible.
   *
   * @param width  the width of the screen
   * @param height the height of the screen
   */
  public GraphicsWrapper(int width, int height) {
    this.height = height;
    this.width = width;
    this.pixelSize = 1;
    init();
  }

  /**
   * Build a virtual screen of size width x height, where one virtual pixel is
   * represented by
   * a pixelSize x pixelSize square.
   * And set its window visible.
   *
   * @param width     the width of the screen
   * @param height    the height of the screen
   * @param pixelSize the size of a virtual pixel
   */
  public GraphicsWrapper(int width, int height, int pixelSize) {
    this.height = height;
    this.width = width;
    this.pixelSize = pixelSize;
    init();
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
  public void setPixel(int x, int y, double r, double g, double b) {

    r = Math.min(1.0, Math.max(0.0, r));
    g = Math.min(1.0, Math.max(0.0, g));
    b = Math.min(1.0, Math.max(0.0, b));

    setPixel(x, y, (char) (r * MathUtils.MAX8INT),
      (char) (g * MathUtils.MAX8INT),
      (char) (b * MathUtils.MAX8INT));
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
  public void setPixel(int x, int y, char r, char g, char b) {

    if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
      int argb = 0xFF000000;
      argb += ((int) r) << (8 * 2);
      argb += ((int) g) << (8 * 1);
      argb += ((int) b);

      for (int i = 0; i < pixelSize; i++) {
        for (int j = 0; j < pixelSize; j++) {
          backBuffer.setRGB(i + (x * pixelSize), j + (y * pixelSize), argb);
        }
      }
    }
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
          backBuffer.setRGB(i + (x * pixelSize), j + (y * pixelSize), rgb);
        }
      }
    }
  }

  /**
   * Gets the pixel in the back buffer.
   *
   * @param x the x coordinate of the pixel
   * @param y the y coordinate of the pixel
   * @return the color of the pixel
   */
  public Color getPixel(int x, int y) {
    Color color;

    if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
      color = new Color(backBuffer.getRGB(x, y), false);
    } else {
      color = Color.BLACK;
    }

    return color;
  }

  /**
   * Gets the pixel in the front buffer.
   *
   * @param x the x coordinate of the pixel
   * @param y the y coordinate of the pixel
   * @return the color of the pixel
   */
  public Color getFrontPixel(int x, int y) {
    Color color;

    if ((x >= 0) && (x < width) && (y >= 0) && (y < height)) {
      color = new Color(frontBuffer.getRGB(x, y), false);
    } else {
      color = Color.BLACK;
    }

    return color;
  }

  /**
   * Gets the width of the screen.
   *
   * @return the width of the screen
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the height of the screen.
   *
   * @return the height of the screen
   */
  public int getHeight() {
    return height;
  }

  /**
   * Clear current draw-buffer (ie Paint it black).
   */
  public void clearBuffer() {
    Graphics2D gd = backBuffer.createGraphics();
    gd.setColor(Color.BLACK);
    gd.fillRect(0, 0, width * pixelSize, height * pixelSize);
  }

  /**
   * Draw current draw-buffer on the window.
   *
   */
  public void swapBuffers() {
    frontBuffer = drawComp.swapImage(backBuffer);
    myFrame.repaint();
  }

  /**
   * Destroy window.
   */
  public void destroy() {
    myFrame.dispose();
  }

}
