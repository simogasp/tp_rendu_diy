package renderer.core.shader.fragmentshaders;

import java.awt.Color;

import renderer.controller.ColorMapFactory;
import renderer.controller.ColorMapFactory.Maps;
import renderer.core.shader.colormap.ColorMap;

/**
 * Shader color the model in function of the depth of the surface.
 */
public class DepthShader implements FragmentShader {

    /**
     * Tolerance to consider a point nearer than the near point and farthest than
     * the far point.
     */
    private static final double EPSILON = 1E-8;

    /**
     * Represents the minimal Depth of the Model.
     */
    private double near = Double.POSITIVE_INFINITY;

    /**
     * Represents the maximum Depth of the Model.
     */
    private double far = Double.NEGATIVE_INFINITY;

    /**
     * A colors Map.
     */
    private ColorMap colorMap;

    /**
     * Creates a DepthShader.
     */
    public DepthShader() {
        this(ColorMapFactory.create(ColorMapFactory.Maps.VERIDIS), Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    /**
     * Creates a DepthShader with the given colorMap.
     *
     * @param initColorMap the init color map
     */
    public DepthShader(final ColorMap initColorMap, double near, double far) {
        super();
        colorMap = initColorMap;
        this.near = near;
        this.far = far;
    }

    @Override
    public FragmentOutput shade(Fragment fragment) {

        double z = fragment.getDepth();

        Color gray = getColorFor(z);

        return new FragmentOutput(gray);
    }

    /**
     * Returns a color in the color gradient according to the depth.
     *
     * @param depth the depth of the current point
     * @return the color in the color gradient
     */
    private Color getColorFor(final double depth) {
        if (depth - near < -EPSILON || depth - far > EPSILON) {
            return Color.RED;
        }

        final int bucketNumber = colorMap.length() - 2;

        final double d = (far - near) / bucketNumber;

        final double cursor = far - depth;
        final int bucket = (int) (cursor / d);

        final double alpha = (cursor - bucket * d) / d;

        return interpolate(colorMap.getColor(bucket),
                colorMap.getColor(bucket + 1),
                alpha);
    }

    /**
     * Returns a mix of c1 and c2 with the value alpha.
     *
     * @param c1    the first color
     * @param c2    the second one
     * @param alpha the coefficient of interpolation
     * @return the mixed color
     */
    private static Color interpolate(final Color c1, final Color c2, final double alpha) {
        final int r = (int) (c1.getRed() * (1 - alpha) + c2.getRed() * alpha);
        final int g = (int) (c1.getGreen() * (1 - alpha) + c2.getGreen() * alpha);
        final int b = (int) (c1.getBlue() * (1 - alpha) + c2.getBlue() * alpha);
        return new Color(r, g, b);
    }

    @Override
    public boolean supportsColorMap() {
        return true;
    }

    @Override
    public void setColorMap(final Maps map) {
        this.colorMap = ColorMapFactory.create(map);
    }

    @Override
    public void setDepthRange(double near, double far) {
        this.near = near;
        this.far = far;
    }
}
