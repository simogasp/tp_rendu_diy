import org.junit.Test;

import renderer.core.shader.Fragment;
import renderer.controller.ImageWrapper;
import renderer.core.rasterizer.Rasterizer;
import renderer.core.shader.Shader;

/**
 * Test class for the Rasterizer class.
 *
 * @author: cdehais
 */
public class TestRasterizer {

    static class TestShader extends Shader {

        TestShader() {
            super();
        }

        @Override
        public void shade(Fragment fragment) {
            System.out.println(
                "  fragment: (" + fragment.getX() + ", " + fragment.getY() + ")"
                    + " - color = (" + fragment.getColor() + ")");
        }
        @Override
        public void reset() {
            // Nothing to reset
        }
    }

    /**
     * Test rasterizing an edge.
     * @throws Exception
     */
    @Test
    public void test() throws Exception {

        System.out.println("OFF\n# Test Start");

        TestShader shader = new TestShader();
        ImageWrapper screen = new ImageWrapper();
        shader.init(null, screen);
        Rasterizer rasterizer = new Rasterizer(shader);

        System.out.println("Rasterizing edge");
        Fragment v1 = new Fragment(0, 20);
        v1.setColor(0, 0, 0);
        Fragment v2 = new Fragment(5, -35);
        v2.setColor((float) 50 / 255, (float) 100 / 255, 0);

        rasterizer.rasterizeEdge(v1, v2);
    }

}
