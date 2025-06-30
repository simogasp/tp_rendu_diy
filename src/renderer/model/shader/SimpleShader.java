package renderer.model.shader;

/**
 * Simple shader that just copy the interpolated color to the screen.
 * @author cdehais
 */
public class SimpleShader extends Shader {

    /**
     * Creates a SimpleShader.
     */
    public SimpleShader() {
        super();
    }

    /**
     * Shade the fragment.
     * @param fragment the fragment to shade
     */
    public void shade(Fragment fragment) {
        screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
    }
}
