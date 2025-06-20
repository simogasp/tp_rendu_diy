package renderer.shader;

import renderer.Fragment;
import renderer.gui.RenderPanel;

/**
 * Simple shader that just copy the interpolated color to the screen.
 * @author cdehais
 */
public class SimpleShader extends Shader {

    /**
     * Creates a SimpleShader with the given screen.
     * @param renderPanel the screen to draw on
     */
    public SimpleShader(final RenderPanel renderPanel) {
        super(renderPanel);
    }

    /**
     * Shade the fragment.
     * @param fragment the fragment to shade
     */
    public void shade(Fragment fragment) {
        screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
    }
}
