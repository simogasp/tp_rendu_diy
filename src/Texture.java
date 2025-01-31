
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * 2D Texture class.
 */
public class Texture {
    int width;
    int height;
    BufferedImage image;

    /**
     * Constructs a new Texture with the content of the image at @path.
     * @param path the path to the image file
     * @throws Exception if the image file is not found
     */
    public Texture(String path) throws Exception {
        image = ImageIO.read(new File(path));
        width = image.getWidth();
        height = image.getHeight();
    }

    /**
     * Samples the texture at texture coordinates (u,v), using nearest neighboor
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
