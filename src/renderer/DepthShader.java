package renderer;

import java.awt.Color;

import renderer.shader.Shader;

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
     * @param screen the screen to draw on
     */
    protected DepthShader(GraphicsWrapper screen) {
        super(screen);
        this.depth = new DepthBuffer(screen.getWidth(), screen.getHeight());    
    }

    @Override
    public void shade(Fragment fragment) {
        if (depth.testFragment(fragment)) {
            screen.setPixel(fragment.getX(), fragment.getY(), getColorFor(fragment.getDepth()));
            depth.writeFragment(fragment);
        }
    }

    @Override
    public void reset() {
        depth.clear();
    }

    public static void update(double depth) {
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
    private static Color getColorFor(double depth) {
        Color res;
        double middle = (farest + nearest) / 2;
        if (depth < middle) {
            double alpha = (depth - nearest) / (middle - nearest);
            res = new Color(Math.round((float) (1 - alpha) * 255), Math.round((float) alpha * 255), 0);
        } else {
            double beta = (depth - middle) / (farest - middle);
            res = new Color(0, Math.round((float) (1 - beta) * 255), Math.round((float) beta * 255));
        }

        return res;
    }
}
