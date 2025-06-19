package renderer.rasterizer;

import java.awt.Color;

import renderer.DepthBuffer;
import renderer.Fragment;
import renderer.GraphicsWrapper;

public class NormalLayer {

    private DepthBuffer buffer;
    private GraphicsWrapper screen;

    public NormalLayer(final GraphicsWrapper screen, final DepthBuffer d) {
        this.screen = screen;
        buffer = d;
    }

    public NormalLayer(GraphicsWrapper screen) {
        this.screen = screen;
        buffer = new DepthBuffer(screen.getWidth(), screen.getHeight());
    }

    /**
     * Writes the fragment to the screen if it has to be.
     * @param fragment the fragment to shade
     */
    public void print(Fragment f) {
        if (!buffer.testFragment(f)) {
            return;
        }
        screen.setPixel(f.getX(), f.getY(), Color.RED);
    }

}
