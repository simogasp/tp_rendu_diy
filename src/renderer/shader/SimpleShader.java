package renderer.shader;

import renderer.Fragment;
import renderer.GraphicsWrapper;

/**
 * Simple shader that just copy the interpolated color to the screen.
 * @author cdehais
 */
public class SimpleShader extends Shader {

    /**
     * Creates a SimpleShader with the given screen.
     * @param screen the screen to draw on
     */
    public SimpleShader(GraphicsWrapper screen) {
        super(screen);
    }

    /**
     * Shade the fragment.
     * @param fragment the fragment to shade
     */
    public void shade(Fragment fragment) {
        screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
    }
}
