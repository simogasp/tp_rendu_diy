import org.junit.Test;

import renderer.model.Fragment;
import renderer.model.rasterizer.Rasterizer;
import renderer.model.shader.Shader;

/**
 * Test class for the Rasterizer class
 * 
 * @author: cdehais
 */
public class TestRasterizer {

    static class TestShader extends Shader {

        public TestShader() {
            super();
        }

        public void shade(Fragment fragment) {
            System.out.println("  fragment: (" + fragment.getX() + ", " + fragment.getY() + ")"
                    + " - color = (" + fragment.getColor() + ")");
        }
    }

    @Test
    public void test() throws Exception {

        System.out.println("OFF\n# Test Start");

        TestShader shader = new TestShader();
        Rasterizer rasterizer = new Rasterizer(shader);

        System.out.println("Rasterizing edge");
        Fragment v1 = new Fragment(0, 20);
        v1.setColor(0, 0, 0);
        Fragment v2 = new Fragment(5, -35);
        v2.setColor((float) 50 / 255, (float) 100 / 255, 0);

        rasterizer.rasterizeEdge(v1, v2);
    }

}
