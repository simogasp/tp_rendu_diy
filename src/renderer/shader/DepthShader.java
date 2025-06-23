package renderer.shader;

import java.awt.Color;

import renderer.DepthBuffer;
import renderer.Fragment;
import renderer.GraphicsWrapper;
import renderer.algebra.MathUtils;

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
     * Creates a DepthShader with the given screen.
     *
     * @param screen the screen to draw on
     */
    public DepthShader(GraphicsWrapper screen) {
        super(screen);
        this.depth = new DepthBuffer(screen.getWidth(), screen.getHeight());
    }

    @Override
    public void shade(Fragment fragment) {
        if (!depth.testFragment(fragment)) {
            return;
        }
        screen.setPixel(fragment.getX(),
            fragment.getY(),
            getColorFor(fragment.getDepth()));
        depth.writeFragment(fragment);
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
    public static void update(double depth) {
        if (depth < nearest) {
            nearest = depth;
        }
        if (depth > farest) {
            farest = depth;
        }
    }

    /**
     * Returns a color in the color gradient (Red, green, Blue) where red is near,
     * green the middle
     * and blue the back of the model.
     *
     * @param depth the depth of the current point
     * @return the color in the color gradient
     */
    public static Color getColorFor(double depth) {
        // get the center of the gap
        final double middle = (farest + nearest) / 2;

        if (depth < middle) {

            // compute a color between red (near) and green (middle)
            final double alpha = (depth - nearest) / (middle - nearest);
            return new Color(Math.round((float) (1 - alpha) * MathUtils.MAX8INT),
                Math.round((float) alpha * MathUtils.MAX8INT),
                0);

        } else {

            // compute a color between green (middle) and blue (far)
            final double beta = (depth - middle) / (farest - middle);
            return new Color(0,
                Math.round((float) (1 - beta) * MathUtils.MAX8INT),
                Math.round((float) beta * MathUtils.MAX8INT));

        }
    }
}
