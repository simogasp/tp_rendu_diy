package renderer.shader;

import renderer.DepthBuffer;
import renderer.Fragment;
import renderer.GraphicsWrapper;

/**
 * Simple shader that just copy the interpolated color to the screen,
 * taking the depth of the fragment into account.
 * @author cdehais
 */
public class PainterShader extends Shader {

    /**
     * The depth buffer.
     */
    private DepthBuffer depth;

    /**
     * Creates a PainterShader with the given screen.
     * @param screen the screen to draw on
     */
    public PainterShader(GraphicsWrapper screen) {
        super(screen);
        depth = new DepthBuffer(screen.getWidth(), screen.getHeight());
    }

    /**
     * Shade the fragment, taking the depth of the fragment into account.
     * @param fragment the fragment to shade
     */
    public void shade(Fragment fragment) {
        if (depth.testFragment(fragment)) {
            screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
            depth.writeFragment(fragment);
        }
    }

    /**
     * Reset the shader.
     */
    @Override
    public void reset() {
        depth.clear();
    }
}
