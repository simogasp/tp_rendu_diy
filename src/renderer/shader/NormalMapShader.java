package renderer.shader;

import renderer.DepthBuffer;
import renderer.Fragment;
import renderer.GraphicsWrapper;
import renderer.Renderer;
import renderer.Transformation;
import renderer.algebra.Vector3;

public class NormalMapShader extends Shader {

    /**
     * Value tolerance to compare two double.
     */
    private static final double EPSILON = 0.0001;

    /**
     * The depth buffer.
     */
    private DepthBuffer depthBuffer;

    
    /**
     * Creates a NormalMapShader with the given screen.
     * @param screen the screen to draw on
     */
    public NormalMapShader(GraphicsWrapper screen) {
        super(screen);
        this.depthBuffer = new DepthBuffer(screen.getWidth(), screen.getHeight());
    }



    @Override
    public void reset() {
        this.depthBuffer.clear();
    }



    @Override
    public void shade(Fragment fragment) {
        // don't shade a normal of a non-visible point
        if (!this.depthBuffer.testFragment(fragment)) {
            return;
        }

        Vector3 n = fragment.getNormal();

        // some vector has NaN value so we skip it
        if (Double.isNaN(n.getX()) || Double.isNaN(n.getY()) || Double.isNaN(n.getZ())) {
            return;
        }

        // we normalize the vector to compute the color of the pixel.
        if (Math.abs(n.norm() - 1) > EPSILON);
        n.normalize();

        // transform a 3D direction in a color.
        final double r = n.getX() / 2 + 0.5;
        final double g = n.getY() / 2 + 0.5;
        final double b = n.getZ() / 2 + 0.5;

        fragment.setColor(r, g, b);

        screen.setPixel(fragment.getX(), fragment.getY(), fragment.getColor());
        this.depthBuffer.writeFragment(fragment);
    }

    
    
}
