import org.junit.Test;

import renderer.algebra.Vector;
import renderer.core.pipeline.FragmentShaderStage;
import renderer.core.pipeline.OutputMerger;
import renderer.core.pipeline.Rasterizer;
import renderer.core.shader.vertexshaders.VertexOutput;
import renderer.core.shader.fragmentshaders.Fragment;
import renderer.core.shader.fragmentshaders.FragmentOutput;
import renderer.core.shader.fragmentshaders.FragmentShader;
import renderer.controller.ImageWrapper;

/**
 * Test class for the Rasterizer class.
 *
 * @author: cdehais
 */
public class TestRasterizer {

    static class TestShader implements FragmentShader {


        @Override
        public FragmentOutput shade(Fragment fragment) {
            System.out.println(
                "  fragment: (" + fragment.getX() + ", " + fragment.getY() + ")"
                    + " - color = (" + fragment.getColor() + ")");
            return new FragmentOutput(fragment.getColor());
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
        FragmentShaderStage fragmentShaderStage = new FragmentShaderStage(shader, new OutputMerger(screen));
        Rasterizer rasterizer = new Rasterizer(fragmentShaderStage);

        System.out.println("Rasterizing edge");

        final int[] vertex1Coords = {0, 20};
        final int[] vertex2Coords = {5, -35};
        final double[] blackColor = {0.0, 0.0, 0.0};
        final double[] greenColor = {50.0 / 255.0, 100.0 / 255.0, 0.0};

        VertexOutput v1 = new VertexOutput();
        v1.x = vertex1Coords[0];
        v1.y = vertex1Coords[1];
        v1.depth = 0.0;
        v1.normal = new Vector(0, 0, 1);
        v1.worldPosition = new Vector(0, 0, 0);
        v1.color = blackColor;
        v1.alpha = 1.0;
        v1.u = 0.0;
        v1.v = 0.0;

        VertexOutput v2 = new VertexOutput();
        v2.x = vertex2Coords[0];
        v2.y = vertex2Coords[1];
        v2.depth = 0.0;
        v2.normal = new Vector(0, 0, 1);
        v2.worldPosition = new Vector(0, 0, 0);
        v2.color = greenColor;
        v2.alpha = 1.0;
        v2.u = 0.0;
        v2.v = 0.0;

        rasterizer.rasterizeEdge(v1, v2);
    }

}
