/**
 * Simple shader that just copy the interpolated color to the screen,
 * taking the depth of the fragment into acount.
 * 
 * @author cdehais
 */
public class PainterShader extends Shader {

    DepthBuffer depth;

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
    public void reset() {
        depth.clear();
    }
}
