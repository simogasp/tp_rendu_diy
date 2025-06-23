package renderer.model.shader;

import renderer.controller.ImageWrapper;
import renderer.controller.Renderer;
import renderer.model.DepthBuffer;
import renderer.model.Fragment;

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
     */
    public PainterShader() {
        super();
    }

    /**
     * Shade the fragment, taking the depth of the fragment into account.
     * @param fragment the fragment to shade
     */
    public void shade(Fragment fragment) {
        if (depth.testFragment(fragment)) {
            screen.setRGB(fragment.getX(), fragment.getY(), fragment.getColor().getRGB());
            depth.writeFragment(fragment);
        }
    }

    @Override
    public void reset() {
        depth.clear();
    }

    @Override
    public void init(Renderer renderer, ImageWrapper screen) {
        super.init(renderer, screen);
        this.depth = new DepthBuffer(screen.getWidth(), screen.getHeight());
    }
}
