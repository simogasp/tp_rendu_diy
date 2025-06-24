package renderer.shader;

import java.awt.Color;

import renderer.DepthBuffer;
import renderer.Fragment;
import renderer.GraphicsWrapper;
import renderer.shader.colormap.ColorMap;
import renderer.shader.colormap.ColorMapFactory;

/**
 * Shader color the model in function of the depth of the surface.
 */
public class DepthShader extends Shader {

    /**
     * Represents the minimal Depth of the Model.
     */
    private static double nearest = Double.POSITIVE_INFINITY;
    
    /**
     * Represents the maximum Depth of the Model.
     */
    private static double farest = 0;

    /**
     * The depth buffer.
     */
    private DepthBuffer depth;

    /**
     * A colors Map.
     */
    public static ColorMap colorMap;



    /**
     * Creates a DepthShader with the given screen.
     * @param screen the screen to draw on
     */
    public DepthShader(final GraphicsWrapper screen) {
        this(screen, ColorMapFactory.create(ColorMapFactory.Maps.VERIDIS));
    }

    /** 
     * Creates a DepthShader with the given screen and colorMap.
     * @param screen the screen to draw on
     * @param initColorMap the init color map
     */
    public DepthShader(final GraphicsWrapper screen, final ColorMap initColorMap) {
        super(screen);
        this.depth = new DepthBuffer(screen.getWidth(), screen.getHeight());
        colorMap = initColorMap;
    }

    @Override
    public void shade(final Fragment fragment) {
        if (depth.testFragment(fragment)) {
            screen.setPixel(fragment.getX(), fragment.getY(), getColorFor(fragment.getDepth()));
            depth.writeFragment(fragment);
        }
    }

    @Override
    public void reset() {
        depth.clear();
    }

    /**
     * Update the nearest and the farest depth according to the given depth.
     *
     * @param depth the new one
     */
    public static void update(final double depth) {
        if (depth < nearest) {
            nearest = depth;
        }
        if (depth > farest) {
            farest = depth;
        }
    }

    /**
     * Returns a color in the color gradient (Red, green, Blue) where red is near, green the middle 
     * and blue the back of the model.
     * @param depth the depth of the current point
     * @return the color in the color gradient
     */
    private static Color getColorFor(final double depth) {

        final int bucketNumber = colorMap.length();

        if (Math.abs(depth - nearest) < 0.01) {
            return colorMap.getColor(bucketNumber - 1);
        }

        final double d = (farest - nearest) / bucketNumber;

        final double cursor = farest - depth;
        final int bucket = (int) (cursor / d);

        final double alpha = (cursor - bucket * d) / d;

        return interpolate(colorMap.getColor(bucket), colorMap.getColor(bucket + 1), alpha);
    }

    /**
     * Returns a mix of c1 and c2 with the value alpha.
     * @param c1 the first color
     * @param c2 the second one
     * @param alpha the coefficient of interpolation
     * @return the mixed color
     */
    private static Color interpolate(final Color c1, final Color c2, final double alpha) {
        final int r = (int) (c1.getRed() * (1 - alpha) + c2.getRed() * alpha);
        final int g = (int) (c1.getGreen() * (1 - alpha) + c2.getGreen() * alpha);
        final int b = (int) (c1.getBlue() * (1 - alpha) + c2.getBlue() * alpha);
        return new Color(r, g, b);
    }
}
