package renderer.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 2D Texture class.
 */
public class Texture {

    /**
     * The width of the texture.
     */
    private int width;
    /**
     * The height of the texture.
     */
    private int height;
    /**
     * The image of the texture.
     */
    private BufferedImage image;

    /**
     * Constructs a new Texture with the content of the image at @path.
     * @param path the path to the image file
     * @throws IOException if the image file is not found
     */
    public Texture(String path) throws IOException {
        image = ImageIO.read(new File(path));
        width = image.getWidth();
        height = image.getHeight();
    }

    /**
     * Samples the texture at texture coordinates (u,v), using nearest neighbor
     * interpolation
     * u and v and wrapped around to [0,1].
     * @param u the u texture coordinate
     * @param v the v texture coordinate
     * @return the color of the texture at (u,v)
     */
    public Color sample(double u, double v) {
        //++ // TODO
        int x = ((int) Math.floor(u * width)) % width; //<!!
        int y = ((int) Math.floor(v * height)) % height;

        return new Color(image.getRGB(x, y), false); //>!!
        //++ return new Color (0,0,0);
    }
}
